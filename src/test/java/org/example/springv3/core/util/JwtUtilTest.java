package org.example.springv3.core.util;

import org.example.springv3.user.User;
import org.junit.jupiter.api.Test;

public class JwtUtilTest {

    @Test
    public void create_test(){
        User user = User.builder().id(1).username("ssar").build();

        String accessToken = JwtUtil.createSecret(user);
        System.out.println(accessToken);
    }

    @Test
    public void verify_test(){
        String accessToken = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiLrsJTrs7QiLCJpZCI6MSwiZXhwIjoxNzI3NjU2MjQzLCJ1c2VybmFtZSI6InNzYXIifQ.jnvac_lPEWbiFmVOrA_ZGvpxTesOBk6JleP0btbiPgCaqEbRAsBTiYl_jw2r_HPBKqT8EAiSQ9Zxd7M2kmpMVg";
        User user = JwtUtil.verify(accessToken);
        System.out.println(user.getId());
        System.out.println(user.getUsername());
    }
}
