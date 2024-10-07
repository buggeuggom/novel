package com.novel.api.dto;

import com.novel.api.domain.user.User;
import lombok.Getter;

@Getter
public class UserDto {

    private Long id;
    private String name;
    private String email;
    private String password;

    private UserDto(Long id, String name, String email, String password) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public static UserDto from(User entity) {
        return new UserDto(
                entity.getId(),
                entity.getName(),
                entity.getEmail(),
                entity.getPassword()
        );
    }
}
