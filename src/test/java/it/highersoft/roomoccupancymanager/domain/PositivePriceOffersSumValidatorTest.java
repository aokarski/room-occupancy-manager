package it.highersoft.roomoccupancymanager.domain;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static it.highersoft.roomoccupancymanager.domain.RoomType.PREMIUM;
import static java.util.Collections.emptyList;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class PositivePriceOffersSumValidatorTest {
    private PositivePriceOffersSumValidator validator = new PositivePriceOffersSumValidator();

    @Test
    void whenValidRequestProvidedNoExceptionThrown() {
        // given:
        var request = new OccupancyRequest(List.of(Vacancy.of(PREMIUM, 5)),
                List.of(BigDecimal.TEN));

        // when:
        // then:
        assertDoesNotThrow(() -> validator.validate(request));
    }

    @Test
    void whenVacancySumNotPositiveProvidedAnExceptionIsThrown() {
        // given:
        var request = new OccupancyRequest(List.of(Vacancy.of(PREMIUM, 1)), List.of(BigDecimal.ZERO));

        // when:
        // then:
        InvalidInputException thrown = assertThrows(
                InvalidInputException.class,
                () -> validator.validate(request)
        );

        assertTrue(thrown.getMessage().contains("All guest offer prices must be greater than 0"));
    }
}