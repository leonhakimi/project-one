package com.revature.dto;


import java.util.Date;
import java.util.Objects;

public class ResolveReimbDTO {
	private String status;
	
	public ResolveReimbDTO() {
		
	}
	
	public ResolveReimbDTO(String status) {
		this.status = status;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Override
	public int hashCode() {
		return Objects.hash(status);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ResolveReimbDTO other = (ResolveReimbDTO) obj;
		return Objects.equals(status, other.status);
	}

	@Override
	public String toString() {
		return "ResolveReimbDTO [status=" + status + "]";
	}
	
	
}
