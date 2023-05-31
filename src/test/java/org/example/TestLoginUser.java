package org.example;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.hamcrest.CoreMatchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.Matchers.isEmptyOrNullString;
import static org.junit.Assert.fail;

@RunWith(Parameterized.class)
public class TestLoginUser {
    private UserClient userClient;
    private User user;
    private User userForLogin;

    private int statusCodeExpected;
    private UserStatus userStatus;

    private String accessToken;
    private String refreshToken;
    public TestLoginUser (User user, int statusCode, UserStatus userStatus){
        this.user = user;
        this.statusCodeExpected = statusCode;
        this.userStatus = userStatus;

    }

    @Parameterized.Parameters
    public static Object[][] getUserVariants() {
        return new Object[][]{
                {UserGenerator.getRandom(), 200,UserStatus.AUTHORIZED},
                {UserGenerator.getRandom(), 401,UserStatus.UNAUTHORIZED},
        };
    }
    @Before
    public void setUp() {
        userClient = new UserClient();
        ValidatableResponse createResponse = userClient.createUser(user);
        accessToken = createResponse.extract().path("accessToken");
        refreshToken = createResponse.extract().path("refreshToken");
    }
    @Test
    @DisplayName("Test логина пользователя")
    @Description("Тест логина пользователя")
    public void loginUserTest() {
        switch (userStatus) {
            case AUTHORIZED:
                userForLogin = UserGenerator.userForLogin(user.getEmail(), user.getPassword());
                ValidatableResponse createSuccessResponse = userClient.loginUser(userForLogin);
                createSuccessResponse.assertThat().statusCode(equalTo(statusCodeExpected));
                createSuccessResponse.assertThat().body("success",equalTo(true));
                createSuccessResponse.assertThat().body("user.email",equalTo(user.getEmail().toLowerCase()));
                createSuccessResponse.assertThat().body("user.name",equalTo(user.getName()));
                createSuccessResponse.assertThat().body("accessToken", CoreMatchers.not(isEmptyOrNullString()));
                createSuccessResponse.assertThat().body("refreshToken",CoreMatchers.not(isEmptyOrNullString()));
                accessToken = createSuccessResponse.extract().path("accessToken");
                refreshToken = createSuccessResponse.extract().path("refreshToken");
                break;
            case UNAUTHORIZED:
                userForLogin = UserGenerator.userForWrongLogin(user.getEmail(), user.getPassword());
                ValidatableResponse createFailResponse = userClient.loginUser(userForLogin);
                createFailResponse.assertThat().statusCode(equalTo(statusCodeExpected));
                createFailResponse.assertThat().body("success",equalTo(false));
                createFailResponse.assertThat().body("message",equalTo("email or password are incorrect"));

                break;
            default:
                fail("Неожиданное состояние");
                break;
        }


    }


    @After
    public void cleanUp() {
        userClient.deleteUser(accessToken, user);
    }
}
