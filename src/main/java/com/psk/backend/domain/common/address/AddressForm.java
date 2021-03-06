package com.psk.backend.domain.common.address;

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

    private String apartmentNumber;
}
