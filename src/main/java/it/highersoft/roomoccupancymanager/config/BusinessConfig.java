/*
 * Copyright (c) 2022 SmartRecruiters Inc. All Rights Reserved.
 */

package it.highersoft.roomoccupancymanager.config;

import java.math.BigDecimal;
import java.util.List;

import it.highersoft.roomoccupancymanager.domain.BusinessValidator;
import it.highersoft.roomoccupancymanager.domain.Currency;
import it.highersoft.roomoccupancymanager.domain.EconomyPremiumThresholdCalculator;
import it.highersoft.roomoccupancymanager.domain.OccupancyCalculator;
import it.highersoft.roomoccupancymanager.domain.PositiveAmountValidator;
import it.highersoft.roomoccupancymanager.domain.ValidRequestValidator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BusinessConfig {
    private static final int PRICE_THRESHOLD = 100;
    private static final String DEFAULT_CURRENCY = "EUR";

    @Bean
    public BigDecimal priceThreshold() {
        return BigDecimal.valueOf(PRICE_THRESHOLD);
    }

    @Bean
    public Currency currency() {
        return new Currency(DEFAULT_CURRENCY);
    }

    @Bean
    public OccupancyCalculator occupancyCalculator() {
        return new EconomyPremiumThresholdCalculator(priceThreshold());
    }

    @Bean
    public List<BusinessValidator> validators() {
        return List.of(new ValidRequestValidator(),
                new PositiveAmountValidator());
    }
}
