package com.psk.backend.domain.reservation

import com.psk.backend.domain.apartment.Apartment
import com.psk.backend.domain.reservation.value.PlacementFilter
import com.psk.backend.domain.reservation.value.PlacementResult
import com.psk.backend.repository.ReservationRepository
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.mongodb.core.MongoOperations
import org.springframework.data.mongodb.core.query.Query
import org.springframework.test.context.ActiveProfiles
import spock.lang.Specification

import javax.annotation.Resource
import java.time.LocalDateTime

import static com.psk.backend.domain.apartment.ApartmentBuilder.apartment
import static com.psk.backend.domain.reservation.ReservationBuilder.reservation


@SpringBootTest
@ActiveProfiles("test")
class AvailableReservationFilteringTest extends Specification {

    @Resource
    MongoOperations operations

    @Resource
    ReservationRepository repository


    def cleanup() {
        operations.remove(new Query(), Reservation)
        operations.remove(new Query(), Apartment)
    }

    def "should calculate available places when filtering between"() {
        setup:
        def apartment = apartment()
        def id = '123'
        apartment.id = id
        operations.insert(apartment, "apartment")


        def reservations = [
                reservation('1', '2019-01-01 12:00', '2019-01-09 12:00'),
                reservation('2', '2019-01-02 12:00', '2019-01-10 12:00')
        ]
        operations.insertAll(reservations)

        def filter = new PlacementFilter(
                LocalDateTime.of(2019, 1, 3, 12, 0),
                LocalDateTime.of(2019, 1, 6, 12, 0)
        )
        def expected = new PlacementResult(4, 2)
        expected.calculateAvailablePlaces(6)

        when:
        def result = repository.availablePlaces(id, filter)

        then:
        result.isSuccess()
        def placementResult = result.getOrElse(null)
        placementResult.availablePlaces == expected.availablePlaces
        placementResult.reservations == expected.reservations
        placementResult.takenPlaces == expected.takenPlaces
    }

    def "should calculate available places on left intersection"() {
        setup:
        def apartment = apartment()
        def id = '123'
        apartment.id = id
        operations.insert(apartment, "apartment")


        def reservations = [
                reservation('1', '2019-01-01 12:00', '2019-01-04 12:00'),
                reservation('2', '2019-01-02 12:00', '2019-01-05 12:00')
        ]
        operations.insertAll(reservations)

        def filter = new PlacementFilter(
                LocalDateTime.of(2019, 1, 3, 12, 0),
                LocalDateTime.of(2019, 1, 6, 12, 0)
        )
        def expected = new PlacementResult(4, 2)
        expected.calculateAvailablePlaces(6)

        when:
        def result = repository.availablePlaces(id, filter)

        then:
        result.isSuccess()
        def placementResult = result.getOrElse(null)
        placementResult.availablePlaces == expected.availablePlaces
        placementResult.reservations == expected.reservations
        placementResult.takenPlaces == expected.takenPlaces
    }

    def "should calculate available places on right intersection"() {
        setup:
        def apartment = apartment()
        def id = '123'
        apartment.id = id
        operations.insert(apartment, "apartment")


        def reservations = [
                reservation('1', '2019-01-05 12:00', '2019-01-15 12:00'),
                reservation('2', '2019-01-06 12:00', '2019-01-16 12:00')
        ]
        operations.insertAll(reservations)

        def filter = new PlacementFilter(
                LocalDateTime.of(2019, 1, 3, 12, 0),
                LocalDateTime.of(2019, 1, 7, 12, 0)
        )
        def expected = new PlacementResult(4, 2)
        expected.calculateAvailablePlaces(6)

        when:
        def result = repository.availablePlaces(id, filter)

        then:
        result.isSuccess()
        def placementResult = result.getOrElse(null)
        placementResult.availablePlaces == expected.availablePlaces
        placementResult.reservations == expected.reservations
        placementResult.takenPlaces == expected.takenPlaces
    }

    def "should calculate available places on full intersection"() {
        setup:
        def apartment = apartment()
        def id = '123'
        apartment.id = id
        operations.insert(apartment, "apartment")


        def reservations = [
                reservation('1', '2019-01-05 12:00', '2019-01-15 12:00'),
                reservation('2', '2019-01-06 12:00', '2019-01-16 12:00')
        ]
        operations.insertAll(reservations)

        def filter = new PlacementFilter(
                LocalDateTime.of(2019, 1, 3, 12, 0),
                LocalDateTime.of(2019, 1, 22, 12, 0)
        )
        def expected = new PlacementResult(4, 2)
        expected.calculateAvailablePlaces(6)

        when:
        def result = repository.availablePlaces(id, filter)

        then:
        result.isSuccess()
        def placementResult = result.getOrElse(null)
        placementResult.availablePlaces == expected.availablePlaces
        placementResult.reservations == expected.reservations
        placementResult.takenPlaces == expected.takenPlaces
    }

    def "should calculate available places on two not intersecting middle reservations"() {
        setup:
        def apartment = apartment()
        def id = '123'
        apartment.id = id
        operations.insert(apartment, "apartment")


        def reservations = [
                reservation('1', '2019-01-02 12:00', '2019-01-04 12:00'),
                reservation('2', '2019-01-06 12:00', '2019-01-16 12:00')
        ]
        operations.insertAll(reservations)

        def filter = new PlacementFilter(
                LocalDateTime.of(2019, 1, 1, 12, 0),
                LocalDateTime.of(2019, 1, 22, 12, 0)
        )
        def expected = new PlacementResult(2, 2)
        expected.calculateAvailablePlaces(6)

        when:
        def result = repository.availablePlaces(id, filter)

        then:
        result.isSuccess()
        def placementResult = result.getOrElse(null)
        placementResult.availablePlaces == expected.availablePlaces
        placementResult.reservations == expected.reservations
        placementResult.takenPlaces == expected.takenPlaces
    }

    def "should calculate available places on two intersecting middle reservations"() {
        setup:
        def apartment = apartment()
        def id = '123'
        apartment.id = id
        operations.insert(apartment, "apartment")


        def reservations = [
                reservation('1', '2019-01-02 12:00', '2019-01-07 12:00'),
                reservation('2', '2019-01-06 12:00', '2019-01-16 12:00')
        ]
        operations.insertAll(reservations)

        def filter = new PlacementFilter(
                LocalDateTime.of(2019, 1, 1, 12, 0),
                LocalDateTime.of(2019, 1, 22, 12, 0)
        )
        def expected = new PlacementResult(4, 2)
        expected.calculateAvailablePlaces(6)

        when:
        def result = repository.availablePlaces(id, filter)

        then:
        result.isSuccess()
        def placementResult = result.getOrElse(null)
        placementResult.availablePlaces == expected.availablePlaces
        placementResult.reservations == expected.reservations
        placementResult.takenPlaces == expected.takenPlaces
    }

    def "should calculate available places on two not intersecting left and right reservations"() {
        setup:
        def apartment = apartment()
        def id = '123'
        apartment.id = id
        operations.insert(apartment, "apartment")


        def reservations = [
                reservation('1', '2019-01-01 12:00', '2019-01-08 12:00'),
                reservation('2', '2019-01-10 12:00', '2019-01-16 12:00')
        ]
        operations.insertAll(reservations)

        def filter = new PlacementFilter(
                LocalDateTime.of(2019, 1, 5, 12, 0),
                LocalDateTime.of(2019, 1, 22, 12, 0)
        )
        def expected = new PlacementResult(2, 2)
        expected.calculateAvailablePlaces(6)

        when:
        def result = repository.availablePlaces(id, filter)

        then:
        result.isSuccess()
        def placementResult = result.getOrElse(null)
        placementResult.availablePlaces == expected.availablePlaces
        placementResult.reservations == expected.reservations
        placementResult.takenPlaces == expected.takenPlaces
    }

    def "should calculate available places on two not intersecting left and middle reservations"() {
        setup:
        def apartment = apartment()
        def id = '123'
        apartment.id = id
        operations.insert(apartment, "apartment")


        def reservations = [
                reservation('1', '2019-01-02 12:00', '2019-01-05 12:00'),
                reservation('2', '2019-01-06 12:00', '2019-01-16 12:00')
        ]
        operations.insertAll(reservations)

        def filter = new PlacementFilter(
                LocalDateTime.of(2019, 1, 4, 12, 0),
                LocalDateTime.of(2019, 1, 22, 12, 0)
        )
        def expected = new PlacementResult(2, 2)
        expected.calculateAvailablePlaces(6)

        when:
        def result = repository.availablePlaces(id, filter)

        then:
        result.isSuccess()
        def placementResult = result.getOrElse(null)
        placementResult.availablePlaces == expected.availablePlaces
        placementResult.reservations == expected.reservations
        placementResult.takenPlaces == expected.takenPlaces
    }

    def "should calculate available places on two not intersecting right and middle reservations"() {
        setup:
        def apartment = apartment()
        def id = '123'
        apartment.id = id
        operations.insert(apartment, "apartment")


        def reservations = [
                reservation('1', '2019-01-05 12:00', '2019-01-08 12:00'),
                reservation('2', '2019-01-12 12:00', '2019-01-24 12:00')
        ]
        operations.insertAll(reservations)

        def filter = new PlacementFilter(
                LocalDateTime.of(2019, 1, 4, 12, 0),
                LocalDateTime.of(2019, 1, 22, 12, 0)
        )
        def expected = new PlacementResult(2, 2)
        expected.calculateAvailablePlaces(6)

        when:
        def result = repository.availablePlaces(id, filter)

        then:
        result.isSuccess()
        def placementResult = result.getOrElse(null)
        placementResult.availablePlaces == expected.availablePlaces
        placementResult.reservations == expected.reservations
        placementResult.takenPlaces == expected.takenPlaces
    }

    def "should calculate available places on three not intersecting middle reservations"() {
        setup:
        def apartment = apartment()
        def id = '123'
        apartment.id = id
        operations.insert(apartment, "apartment")


        def reservations = [
                reservation('1', '2019-01-05 12:00', '2019-01-08 12:00'),
                reservation('2', '2019-01-09 12:00', '2019-01-12 12:00'),
                reservation('3', '2019-01-12 13:00', '2019-01-14 13:00'),
        ]
        operations.insertAll(reservations)

        def filter = new PlacementFilter(
                LocalDateTime.of(2019, 1, 4, 12, 0),
                LocalDateTime.of(2019, 1, 22, 12, 0)
        )
        def expected = new PlacementResult(2, 3)
        expected.calculateAvailablePlaces(6)

        when:
        def result = repository.availablePlaces(id, filter)

        then:
        result.isSuccess()
        def placementResult = result.getOrElse(null)
        placementResult.availablePlaces == expected.availablePlaces
        placementResult.reservations == expected.reservations
        placementResult.takenPlaces == expected.takenPlaces
    }

    def "should calculate available places on three not intersecting one left two middle reservations"() {
        setup:
        def apartment = apartment()
        def id = '123'
        apartment.id = id
        operations.insert(apartment, "apartment")


        def reservations = [
                reservation('1', '2019-01-02 12:00', '2019-01-08 12:00'),
                reservation('2', '2019-01-09 12:00', '2019-01-12 12:00'),
                reservation('3', '2019-01-12 13:00', '2019-01-14 13:00'),
        ]
        operations.insertAll(reservations)

        def filter = new PlacementFilter(
                LocalDateTime.of(2019, 1, 4, 12, 0),
                LocalDateTime.of(2019, 1, 22, 12, 0)
        )
        def expected = new PlacementResult(2, 3)
        expected.calculateAvailablePlaces(6)

        when:
        def result = repository.availablePlaces(id, filter)

        then:
        result.isSuccess()
        def placementResult = result.getOrElse(null)
        placementResult.availablePlaces == expected.availablePlaces
        placementResult.reservations == expected.reservations
        placementResult.takenPlaces == expected.takenPlaces
    }

    def "should calculate available places on three not intersecting one right two middle reservations"() {
        setup:
        def apartment = apartment()
        def id = '123'
        apartment.id = id
        operations.insert(apartment, "apartment")


        def reservations = [
                reservation('1', '2019-01-05 12:00', '2019-01-08 12:00'),
                reservation('2', '2019-01-09 12:00', '2019-01-12 12:00'),
                reservation('3', '2019-01-12 13:00', '2019-01-14 13:00'),
        ]
        operations.insertAll(reservations)

        def filter = new PlacementFilter(
                LocalDateTime.of(2019, 1, 4, 12, 0),
                LocalDateTime.of(2019, 1, 22, 12, 0)
        )
        def expected = new PlacementResult(2, 3)
        expected.calculateAvailablePlaces(6)

        when:
        def result = repository.availablePlaces(id, filter)

        then:
        result.isSuccess()
        def placementResult = result.getOrElse(null)
        placementResult.availablePlaces == expected.availablePlaces
        placementResult.reservations == expected.reservations
        placementResult.takenPlaces == expected.takenPlaces
    }

    def "should calculate available places on three not intersecting one right three middle reservations"() {
        setup:
        def apartment = apartment()
        def id = '123'
        apartment.id = id
        operations.insert(apartment, "apartment")


        def reservations = [
                reservation('1', '2019-01-05 12:00', '2019-01-08 12:00'),
                reservation('2', '2019-01-09 12:00', '2019-01-12 12:00'),
                reservation('3', '2019-01-12 13:00', '2019-01-14 13:00'),
                reservation('4', '2019-01-16 13:00', '2019-01-18 13:00'),
        ]
        operations.insertAll(reservations)

        def filter = new PlacementFilter(
                LocalDateTime.of(2019, 1, 4, 12, 0),
                LocalDateTime.of(2019, 1, 22, 12, 0)
        )
        def expected = new PlacementResult(2, 4)
        expected.calculateAvailablePlaces(6)

        when:
        def result = repository.availablePlaces(id, filter)

        then:
        result.isSuccess()
        def placementResult = result.getOrElse(null)
        placementResult.availablePlaces == expected.availablePlaces
        placementResult.reservations == expected.reservations
        placementResult.takenPlaces == expected.takenPlaces
    }

    def "should calculate available places on three not intersecting middle and two left intersecting reservations"() {
        setup:
        def apartment = apartment()
        def id = '123'
        apartment.id = id
        operations.insert(apartment, "apartment")


        def reservations = [
                reservation('1', '2019-01-05 12:00', '2019-01-08 12:00'),
                reservation('2', '2019-01-05 12:00', '2019-01-08 12:00'),
                reservation('3', '2019-01-09 12:00', '2019-01-12 12:00'),
                reservation('4', '2019-01-12 13:00', '2019-01-14 13:00'),
                reservation('5', '2019-01-16 13:00', '2019-01-18 13:00'),
        ]
        operations.insertAll(reservations)

        def filter = new PlacementFilter(
                LocalDateTime.of(2019, 1, 4, 12, 0),
                LocalDateTime.of(2019, 1, 22, 12, 0)
        )
        def expected = new PlacementResult(4, 5)
        expected.calculateAvailablePlaces(6)

        when:
        def result = repository.availablePlaces(id, filter)

        then:
        result.isSuccess()
        def placementResult = result.getOrElse(null)
        placementResult.availablePlaces == expected.availablePlaces
        placementResult.reservations == expected.reservations
        placementResult.takenPlaces == expected.takenPlaces
    }

    def "should calculate available places on three not intersecting two right three middle reservations"() {
        setup:
        def apartment = apartment()
        def id = '123'
        apartment.id = id
        operations.insert(apartment, "apartment")


        def reservations = [
                reservation('1', '2019-01-09 12:00', '2019-01-12 12:00'),
                reservation('2', '2019-01-12 13:00', '2019-01-14 13:00'),
                reservation('3', '2019-01-16 13:00', '2019-01-18 13:00'),
                reservation('4', '2019-01-19 13:00', '2019-01-24 13:00'),
                reservation('5', '2019-01-20 13:00', '2019-01-25 13:00'),
        ]
        operations.insertAll(reservations)

        def filter = new PlacementFilter(
                LocalDateTime.of(2019, 1, 4, 12, 0),
                LocalDateTime.of(2019, 1, 22, 12, 0)
        )
        def expected = new PlacementResult(4, 5)
        expected.calculateAvailablePlaces(6)

        when:
        def result = repository.availablePlaces(id, filter)

        then:
        result.isSuccess()
        def placementResult = result.getOrElse(null)
        placementResult.availablePlaces == expected.availablePlaces
        placementResult.reservations == expected.reservations
        placementResult.takenPlaces == expected.takenPlaces
    }

    def "should calculate available places on three intersecting middle and one not intersecting right reservations"() {
        setup:
        def apartment = apartment()
        def id = '123'
        apartment.id = id
        operations.insert(apartment, "apartment")


        def reservations = [
                reservation('1', '2019-01-09 12:00', '2019-01-12 12:00'),
                reservation('2', '2019-01-11 13:00', '2019-01-14 13:00'),
                reservation('3', '2019-01-16 13:00', '2019-01-18 13:00'),
                reservation('4', '2019-01-19 13:00', '2019-01-24 13:00'),
        ]
        operations.insertAll(reservations)

        def filter = new PlacementFilter(
                LocalDateTime.of(2019, 1, 4, 12, 0),
                LocalDateTime.of(2019, 1, 22, 12, 0)
        )
        def expected = new PlacementResult(4, 4)
        expected.calculateAvailablePlaces(6)

        when:
        def result = repository.availablePlaces(id, filter)

        then:
        result.isSuccess()
        def placementResult = result.getOrElse(null)
        placementResult.availablePlaces == expected.availablePlaces
        placementResult.reservations == expected.reservations
        placementResult.takenPlaces == expected.takenPlaces
    }
}
