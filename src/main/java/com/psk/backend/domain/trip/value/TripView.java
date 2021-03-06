package com.psk.backend.domain.trip.value;

import com.psk.backend.domain.common.address.AddressForm;
import com.psk.backend.domain.trip.TripStatus;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class TripView {
    private String id;
    private String name;
    private AddressForm source;
    private AddressForm destination;
    private String destinationId;
    private boolean reservation;
    private TripStatus status;
    private LocalDateTime departure;
    private LocalDateTime reservationBegin;
    private LocalDateTime reservationEnd;
    private LocalDateTime arrival;
    private String description;

    private ExpensesForm carRent;
    private ExpensesForm hotel;
    private ExpensesForm flight;

    private BigDecimal otherExpenses;

    private List<TripUserForm> users;

    private LocalDateTime updatedAt;
}
