package com.psk.backend.user.value;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
@ToString
@EqualsAndHashCode
public class PasswordForm {

    @NotEmpty
    private String token;

    @NotEmpty
    private String userID;

    @NotEmpty
    private String password;

}