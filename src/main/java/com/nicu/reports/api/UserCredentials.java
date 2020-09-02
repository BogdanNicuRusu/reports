package com.nicu.reports.api;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Getter;

@Getter
public class UserCredentials {

    @NotNull
    @Size(min = 5, message = "username needs to have at least {min} characters")
    private String username;

    @NotNull
    @Size(min = 5, message = "password needs to have at least {min} characters")
    private String password;

}
