package com.pvcom.services.impl;

import com.pvcom.beans.ReportRequestBean;
import com.pvcom.common.WorkflowConstants;
import com.pvcom.model.UserWorkflows;
import com.pvcom.model.Workflow;
import com.pvcom.projections.UserIdNames;
import com.pvcom.repository.UserWorkflowRepository;
import com.pvcom.repository.WorkflowRepository;
import com.pvcom.services.ReportServices;
import com.pvcom.services.UserServices;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.IntStream;

import static com.pvcom.common.Util.getCasesDataSequence;
import static com.pvcom.common.Util.getEntrySequence;

@Service
public class ReportServiceImpl implements ReportServices {

    @Autowired
    UserWorkflowRepository userWorkflowRepository;

    @Autowired
    WorkflowRepository workflowRepository;

    @Autowired
    UserServices userServices;

    @Override
    public XSSFWorkbook getExcelReport(ReportRequestBean bean) {
        XSSFWorkbook workbook = new XSSFWorkbook();
        //Create a blank sheet
        XSSFSheet sheet = workbook.createSheet("Employee Data");

        List<UserIdNames> userIdNames=userServices.getIdNames();
        //This data needs to be written (Object[])

        List<WorkflowConstants.FiedConstants> headers;
        headers = new LinkedList<>(Arrays.asList(
                WorkflowConstants.FiedConstants.DATE,
                WorkflowConstants.FiedConstants.RECEIVE_DATE,
                WorkflowConstants.FiedConstants.LOCAL_REF,
                WorkflowConstants.FiedConstants.COUNTRY,
                WorkflowConstants.FiedConstants.SOURCE,
                WorkflowConstants.FiedConstants.DRUG,
                WorkflowConstants.FiedConstants.VALID,
                WorkflowConstants.FiedConstants.RESON,
                WorkflowConstants.FiedConstants.SERIOUSNESS,
                WorkflowConstants.FiedConstants.EXPEDIT,
                WorkflowConstants.FiedConstants.SUBMISSION_COUNTY,
                WorkflowConstants.FiedConstants.LPNAME,
                WorkflowConstants.FiedConstants.TRIAGE_COMMENT,
                WorkflowConstants.FiedConstants.ENTRY_CREATED_BY,
                WorkflowConstants.FiedConstants.WWUID,

                WorkflowConstants.FiedConstants.DE_DONE_DATE,
                WorkflowConstants.FiedConstants.DE_DONE_BY,
                WorkflowConstants.FiedConstants.DE_COMMENT,

                WorkflowConstants.FiedConstants.QC_DONE_DATE,
                WorkflowConstants.FiedConstants.QC_DONE_BY,
                WorkflowConstants.FiedConstants.QC_COMMENT,

                WorkflowConstants.FiedConstants.MR_DONE_DATE,
                WorkflowConstants.FiedConstants.MR_DONE_BY,
                WorkflowConstants.FiedConstants.MR_COMMENT,

                WorkflowConstants.FiedConstants.FS_DONE_DATE,
                WorkflowConstants.FiedConstants.FS_DONE_BY,
                WorkflowConstants.FiedConstants.FS_SUBMISSION_COUTRY,
                WorkflowConstants.FiedConstants.FS_COMMENT
        ));

        List<Workflow> workflows = null;
        List<UserWorkflows> cses = null;
        if (!bean.getEntries().isEmpty()) {
            workflows = workflowRepository.findByIdIn(bean.getEntries());
        }

        if (!bean.getCases().isEmpty()) {
            cses = userWorkflowRepository.findByIdIn(bean.getCases());
        }

        XSSFCellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setFontHeightInPoints((short) 12);
        font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
        style.setFont(font);
        style.setFillForegroundColor(new XSSFColor(new java.awt.Color(0x92d050)));
        style.setFillPattern(CellStyle.SOLID_FOREGROUND);

        Row hrow = sheet.createRow(0);
        int hcellnum = 0;
        for (WorkflowConstants.FiedConstants s : headers) {
            Cell cell = hrow.createCell(hcellnum++);
            cell.setCellStyle(style);
            cell.setCellValue(s.getName());
        }

        IntStream.range(0, hcellnum).forEach((columnIndex) -> sheet.autoSizeColumn(columnIndex));


        CellStyle wrapText=workbook.createCellStyle();
        wrapText.setWrapText(true);



        if (workflows != null) {
            int rownum = 1;
            for (Workflow w : workflows) {
                HashMap<WorkflowConstants.FiedConstants, Object> map = getEntrySequence(w,userIdNames);
                Row row = sheet.createRow(rownum++);
                int cellnum = 0;
                for (WorkflowConstants.FiedConstants s : headers) {
                    Cell cell = row.createCell(cellnum++);
                    cell.setCellStyle(wrapText);
                    if (map.containsKey(s)) {
                        Object obj=map.get(s);
                        if (obj instanceof String) {
                            cell.setCellValue((String) obj);
                        } else if (obj instanceof Integer) {
                            cell.setCellValue((Integer) obj);
                        }else if(obj instanceof Date){
                            cell.setCellValue(new SimpleDateFormat("dd-MM-yyyy").format((Date)obj));
                        }
                    }
                }
            }
        }

        if (cses != null) {
            int rownum = 1;
            for (UserWorkflows w : cses) {
                HashMap<WorkflowConstants.FiedConstants, Object> map = getCasesDataSequence(w,userIdNames);
                Row row = sheet.createRow(rownum++);
                int cellnum = 0;
                for (WorkflowConstants.FiedConstants s : headers) {
                    Cell cell = row.createCell(cellnum++);
                    cell.setCellStyle(wrapText);
                    if (map.containsKey(s)) {
                        Object obj=map.get(s);
                        if (obj instanceof String) {
                            cell.setCellValue((String) obj);
                        } else if (obj instanceof Integer) {
                            cell.setCellValue((Integer) obj);
                        }else if(obj instanceof Date){
                            cell.setCellValue(new SimpleDateFormat("dd-MM-yyyy").format((Date)obj));
                        }
                    }
                }
            }
        }
        return workbook;
    }
}
