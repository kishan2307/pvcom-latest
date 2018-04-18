package com.pvcom.model;

import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@ToString
public class AutoEntryLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private Date created;
    private String msg;
    private String emailFrom;
    private String emailto;
    private String subject;
    private String filePath;

    @PrePersist
    protected void onCreate() {
        created = new Date();
    }
}
