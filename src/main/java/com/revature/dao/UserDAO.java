package com.revature.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.revature.model.User;
import com.revature.utility.JDBCUtility;

public class UserDAO {
	public User getUserByUsernameAndPassword(String username, String password) throws SQLException {
		try (Connection con = JDBCUtility.getConnection()) {
			String sql = "SELECT * FROM users WHERE username = ? and user_password = ?";
			
			PreparedStatement pstmt = con.prepareStatement(sql);
			pstmt.setString(1, username);
			pstmt.setString(2, password);
			
			ResultSet rs = pstmt.executeQuery();
			
			if (rs.next()) {
				int id = rs.getInt("user_id");
				String firstName = rs.getString("user_first_name");
				String lastName = rs.getString("user_last_name");
				String user = rs.getString("username");
				String pass = rs.getString("user_password");
				String userRole = rs.getString("user_role");
				String email = rs.getString("user_email");
				
				return new User(id, firstName, lastName, userRole, user, pass, email);
			} else {
				return null;
			}
		}
	}
}
