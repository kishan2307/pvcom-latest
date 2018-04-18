package com.pvcom.beans;

import lombok.Data;
import lombok.ToString;

import java.util.List;

@Data
@ToString
public class ReportRequestBean {

    private  List<Integer> cases;
    private  List<Integer> entries;

}
