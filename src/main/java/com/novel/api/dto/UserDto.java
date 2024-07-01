package com.novel.api.dto;

import com.novel.api.domain.user.User;
import com.novel.api.domain.user.UserRole;
import lombok.Getter;

@Getter
public class UserDto {

    private Long id;
    private String name;
    private String email;
    private String password;
    private UserRole userRole;

    private UserDto(Long id, String name, String email, String password, UserRole userRole) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.userRole = userRole;
    }

    public static UserDto fromEntity(User entity) {
        return new UserDto(
                entity.getId(),
                entity.getName(),
                entity.getEmail(),
                entity.getPassword(),
                entity.getUserRole()
        );
    }
}
