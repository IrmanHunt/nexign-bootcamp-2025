package com.romanlotohin.nexign_bootcamp_2025.dto;

public class UdrDto {
    private String msisdn;
    private CallDuration incomingCall;
    private CallDuration outgoingCall;

    public UdrDto(String msisdn, CallDuration incomingCall, CallDuration outgoingCall) {
        this.msisdn = msisdn;
        this.incomingCall = incomingCall;
        this.outgoingCall = outgoingCall;
    }

    public String getMsisdn() {
        return msisdn;
    }

    public void setMsisdn(String msisdn) {
        this.msisdn = msisdn;
    }

    public CallDuration getIncomingCall() {
        return incomingCall;
    }

    public void setIncomingCall(CallDuration incomingCall) {
        this.incomingCall = incomingCall;
    }

    public CallDuration getOutgoingCall() {
        return outgoingCall;
    }

    public void setOutgoingCall(CallDuration outgoingCall) {
        this.outgoingCall = outgoingCall;
    }

    @Override
    public String toString() {
        return "UdrDto{" +
                "msisdn='" + msisdn + '\'' +
                ", incomingCall=" + incomingCall +
                ", outgoingCall=" + outgoingCall +
                '}';
    }
}