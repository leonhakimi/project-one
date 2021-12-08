package com.revature.dto;

import java.math.BigDecimal;
import java.util.Objects;

public class CreateReimbDTO {
	
	private BigDecimal reimbAmount;
	private String reimbType;
	private String reimbDescription;
	
	public CreateReimbDTO() {
		
	}
	
	public CreateReimbDTO(BigDecimal amount, String type, String description) {
		this.reimbAmount = amount;
		this.reimbType = type;
		this.reimbDescription = description;
	}

	public BigDecimal getReimbAmount() {
		return reimbAmount;
	}

	public void setReimbAmount(BigDecimal reimbAmount) {
		this.reimbAmount = reimbAmount;
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

	@Override
	public int hashCode() {
		return Objects.hash(reimbAmount, reimbDescription, reimbType);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CreateReimbDTO other = (CreateReimbDTO) obj;
		return Objects.equals(reimbAmount, other.reimbAmount)
				&& Objects.equals(reimbDescription, other.reimbDescription)
				&& Objects.equals(reimbType, other.reimbType);
	}

	@Override
	public String toString() {
		return "CreateReimbDTO [reimbAmount=" + reimbAmount + ", reimbType=" + reimbType + ", reimbDescription="
				+ reimbDescription + "]";
	}
	
	
	
}
