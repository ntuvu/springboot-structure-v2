package com.example.springbootstructurever2.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Set;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class UserDetailResponse implements Serializable {

    private Long id;
    private String firstName;
    private String lastName;
    private String phone;
    private String email;
}
