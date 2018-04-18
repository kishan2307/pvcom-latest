package com.pvcom.beans;

import lombok.Data;
import lombok.ToString;

import java.util.List;

@Data
@ToString
public class AssignBean {
    private int fs;
    private int mr;
    private int de;
    private int qc;
    private int user;
    private String email;
    private String type;
    private List<Integer> ids;
}
