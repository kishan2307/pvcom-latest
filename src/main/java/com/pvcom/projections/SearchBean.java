package com.pvcom.projections;

import lombok.Data;

import java.util.Date;

@Data
public class SearchBean {
    private String company_receive_date;
    private String date;
    private String to;
    private String from;
    private String country;
    private String local_uniq_id;
    private String source;
    private String lp_name;
    private String expedite;
    private String seriousness;
    private String drugs;
}
