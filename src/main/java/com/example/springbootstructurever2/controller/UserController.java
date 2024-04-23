package com.example.springbootstructurever2.controller;

import com.example.springbootstructurever2.dto.request.UserRequestDTO;
import com.example.springbootstructurever2.dto.response.ResponseData;
import com.example.springbootstructurever2.dto.response.ResponseSuccess;
import com.example.springbootstructurever2.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/users")
@Slf4j
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseData<Integer> createUser(@RequestBody @Valid UserRequestDTO request) {
        log.info("User added");

        return new ResponseData<>(HttpStatus.CREATED.value(), "User added", 1);
    }

    @PutMapping("/{userId}")
    public ResponseData<?> updateUser(@PathVariable int userId, @RequestBody UserRequestDTO request) {
        log.info("User updated with id {}", userId);
        return new ResponseData<>(HttpStatus.ACCEPTED.value(), "User updated");
    }

    @PatchMapping("/{userId}")
    public ResponseData<?> changeStatus(@PathVariable @Min(1) int userId, @RequestParam boolean status) {
        log.info("User status updated with id {}", userId);
        return new ResponseData<>(HttpStatus.ACCEPTED.value(), "User status changed");
    }

    @DeleteMapping("/{userId}")
    public ResponseData<?> deleteUser(@PathVariable int userId) {
        log.info("User with id {} deleted", userId);
        return new ResponseData<>(HttpStatus.NO_CONTENT.value(), "User deleted");
    }

    @GetMapping("/{userId}")
    public ResponseData<UserRequestDTO> getUser(@PathVariable int userId) {
        return new ResponseData<>(HttpStatus.OK.value(), "user detail", null);
    }

    @GetMapping
    public ResponseData<List<UserRequestDTO>> getAllUsers(@RequestParam(defaultValue = "0") int pageNo,
                                                          @RequestParam(defaultValue = "10") int pageSize,
                                                          @RequestParam(required = false) String email) {
        return new ResponseData<>(HttpStatus.OK.value(), "user detail", null);
    }

//    @GetMapping
//    public ResponseSuccess getAllUsers(@RequestParam(defaultValue = "0") int pageNo,
//                                                          @RequestParam(defaultValue = "10") int pageSize,
//                                                          @RequestParam(required = false) String email) {
//        return new ResponseSuccess(HttpStatus.OK, "user detail", null);
//    }
}
