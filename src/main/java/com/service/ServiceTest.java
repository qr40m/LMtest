package com.service;
import com.jayway.restassured.internal.assertion.Assertion;
import com.jayway.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import com.jayway.restassured.http.ContentType;


import java.util.HashMap;
import java.util.Map;

import static com.jayway.restassured.RestAssured.given;

public class ServiceTest {

    private static String basePath = "/process/start";

    @Test (dataProvider = "correctValues")
    public void Getting200Test(Integer value, Integer code){

        Map<String, Integer> procId = new HashMap<>();
        procId.put("processId", value);

        Response response = given()
                .contentType(ContentType.JSON)
                .body(procId)
                .when()
                .post(basePath)
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .extract()
                .response();
        String status = response.jsonPath().getString("status");
        Assert.assertEquals(status, "DONE");
    }

    @Test (dataProvider = "incorrectValues")
    public void Getting400Test(Integer value, Integer code){

        Map<String, Integer> procId = new HashMap<>();
        procId.put("processId", value);

        Response response = given()
                .contentType(ContentType.JSON)
                .body(procId)
                .when()
                .post(basePath)
                .then()
                .statusCode(400)
                .contentType(ContentType.JSON)
                .extract()
                .response();
        assertions(
                response.jsonPath().getString("code"),
                response.jsonPath().getString("message")
        );
    }

    @Test
    public void NoProcIdTest(){

        Map<String, Integer> procId = new HashMap<>();
        Response response = given()
                .contentType(ContentType.JSON)
                .body(procId)
                .when()
                .post(basePath)
                .then()
                .statusCode(400)
        .contentType(ContentType.JSON)
                .extract()
                .response();
        assertions(
                response.jsonPath().getString("code"),
                response.jsonPath().getString("message")
        );

    }

    private void assertions(String code, String message){
        Assert.assertEquals(code, "1");
        Assert.assertEquals(message, "Invalid processId");
    }

    @DataProvider
    public Object[] correctValues(){
        return new Object[][]{
                {1, 200},
                {122, 200},
                {31337, 200},
        };
    }

    @DataProvider
    private Object[] incorrectValues(){
        return new Object[][]{
                {0, 400},
                {null, 400},
                {33337, 400},
        };
    }

}
