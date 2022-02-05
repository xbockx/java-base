package com.linbo.boot.bean;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

import java.io.Serializable;

/**
 * @Description
 * @Author xbockx
 * @Date 1/10/2022
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Role implements GrantedAuthority, Serializable {

    private Long id;

    private String authority;

}
