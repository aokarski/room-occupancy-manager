package it.highersoft.roomoccupancymanager.domain;

public interface OccupancyCalculator {
    OccupancyResult calculate(OccupancyRequest request);
}
