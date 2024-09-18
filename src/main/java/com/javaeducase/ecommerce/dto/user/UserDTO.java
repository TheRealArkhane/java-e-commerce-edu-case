package com.javaeducase.ecommerce.dto.user;

import com.javaeducase.ecommerce.entities.user.Role;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class UserDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private Role role;
    private boolean isDeleted;
}
