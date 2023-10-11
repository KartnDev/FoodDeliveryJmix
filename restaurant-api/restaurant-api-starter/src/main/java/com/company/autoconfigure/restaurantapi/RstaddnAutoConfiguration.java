package com.company.autoconfigure.restaurantapi;

import com.company.restaurantapi.RstaddnConfiguration;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Import;

@AutoConfiguration
@Import({RstaddnConfiguration.class})
public class RstaddnAutoConfiguration {
}

