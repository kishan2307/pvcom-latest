package com.pvcom.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pvcom.beans.ReportRequestBean;
import com.pvcom.services.ReportServices;
import com.pvcom.services.UserServices;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.thymeleaf.util.StringUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.util.Date;

@Controller
@RequestMapping("/export")
public class ReportController {

    @Autowired
    private UserServices userServices;

    private static final Logger LOGGER = LoggerFactory.getLogger(ReportController.class);

    @Autowired
    ReportServices reportServices;

    @PostMapping("/report")
    public void getLogFile(HttpServletResponse response, @RequestParam("data") String data1) {
        try {
            ReportRequestBean requestBean = null;
            if (!StringUtils.isEmpty(data1)) {
                ObjectMapper mapper = new ObjectMapper();
                requestBean = mapper.readValue(data1, ReportRequestBean.class);
                if (requestBean != null) {
                    XSSFWorkbook workbook = reportServices.getExcelReport(requestBean);
                    if (workbook != null) {
                        response.setContentType("application/vnd.ms-excel");
                        response.setHeader("Content-Disposition", "attachment; filename=" + new Date().getTime() + ".xlsx");
                        OutputStream out = response.getOutputStream();
                        workbook.write(out);
                        out.flush();
                        out.close();
                        response.flushBuffer();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
