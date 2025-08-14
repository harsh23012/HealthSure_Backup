package com.infinite.jsf.provider.model;
import java.util.Date;

import com.infinite.jsf.recipient.model.Recipient;


public class PaymentHistory {

	private String paymentId;
	
	private Recipient recipient;
	private Provider providers;
	private Double amount;
	private String paymentMethod;
	private String paymentStatus;
	private Date paymentDate;
	private String remarks;


	public Recipient getRecipient() {
		return recipient;
	}

	
	public Provider getProvider() {
		return providers;
	}

	public void setProvider(Provider providers) {
		this.providers = providers;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public String getPaymentMethod() {
		return paymentMethod;
	}

	public void setPaymentMethod(String paymentMethod) {
		this.paymentMethod = paymentMethod;
	}

	public String getPaymentStatus() {
		return paymentStatus;
	}

	public void setPaymentStatus(String paymentStatus) {
		this.paymentStatus = paymentStatus;
	}

	public Date getPaymentDate() {
		return paymentDate;
	}

	public void setPaymentDate(Date paymentDate) {
		this.paymentDate = paymentDate;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}


	public String getPaymentId() {
		return paymentId;
	}


	public void setPaymentId(String paymentId) {
		this.paymentId = paymentId;
	}


	public void setRecipient(Recipient recipient) {
		this.recipient = recipient;
	}


	@Override
	public String toString() {
		return "PaymentHistory [paymentId=" + paymentId + ", recipient=" + recipient + ", providers=" + providers
				+ ", amount=" + amount + ", paymentMethod=" + paymentMethod + ", paymentStatus=" + paymentStatus
				+ ", paymentDate=" + paymentDate + ", remarks=" + remarks + "]";
	}

	
	
}
