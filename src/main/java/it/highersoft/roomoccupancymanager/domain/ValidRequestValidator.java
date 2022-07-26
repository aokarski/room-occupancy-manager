package it.highersoft.roomoccupancymanager.domain;

public class ValidRequestValidator implements BusinessValidator {
    @Override
    public void validate(OccupancyRequest request) {
        if (request.getGuestOffers() == null || request.getGuestOffers().isEmpty()) {
            throw new InvalidInputException("At least one guest offer price must be provided");
        }

        if (request.getVacancies() == null || request.getVacancies().isEmpty()) {
            throw new InvalidInputException("At least one vacancy must be provided");
        }

        if (!allVacancyNotNegative(request) || !vacanciesSumPositive(request)) {
            throw new InvalidInputException("Invalid number of vacancy provided");
        }
    }

    private boolean allVacancyNotNegative(OccupancyRequest request) {
        return request.getVacancies().stream()
                .allMatch(vacancy -> vacancy.getSize() > 0);
    }

    private boolean vacanciesSumPositive(OccupancyRequest request) {
        return request.getVacancies().stream()
                .mapToInt(Vacancy::getSize)
                .sum() > 0;
    }
}
