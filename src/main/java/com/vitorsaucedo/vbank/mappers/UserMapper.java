package com.vitorsaucedo.vbank.mappers;

import com.vitorsaucedo.vbank.dtos.UserRegistrationRequest;
import com.vitorsaucedo.vbank.dtos.UserResponse;
import com.vitorsaucedo.vbank.entities.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public User toEntity(UserRegistrationRequest request) {
        User user = new User();
        user.setFullName(request.fullName());
        user.setDocument(request.document());
        user.setEmail(request.email());
        return user;
    }

    public UserResponse toResponse(User user) {
        return new UserResponse(
                user.getId(),
                user.getFullName(),
                user.getEmail(),
                user.getAccount().getAccountNumber(),
                user.getAccount().getAgency()
        );
    }
}