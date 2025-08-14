package com.infinite.jsf.provider.model;

import java.util.Date;
public class PasswordHistory {
	private int id;
	private Provider provider;
	private String passwordHash;
	private Date createdAt;
	public PasswordHistory(int id, Provider provider, String passwordHash, Date createdAt) {
		super();
		this.id = id;
		this.provider = provider;
		this.passwordHash = passwordHash;
		this.createdAt = createdAt;
	}
	  public PasswordHistory(Provider provider, String passwordHash, Date createdAt) {
		    this.provider     = provider;
		    this.passwordHash = passwordHash;
		    this.createdAt    = createdAt;
		}

	  public PasswordHistory() {
		super();
		// TODO Auto-generated constructor stub
	}
	  @Override
	public String toString() {
		return "PasswordHistory [id=" + id +  ", passwordHash=" + passwordHash
				+ ", createdAt=" + createdAt + "]";
	}
	  public int getId() {
		  return id;
	  }
	  public void setId(int id) {
		  this.id = id;
	  }
	  public Provider getProvider() {
		  return provider;
	  }
	  public void setProvider(Provider provider) {
		  this.provider = provider;
	  }
	  public String getPasswordHash() {
		  return passwordHash;
	  }
	  public void setPasswordHash(String passwordHash) {
		  this.passwordHash = passwordHash;
	  }
	  public Date getCreatedAt() {
		  return createdAt;
	  }
	  public void setCreatedAt(Date createdAt) {
		  this.createdAt = createdAt;
	  }

}
