package com.example.springbatchdemo.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Date;

@Table( name = "ALERTSUBSCRIPTION")
@Entity
@Data
public class Alert {

    @Id
    private Long id;

    @Column
    private String name;

    @Column
    private String type;

    @Column
    private String email;

    @Column
    private String sms;


}
