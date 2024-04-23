package com.example.springbootstructurever2.dto.request;

import com.example.springbootstructurever2.enums.Gender;
import com.example.springbootstructurever2.enums.UserStatus;
import com.example.springbootstructurever2.enums.UserType;
import com.example.springbootstructurever2.validator.EnumPattern;
import com.example.springbootstructurever2.validator.EnumValue;
import com.example.springbootstructurever2.validator.GenderSubset;
import com.example.springbootstructurever2.validator.PhoneNumber;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserRequestDTO implements Serializable {

    @NotEmpty(message = "firstName must be not blank")
    String firstName;

    @NotNull(message = "lastName must be not null")
    String lastName;

    @PhoneNumber
    String phone;

    @Email(message = "email not valid")
    String email;

    @NotNull(message = "dob must be not null")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @JsonFormat(pattern = "MM/dd/yyyy")
    Date dob;

    @NotEmpty(message = "addresses can not empty")
    private Set<Address> addresses;

    @EnumPattern(name = "status", regexp = "ACTIVE|INACTIVE|NONE")
    UserStatus status;

    @GenderSubset(anyOf = {Gender.MALE, Gender.FEMALE, Gender.OTHER})
    Gender gender;

    @EnumValue(name = "type", enumClass = UserType.class)
    String type;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class Address {
        String apartmentNumber;
        String floor;
        String building;
        String streetNumber;
        String street;
        String city;
        String country;
        Integer addressType;
    }
}
