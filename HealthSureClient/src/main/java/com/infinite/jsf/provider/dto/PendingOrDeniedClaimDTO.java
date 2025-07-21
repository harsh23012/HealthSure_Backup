package com.infinite.jsf.provider.dto;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class PendingOrDeniedClaimDTO {
    private String claimId;
    private String subscribeId;
    private String procedureId;
    private String providerId;
    private String healthId;
    private String claimStatus;
    private Timestamp claimDate;
    private BigDecimal amountClaimed;
    private BigDecimal amountApproved;
    private String description; // from Claim_history
    private Timestamp actionDate;
	public String getClaimId() {
		return claimId;
	}
	public void setClaimId(String claimId) {
		this.claimId = claimId;
	}
	public String getSubscribeId() {
		return subscribeId;
	}
	public void setSubscribeId(String subscribeId) {
		this.subscribeId = subscribeId;
	}
	public String getProcedureId() {
		return procedureId;
	}
	public void setProcedureId(String procedureId) {
		this.procedureId = procedureId;
	}
	public String getProviderId() {
		return providerId;
	}
	public void setProviderId(String providerId) {
		this.providerId = providerId;
	}
	public String getHealthId() {
		return healthId;
	}
	public void setHealthId(String healthId) {
		this.healthId = healthId;
	}
	public String getClaimStatus() {
		return claimStatus;
	}
	public void setClaimStatus(String claimStatus) {
		this.claimStatus = claimStatus;
	}
	public Timestamp getClaimDate() {
		return claimDate;
	}
	public void setClaimDate(Timestamp claimDate) {
		this.claimDate = claimDate;
	}
	public BigDecimal getAmountClaimed() {
		return amountClaimed;
	}
	public void setAmountClaimed(BigDecimal amountClaimed) {
		this.amountClaimed = amountClaimed;
	}
	public BigDecimal getAmountApproved() {
		return amountApproved;
	}
	public void setAmountApproved(BigDecimal amountApproved) {
		this.amountApproved = amountApproved;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Timestamp getActionDate() {
		return actionDate;
	}
	public void setActionDate(Timestamp actionDate) {
		this.actionDate = actionDate;
	}
	@Override
	public String toString() {
		return "PendingOrDeniedClaimDTO [claimId=" + claimId + ", subscribeId=" + subscribeId + ", procedureId="
				+ procedureId + ", providerId=" + providerId + ", healthId=" + healthId + ", claimStatus=" + claimStatus
				+ ", claimDate=" + claimDate + ", amountClaimed=" + amountClaimed + ", amountApproved=" + amountApproved
				+ ", description=" + description + ", actionDate=" + actionDate + "]";
	}
	public PendingOrDeniedClaimDTO(String claimId, String subscribeId, String procedureId, String providerId,
			String healthId, String claimStatus, Timestamp claimDate, BigDecimal amountClaimed,
			BigDecimal amountApproved, String description, Timestamp actionDate) {
		super();
		this.claimId = claimId;
		this.subscribeId = subscribeId;
		this.procedureId = procedureId;
		this.providerId = providerId;
		this.healthId = healthId;
		this.claimStatus = claimStatus;
		this.claimDate = claimDate;
		this.amountClaimed = amountClaimed;
		this.amountApproved = amountApproved;
		this.description = description;
		this.actionDate = actionDate;
	}
	public PendingOrDeniedClaimDTO() {
		super();
		// TODO Auto-generated constructor stub
	}
    
}
