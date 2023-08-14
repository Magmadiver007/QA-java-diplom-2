package org.example;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import static io.restassured.RestAssured.given;

public class UserClient extends RestClient{
    private static final String USER_REGISTER_PATH = "/api/auth/register";
    private static final String USER_LOGIN_PATH = "/api/auth/login";
    private static final String USER_LOGOUT_PATH = "/api/auth/logout";
    private static final String USER_TOKEN_PATH = "/api/auth/token";
    private static final String USER_DATA_PATH = "/api/auth/user";
    @Step("Создаем пользователя")
    public ValidatableResponse createUser (User user){
        return given()
                .spec(getBaseSpec())
                .body(user)
                .when()
                .post(USER_REGISTER_PATH)
                .then();
    }
    @Step ("Логин пользователя")
    public ValidatableResponse loginUser (User user) {
        return given()
                .spec(getBaseSpec())
                .body(user)
                .when()
                .post(USER_LOGIN_PATH)
                .then();

    }
    @Step ("Логаут пользователя")
    public ValidatableResponse logoutUser (Token refreshToken) {
        return given()
                .spec(getBaseSpec())
                .body(refreshToken)
                .when()
                .post(USER_LOGOUT_PATH)
                .then();

    }
    @Step ("Изменение пользователя")
    public ValidatableResponse changeUserData (String accessToken, User user) {
        return given()
                .spec(getBaseSpec())
                .header("Authorization",accessToken)
                .body(user)
                .when()
                .patch(USER_DATA_PATH)
                .then();
    }
    @Step ("Изменение пользователя без авторизации")
    public ValidatableResponse changeUserDataNoAuth (User user) {
        return given()
                .spec(getBaseSpec())
                .body(user)
                .when()
                .patch(USER_DATA_PATH)
                .then();
    }

    @Step("Удаляем пользователя")
    public ValidatableResponse deleteUser (String accessToken, User user){
        return given()
                .spec(getBaseSpec())
                .header("Authorization",accessToken)
                .body(user)
                .when()
                .delete(USER_DATA_PATH)
                .then();
    }
}
