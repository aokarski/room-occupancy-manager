package it.highersoft.roomoccupancymanager.rest;

import java.math.BigDecimal;
import java.util.List;

import it.highersoft.roomoccupancymanager.domain.Currency;
import it.highersoft.roomoccupancymanager.domain.InvalidInputException;
import it.highersoft.roomoccupancymanager.domain.Occupancy;
import it.highersoft.roomoccupancymanager.domain.OccupancyResult;
import it.highersoft.roomoccupancymanager.domain.RoomOccupancyService;
import it.highersoft.roomoccupancymanager.domain.RoomType;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(RoomOccupancyController.class)
class RoomOccupancyControllerTest {
    @MockBean
    RoomOccupancyService roomOccupancyService;

    @Autowired
    Currency currency;

    @Autowired
    MockMvc mockMvc;

    @Test
    void whenValidInputShouldReturnUsages() throws Exception {
        Mockito.when(roomOccupancyService.book(any())).thenReturn(new OccupancyResult(
                List.of(new Occupancy(RoomType.PREMIUM, BigDecimal.TEN))));

        mockMvc.perform(MockMvcRequestBuilders.post("/occupancy")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                    {
                                        "freeEconomyRooms":3,
                                        "freePremiumRooms":3,
                                        "priceOffers":[23, 45, 155, 374, 22, 99.99, 100, 101, 115, 209]
                                    }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.usagePremium", Matchers.matchesPattern(".*USD 10.*")))
                .andExpect(jsonPath("$.usageEconomy", Matchers.matchesPattern(".*USD 0.*")));
    }

    @Test
    void shouldReturn400WhenInvalidInputProvided() throws Exception {
        Mockito.when(roomOccupancyService.book(any())).thenThrow(new InvalidInputException("invalid input"));

        mockMvc.perform(MockMvcRequestBuilders.post("/occupancy")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                    {
                                        "freeEconomyRooms":3,
                                        "freePremiumRooms":3,
                                        "priceOffers":[0, 0]
                                    }
                                """))
                .andExpect(status().isBadRequest());
    }

    @TestConfiguration
    static class AdditionalConfig {
        @Bean
        public Currency currency() {
            return new Currency("USD");
        }
    }
}