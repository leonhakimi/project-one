package com.revature.dao;

import java.io.InputStream;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.revature.model.Reimbursement;
import com.revature.model.User;
import com.revature.utility.JDBCUtility;

public class ReimbursementDAO {

	public List<Reimbursement> getReimbsForEmployee(int userId) throws SQLException {
		try (Connection con = JDBCUtility.getConnection()) {

			List<Reimbursement> reimbs = new ArrayList<>();
			String sql = "SELECT reimb_id, reimb_amount, reimb_submitted, reimb_resolved, reimb_status, reimb_type, reimb_description, reimb_author, reimb_resolver FROM reimbursements WHERE reimb_author = ?";
			PreparedStatement pstmt = con.prepareStatement(sql);

			pstmt.setInt(1, userId);

			ResultSet rs = pstmt.executeQuery();

			while (rs.next()) {
				int id = rs.getInt("reimb_id");
				BigDecimal amount = rs.getBigDecimal("reimb_amount");
				Date submission = rs.getTimestamp("reimb_submitted");
				Date resolved = rs.getTimestamp("reimb_resolved");
				String status = rs.getString("reimb_status");
				String type = rs.getString("reimb_type");
				String description = rs.getString("reimb_description");
				int author = rs.getInt("reimb_author");
				int resolver = rs.getInt("reimb_resolver");

				Reimbursement reimb = new Reimbursement(id, amount, submission, resolved, status, type, description,
						author, resolver);

				reimbs.add(reimb);
			}
			return reimbs;
		}

	}

	public Reimbursement getReimbById(int id) throws SQLException {
		try (Connection con = JDBCUtility.getConnection()) {
			String sql = "SELECT reimb_id, reimb_amount, reimb_submitted, reimb_resolved, reimb_status, reimb_type, reimb_description, reimb_author, reimb_resolver FROM reimbursements WHERE reimb_id = ?";

			PreparedStatement pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, id);

			ResultSet rs = pstmt.executeQuery();

			if (rs.next()) {
				int reimbId = rs.getInt("reimb_id");
				BigDecimal amount = rs.getBigDecimal("reimb_amount");
				Date submission = rs.getTimestamp("reimb_submitted");
				Date resolved = rs.getTimestamp("reimb_resolved");
				String status = rs.getString("reimb_status");
				String type = rs.getString("reimb_type");
				String description = rs.getString("reimb_description");
				int author = rs.getInt("reimb_author");
				int resolver = rs.getInt("reimb_resolver");

				return new Reimbursement(reimbId, amount, submission, resolved, status, type, description, author,
						resolver);
			} else {
				return null;
			}

		}
	}

	public void resolveReimb(int id, int userId, String status) throws SQLException {
		try (Connection con = JDBCUtility.getConnection()) {
			String sql = "UPDATE reimbursements SET reimb_status = ?, reimb_resolver = ?, reimb_resolved = ? WHERE reimb_id = ?";

			PreparedStatement pstmt = con.prepareStatement(sql);

			pstmt.setString(1, status);
			pstmt.setInt(2, userId);
			pstmt.setTimestamp(3, new Timestamp(System.currentTimeMillis()));
			pstmt.setInt(4, id);

			int updatedRecords = pstmt.executeUpdate();

			if (updatedRecords != 1) {
				throw new SQLException("Something bad occured when trying to update reimbursement");
			}

		}

	}

	public List<Reimbursement> getAllReimbs() throws SQLException {
		try (Connection con = JDBCUtility.getConnection()) {

			List<Reimbursement> reimbs = new ArrayList<>();
			String sql = "SELECT reimb_id, reimb_amount, reimb_submitted, reimb_resolved, reimb_status, reimb_type, reimb_description, reimb_author, reimb_resolver FROM reimbursements";
			PreparedStatement pstmt = con.prepareStatement(sql);

			ResultSet rs = pstmt.executeQuery();

			while (rs.next()) {
				int id = rs.getInt("reimb_id");
				BigDecimal amount = rs.getBigDecimal("reimb_amount");
				Date submission = rs.getTimestamp("reimb_submitted");
				Date resolved = rs.getTimestamp("reimb_resolved");
				String status = rs.getString("reimb_status");
				String type = rs.getString("reimb_type");
				String description = rs.getString("reimb_description");
				int author = rs.getInt("reimb_author");
				int resolver = rs.getInt("reimb_resolver");

				Reimbursement reimb = new Reimbursement(id, amount, submission, resolved, status, type, description,
						author, resolver);

				reimbs.add(reimb);
			}
			return reimbs;
		}
	}

	public Reimbursement addReimb(String reimbType, String reimbDescription, BigDecimal amount, int authorId,
			InputStream content) throws SQLException {
		try (Connection con = JDBCUtility.getConnection()) {
			con.setAutoCommit(false);

			String sql = "INSERT INTO reimbursements (reimb_author, reimb_amount, reimb_type, reimb_description, reimb_receipt) VALUES (?,?,?,?,?);";
			PreparedStatement pstmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

			pstmt.setInt(1, authorId);
			pstmt.setBigDecimal(2, amount);
			pstmt.setString(3, reimbType);
			pstmt.setString(4, reimbDescription);
			pstmt.setBinaryStream(5, content);

			int numberOfInsertedRecords = pstmt.executeUpdate();

			if (numberOfInsertedRecords != 1) {
				throw new SQLException("Issue occurred when adding assignment");
			}

			ResultSet rs = pstmt.getGeneratedKeys();

			rs.next();
			int generatedId = rs.getInt(1);
			Date generatedDate = rs.getTimestamp(3);

			con.commit(); 
			
			return new Reimbursement(generatedId, amount, generatedDate, null, "PENDING", reimbType, reimbDescription, authorId, 0);
		}
	}

	public InputStream getReceiptFromReimbById(int id) throws SQLException {
		try (Connection con = JDBCUtility.getConnection()) {
			String sql = "SELECT reimb_receipt FROM reimbursements WHERE reimb_id = ?";
			
			PreparedStatement pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, id);
			ResultSet rs = pstmt.executeQuery();
			
			if (rs.next()) {
				InputStream image = rs.getBinaryStream("reimb_receipt");
				
				return image;
			}
			
			return null;
			
		}
		
	}

}
