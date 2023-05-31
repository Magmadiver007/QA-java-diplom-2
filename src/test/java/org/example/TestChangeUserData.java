package org.example;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.fail;

@RunWith(Parameterized.class)
public class TestChangeUserData {
    private UserClient userClient;
    private User user;
    private User changedUser;

    private int statusCodeExpected;
    private UserStatus userStatus;
    private User userForLogin;
    private String accessToken;
    private Token refreshToken;
    private boolean isSuccess;
    public TestChangeUserData (User user, int statusCode, UserStatus userStatus){
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
        refreshToken = new Token(createResponse.extract().path("refreshToken"));
    }

    @Test
    @DisplayName("Test Изменения Логина/почты пользователя")
    @Description("Тест Изменения Логина/почты пользователя")
    public void changeUserEmailTest() {
        changedUser = UserGenerator.changedUser("test"+user.getEmail(), "test"+user.getPassword(), "test"+user.getName());
        switch (userStatus) {
            case AUTHORIZED:
                ValidatableResponse createSuccessResponse = userClient.changeUserData(accessToken, changedUser);
                createSuccessResponse.assertThat().statusCode(equalTo(statusCodeExpected));
                createSuccessResponse.assertThat().body("success",equalTo(true));
                createSuccessResponse.assertThat().body("user.email",equalTo(changedUser.getEmail().toLowerCase()));
                createSuccessResponse.assertThat().body("user.name",equalTo(changedUser.getName()));
                //Так как пароль нигде не приходит проверю что он изменился вызвав логин с новыми данными
                ValidatableResponse createLoginResponse = userClient.loginUser(UserGenerator.userForLogin(changedUser.getEmail(), changedUser.getPassword()));
                createLoginResponse.assertThat().statusCode(equalTo(200));
                createLoginResponse.assertThat().body("success",equalTo(true));
                accessToken = createLoginResponse.extract().path("accessToken");
                refreshToken = new Token(createLoginResponse.extract().path("refreshToken"));
                isSuccess = createSuccessResponse.extract().path("success");
                break;
            case UNAUTHORIZED:
                ValidatableResponse createFailResponse = userClient.changeUserDataNoAuth(changedUser);
                createFailResponse.assertThat().statusCode(equalTo(statusCodeExpected));
                createFailResponse.assertThat().body("success",equalTo(false));
                createFailResponse.assertThat().body("message",equalTo("You should be authorised"));
                isSuccess = createFailResponse.extract().path("success");
                break;
            default:
                fail("Неожиданное состояние");
                break;
        }
    }


    @After
    public void cleanUp() {
        if (isSuccess){
            userClient.deleteUser(accessToken, changedUser);
        } else {
            userClient.deleteUser(accessToken, user);
        }

    }
}
