package com.ikun.model.dto;

import lombok.Data;

@Data
public class UserUpdateDTO {
    private String username;
    private String userPassword;
    private String avatarUrl;
}
