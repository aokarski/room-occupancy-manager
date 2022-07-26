package it.highersoft.roomoccupancymanager.rest;

import lombok.Data;

@Data
public class OccupancyResultDto {
    private final String usagePremium;
    private final String usageEconomy;
}
