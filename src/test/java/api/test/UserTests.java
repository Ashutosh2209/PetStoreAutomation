package api.test;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.github.javafaker.Faker;

import api.endpoints.UserEndpoints;
import api.payload.User;
import io.restassured.response.Response;

public class UserTests {
	
	Faker faker;
	User userPayload;
	public Logger logger;
	
	
	@BeforeClass
	public void setup() {
		faker = new Faker();
		userPayload = new User();
		
		userPayload.setId(faker.idNumber().hashCode());
		userPayload.setUsername(faker.name().username());
		userPayload.setFirstName(faker.name().firstName());
		userPayload.setLastName(faker.name().lastName());
		userPayload.setEmail(faker.internet().safeEmailAddress());
		userPayload.setPassword(faker.internet().password(5, 10));
		userPayload.setPhone(faker.phoneNumber().cellPhone());
		
		//logs
		logger = LogManager.getLogger(this.getClass());
		
	}
	
	@Test(priority=1)
	public void testPostUser(){
		logger.info("***********Creating User***********");
		Response res = UserEndpoints.createUser(userPayload);
		res.then().log().all();
		Assert.assertEquals(res.getStatusCode(), 200);
		logger.info("**********User Created**************");
	}
	
	@Test(priority=2)
	public void testGetUserByName() {
		logger.info("**********Reading User**************");
		Response response = UserEndpoints.readUser(this.userPayload.getUsername());
		response.then().log().all();
		Assert.assertEquals(response.getStatusCode(), 200);
		logger.info("**********User Info Displayed**************");
	}
	
	@Test(priority=3)
	public void testUpdateuserByName() {
		//update data using payload
		logger.info("**********User Updating**************");
		userPayload.setFirstName(faker.name().firstName());
		userPayload.setLastName(faker.name().lastName());
		userPayload.setEmail(faker.internet().safeEmailAddress());
		
		Response res = UserEndpoints.updateUser(this.userPayload.getUsername(), userPayload);
		res.then().log().all();
		Assert.assertEquals(res.getStatusCode(), 200);
		logger.info("**********User Updated**************");
		//checking data after updation
		Response responseAfterupdate = UserEndpoints.readUser(this.userPayload.getUsername());
		Assert.assertEquals(responseAfterupdate.getStatusCode(), 200);
	}
	
	@Test(priority=4)
	public void testDeleteuserByname() {
		logger.info("**********User Deleting**************");
		Response res = UserEndpoints.deleteUser(this.userPayload.getUsername());
		Assert.assertEquals(res.getStatusCode(), 200);
		logger.info("**********User Deleted**************");
	}
	
	
}
