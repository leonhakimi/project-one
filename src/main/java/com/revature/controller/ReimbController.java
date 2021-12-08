package com.revature.controller;

import java.io.InputStream;
import java.math.BigDecimal;
import java.util.List;

import org.apache.tika.Tika;

import com.revature.dto.CreateReimbDTO;
import com.revature.dto.MessageDTO;
import com.revature.dto.ResolveReimbDTO;
import com.revature.model.Reimbursement;
import com.revature.model.User;
import com.revature.service.AuthorizationService;
import com.revature.service.ReimbursementService;

import io.javalin.Javalin;
import io.javalin.http.Handler;
import io.javalin.http.UploadedFile;

public class ReimbController implements Controller {
	
	private AuthorizationService authorizationService;
	private ReimbursementService reimbService;
	
	public ReimbController() {
		this.authorizationService = new AuthorizationService();
		this.reimbService = new ReimbursementService();
	}
	
	private Handler getReimbs = (ctx) -> {
		User user = (User) ctx.req.getSession().getAttribute("currentUser");
		
		authorizationService.authorizeEmployeeAndManager(user);
		
		List<Reimbursement> reimbursements = this.reimbService.getReimbs(user);
		
		ctx.json(reimbursements);
	};
	
	private Handler createReimb = (ctx) -> {
		User user = (User) ctx.req.getSession().getAttribute("currentUser");
		
		authorizationService.authorizeEmployee(user);
		
		String reimbType = ctx.formParam("reimb_type");
		String reimbDescription = ctx.formParam("reimb_description");
		String reimbAmount = ctx.formParam("reimb_amount");
		
		UploadedFile file = ctx.uploadedFile("reimb_receipt");
		
		if (file == null) {
			ctx.status(400);
			ctx.json(new MessageDTO("Must have an image to upload"));
			return;
		}
		
		InputStream content = file.getContent();
		
		Tika tika = new Tika();
		
		String mimeType = tika.detect(content);
		
		Reimbursement createdReimbursement = this.reimbService.addReimb(user, mimeType, reimbType, reimbDescription, reimbAmount, content);
		ctx.json(createdReimbursement);
		ctx.status(201);
		
		int id = user.getId();
		System.out.println("create reimb for id of " + id);
	};
	
	private Handler resolveReimb = (ctx) -> {
		User user = (User) ctx.req.getSession().getAttribute("currentUser");
		
		authorizationService.authorizeManager(user);
		
		ResolveReimbDTO resolveReimbDto = ctx.bodyAsClass(ResolveReimbDTO.class);
		
		String reimbId = ctx.pathParam("id");
		
		Reimbursement resolvedReimb = this.reimbService.resolveReimb(reimbId, user, resolveReimbDto.getStatus());
		
		ctx.json(resolvedReimb);
	};
	
	private Handler getReceiptFromReimbById = (ctx) -> {
		User currentlyLoggedInUser = (User) ctx.req.getSession().getAttribute("currentUser");
		this.authorizationService.authorizeEmployeeAndManager(currentlyLoggedInUser);
		
		String reimbId = ctx.pathParam("id");
		
		InputStream image = this.reimbService.getReceiptFromReimbById(currentlyLoggedInUser, reimbId);
		
		Tika tika = new Tika();
		String mimeType = tika.detect(image);
		
		ctx.result(image);
	};
	
	
	
	
	@Override
	public void mapEndpoints(Javalin app) {
		app.get("/reimb", getReimbs);
		app.post("/reimb", createReimb);
		app.put("/reimb/{id}", resolveReimb);
		app.get("/reimb/{id}/receipt", getReceiptFromReimbById);
	}
	
}
