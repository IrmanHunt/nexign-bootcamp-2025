package com.romanlotohin.nexign_bootcamp_2025.entity;

import jakarta.persistence.*;

@Entity
public class Subscriber {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String msisdn;

    public Subscriber() {
    }

    public Subscriber(String msisdn) {
        this.msisdn = msisdn;
    }

    public Long getId() {
        return id;
    }

    public String getMsisdn() {
        return msisdn;
    }

    public void setMsisdn(String msisdn) {
        this.msisdn = msisdn;
    }
}
