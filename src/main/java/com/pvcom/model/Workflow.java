package com.pvcom.model;

import lombok.Data;

import java.util.Date;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Data
public class Workflow {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private Date company_receive_date;
    @NotNull
    private Date date;

    private String country;
    @NotNull
    private String local_uniq_id;

    private String source;

    private String submission_country;
    private String lp_name;

    private String expedite;

    private String seriousness;

    private String drugs;
    private String reason;

    private String triageComment;

    private String valid;
    private String file_path;
    private int createdBy;
    private int updatedBy;
    private int status;
    @OneToOne(cascade = CascadeType.ALL, optional = true, fetch = FetchType.EAGER)
    private SubWorkflowData de;
    @OneToOne(cascade = CascadeType.ALL, optional = true, fetch = FetchType.EAGER)
    private SubWorkflowData qc;
    @OneToOne(cascade = CascadeType.ALL, optional = true, fetch = FetchType.EAGER)
    private SubWorkflowData mr;
    @OneToOne(cascade = CascadeType.ALL, optional = true, fetch = FetchType.EAGER)
    private SubWorkflowData fs;

    private Date created;
    private Date updated;

    @PrePersist
    protected void onCreate() {
        created = new Date();
    }

    @PreUpdate
    protected void onUpdate() {
        updated = new Date();
    }
}
