package it.highersoft.roomoccupancymanager.domain;

import java.math.BigDecimal;
import java.util.List;

import lombok.ToString;
import lombok.Value;

@ToString
@Value
public class OccupancyRequest {
    List<Vacancy> vacancies;
    List<BigDecimal> guestOffers;
}
