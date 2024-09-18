package com.javaeducase.ecommerce.dto.user;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(exclude = "password")
public class RegistrationDTO {
    private String firstName;
    private String lastName;
    private String email;
    private String password;
}