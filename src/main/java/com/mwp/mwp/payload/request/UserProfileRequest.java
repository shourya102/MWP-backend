package com.mwp.mwp.payload.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserProfileRequest {


    private String firstName;
    private String lastName;
    private Integer age;

    @Email
    @Size(max = 50)
    private String email;
    private String country;
    private String phoneNumber;
}
