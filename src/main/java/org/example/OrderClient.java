package org.example;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;

import static io.restassured.RestAssured.given;

public class OrderClient extends RestClient{
    private static final String GET_INGREDIENTS_PATH = "/api/ingredients";
    private static final String CREATE_ORDER_PATH = "/api/orders";
    private static final String GET_ORDERS_ALL_PATH = "/api/orders/all";
    @Step("Получение данных об ингредиентах")
    public ValidatableResponse getIngredients() {
        return given()
                .spec(getBaseSpec())
                .when()
                .get(GET_INGREDIENTS_PATH)
                .then();

    }

    @Step("Создание заказа с авторизацией")
    public ValidatableResponse createOrderAuthorized (String accessToken, Order order){
        return given()
                .spec(getBaseSpec())
                .header("Authorization",accessToken)
                .body(order)
                .when()
                .post(CREATE_ORDER_PATH)
                .then();
    }
    @Step("Создание заказа без аторизаии")
    public ValidatableResponse createOrderUnauthorized (Order order){
        return given()
                .spec(getBaseSpec())
                .body(order)
                .when()
                .post(CREATE_ORDER_PATH)
                .then();
    }
    @Step("Получить все заказы без авторизации")
    public ValidatableResponse getOrdersAllUnauthorized() {
        return given()
                .spec(getBaseSpec())
                .when()
                .get(GET_ORDERS_ALL_PATH)
                .then();

    }
    @Step("Получить все заказы с авторизацией")
    public ValidatableResponse getOrdersAllАuthorized(String accessToken) {
        return given()
                .spec(getBaseSpec())
                .header("Authorization",accessToken)
                .when()
                .get(GET_ORDERS_ALL_PATH)
                .then();

    }
    @Step("Получить заказы пользователя с авторизацией")
    public ValidatableResponse getOrdersАuthorized(String accessToken) {
        return given()
                .spec(getBaseSpec())
                .header("Authorization",accessToken)
                .when()
                .get(CREATE_ORDER_PATH)
                .then();

    }
    @Step("Получить заказы пользователя с авторизацией")
    public ValidatableResponse getOrdersUnauthorized(String accessToken) {
        return given()
                .spec(getBaseSpec())
                .when()
                .get(CREATE_ORDER_PATH)
                .then();

    }
}
