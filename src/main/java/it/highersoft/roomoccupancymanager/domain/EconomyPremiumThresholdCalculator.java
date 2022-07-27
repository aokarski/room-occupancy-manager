package it.highersoft.roomoccupancymanager.domain;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import static it.highersoft.roomoccupancymanager.domain.RoomType.ECONOMY;
import static it.highersoft.roomoccupancymanager.domain.RoomType.PREMIUM;
import static java.util.Comparator.reverseOrder;

@Component
public class EconomyPremiumThresholdCalculator implements OccupancyCalculator {
    private final BigDecimal priceThreshold;

    public EconomyPremiumThresholdCalculator(BigDecimal priceThreshold) {
        this.priceThreshold = priceThreshold;
    }

    @Override
    public OccupancyResult calculate(OccupancyRequest request) {
        var availableEconomy = getVacancyForTheType(ECONOMY, request);
        var availablePremium = getVacancyForTheType(PREMIUM, request);

        var offersBelowThresholdDesc = belowThreshold(request);
        offersBelowThresholdDesc.sort(reverseOrder());

        var offersEqualAboveDesc = equalOrAboveThreshold(request);
        offersEqualAboveDesc.sort(reverseOrder());

        final List<Occupancy> occupancies = new ArrayList<>();

        var economyIdx = 0;
        var premiumIdx = 0;

        while (premiumIdx < availablePremium && premiumIdx < offersEqualAboveDesc.size()) {
            occupancies.add(new Occupancy(PREMIUM, offersEqualAboveDesc.get(premiumIdx)));
            premiumIdx++;
        }
        var premiumsVacancyLeft = availablePremium - premiumIdx;
        var economyOverbookCount = offersBelowThresholdDesc.size() - availableEconomy;

        while (economyOverbookCount > 0 && premiumsVacancyLeft > 0) {
            occupancies.add(new Occupancy(PREMIUM, offersBelowThresholdDesc.get(economyIdx)));
            economyIdx++;
            economyOverbookCount--;
            premiumsVacancyLeft--;
        }

        var economyBooked = 0;
        while (economyIdx < offersBelowThresholdDesc.size() && economyBooked < availableEconomy) {
            occupancies.add(new Occupancy(ECONOMY, offersBelowThresholdDesc.get(economyIdx)));
            economyIdx++;
            economyBooked++;
        }

        return new OccupancyResult(occupancies);
    }

    private int getVacancyForTheType(RoomType type, OccupancyRequest request) {
        return request.getVacancies().stream()
                .filter(vacancy -> vacancy.getRoomType() == type)
                .mapToInt(Vacancy::getSize)
                .sum();
    }

    private List<BigDecimal> belowThreshold(OccupancyRequest request) {
        return request.getGuestOffers().stream()
                .filter(offer -> offer.compareTo(priceThreshold) < 0)
                .collect(Collectors.toList());
    }

    private List<BigDecimal> equalOrAboveThreshold(OccupancyRequest request) {
        return request.getGuestOffers().stream()
                .filter(offer -> offer.compareTo(priceThreshold) >= 0)
                .collect(Collectors.toList());
    }
}
