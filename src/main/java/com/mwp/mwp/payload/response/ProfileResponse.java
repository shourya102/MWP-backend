package com.mwp.mwp.payload.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProfileResponse {

    private String firstName;
    private String lastName;
    private Integer age;
    private String email;
    private String country;
    private String phoneNumber;
}
