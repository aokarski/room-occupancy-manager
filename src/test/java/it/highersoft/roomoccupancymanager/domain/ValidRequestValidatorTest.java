package it.highersoft.roomoccupancymanager.domain;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static it.highersoft.roomoccupancymanager.domain.RoomType.ECONOMY;
import static it.highersoft.roomoccupancymanager.domain.RoomType.PREMIUM;
import static java.util.Collections.emptyList;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class ValidRequestValidatorTest {
    private ValidRequestValidator validRequestValidator = new ValidRequestValidator();

    @Test
    void whenValidRequestProvidedNoExceptionThrown() {
        // given:
        var request = new OccupancyRequest(List.of(Vacancy.of(PREMIUM, 5)),
                List.of(BigDecimal.TEN));

        // when:
        // then:
        assertDoesNotThrow(() -> validRequestValidator.validate(request));
    }

    @Test
    void whenVacancySumNotPositiveProvidedAnExceptionIsThrown() {
        // given:
        var request = new OccupancyRequest(List.of(Vacancy.of(PREMIUM, 0)),
                List.of(BigDecimal.TEN));

        // when:
        // then:
        InvalidInputException thrown = assertThrows(
                InvalidInputException.class,
                () -> validRequestValidator.validate(request)
        );

        assertTrue(thrown.getMessage().contains("Invalid number of vacancy provided"));
    }

    @Test
    void whenVacancyNotProvidedAnExceptionIsThrown() {
        // given:
        var request = new OccupancyRequest(emptyList(), List.of(BigDecimal.TEN));

        // when:
        // then:
        InvalidInputException thrown = assertThrows(
                InvalidInputException.class,
                () -> validRequestValidator.validate(request)
        );

        assertTrue(thrown.getMessage().contains("At least one vacancy must be provided"));
    }

    @Test
    void whenPriceOffersNotProvidedAnExceptionIsThrown() {
        // given:
        var request = new OccupancyRequest(List.of(Vacancy.of(ECONOMY, 1)), emptyList());

        // when:
        // then:
        InvalidInputException thrown = assertThrows(
                InvalidInputException.class,
                () -> validRequestValidator.validate(request)
        );

        assertTrue(thrown.getMessage().contains("At least one guest offer price must be provided"));
    }
}