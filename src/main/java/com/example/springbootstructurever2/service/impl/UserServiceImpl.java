package com.example.springbootstructurever2.service.impl;

import com.example.springbootstructurever2.dto.request.UserRequestDTO;
import com.example.springbootstructurever2.exception.ResourceNotFoundException;
import com.example.springbootstructurever2.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    @Override
    public int addUser(UserRequestDTO request) {
        log.info("Save user service");
        if (!request.getFirstName().equals("Tu")) {
            throw new ResourceNotFoundException("Tu khong ton tai");
        }
        return 0;
    }
}
