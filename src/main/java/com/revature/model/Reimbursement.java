package com.revature.model;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;

public class Reimbursement {
	private int reimbId;
	private BigDecimal reimbAmount;
	private Date reimbSubmitted;
	private Date reimbResolved;
	private String reimbStatus;
	private String reimbType;
	private String reimbDescription;
	private int reimbAuthor;
	private int reimbResolver;
	
	public Reimbursement() {
		
	}
	public Reimbursement(int id, BigDecimal amount, Date submitted, Date resolved, String status, String type, String description, int author, int resolver) {
		this.reimbId = id;
		this.reimbAmount = amount;
		this.reimbSubmitted = submitted;
		this.reimbResolved = resolved;
		this.reimbStatus = status;
		this.reimbType = type;
		this.reimbDescription = description;
		this.reimbAuthor = author;
		this.reimbResolver = resolver;
	}
	public int getReimbId() {
		return reimbId;
	}
	public void setReimbId(int reimbId) {
		this.reimbId = reimbId;
	}
	public BigDecimal getReimbAmount() {
		return reimbAmount;
	}
	public void setReimbAmount(BigDecimal reimbAmount) {
		this.reimbAmount = reimbAmount;
	}
	public Date getReimbSubmitted() {
		return reimbSubmitted;
	}
	public void setReimbSubmitted(Date reimbSubmitted) {
		this.reimbSubmitted = reimbSubmitted;
	}
	public Date getReimbResolved() {
		return reimbResolved;
	}
	public void setReimbResolved(Date reimbResolved) {
		this.reimbResolved = reimbResolved;
	}
	public String getReimbStatus() {
		return reimbStatus;
	}
	public void setReimbStatus(String reimbStatus) {
		this.reimbStatus = reimbStatus;
	}
	public String getReimbType() {
		return reimbType;
	}
	public void setReimbType(String reimbType) {
		this.reimbType = reimbType;
	}
	public String getReimbDescription() {
		return reimbDescription;
	}
	public void setReimbDescription(String reimbDescription) {
		this.reimbDescription = reimbDescription;
	}
	public int getReimbAuthor() {
		return reimbAuthor;
	}
	public void setReimbAuthor(int reimbAuthor) {
		this.reimbAuthor = reimbAuthor;
	}
	public int getReimbResolver() {
		return reimbResolver;
	}
	public void setReimbResolver(int reimbResolver) {
		this.reimbResolver = reimbResolver;
	}
	@Override
	public int hashCode() {
		return Objects.hash(reimbAmount, reimbAuthor, reimbDescription, reimbId, reimbResolved, reimbResolver,
				reimbStatus, reimbSubmitted, reimbType);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Reimbursement other = (Reimbursement) obj;
		return Objects.equals(reimbAmount, other.reimbAmount) && reimbAuthor == other.reimbAuthor
				&& Objects.equals(reimbDescription, other.reimbDescription) && reimbId == other.reimbId
				&& Objects.equals(reimbResolved, other.reimbResolved) && reimbResolver == other.reimbResolver
				&& Objects.equals(reimbStatus, other.reimbStatus)
				&& Objects.equals(reimbSubmitted, other.reimbSubmitted) && Objects.equals(reimbType, other.reimbType);
	}
	@Override
	public String toString() {
		return "Reimbursement [reimbId=" + reimbId + ", reimbAmount=" + reimbAmount + ", reimbSubmitted="
				+ reimbSubmitted + ", reimbResolved=" + reimbResolved + ", reimbStatus=" + reimbStatus + ", reimbType="
				+ reimbType + ", reimbDescription=" + reimbDescription + ", reimbAuthor=" + reimbAuthor
				+ ", reimbResolver=" + reimbResolver + "]";
	}
	
}
