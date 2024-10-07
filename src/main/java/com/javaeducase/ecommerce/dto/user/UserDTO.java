package com.javaeducase.ecommerce.dto.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.javaeducase.ecommerce.entity.user.Role;
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

    @JsonProperty("isDeleted")
    private boolean isDeleted;
}
