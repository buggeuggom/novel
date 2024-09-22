package com.novel.api.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
public class UserSignupRequest {

    @NotBlank(message = "Write email. Not Blank")
    @Email(message = "Write email. Email type")
    private String email;
    @Length(min= 2, max = 10, message = "Write name 2 ~ 10")
    private String name;
    @Length(min = 5, max = 30, message = "Write password 5 ~ 10")
    private String password;

    @Builder
    public UserSignupRequest(String email, String name, String password) {
        this.email = email;
        this.name = name;
        this.password = password;
    }
}
