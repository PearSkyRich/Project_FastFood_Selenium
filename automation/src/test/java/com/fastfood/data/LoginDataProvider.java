package com.fastfood.data;

import com.fastfood.model.LoginData;
import com.fastfood.utils.JsonUtils;
import org.testng.annotations.DataProvider;

import java.util.Arrays;

public class LoginDataProvider {

    @DataProvider(name = "loginSuccessData")
    public static Object[][] loginSuccessData() throws Exception {

        LoginData[] data =
                JsonUtils.readData("validlogin.json", LoginData[].class);

        return Arrays.stream(data)
                .map(d -> new Object[]{
                        d.getUsername(),
                        d.getPassword(),
                        d.getExpectedUrl()
                })
                .toArray(Object[][]::new);
    }

    @DataProvider(name = "loginFailData")
    public static Object[][] loginFailData() throws Exception {

        LoginData[] data =
                JsonUtils.readData("invalidlogin.json", LoginData[].class);

        return Arrays.stream(data)
                .map(d -> new Object[]{
                        d.getUsername(),
                        d.getPassword(),
                        d.getExpectedUrl()
                })
                .toArray(Object[][]::new);
    }
}