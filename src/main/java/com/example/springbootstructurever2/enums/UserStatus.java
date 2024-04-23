package com.example.springbootstructurever2.enums;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum UserStatus {
    @JsonProperty("active")
    ACTIVE,

    @JsonProperty("inactive")
    INACTIVE,

    @JsonProperty("none")
    NONE
}