package com.company.autoconfigure.orderapi;

import com.company.orderapi.OrdrapiConfiguration;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Import;

@AutoConfiguration
@Import({OrdrapiConfiguration.class})
public class OrdrapiAutoConfiguration {
}

