package com.psk.backend.scheduled;


import com.psk.backend.trip.Trip;
import com.psk.backend.trip.TripRepository;
import com.psk.backend.trip.TripStatus;
import com.psk.backend.trip.TripUser;
import com.psk.backend.trip.TripUserStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ScheduledTaskService {
    private TripRepository tripRepository;

    public ScheduledTaskService(TripRepository tripRepository){
        this.tripRepository=tripRepository;
    }

    @Scheduled(fixedRate = 10)
    public void updateTripStatus(){
        checkStartedTrips();
        checkConfirmedTrips();
        checkDraftedTrips();
    }

    private void checkDraftedTrips(){
        List <Trip>  trips = tripRepository.getTripsByStatus(TripStatus.DRAFT);
        for (Trip trip :trips) {

            long noOfConfirmedUsers = trip.getUsers().stream()
                    .filter(user -> user.getStatus().equals(TripUserStatus.CONFIRMED))
                    .count();
            if (noOfConfirmedUsers  == trip.getUsers().size()){
                trip.setStatus(TripStatus.CONFIRMED);
                tripRepository.save(trip);
            }
            else {
                long noOfDeclinedUsers = trip.getUsers().stream()
                        .filter(user -> user.getStatus().equals(TripUserStatus.DECLINED))
                        .count();

                if (noOfDeclinedUsers == trip.getUsers().size()
                        || trip.getDeparture().isBefore(LocalDateTime.now()) ){
                    trip.setStatus(TripStatus.CANCELED);
                    tripRepository.save(trip);
                }
            }

        }
    }

    private void checkConfirmedTrips(){
        List <Trip>  trips = tripRepository.getTripsByStatus(TripStatus.CONFIRMED);
        for (Trip trip :trips) {
            if (trip.getDeparture().isBefore(LocalDateTime.now())) {
                trip.setStatus(TripStatus.STARTED);
                tripRepository.save(trip);
            }
        }
    }

    private void checkStartedTrips(){
        List <Trip>  trips = tripRepository.getTripsByStatus(TripStatus.STARTED);
        for (Trip trip :trips) {
            if (trip.getReservationEnd().isBefore(LocalDateTime.now())){
                trip.setStatus(TripStatus.FINISHED);
                tripRepository.save(trip);
            }
        }
    }
}
