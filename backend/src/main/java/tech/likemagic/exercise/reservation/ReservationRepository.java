package tech.likemagic.exercise.reservation;

import java.time.OffsetDateTime;
import java.util.UUID;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

public interface ReservationRepository extends ReactiveCrudRepository<Reservation, UUID> {

    Flux<Reservation> findAllByPropertyIdOrderByArrivalAsc(UUID propertyId);

    /**
     * Half-open range [from, until). Provided as a starting point; extend or
     * replace it as you see fit.
     */
    @Query("""
            select * from reservation
            where property_id = :propertyId
              and arrival >= :from
              and arrival <  :until
            order by arrival asc
            """)
    Flux<Reservation> findArrivals(@Param("propertyId") UUID propertyId,
                                   @Param("from") OffsetDateTime from,
                                   @Param("until") OffsetDateTime until);

    @Query("""
               SELECT r.id, r.guest_name, r.arrival, u.id AS unit_id, u.label AS unit_label, u.floor
                   FROM reservation r
                   JOIN property p ON r.property_id = p.id
                   LEFT JOIN unit u ON r.unit_id = u.id
                   WHERE r.property_id = :propertyId
                     AND (r.arrival AT TIME ZONE p.timezone)::date = (CURRENT_TIMESTAMP AT TIME ZONE p.timezone)::date
                   ORDER BY r.arrival ASC
            """)
    Flux<Reservation> findTodayArrivalsByProperty(@Param("propertyId") UUID propertyId);

}