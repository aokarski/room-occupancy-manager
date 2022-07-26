package it.highersoft.roomoccupancymanager.domain;

import java.util.List;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class RoomOccupancyService {
    private final OccupancyCalculator occupancyCalculator;
    private final List<BusinessValidator> validators;

    public RoomOccupancyService(OccupancyCalculator occupancyCalculator,
                                List<BusinessValidator> validators) {
        this.occupancyCalculator = occupancyCalculator;
        this.validators = validators;
    }

    public OccupancyResult book(OccupancyRequest request) {
        log.info("Booking the following request: {}", request);

        validate(request);

        return occupancyCalculator.calculate(request);
    }

    private void validate(OccupancyRequest request) {
        validators.forEach(businessValidator -> businessValidator.validate(request));
    }
}
