package com.romanlotohin.nexign_bootcamp_2025.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.Objects;

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

    public CdrRecord() {
    }

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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CdrRecord record)) return false;
        return Objects.equals(getId(), record.getId()) &&
                Objects.equals(getCallType(), record.getCallType()) &&
                Objects.equals(getCallerMsisdn(), record.getCallerMsisdn()) &&
                Objects.equals(getCalleeMsisdn(), record.getCalleeMsisdn()) &&
                Objects.equals(getCallStart(), record.getCallStart()) && Objects.equals(getCallEnd(), record.getCallEnd());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getCallType(), getCallerMsisdn(), getCalleeMsisdn(), getCallStart(), getCallEnd());
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
