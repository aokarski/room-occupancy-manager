package it.highersoft.roomoccupancymanager.domain;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static it.highersoft.roomoccupancymanager.domain.RoomType.ECONOMY;
import static it.highersoft.roomoccupancymanager.domain.RoomType.PREMIUM;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
class EconomyPremiumThresholdCalculatorTest {
    private static final BigDecimal PRICE_THRESHOLD = new BigDecimal("100.00");

    private static final List<BigDecimal> GUEST_OFFERS = Arrays.stream(
                    new double[]{23, 45, 155, 374, 22, 99.99, 100, 101, 115, 209})
            .mapToObj(String::valueOf)
            .map(BigDecimal::new)
            .collect(Collectors.toList());

    private final EconomyPremiumThresholdCalculator underTest = new EconomyPremiumThresholdCalculator(PRICE_THRESHOLD);

    @Test
    void testCase1() {
        // given:
        var premiumVacancy = Vacancy.of(PREMIUM, 3);
        var economyVacancy = Vacancy.of(ECONOMY, 3);

        // when:
        var result = underTest.calculate(new OccupancyRequest(List.of(premiumVacancy, economyVacancy), GUEST_OFFERS));

        // then:
        assertNotNull(result);
        assertNotNull(result.getOccupancies());
        assertFalse(result.getOccupancies().isEmpty());

        assertEquals(3, result.count(PREMIUM));
        assertEquals(new BigDecimal("738.0"), result.sum(PREMIUM));

        assertEquals(3, result.count(PREMIUM));
        assertEquals(new BigDecimal("167.99"), result.sum(ECONOMY));
    }

    @Test
    void testCase2() {
        // given:
        var premiumVacancy = Vacancy.of(PREMIUM, 7);
        var economyVacancy = Vacancy.of(ECONOMY, 5);

        // when:
        var result = underTest.calculate(new OccupancyRequest(List.of(premiumVacancy, economyVacancy), GUEST_OFFERS));

        // then:
        assertNotNull(result);
        assertNotNull(result.getOccupancies());
        assertFalse(result.getOccupancies().isEmpty());

        assertEquals(6, result.count(PREMIUM));
        assertEquals(new BigDecimal("1054.0"), result.sum(PREMIUM));

        assertEquals(4, result.count(ECONOMY));
        assertEquals(new BigDecimal("189.99"), result.sum(ECONOMY));
    }

    @Test
    void testCase3() {
        // given:
        var premiumVacancy = Vacancy.of(PREMIUM, 2);
        var economyVacancy = Vacancy.of(ECONOMY, 7);

        // when:
        var result = underTest.calculate(new OccupancyRequest(List.of(premiumVacancy, economyVacancy), GUEST_OFFERS));

        // then:
        assertNotNull(result);
        assertNotNull(result.getOccupancies());
        assertFalse(result.getOccupancies().isEmpty());

        assertEquals(2, result.count(PREMIUM));
        assertEquals(new BigDecimal("583.0"), result.sum(PREMIUM));

        assertEquals(4, result.count(ECONOMY));
        assertEquals(new BigDecimal("189.99"), result.sum(ECONOMY));
    }

    @Test
    void testCase4() {
        // given:
        var premiumVacancy = Vacancy.of(PREMIUM, 7);
        var economyVacancy = Vacancy.of(ECONOMY, 1);

        // when:
        var result = underTest.calculate(new OccupancyRequest(List.of(premiumVacancy, economyVacancy), GUEST_OFFERS));

        // then:
        assertNotNull(result);
        assertNotNull(result.getOccupancies());
        assertFalse(result.getOccupancies().isEmpty());

        assertEquals(7, result.count(PREMIUM));
        assertEquals(new BigDecimal("1153.99"), result.sum(PREMIUM));

        assertEquals(1, result.count(ECONOMY));
        assertEquals(new BigDecimal("45.0"), result.sum(ECONOMY));
    }

    @Test
    void whenNoVacancyForSomeRoomsTheCalculationStillPossible() {
        // given:
        var premiumVacancy = Vacancy.of(PREMIUM, 0);
        var economyVacancy = Vacancy.of(ECONOMY, 1);

        // when:
        var result = underTest.calculate(new OccupancyRequest(List.of(premiumVacancy, economyVacancy), GUEST_OFFERS));

        // then:
        assertNotNull(result);
        assertNotNull(result.getOccupancies());
        assertFalse(result.getOccupancies().isEmpty());

        assertEquals(0, result.count(PREMIUM));
        assertEquals(BigDecimal.ZERO, result.sum(PREMIUM));

        assertEquals(1, result.count(ECONOMY));
        assertEquals(new BigDecimal("99.99"), result.sum(ECONOMY));
    }
}