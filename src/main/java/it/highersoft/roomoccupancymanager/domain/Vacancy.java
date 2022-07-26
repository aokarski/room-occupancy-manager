package it.highersoft.roomoccupancymanager.domain;

import lombok.Value;

@Value(staticConstructor = "of")
public class Vacancy {
    RoomType roomType;
    int size;
}
