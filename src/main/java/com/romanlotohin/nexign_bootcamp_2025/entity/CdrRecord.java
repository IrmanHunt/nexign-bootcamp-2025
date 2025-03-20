package com.romanlotohin.nexign_bootcamp_2025.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class CdrRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String callType;

    @Column(nullable = false)
    private String callerMsisdn;

    @Column(nullable = false)
    private String calleeMsisdn;

    @Column(nullable = false)
    private LocalDateTime callStart;

    @Column(nullable = false)
    private LocalDateTime callEnd;

    public CdrRecord() {}

    public CdrRecord(String callType, String callerMsisdn, String calleeMsisdn, LocalDateTime callStart, LocalDateTime callEnd) {
        this.callType = callType;
        this.callerMsisdn = callerMsisdn;
        this.calleeMsisdn = calleeMsisdn;
        this.callStart = callStart;
        this.callEnd = callEnd;
    }

    public Long getId() {
        return id;
    }

    public String getCallType() {
        return callType;
    }

    public void setCallType(String callType) {
        this.callType = callType;
    }

    public String getCallerMsisdn() {
        return callerMsisdn;
    }

    public void setCallerMsisdn(String callerMsisdn) {
        this.callerMsisdn = callerMsisdn;
    }

    public String getCalleeMsisdn() {
        return calleeMsisdn;
    }

    public void setCalleeMsisdn(String calleeMsisdn) {
        this.calleeMsisdn = calleeMsisdn;
    }

    public LocalDateTime getCallStart() {
        return callStart;
    }

    public void setCallStart(LocalDateTime callStart) {
        this.callStart = callStart;
    }

    public LocalDateTime getCallEnd() {
        return callEnd;
    }

    public void setCallEnd(LocalDateTime callEnd) {
        this.callEnd = callEnd;
    }

    @Override
    public String toString() {
        return "CdrRecord{" +
                "id=" + id +
                ", callType='" + callType + '\'' +
                ", callerMsisdn='" + callerMsisdn + '\'' +
                ", calleeMsisdn='" + calleeMsisdn + '\'' +
                ", callStart=" + callStart +
                ", callEnd=" + callEnd +
                '}';
    }
}
