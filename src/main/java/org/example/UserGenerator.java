package org.example;

import org.apache.commons.lang3.RandomStringUtils;
public class UserGenerator {
    public static User getRandom(){
        final String email = RandomStringUtils.randomAlphabetic(10)+"@yandex.ru";
        final String password = RandomStringUtils.randomAlphabetic(10);
        final String name = RandomStringUtils.randomAlphabetic(10);
        return new User(email, password, name);
    }
    public static User getRandomNoEmail(){
        final String email = RandomStringUtils.randomAlphabetic(10)+"@yandex.ru";
        final String password = RandomStringUtils.randomAlphabetic(10);
        final String name = RandomStringUtils.randomAlphabetic(10);
        return new User(null, password, name);
    }
    public static User getRandomNoPassword(){
        final String email = RandomStringUtils.randomAlphabetic(10)+"@yandex.ru";
        final String password = RandomStringUtils.randomAlphabetic(10);
        final String name = RandomStringUtils.randomAlphabetic(10);
        return new User(email, null, name);
    }
    public static User getRandomNoName(){
        final String email = RandomStringUtils.randomAlphabetic(10)+"@yandex.ru";
        final String password = RandomStringUtils.randomAlphabetic(10);
        final String name = RandomStringUtils.randomAlphabetic(10);
        return new User(email, password, null);
    }
    public static User getRandomWrongEmail(){
        final String email = RandomStringUtils.randomAlphabetic(10);
        final String password = RandomStringUtils.randomAlphabetic(10);
        final String name = RandomStringUtils.randomAlphabetic(10);
        return new User(email, password, name);
    }
    public static User userForLogin(String email, String password) {
        return new User(email, password, null);
    }
    public static User userForWrongLogin(String email, String password) {
        return new User("failTest"+email, "failTest"+password, null);
    }
    public static User changedUser (String email, String password, String name) {
        return new User(email, password, name);
    }
}
