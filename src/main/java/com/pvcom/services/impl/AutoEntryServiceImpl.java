package com.pvcom.services.impl;

import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;
import com.pvcom.model.AutoEntry;
import com.pvcom.model.AutoEntryLog;
import com.pvcom.model.Workflow;
import com.pvcom.repository.AutoEntryLogsRepository;
import com.pvcom.repository.AutoEntryRepository;
import com.pvcom.services.AutoEntryService;
import com.pvcom.services.EntryService;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.thymeleaf.util.StringUtils;

import javax.mail.*;
import javax.mail.internet.MimeBodyPart;
import javax.mail.search.FlagTerm;
import java.io.File;
import java.io.IOException;
import java.util.*;

@Service
public class AutoEntryServiceImpl implements AutoEntryService {

    private static final Logger logger = LoggerFactory.getLogger(AutoEntryServiceImpl.class);

    @Autowired
    private AutoEntryRepository repository;

    @Autowired
    private AutoEntryLogsRepository logRepository;

    @Autowired
    private Environment env;

    @Autowired
    private EntryService entryService;


    @Override
    public boolean add(AutoEntry entry) {
        if (repository.save(entry) != null) {
            return true;
        }
        return false;
    }

    @Override
    public boolean update(AutoEntry entry) {
        if (repository.save(entry) != null) {
            return true;
        }
        return false;
    }

    @Override
    public boolean delete(int entry) {
        repository.delete(entry);
        return true;
    }

    @Override
    public AutoEntry get(int id) {
        return repository.findOne(id);
    }

    @Override
    public Iterable<AutoEntry> get() {
        return repository.findAll();
    }

    public void executeAutoSchedule() {
        Iterable<AutoEntry> entries = get();
        for (AutoEntry entry : entries) {
            readEmails(entry);
        }
    }

    private void readEmails(AutoEntry entry) {

        List<AutoEntryLog> entryLogs = new ArrayList<>();
        logger.info("connecting to :: ",entry.getEmail());
        try {
            Session session = Session.getDefaultInstance(new Properties());
            Store store = session.getStore("imaps");
            store.connect(entry.getHost(), entry.getPort(), entry.getEmail(), entry.getPassword());
            Folder inbox = store.getFolder("INBOX");
            inbox.open(Folder.READ_WRITE);

            // Fetch unseen messages from inbox folder
            Message[] messages = new Message[0];
            messages = inbox.search(
                    new FlagTerm(new Flags(Flags.Flag.SEEN), false));

            // Sort messages from recent to oldest
            Arrays.sort(messages, (m1, m2) -> {
                try {
                    return m2.getSentDate().compareTo(m1.getSentDate());
                } catch (MessagingException e) {
                    throw new RuntimeException(e);
                }
            });
            logger.info("************* message reading started ****************");
            for (Message message : messages) {
                if (hasAttachments(message)) {
                    Address[] fromAddress = message.getFrom();

                    if(!StringUtils.isEmpty(entry.getReceiveEmailFrom())) {
                        if (!message.getFrom()[0].toString().toLowerCase().contains(entry.getReceiveEmailFrom().toLowerCase())) {
                            continue;
                        }
                    }

                    Multipart multiPart = (Multipart) message.getContent();
                    int numberOfParts = multiPart.getCount();

                    String contentType = message.getContentType();
                    String messageContent = "";
                    AutoEntryLog entryLog = new AutoEntryLog();
                    entryLog.setSubject(message.getSubject());
                    entryLog.setEmailFrom(fromAddress[0].toString());
                    entryLog.setEmailto(entry.getEmail());

                    boolean flag = false;

                    for (int partCount = 0; partCount < numberOfParts; partCount++) {
                        MimeBodyPart part = (MimeBodyPart) multiPart.getBodyPart(partCount);
                        if (Part.ATTACHMENT.equalsIgnoreCase(part.getDisposition())) {
                            // this part is attachment
                            String fileName = part.getFileName();
                            if ("PDF".equalsIgnoreCase(FilenameUtils.getExtension(fileName))) {
                                flag = true;
                                logger.info("Filename size : " + part.getSize());
                                String name = "AUTO_" + new Date().getTime() + ".pdf";
                                part.saveFile(env.getProperty("file.mount.path") + File.separator + name);
                                entryLog.setFilePath(name);
                                break;
                            }
                        } else {
                            messageContent = part.getContent().toString();
                        }
                    }

                    entryLog.setMsg(messageContent);
                    message.setFlag(Flags.Flag.SEEN, true);
                    if (flag) {
                        entryLogs.add(entryLog);
                    }
                }
            }
            logger.info(" ************** message reading End *************");
        } catch (MessagingException e) {
            logger.error("Exception while reading pdf", e);
        } catch (IOException e) {
            logger.error("Exception while reading pdf", e);
        } finally {
            if (!entryLogs.isEmpty()) {
                crateEntries(entryLogs);
                logRepository.save(entryLogs);
            }
        }
    }

    private void crateEntries(List<AutoEntryLog> entryLogs) {

        for (AutoEntryLog log : entryLogs) {
            try {
                String pdfStr = readPdf(log.getFilePath());
                if (!StringUtils.isEmpty(pdfStr)) {
                    List<String> strings = Arrays.asList(pdfStr.split("(\r\n|\r|\n)", -1));
                    logger.info("local ref :: " + strings.get(1));
                    logger.info("drugs :: " + strings.get(35));

                    Workflow entry=new Workflow();
                    entry.setLocal_uniq_id(strings.get(1));
                    entry.setDrugs(strings.get(35));
                    entry.setDate(new Date());
                    entry.setFile_path(log.getFilePath());
                    entryService.add(entry);
                }
            } catch (Exception e) {
                logger.error("Exception while reading pdf", e);
            }
        }
    }

    private boolean hasAttachments(Message msg) throws MessagingException, IOException {
        if (msg.isMimeType("multipart/mixed")) {
            Multipart mp = (Multipart) msg.getContent();
            if (mp.getCount() > 1)
                return true;
        }
        return false;
    }

    private String readPdf(String filePath) {
        PdfReader reader = null;
        try {
            reader = new PdfReader(env.getProperty("file.mount.path") + File.separator + filePath);
            String textFromPage = PdfTextExtractor.getTextFromPage(reader, 1);
            logger.info(textFromPage);
            if(textFromPage.contains("SUSPECT DRUG(S) INFORMATION")) {
                return textFromPage;
            }
        } catch (Exception e) {
            logger.error("Exception while reading pdf", e);
        } finally {
            if (reader != null) {
                reader.close();
            }
        }
        return null;
    }
}
