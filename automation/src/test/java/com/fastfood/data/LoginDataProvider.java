package com.fastfood.data;

import com.fastfood.model.LoginData;
import com.fastfood.utils.JsonUtils;
import org.testng.annotations.DataProvider;

public class LoginDataProvider {

    @DataProvider(name = "loginData")
    public static Object[][] loginData() throws Exception {

        LoginData[] data = JsonUtils.readLoginData();

        Object[][] result = new Object[data.length][4];

        for (int i = 0; i < data.length; i++) {
            result[i][0] = data[i].getUsername();
            result[i][1] = data[i].getPassword();
            result[i][2] = data[i].getExpectedUrl();
            result[i][3] = data[i].isExpected();
        }

        return result;
    }
}