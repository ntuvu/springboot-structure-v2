package com.example.springbootstructurever2.enums;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum Gender {
    @JsonProperty("male")
    MALE,

    @JsonProperty("female")
    FEMALE,

    @JsonProperty("other")
    OTHER;
}
