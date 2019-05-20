package com.psk.backend.appartment.value;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
public class AddressForm {

    @NotEmpty
    private String city;

    @NotEmpty
    private String street;

    private String appartmentNumber;
}