package it.highersoft.roomoccupancymanager.domain;

import java.math.BigDecimal;

import lombok.Value;

@Value
public class Occupancy {
    RoomType roomType;
    BigDecimal price;
}
