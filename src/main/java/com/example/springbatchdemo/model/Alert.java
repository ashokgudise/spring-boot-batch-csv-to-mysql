package com.ashok.demos.mysqldemo.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import java.time.LocalDateTime;

@Table(schema = "deals_db", name = "ALERTSUBSCRIPTION")
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

    @Column
    private LocalDateTime date;

}
