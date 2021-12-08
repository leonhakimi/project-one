package com.revature.service;

import com.revature.exception.UnauthorizedException;
import com.revature.model.User;

public class AuthorizationService {
	public void authorizeEmployeeAndManager(User user) throws UnauthorizedException {
		if (user == null || !(user.getRole().equals("employee") || user.getRole().equals("manager"))) {
			throw new UnauthorizedException("You must have a regular or admin role to access this resource");
		}
	}
	
	public void authorizeManager(User user) throws UnauthorizedException {
		if (user == null || !user.getRole().equals("manager")) {
			throw new UnauthorizedException("You must have an admin role to access this resource");
		}
	}
	
	public void authorizeEmployee(User user) throws UnauthorizedException {
		if (user == null || !user.getRole().equals("employee")) {
			throw new UnauthorizedException("You must have an employee role to access this resource");
		}
	}
}
