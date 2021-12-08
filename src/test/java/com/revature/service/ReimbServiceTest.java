package com.revature.service;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.security.InvalidParameterException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.tika.io.IOUtils;
import org.checkerframework.checker.units.qual.m;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.revature.dao.ReimbursementDAO;
import com.revature.exception.ReimbursementAlreadyResolvedException;
import com.revature.exception.ReimbursementNotFoundException;
import com.revature.exception.UnacceptedStatusException;
import com.revature.exception.UnauthorizedException;
import com.revature.model.Reimbursement;
import com.revature.model.User;

public class ReimbServiceTest {
	private ReimbursementService sut;

	@Test
	public void testGetReimbsForEmployeePositive() throws SQLException {
		ReimbursementDAO mockReimbDao = mock(ReimbursementDAO.class);

		User user = new User(1, "Leon", "Hakimi", "employee", "lahakimi", "Password!", "leonhakimi@gmail.com");

		Reimbursement reimb1 = new Reimbursement(1, new BigDecimal("1244"), new Date(1638850308104L), null, "PENDING",
				"FOOD", "Sample description", 1, 0);
		Reimbursement reimb2 = new Reimbursement(2, new BigDecimal("344"), new Date(1738850308104L), null, "PENDING",
				"TRAVEL", "Sample description", 1, 0);
		Reimbursement reimb3 = new Reimbursement(3, new BigDecimal("234.46"), new Date(1838850308104L),
				new Date(1938850308104L), "ACCEPTED", "OTHER", "Sample description", 1, 2);

		List<Reimbursement> listOfReimbs = new ArrayList<>();
		listOfReimbs.add(reimb1);
		listOfReimbs.add(reimb2);
		listOfReimbs.add(reimb3);

		when(mockReimbDao.getReimbsForEmployee(eq(1))).thenReturn(listOfReimbs);

		ReimbursementService reimbService = new ReimbursementService(mockReimbDao);

		List<Reimbursement> actual = reimbService.getReimbs(user);

		List<Reimbursement> expected = new ArrayList<>();

		expected.add(reimb1);
		expected.add(reimb2);
		expected.add(reimb3);

		Assertions.assertEquals(expected, actual);
	}

	@Test
	public void testGetReimbsForManagerPositive() throws SQLException {
		ReimbursementDAO mockReimbDao = mock(ReimbursementDAO.class);

		User user = new User(2, "Leon", "Hakimi", "manager", "lahakimi", "Password!", "leonhakimi@gmail.com");

		Reimbursement reimb1 = new Reimbursement(1, new BigDecimal("1244"), new Date(1638850308104L), null, "PENDING",
				"FOOD", "Sample description", 1, 0);
		Reimbursement reimb2 = new Reimbursement(2, new BigDecimal("344"), new Date(1738850308104L), null, "PENDING",
				"TRAVEL", "Sample description", 1, 0);
		Reimbursement reimb3 = new Reimbursement(3, new BigDecimal("234.46"), new Date(1838850308104L),
				new Date(1938850308104L), "ACCEPTED", "OTHER", "Sample description", 1, 2);

		List<Reimbursement> listOfReimbs = new ArrayList<>();
		listOfReimbs.add(reimb1);
		listOfReimbs.add(reimb2);
		listOfReimbs.add(reimb3);

		when(mockReimbDao.getAllReimbs()).thenReturn(listOfReimbs);

		ReimbursementService reimbService = new ReimbursementService(mockReimbDao);

		List<Reimbursement> actual = reimbService.getReimbs(user);

		List<Reimbursement> expected = new ArrayList<>();

		expected.add(reimb1);
		expected.add(reimb2);
		expected.add(reimb3);

		Assertions.assertEquals(expected, actual);

	}

	@Test
	void testGetReimbsForEmployeeNegative() throws SQLException {
		ReimbursementDAO mockReimbDao = mock(ReimbursementDAO.class);
		User user = new User(1, "Leon", "Hakimi", "employee", "lahakimi", "Password!", "leonhakimi@gmail.com");

		when(mockReimbDao.getReimbsForEmployee(eq(1))).thenThrow(SQLException.class);

		ReimbursementService rs = new ReimbursementService(mockReimbDao);

		Assertions.assertThrows(SQLException.class, () -> {
			rs.getReimbs(user);
		});
	}

	@Test
	void testGetReimbsForManagerNegative() throws SQLException {
		ReimbursementDAO mockReimbDao = mock(ReimbursementDAO.class);
		User user = new User(2, "Leon", "Hakimi", "manager", "lahakimi", "Password!", "leonhakimi@gmail.com");

		when(mockReimbDao.getAllReimbs()).thenThrow(SQLException.class);

		ReimbursementService rs = new ReimbursementService(mockReimbDao);

		Assertions.assertThrows(SQLException.class, () -> {
			rs.getReimbs(user);
		});
	}

	@Test
	void testAddReimbPositive() throws SQLException, IOException {
		User user = new User(1, "Leon", "Hakimi", "employee", "lahakimi", "Password!", "leonhakimi@gmail.com");

		InputStream dummyInput = IOUtils.toInputStream("dummy input", "UTF-8");

		ReimbursementDAO mockReimbDao = mock(ReimbursementDAO.class);

		String mimeType = "image/png";
		String reimbType = "FOOD";
		String description = "Sample description";
		String amount = "124.23";

		when(mockReimbDao.addReimb(eq(reimbType), eq(description), eq(new BigDecimal(amount)), eq(1), eq(dummyInput)))
				.thenReturn(new Reimbursement(1, new BigDecimal("124.23"), new Date(1638850308104L), null, "PENDING",
						"FOOD", "Sample description", 1, 0));

		ReimbursementService rs = new ReimbursementService(mockReimbDao);

		Reimbursement expexcted = new Reimbursement(1, new BigDecimal("124.23"), new Date(1638850308104L), null,
				"PENDING", "FOOD", "Sample description", 1, 0);
		Reimbursement actual = rs.addReimb(user, mimeType, reimbType, description, amount, dummyInput);

		Assertions.assertEquals(expexcted, actual);
	}

	@Test
	void testAddReimbFileTypeNotAccepted() throws IOException {
		User user = new User(1, "Leon", "Hakimi", "employee", "lahakimi", "Password!", "leonhakimi@gmail.com");

		InputStream dummyInput = IOUtils.toInputStream("dummy input", "UTF-8");

		ReimbursementDAO mockReimbDao = mock(ReimbursementDAO.class);

		String mimeType = "wrong";
		String reimbType = "FOOD";
		String description = "Sample description";
		String amount = "124.23";

		ReimbursementService rs = new ReimbursementService(mockReimbDao);

		Assertions.assertThrows(InvalidParameterException.class, () -> {
			rs.addReimb(user, mimeType, reimbType, description, amount, dummyInput);
		});

	}

	@Test
	void testAddReimbImproperTypeRequested() throws IOException {
		User user = new User(1, "Leon", "Hakimi", "employee", "lahakimi", "Password!", "leonhakimi@gmail.com");

		InputStream dummyInput = IOUtils.toInputStream("dummy input", "UTF-8");

		ReimbursementDAO mockReimbDao = mock(ReimbursementDAO.class);

		String mimeType = "image/jpeg";
		String reimbType = "something wrong";
		String description = "Sample description";
		String amount = "124.23";

		ReimbursementService rs = new ReimbursementService(mockReimbDao);

		Assertions.assertThrows(InvalidParameterException.class, () -> {
			rs.addReimb(user, mimeType, reimbType, description, amount, dummyInput);
		});
	}

	@Test
	void testAddReimbInvalidAmount() throws IOException {
		User user = new User(1, "Leon", "Hakimi", "employee", "lahakimi", "Password!", "leonhakimi@gmail.com");

		InputStream dummyInput = IOUtils.toInputStream("dummy input", "UTF-8");

		ReimbursementDAO mockReimbDao = mock(ReimbursementDAO.class);

		String mimeType = "image/jpeg";
		String reimbType = "FOOD";
		String description = "Sample description";
		String amount = "wrong";

		ReimbursementService rs = new ReimbursementService(mockReimbDao);

		Assertions.assertThrows(InvalidParameterException.class, () -> {
			rs.addReimb(user, mimeType, reimbType, description, amount, dummyInput);
		});
	}

	@Test
	void testGetReceiptFromReimbByUserIdPositive()
			throws SQLException, IOException, UnauthorizedException, ReimbursementNotFoundException {
		User user = new User(1, "Leon", "Hakimi", "employee", "lahakimi", "Password!", "leonhakimi@gmail.com");

		ReimbursementDAO mockReimbDao = mock(ReimbursementDAO.class);

		Reimbursement reimb1 = new Reimbursement(1, new BigDecimal("1244"), new Date(1638850308104L), null, "PENDING",
				"FOOD", "Sample description", 1, 0);
		Reimbursement reimb2 = new Reimbursement(2, new BigDecimal("344"), new Date(1738850308104L), null, "PENDING",
				"TRAVEL", "Sample description", 1, 0);
		Reimbursement reimb3 = new Reimbursement(3, new BigDecimal("234.46"), new Date(1838850308104L),
				new Date(1938850308104L), "ACCEPTED", "OTHER", "Sample description", 1, 2);

		List<Reimbursement> listOfReimbs = new ArrayList<>();
		listOfReimbs.add(reimb1);
		listOfReimbs.add(reimb2);
		listOfReimbs.add(reimb3);

		InputStream dummy = IOUtils.toInputStream("dummy input", "UTF-8");

		when(mockReimbDao.getReimbById(eq(3))).thenReturn(reimb3);
		when(mockReimbDao.getReimbsForEmployee(eq(1))).thenReturn(listOfReimbs);
		when(mockReimbDao.getReceiptFromReimbById(eq(3))).thenReturn(dummy);

		ReimbursementService rs = new ReimbursementService(mockReimbDao);

		InputStream expected = dummy;
		InputStream actual = rs.getReceiptFromReimbById(user, "3");

		Assertions.assertEquals(expected, actual);
	}

	@Test
	void testGetReceiptFromReimbByUserIdButReimbIdInvalid() {
		User user = new User(1, "Leon", "Hakimi", "employee", "lahakimi", "Password!", "leonhakimi@gmail.com");

		ReimbursementDAO mockReimbDao = mock(ReimbursementDAO.class);

		ReimbursementService rs = new ReimbursementService(mockReimbDao);

		Assertions.assertThrows(InvalidParameterException.class, () -> {
			rs.getReceiptFromReimbById(user, "2i1g");
		});
	}

	@Test
	void testGetReceiptFromReimbByUserIdButReimbDoesNotExist() {
		User user = new User(1, "Leon", "Hakimi", "employee", "lahakimi", "Password!", "leonhakimi@gmail.com");

		ReimbursementDAO mockreimbDao = mock(ReimbursementDAO.class);

		ReimbursementService rs = new ReimbursementService(mockreimbDao);

		Assertions.assertThrows(ReimbursementNotFoundException.class, () -> {
			rs.getReceiptFromReimbById(user, "1");
		});
	}

	@Test
	void testGetReceiptFromReimbByUserIdButReimbWithIdDoesNotBelongToUser() throws SQLException {
		User user = new User(1, "Leon", "Hakimi", "employee", "lahakimi", "Password!", "leonhakimi@gmail.com");

		Reimbursement reimb1 = new Reimbursement(1, new BigDecimal("1244"), new Date(1638850308104L), null, "PENDING",
				"FOOD", "Sample description", 1, 0);
		Reimbursement reimb2 = new Reimbursement(2, new BigDecimal("344"), new Date(1738850308104L), null, "PENDING",
				"TRAVEL", "Sample description", 1, 0);
		Reimbursement reimb3 = new Reimbursement(3, new BigDecimal("234.46"), new Date(1838850308104L),
				new Date(1938850308104L), "ACCEPTED", "OTHER", "Sample description", 1, 2);

		Reimbursement reimb4 = new Reimbursement(4, new BigDecimal("123"), new Date(1838850308104L), null, "PENDING",
				"OTHER", "Sample Description", 45, 0);

		List<Reimbursement> listOfReimbs = new ArrayList<>();
		listOfReimbs.add(reimb1);
		listOfReimbs.add(reimb2);
		listOfReimbs.add(reimb3);

		ReimbursementDAO mockReimbDao = mock(ReimbursementDAO.class);

		when(mockReimbDao.getReimbById(eq(4))).thenReturn(reimb4);
		when(mockReimbDao.getReimbsForEmployee(eq(1))).thenReturn(listOfReimbs);

		ReimbursementService rs = new ReimbursementService(mockReimbDao);

		Assertions.assertThrows(UnauthorizedException.class, () -> {
			rs.getReceiptFromReimbById(user, "4");
		});

	}

	@Test
	void testResolveReimbPositive() throws SQLException, ReimbursementAlreadyResolvedException,
			ReimbursementNotFoundException, UnacceptedStatusException {
		User user = new User(1, "Leon", "Hakimi", "manager", "lahakimi", "Password!", "leonhakimi@gmail.com");
		String reimbId = "1";
		String status = "APPROVED";

		Reimbursement reimb1 = new Reimbursement(1, new BigDecimal("1244"), new Date(1638850308104L), null, "PENDING",
				"FOOD", "Sample description", 2, 0);

		Reimbursement updatedReimb = new Reimbursement(1, new BigDecimal("1244"), new Date(1638850308104L),
				new Date(1738850308104L), "APPROVED", "FOOD", "Sample description", 2, 1);

		ReimbursementDAO mockReimbDao = mock(ReimbursementDAO.class);

		when(mockReimbDao.getReimbById(eq(1))).thenReturn(reimb1).thenReturn(updatedReimb);

		ReimbursementService rs = new ReimbursementService(mockReimbDao);

		Assertions
				.assertEquals(
						new Reimbursement(1, new BigDecimal("1244"), new Date(1638850308104L), new Date(1738850308104L),
								"APPROVED", "FOOD", "Sample description", 2, 1),
						rs.resolveReimb("1", user, "APPROVED"));
	}

	@Test
	void testResolveReimbInvalidId() {
		ReimbursementDAO mockReimbursementDao = mock(ReimbursementDAO.class);

		ReimbursementService rs = new ReimbursementService(mockReimbursementDao);

		Assertions.assertThrows(InvalidParameterException.class, () -> {
			rs.resolveReimb("lkhj", new User(), "APPROVED");
		});
	}

	@Test
	void testResolveReimbInvalidStatus() {
		ReimbursementDAO mockReimbursementDao = mock(ReimbursementDAO.class);

		ReimbursementService rs = new ReimbursementService(mockReimbursementDao);

		Assertions.assertThrows(UnacceptedStatusException.class, () -> {
			rs.resolveReimb("1", new User(), "ytjmt");
		});
	}
	
	@Test
	void testResolveReimbButReimbDoesNotExist() {
		ReimbursementDAO mockReimbursementDao = mock(ReimbursementDAO.class);

		ReimbursementService rs = new ReimbursementService(mockReimbursementDao);

		Assertions.assertThrows(ReimbursementNotFoundException.class, () -> {
			rs.resolveReimb("1", new User(), "APPROVED");
		});
	}
	
	@Test
	void testResolveReimbButReimbAlreadyResolved() throws SQLException {
		User user = new User(1, "Leon", "Hakimi", "manager", "lahakimi", "Password!", "leonhakimi@gmail.com");
		String reimbId = "1";
		String status = "APPROVED";

		Reimbursement reimb1 = new Reimbursement(1, new BigDecimal("1244"), new Date(1638850308104L), new Date(1738850308104L), "APPROVED",
				"FOOD", "Sample description", 2, 4);
		

		ReimbursementDAO mockReimbDao = mock(ReimbursementDAO.class);

		when(mockReimbDao.getReimbById(eq(1))).thenReturn(reimb1);
		
		ReimbursementService rs = new ReimbursementService(mockReimbDao);
		
		Assertions.assertThrows(ReimbursementAlreadyResolvedException.class, () -> {
			rs.resolveReimb(reimbId, user, status);
		});
	}

}
