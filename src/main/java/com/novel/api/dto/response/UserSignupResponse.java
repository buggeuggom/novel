package com.novel.api.dto.response;

import com.novel.api.dto.UserDto;
import lombok.Getter;

@Getter
public class UserSignupResponse {

    private String email;
    private String name;

    private UserSignupResponse(String email, String name) {
        this.email = email;
        this.name = name;
    }

    public static UserSignupResponse fromDto(UserDto dto){
        return new UserSignupResponse(
                dto.getEmail(),
                dto.getName()
        );
    }
}
