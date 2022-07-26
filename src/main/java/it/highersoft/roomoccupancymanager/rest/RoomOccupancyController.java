package it.highersoft.roomoccupancymanager.rest;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import it.highersoft.roomoccupancymanager.domain.Currency;
import it.highersoft.roomoccupancymanager.domain.OccupancyRequest;
import it.highersoft.roomoccupancymanager.domain.OccupancyResult;
import it.highersoft.roomoccupancymanager.domain.RoomOccupancyService;
import it.highersoft.roomoccupancymanager.domain.Vacancy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import static it.highersoft.roomoccupancymanager.domain.RoomType.ECONOMY;
import static it.highersoft.roomoccupancymanager.domain.RoomType.PREMIUM;

@Slf4j
@RestController
@RequestMapping("/occupancy")
public class RoomOccupancyController {
    private final Currency currency;
    private final RoomOccupancyService roomOccupancyService;

    public RoomOccupancyController(Currency currency,
                                   RoomOccupancyService roomOccupancyService) {
        this.currency = currency;
        this.roomOccupancyService = roomOccupancyService;
    }

    @PostMapping
    public @ResponseBody
    ResponseEntity<OccupancyResultDto> book(@RequestBody OccupancyRequestDto request) {
        log.info("Booking request: {}", request);

        final OccupancyResult occupancyResult = roomOccupancyService.book(mapRequest(request));

        return new ResponseEntity<>(mapResponse(occupancyResult), HttpStatus.OK);
    }

    private OccupancyRequest mapRequest(OccupancyRequestDto request) {
        final List<Vacancy> vacancies = new ArrayList<>();
        vacancies.add(Vacancy.of(ECONOMY, request.getFreeEconomyRooms()));
        vacancies.add(Vacancy.of(PREMIUM, request.getFreePremiumRooms()));

        return new OccupancyRequest(vacancies, request.getPriceOffers().stream()
                .map(BigDecimal::valueOf)
                .collect(Collectors.toList()));
    }

    private OccupancyResultDto mapResponse(OccupancyResult result) {
        var premium = "Usage Premium: %s (%s %s)".formatted(result.count(PREMIUM),
                currency.getName(), result.sum(PREMIUM));

        var economy = "Usage Economy: %s (%s %s)".formatted(result.count(ECONOMY),
                currency.getName(), result.sum(ECONOMY));

        return new OccupancyResultDto(premium, economy);
    }
}
