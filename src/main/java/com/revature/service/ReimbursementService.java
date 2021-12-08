package com.revature.service;

import java.io.InputStream;
import java.math.BigDecimal;
import java.security.InvalidParameterException;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.revature.dao.ReimbursementDAO;
import com.revature.exception.ReimbNotFoundException;
import com.revature.exception.ReimbursementAlreadyResolvedException;
import com.revature.exception.ReimbursementNotFoundException;
import com.revature.exception.UnacceptedStatusException;
import com.revature.exception.UnauthorizedException;
import com.revature.model.Reimbursement;
import com.revature.model.User;

public class ReimbursementService {
	private ReimbursementDAO reimbDao;

	public ReimbursementService() {
		this.reimbDao = new ReimbursementDAO();
	}
	public ReimbursementService(ReimbursementDAO reimbDao) {
		this.reimbDao = reimbDao;
	}

	public List<Reimbursement> getReimbs(User user) throws SQLException {
		List<Reimbursement> reimbs = null;

		if (user.getRole().equals("employee")) {
			reimbs = this.reimbDao.getReimbsForEmployee(user.getId());
		} else if (user.getRole().equals("manager")) {
			reimbs = this.reimbDao.getAllReimbs();
		}
		return reimbs;
	}

	public Reimbursement resolveReimb(String reimbId, User user, String status)
			throws SQLException, ReimbursementAlreadyResolvedException, ReimbursementNotFoundException, UnacceptedStatusException {
		try {
			int id = Integer.parseInt(reimbId);
			
			Set<String> allowedResolutionStates = new HashSet<>();
			allowedResolutionStates.add("APPROVED");
			allowedResolutionStates.add("DENIED");
			
			
			if (!allowedResolutionStates.contains(status)) {
				throw new UnacceptedStatusException("Status can only  be APPROVED or DENIED");
			}
			
			Reimbursement reimb = this.reimbDao.getReimbById(id);

			if (reimb == null) {
				throw new ReimbursementNotFoundException("Reimbursement with id " + id + " was not found");
			}

			if (reimb.getReimbResolver() == 0) {
				this.reimbDao.resolveReimb(id, user.getId(), status);
			} else {
				throw new ReimbursementAlreadyResolvedException("reimbursement already resolved");
			}
			return this.reimbDao.getReimbById(id);
		} catch (NumberFormatException e) {
			throw new InvalidParameterException("Reimbursement id supplied must be an int");
		}

	}

	public Reimbursement addReimb(User user, String mimeType, String reimbType, String reimbDescription,
			String reimbAmount, InputStream content) throws SQLException {
		Set<String> allowedFileTypes = new HashSet<>();
		allowedFileTypes.add("image/jpeg");
		allowedFileTypes.add("image/png");
		allowedFileTypes.add("image/gif");

		Set<String> allowedReimbTypes = new HashSet<>();
		allowedReimbTypes.add("FOOD");
		allowedReimbTypes.add("LODGING");
		allowedReimbTypes.add("TRAVEL");
		allowedReimbTypes.add("OTHER");

		if (!allowedFileTypes.contains(mimeType)) {
			throw new InvalidParameterException("When adding an assignment image, only PNG, JPEG, or GIF are allowed");
		}

		if (!allowedReimbTypes.contains(reimbType)) {
			throw new InvalidParameterException("Unaccepted Reimbursement type");
		}
		try {
			BigDecimal amount = new BigDecimal(reimbAmount);

			int authorId = user.getId();
			Reimbursement addedReimb = this.reimbDao.addReimb(reimbType, reimbDescription, amount, authorId, content);
			return addedReimb;
		} catch (NumberFormatException e) {
			throw new InvalidParameterException("invalid amount");
		}

	}

	public InputStream getReceiptFromReimbById(User currentlyLoggedInUser, String reimbId) throws SQLException, UnauthorizedException, ReimbursementNotFoundException {
		try {
			int id = Integer.parseInt(reimbId);
			
			if (this.reimbDao.getReimbById(id) == null) {
				throw new ReimbursementNotFoundException("Reimbursement with id of " + id + " not found");
			}
			
			if (currentlyLoggedInUser.getRole().equals("employee")) {
				int employeeId = currentlyLoggedInUser.getId();
				List<Reimbursement> reimbsThatbelongToEmployee = this.reimbDao.getReimbsForEmployee(employeeId);
				
				Set<Integer> reimbIdsEncountered = new HashSet<>();
				for( Reimbursement r : reimbsThatbelongToEmployee) {
					reimbIdsEncountered.add(r.getReimbId());
				}
				
				if (!reimbIdsEncountered.contains(id)) {
					throw new UnauthorizedException("You cannot access reimbursement receipts that do not belong to yourself");
				}
			}
			
			InputStream image = this.reimbDao.getReceiptFromReimbById(id);
			
			return image;
			
		} catch (NumberFormatException e) {
			throw new InvalidParameterException("Reimbusement id supplied must be an int");
		}
	}
}
