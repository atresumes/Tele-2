package com.trigma.tiktok.model;

public class PrescriptionObject {
    public int RefillRequestsCount;
    public String Status;
    public int TransactionErrorsCount;
    public String error;
    public String url;

    public int getRefillRequestsCount() {
        return this.RefillRequestsCount;
    }

    public void setRefillRequestsCount(int refillRequestsCount) {
        this.RefillRequestsCount = refillRequestsCount;
    }

    public String getStatus() {
        return this.Status;
    }

    public void setStatus(String status) {
        this.Status = status;
    }

    public String getError() {
        return this.error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public int getTransactionErrorsCount() {
        return this.TransactionErrorsCount;
    }

    public void setTransactionErrorsCount(int transactionErrorsCount) {
        this.TransactionErrorsCount = transactionErrorsCount;
    }

    public String getUrl() {
        return this.url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
