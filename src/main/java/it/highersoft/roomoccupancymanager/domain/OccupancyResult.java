package it.highersoft.roomoccupancymanager.domain;

import java.math.BigDecimal;
import java.util.List;

import lombok.Value;

@Value
public class OccupancyResult {
    List<Occupancy> occupancies;

    public BigDecimal sum(RoomType type) {
        if (occupancies.isEmpty()) {
            return BigDecimal.ZERO;
        }

        return occupancies.stream()
                .filter(occupancy -> type == occupancy.getRoomType())
                .map(Occupancy::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public int count(RoomType type) {
        if (occupancies.isEmpty()) {
            return 0;
        }

        return (int) occupancies.stream()
                .filter(occupancy -> type == occupancy.getRoomType())
                .count();
    }
}
