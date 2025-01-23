package com.javaeducase.ecommerce.dto.user;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChangeUserDataRequestDTO {
    private String firstName;
    private String lastName;
    private String email;
}
