# Expense Reimbursement System

## Project Description

Expense reimbursement management system with two user roles of manager and employee. Employees submit reimbursement requests for expenses to be approved by managers. Managers view uploaded requests along with receipt image upload and choose to either accept or deny request.

## Technologies Used

* Java 8, Javalin backend
* HTML, JavaScript, CSS front end
* Selenium and Cucumber for e2e testing
* JUnit
* AWS RDS 
* JBCrypt

## Features
* Users can sign up as either manager or employee
* Users can login
* Users can logout
* Employees can view all past reimbursement requests that they requested
* Employees can submit new reimbursement requests
* Employees can upload receipt with request
* Managers can view all reimbursement requests
* Mangagers can update status of requests to either PENDING or DENIED
* Managers can filter requests by status

To-do list:
* Deploy on ec2 instance
* Angular frontend
* email verification
