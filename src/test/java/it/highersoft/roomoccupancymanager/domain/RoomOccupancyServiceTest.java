package it.highersoft.roomoccupancymanager.domain;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RoomOccupancyServiceTest {
    @InjectMocks
    RoomOccupancyService underTest;

    @Mock
    OccupancyCalculator calculator;

    @Mock
    List<BusinessValidator> validators;

    @Test
    void testHappyPath() {
        // given:
        OccupancyRequest request = new OccupancyRequest(List.of(Vacancy.of(RoomType.PREMIUM, 1)), List.of(BigDecimal.TEN));

        when(calculator.calculate(request))
                .thenReturn(new OccupancyResult(List.of(new Occupancy(RoomType.PREMIUM, BigDecimal.TEN))));

        // when:
        var result = underTest.book(request);

        // then:
        assertNotNull(request);
        assertEquals(1, result.count(RoomType.PREMIUM));
        assertEquals(BigDecimal.TEN, result.sum(RoomType.PREMIUM));
    }
}