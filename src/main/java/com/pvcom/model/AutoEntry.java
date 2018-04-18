package com.pvcom.model;

import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import java.util.Date;

@Data
@ToString
@Entity
public class AutoEntry {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;
    String email;
    String password;
    String host;
    String receiveEmailFrom;
    String subject;
    int port;
    int user;
    private Date created;

    @PrePersist
    protected void onCreate() {
        created = new Date();
    }


}
