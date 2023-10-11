package com.company.autoconfigure.useroidcplagin;

import com.company.useroidcplagin.AppuserConfiguration;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Import;

@AutoConfiguration
@Import({AppuserConfiguration.class})
public class AppuserAutoConfiguration {
}

