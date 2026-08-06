package com.fastfood.data;

import com.fastfood.model.LoginData;
import com.fastfood.utils.JsonUtils;
import org.testng.annotations.DataProvider;

import java.util.Arrays;
import java.util.List;

public class LoginDataProvider {

    @DataProvider(name = "loginSuccessData")
    public Object[][] loginSuccessData() {

        LoginData[] data =
                JsonUtils.readData(
                        "validlogin.json",
                        LoginData[].class);

        Object[][] result = new Object[data.length][1];

        for (int i = 0; i < data.length; i++) {
            result[i][0] = data[i];
        }

        return result;
    }

    @DataProvider(name = "loginFailData")
    public Object[][] loginFailData() {

        LoginData[] data =
                JsonUtils.readData(
                        "invalidlogin.json",
                        LoginData[].class);

        Object[][] result = new Object[data.length][1];

        for (int i = 0; i < data.length; i++) {
            result[i][0] = data[i];
        }

        return result;
    }
}