package org.example;

import java.util.List;

public class TestData {
    public static final List<String> INGREDIENTS = List.of(
            "61c0c5a71d1f82001bdaaa72",
            "61c0c5a71d1f82001bdaaa77",
            "61c0c5a71d1f82001bdaaa7a"
    );
    public static final List<String> WRONG_INGREDIENTS = List.of(
            "61c0c5a7fd1f82001bdaaa72",
            "61c0c5a7fd1f82001bdaaa77",
            "61c0c5a7fd1f82001bdaaa7a"
    );
    public static final String ERROR_NO_ID = "Ingredient ids must be provided";
    public static final String ERROR_WRONG_ID = "One or more ids provided are incorrect";
    public static final String BURGER_NAME = "Фалленианский spicy астероидный бургер";

    public static final String ERROR_NOT_AUTHORIZED = "You should be authorised";

}
