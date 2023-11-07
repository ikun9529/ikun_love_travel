package com.ikun.model.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SafetyUser {
    private Long id;
    private String username;
    private String userAccount;
    private String avatarUrl;
    private Integer userRole;
}
