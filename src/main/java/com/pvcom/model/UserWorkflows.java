package com.pvcom.model;

import lombok.Data;

import java.util.Date;

import javax.persistence.*;

@Entity
@Data
public class UserWorkflows {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @OneToOne(cascade = CascadeType.ALL, optional = false, fetch = FetchType.LAZY)
    private Workflow workflow;
    private int userId;
    private int deUserId;
    private int qcUserId;
    private int mrUserId;
    private int fsUserId;
    private int createdBy;
    private int UpdatedBy;
    private int status = 1;
    private String worldWideUniqId;

    private Date createdOn;
    private Date updatedOn;

    @PrePersist
    protected void onCreate() {
        createdOn = new Date();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedOn = new Date();
    }
}
