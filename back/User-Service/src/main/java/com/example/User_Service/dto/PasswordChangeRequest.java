package com.example.User_Service.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PasswordChangeRequest {
    private String email;
    private String oldPassword;
    private String newPassword;
}
