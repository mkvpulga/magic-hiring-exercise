package tech.likemagic.exercise.property;

import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Flux;
import tech.likemagic.exercise.reservation.Reservation;
import tech.likemagic.exercise.reservation.ReservationRepository;
import tech.likemagic.exercise.unit.UnitRepository;
import java.util.UUID;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import reactor.core.publisher.Mono;
import tech.likemagic.exercise.unit.Unit;


/**
 * WORKED EXAMPLE.
 *
 * This is the complete pattern for a reactive GraphQL query:
 *   schema.graphqls -> this controller -> ReactiveCrudRepository -> Flux
 *
 * Note that nothing here blocks. The Flux is handed to Spring GraphQL, which
 * subscribes to it. Copy this shape for your own query.
 */
@Controller
public class PropertyGraphqlController {

    private final PropertyRepository properties;

    private final ReservationRepository reservations;

    private final UnitRepository units;

    public PropertyGraphqlController(
            PropertyRepository properties,
            ReservationRepository reservations,
            UnitRepository units
    ) {
        this.properties = properties;
        this.reservations = reservations;
        this.units = units;
    }


    @QueryMapping
    public Flux<Property> properties() {
        return properties.findAllByOrderByNameAsc();
    }

    @QueryMapping
    public Flux<Reservation> todayArrivals(@Argument UUID propertyId) {
        // Return your reactive stream of mapped arrivals here
        return reservations.findTodayArrivalsByProperty(propertyId);
    }


    @SchemaMapping(typeName = "Reservation", field = "assignedUnit")
    public Mono<Unit> assignedUnit(Reservation reservation) {
        // Call .unitId() directly instead of getUnitId()
        if (reservation.unitId() == null) {
            return Mono.empty();
        }
        return units.findById(reservation.unitId());
    }

}
