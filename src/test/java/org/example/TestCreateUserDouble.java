package org.example;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;

public class TestCreateUserDouble {
    private UserClient userClient;
    private User user;
    private String accessToken;
    @Before
    public void setUp() {
        userClient = new UserClient();
        user = UserGenerator.getRandom();
        ValidatableResponse createResponse = userClient.createUser(user);
        accessToken = createResponse.extract().path("accessToken");
    }
    @Test
    @DisplayName("Test создания дубля пользователя")
    @Description("Тест создания дубля пользователя")
    public void createUserDoubleTest (){
        ValidatableResponse createResponse = userClient.createUser(user);
        createResponse.assertThat().statusCode(equalTo(403));
        createResponse.assertThat().body("success",equalTo(false));
        createResponse.assertThat().body("message",equalTo("User already exists"));

    }
    @After
    public void cleanUp() {
        userClient.deleteUser(accessToken, user);
    }
}
