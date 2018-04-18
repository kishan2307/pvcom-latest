package com.pvcom.model;

import lombok.Data;

import java.util.Date;

import javax.persistence.*;

@Entity
@Data
public class SubWorkflowData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private int doneBy;
    private String comment;
    private String country;

    public int getId() {
        return id;
    }

    private Date created;
    private Date doneDate;

    @PrePersist
    protected void onCreate() {
        created = new Date();
    }
}
