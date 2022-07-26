package it.highersoft.roomoccupancymanager.domain;

import java.math.BigDecimal;
import java.util.function.Predicate;

public class PositiveAmountValidator implements BusinessValidator {
    private static final Predicate<BigDecimal> POSITIVE_NUM = num -> num.compareTo(BigDecimal.ZERO) > 0;

    @Override
    public void validate(OccupancyRequest request) {
        if (!request.getGuestOffers().stream().allMatch(POSITIVE_NUM)) {
            throw new InvalidInputException("All guest offer prices must be greater than 0");
        }
    }
}
