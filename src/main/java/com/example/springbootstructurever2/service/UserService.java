package com.example.springbootstructurever2.service;

import com.example.springbootstructurever2.dto.request.UserRequestDTO;
import com.example.springbootstructurever2.dto.response.PageResponse;
import com.example.springbootstructurever2.dto.response.UserDetailResponse;
import com.example.springbootstructurever2.enums.UserStatus;

import java.util.List;

public interface UserService {

    long saveUser(UserRequestDTO request);

    void updateUser(long userId, UserRequestDTO request);

    void changeStatus(long userId, UserStatus status);

    void deleteUser(long userId);

    UserDetailResponse getUser(long userId);

    PageResponse<?> getAllUsersWithSortBy(int pageNo, int pageSize, String sortBy);

    PageResponse<?> getAllUsersWithSortByMultipleColumns(int pageNo, int pageSize, String... sorts);
}
