package it.highersoft.roomoccupancymanager.rest;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class OccupancyRequestDto {
    private Integer freeEconomyRooms;
    private Integer freePremiumRooms;
    private List<Double> priceOffers;
}
