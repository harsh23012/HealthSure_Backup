package com.infinite.ejb.insurance.model;

import java.io.Serializable;

public class InsuranceCoverageOption implements Serializable{

    private String coverageId;
    private InsurancePlan insurancePlan;
    private double premiumAmount;
    private double coverageAmount;
    private String status;

    public String getCoverageId() {
        return coverageId;
    }

    public InsuranceCoverageOption() {
		super();
		// TODO Auto-generated constructor stub
	}

	public void setCoverageId(String coverageId) {
        this.coverageId = coverageId;
    }

    public InsurancePlan getInsurancePlan() {
        return insurancePlan;
    }

    public void setInsurancePlan(InsurancePlan insurancePlan) {
        this.insurancePlan = insurancePlan;
    }

    public double getPremiumAmount() {
        return premiumAmount;
    }

    public void setPremiumAmount(double premiumAmount) {
        this.premiumAmount = premiumAmount;
    }

    public double getCoverageAmount() {
        return coverageAmount;
    }

    public void setCoverageAmount(double coverageAmount) {
        this.coverageAmount = coverageAmount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
