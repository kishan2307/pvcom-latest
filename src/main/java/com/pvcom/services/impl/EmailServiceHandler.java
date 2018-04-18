package com.pvcom.services.impl;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import com.pvcom.services.EmailService;
import it.ozimov.springboot.mail.model.Email;
import it.ozimov.springboot.mail.model.EmailAttachment;
import it.ozimov.springboot.mail.model.ImageType;
import it.ozimov.springboot.mail.model.InlinePicture;
import it.ozimov.springboot.mail.model.defaultimpl.DefaultEmail;
import it.ozimov.springboot.mail.model.defaultimpl.DefaultEmailAttachment;
import it.ozimov.springboot.mail.model.defaultimpl.DefaultInlinePicture;
import it.ozimov.springboot.mail.service.exception.CannotSendEmailException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import javax.mail.internet.InternetAddress;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;

import static com.google.common.collect.Lists.newArrayList;

@Service
public class EmailServiceHandler implements EmailService {

    @Autowired
    private it.ozimov.springboot.mail.service.EmailService emailService;

    public void sendMimeEmailWithThymeleaf(String to, String name, String sub, String template, Map<String, Object> modelObject) throws IOException, CannotSendEmailException, URISyntaxException {
        //InlinePicture inlinePicture = createGalaxyInlinePicture();

        final Email email = DefaultEmail.builder()
                .from(new InternetAddress("kishan.ilovejava@gmail.com", "PVCOM"))
                .to(newArrayList(new InternetAddress(to, name)))
                .subject(sub)
                .body("")
                .encoding("UTF-8").build();

        CompletableFuture.runAsync(() -> {
            try {
                emailService.send(email, template, modelObject);
            } catch (CannotSendEmailException e) {
                e.printStackTrace();
            }
        }, Executors.newSingleThreadExecutor())
                .exceptionally(exc -> {
                    exc.printStackTrace();
                    return null;
                });
    }

    private InlinePicture createGalaxyInlinePicture() throws URISyntaxException {
        ClassLoader classLoader = getClass().getClassLoader();
        File pictureFile = new File(classLoader.getResource("images" + File.separator + "galaxy.jpeg").toURI());
        Preconditions.checkState(pictureFile.exists(), "There is not picture %s", pictureFile.getName());

        return DefaultInlinePicture.builder()
                .file(pictureFile)
                .imageType(ImageType.JPG)
                .templateName("galaxy.jpeg").build();
    }

    private EmailAttachment getPdfWithAccentedCharsAttachment(String filename) throws URISyntaxException, IOException {
        ClassLoader classLoader = getClass().getClassLoader();
        File pdfFile = new File(classLoader.getResource("attachments" + File.separator + "Questo documento è un test.pdf").toURI());

        final DefaultEmailAttachment attachment = DefaultEmailAttachment.builder()
                .attachmentName(filename + ".pdf")
                .attachmentData(Files.readAllBytes(pdfFile.toPath()))
                .mediaType(MediaType.APPLICATION_PDF).build();
        return attachment;
    }


    @Override
    public void sendNewUserWelcomeEmail(String email, String name, String username, String pwd) {
        Map<String, Object> modelObject = ImmutableMap.of(
                "title", "Emperor",
                "name", name,
                "email", email,
                "password", pwd
        );
        try {
            sendMimeEmailWithThymeleaf(email, name, "Greeting", "emailTemplates/userWelcomeTemplate.html", modelObject);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (CannotSendEmailException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean sendForgetPwdEmail(String email, String name, String pwd) {
        Map<String, Object> modelObject = ImmutableMap.of(
                "name", name,
                "email", email,
                "password", pwd
        );
        try {
            sendMimeEmailWithThymeleaf(email, name, "Greeting", "emailTemplates/userForgotPwdTemplate.html", modelObject);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
