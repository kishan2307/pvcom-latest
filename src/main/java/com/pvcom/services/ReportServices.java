package com.pvcom.services;


import com.pvcom.beans.ReportRequestBean;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public interface ReportServices {


    public XSSFWorkbook getExcelReport(ReportRequestBean bean);

}
