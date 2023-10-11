package com.company.restaurantsystem.service;

import com.vaadin.flow.data.provider.CallbackDataProvider;
import com.vaadin.flow.data.provider.DataProvider;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Stream;

@SuppressWarnings({"rawtypes", "unchecked"})
@Component
public class DataProviderHelper {

    public DataProvider createCallbackDataProvider(List<?> items) {
        return new CallbackDataProvider<>(e -> (Stream) items.stream()
                .limit(e.getLimit())
                .skip(e.getOffset()), e -> Math.toIntExact(items.stream()
                        .limit(e.getLimit())
                        .skip(e.getOffset())
                        .count()));
    }
}
