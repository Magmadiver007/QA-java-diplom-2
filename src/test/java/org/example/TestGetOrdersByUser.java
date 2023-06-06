package org.example;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.example.TestData.*;
import static org.hamcrest.CoreMatchers.equalTo;

public class TestGetOrdersByUser {
    private UserClient userClient;
    private User user;
    private OrderClient orderClient;
    private String accessToken;
    private Order order;
    private int orderNumber;
    private String burgerName;
    @Before
    public void setUp() {
        userClient = new UserClient();
        user = UserGenerator.getRandom();
        ValidatableResponse createResponse = userClient.createUser(user);
        accessToken = createResponse.extract().path("accessToken");
        orderClient = new OrderClient();
        order = new Order(INGREDIENTS);
        ValidatableResponse createOrderResponse = orderClient.createOrderAuthorized(accessToken, order);
        orderNumber = createOrderResponse.extract().path("order.number");
        burgerName = createOrderResponse.extract().path("order.name");
    }
    @Test
    @DisplayName("Test получения заказа авторизованного пользователя")
    @Description("Тест получения заказа авторизованного пользователя")
    public void getAuthorizedUserOrders() {
        ValidatableResponse createResponse = orderClient.getOrdersАuthorized(accessToken);
        createResponse.assertThat().statusCode(equalTo(200));
        createResponse.assertThat().body("success",equalTo(true));
        createResponse.assertThat().body("orders[0].name", equalTo(burgerName));
        createResponse.assertThat().body("orders[0].number", equalTo(orderNumber));


    }
    @Test
    @DisplayName("Test получения заказа НЕ авторизованного пользователя")
    @Description("Тест получения заказа НЕ авторизованного пользователя")
    public void getUnauthorizedUserOrders() {
        ValidatableResponse createResponse = orderClient.getOrdersUnauthorized(accessToken);
        createResponse.assertThat().statusCode(equalTo(401));
        createResponse.assertThat().body("success",equalTo(false));
        createResponse.assertThat().body("message", equalTo(ERROR_NOT_AUTHORIZED));
    }

    @After
    public void cleanUp() {
        userClient.deleteUser(accessToken, user);
    }
}
