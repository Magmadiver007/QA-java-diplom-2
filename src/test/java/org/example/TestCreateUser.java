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
public class TestCreateUser {
    private UserClient userClient;
    private User user;
    private int statusCode;
    private int statusCodeExpected;

    private String accessToken;
    private String refreshToken;
    private boolean isSuccess;

    public TestCreateUser (User user, int statusCode){
        this.user = user;
        this.statusCodeExpected = statusCode;
    }

    @Parameterized.Parameters
    public static Object[][] getUserVariants() {
        return new Object[][]{
                {UserGenerator.getRandom(), 200},
                {UserGenerator.getRandomNoEmail(), 403},
                {UserGenerator.getRandomNoPassword(), 403},
                {UserGenerator.getRandomNoName(), 403},
                {UserGenerator.getRandomWrongEmail(), 500},

        };
    }
    @Before
    public void setUp() {
        userClient = new UserClient();
    }

    @Test
    @DisplayName("Test создания пользователя")
    @Description("Тест создания пользователя")
    public void createUserTest() {
        ValidatableResponse createResponse = userClient.createUser(user);
        createResponse.assertThat().statusCode(equalTo(statusCodeExpected));
        statusCode = createResponse.extract().statusCode();
        switch (statusCode) {
            case (200) :
                isSuccess = createResponse.extract().path("success");
                createResponse.assertThat().body("user.email",equalTo(user.getEmail().toLowerCase()));
                createResponse.assertThat().body("user.name",equalTo(user.getName()));
                accessToken = createResponse.extract().path("accessToken");
                refreshToken = createResponse.extract().path("refreshToken");
                break;
            case (403) :
                isSuccess = createResponse.extract().path("success");
                createResponse.assertThat().body("message",equalTo("Email, password and name are required fields"));
                break;
            default:
                isSuccess = false;
                createResponse.assertThat().statusCode(equalTo(statusCodeExpected));
                //Сервис падает если отправить в email строку без хвоста типа  "@yandex.ru"
                break;
        }



    }
    @After
    public void cleanUp() {
        if (isSuccess) {
            userClient.deleteUser(accessToken, user);
        }
    }
}
