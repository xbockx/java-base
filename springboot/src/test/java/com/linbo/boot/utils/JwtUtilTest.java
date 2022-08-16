package com.linbo.boot.utils;

import com.linbo.boot.bean.Role;
import com.linbo.boot.bean.User;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;


/**
 * @Description
 * @Author xbockx
 * @Date 1/10/2022
 */
public class JwtUtilTest {

    @Test
    public void testCreateToken() {
        String username = "user";
        List<Role> authorities = Arrays.asList(
                Role.builder()
                        .authority("ROLE_USER")
                        .build(),
                Role.builder()
                        .authority("ROLE_ADMIN")
                        .build());
//        User.builder()
//                .name(username)
//                .a.build();
    }

}
