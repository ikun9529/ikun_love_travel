package com.ikun.model.dto;

import lombok.Data;

@Data
public class UserRegisterDTO {
    private String username;
    private String userAccount;
    private String userPassword;
    private String checkPassword;
}
