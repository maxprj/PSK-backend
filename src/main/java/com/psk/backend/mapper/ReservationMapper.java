package com.psk.backend.mapper;

import com.psk.backend.domain.reservation.Reservation;
import com.psk.backend.domain.reservation.value.ReservationForm;
import com.psk.backend.domain.reservation.value.ReservationListView;
import com.psk.backend.config.BaseMapperConfig;
import com.psk.backend.domain.trip.value.TripCreateForm;
import com.psk.backend.domain.trip.value.TripForm;
import com.psk.backend.domain.user.AuditUser;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(config = BaseMapperConfig.class)
public abstract class ReservationMapper {

    public abstract ReservationListView listView(Reservation reservation);

    public String map(AuditUser user) {
        return user.toString();
    }

    @Mapping(target = "apartmentId", ignore = true)
    @Mapping(target = "tripId", ignore = true)
    public abstract Reservation update(ReservationForm newReservation, @MappingTarget Reservation reservation);

    @Mapping(source = "reservationBegin", target = "from")
    @Mapping(source = "reservationEnd", target = "till")
    @Mapping(source = "destination", target = "apartmentId")
    @Mapping(expression = "java((long)form.getUsers()" +
            ".stream()" +
            ".filter(f -> f.isInApartment())" +
            ".count())", target = "places")
    public abstract ReservationForm fromTrip(TripCreateForm form);

    @Mapping(source = "reservationBegin", target = "from")
    @Mapping(source = "reservationEnd", target = "till")
    @Mapping(expression = "java((long)form.getUsers()" +
            ".stream()" +
            ".filter(f -> f.isInApartment())" +
            ".count())", target = "places")
    public abstract ReservationForm fromTrip(TripForm form);

    public abstract Reservation fromForm(ReservationForm form);
}
