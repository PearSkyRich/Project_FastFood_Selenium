package com.fastfood.model;

public class LoginData {

    private String username;
    private String password;

    public void setExpectedUrl(String expectedUrl) {
        this.expectedUrl = expectedUrl;
    }

    public String getExpectedUrl() {
        return expectedUrl;
    }

    private String expectedUrl;
    private boolean expected;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isExpected() {
        return expected;
    }

    public void setExpected(boolean expected) {
        this.expected = expected;
    }

}