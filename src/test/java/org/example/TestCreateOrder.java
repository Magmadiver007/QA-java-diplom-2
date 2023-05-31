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
import static org.hamcrest.CoreMatchers.notNullValue;


public class TestCreateOrder {
    private UserClient userClient;
    private User user;
    private OrderClient orderClient;
    private String accessToken;
    private List<String> ingredients;
    private Order order;
    private String burgerName;
    @Before
    public void setUp() {
        userClient = new UserClient();
        user = UserGenerator.getRandom();
        ValidatableResponse createResponse = userClient.createUser(user);
        accessToken = createResponse.extract().path("accessToken");
        orderClient = new OrderClient();


    }
    @Test
    @DisplayName("Test создания Заказа c авторизацией и ингридиентами")
    @Description("Тест создания Заказа c авторизацией и ингридиентами")
    public void testCreateAuthorizedOrder(){
        order = new Order(INGREDIENTS);
        ValidatableResponse createResponse = orderClient.createOrderAuthorized(accessToken, order);
        createResponse.assertThat().statusCode(equalTo(200));
        createResponse.assertThat().body("success",equalTo(true));
        createResponse.assertThat().body("name",equalTo(BURGER_NAME));
        createResponse.assertThat().body("order.number", notNullValue());


    }
    @Test
    @DisplayName("Test создания Заказа без авторизации")
    @Description("Тест создания Заказа без авторизации")
    public void testCreateUnauthorizedOrder(){
        order = new Order(INGREDIENTS);
        ValidatableResponse createResponse = orderClient.createOrderUnauthorized(order);
        createResponse.assertThat().statusCode(equalTo(200));
        createResponse.assertThat().body("success",equalTo(true));
        createResponse.assertThat().body("name",equalTo(BURGER_NAME));
        createResponse.assertThat().body("order.number", notNullValue());

    }
    @Test
    @DisplayName("Test создания Заказа без ингредиентов")
    @Description("Тест создания Заказа без ингредиентов")
    public void testCreateNoIngredientsOrder(){
        order = new Order(ingredients);
        ValidatableResponse createResponse = orderClient.createOrderAuthorized(accessToken, order);
        createResponse.assertThat().statusCode(equalTo(400));
        createResponse.assertThat().body("success",equalTo(false));
        createResponse.assertThat().body("message",equalTo(ERROR_NO_ID));

    }
    @Test
    @DisplayName("Тест создания Заказа с неправильными ID ингредиентов")
    @Description("Тест создания Заказа с неправильными ID ингредиентов")
    public void testCreateWrongIngredientsOrder(){
        order = new Order(WRONG_INGREDIENTS);
        ValidatableResponse createResponse = orderClient.createOrderAuthorized(accessToken, order);
        createResponse.assertThat().statusCode(equalTo(400));
        createResponse.assertThat().body("success",equalTo(false));
        createResponse.assertThat().body("message",equalTo(ERROR_WRONG_ID));

    }

    @After
    public void cleanUp() {
        userClient.deleteUser(accessToken, user);
    }
}
