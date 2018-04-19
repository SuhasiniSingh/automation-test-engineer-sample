package com.att.demo.integration;

import static org.hamcrest.CoreMatchers.is;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;


public class Billing {
private String uri = "/Billings";

@BeforeClass
public static void init() {
	RestAssured.baseURI ="http://localhost:8080";
}
@Test(priority=0)
public void getbilling() {
	RestAssured.given()
	.contentType(ContentType.JSON)
	.when()
	.get(uri)
	.then()
	.statusCode(200)
	.body("", is(""));
	
}

@DataProvider(name="validBilling")
public Object[][] createData(){
	return new Object[][] {
		{ new  Billing(345,"shet","shsi")
	},
};
}


@Test(priority=1,dataProvider="validBilling")
public void postBilling(Billing billing) {
	String accountNo=given()
	.accept(ContentType.JSON)
	.contentType(ContentType.JSON)
	.body(billing)
	.when()
	.post(uri)
	.then()
	.statusCode(201)
	.body("FirstName",is(billing.getFirstName()))
	.body("LastName",is(billing.getLastName()))
	.extract()
	.path("accountNo");
	
	RestAssured.given()
	.pathParam("accountNo",accountNo)
	.when()
	.get("/billings/{accountNo}")
	.then()
	.statusCode(200)
	.body("accountNo",is(accountNo))
	.body("FirstName",is(billing.getFirstName))
	.body("LastName",is(billing.getLastName));
	}
@Test(priority=2)
public void validPostBilling() {
	Billing billing = new Billing(234,"shus","shsjd");
	String accountNo=given()
			.contentType(ContentType.JSON)
			.body(billing)
			.when()
			.post(uri)
			.then()
			.statuscode(200)
			.body("FirstName",is(billing.getFirstName()))
			.body("LastName",is(billing.getLastName()))
			.extract()
			.path("accountNo");
	given()
	.pathParam("accountNo",accountNo)
	.when()
	.get("/billings/{accountNo}")
	.then()
	.statusCode(200)
	.body("accountNo",is(accountNo))
	.body("FirstName",is(billing.getFirstName))
	.body("LastName",is(billing.getLastName));
	
}


@Test(priority=3)
public void negativePostBilling() {
	Billing billing = new Billing( ," "," ");
	RestAssured.given()
			.accept(ContentType.JSON)
			.contentType(ContentType.JSON)
			.body(billing)
			.when()
			.post(uri)
			.then()
			.statusCode(201)
			.body("message", is("Fields cannot be empty"));
	
}


}
