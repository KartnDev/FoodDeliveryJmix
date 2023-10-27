package com.company.ordersystem.service.process.order.steps;

import com.company.ordersystem.client.RestaurantClient;
import com.company.ordersystem.entity.Order;
import com.company.ordersystem.repository.OrderRepository;
import com.company.ordersystem.service.OrderService;
import com.company.ordersystem.service.process.order.abc.AbstractTransactionalStep;
import io.jmix.core.DataManager;
import io.jmix.core.FetchPlans;
import io.jmix.core.SaveContext;
import io.jmix.core.security.SystemAuthenticator;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.delegate.DelegateExecution;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;

@Service
public class RequestRestaurantCookStep extends AbstractTransactionalStep {
    private final RestaurantClient restaurantClient;
    private final OrderService orderService;
    protected RequestRestaurantCookStep(SystemAuthenticator systemAuthenticator,
                                        RuntimeService runtimeService,
                                        PlatformTransactionManager transactionManager,
                                        DataManager dataManager,
                                        OrderRepository orderRepository,
                                        FetchPlans fetchPlans,
                                        RestaurantClient restaurantClient,
                                        OrderService orderService) {
        super(systemAuthenticator, runtimeService, transactionManager, dataManager, orderRepository, fetchPlans);
        this.restaurantClient = restaurantClient;
        this.orderService = orderService;
    }

    @Override
    protected void doTransactionalStep(DelegateExecution execution, Order order, SaveContext saveContext) {
        restaurantClient.publishRestaurantCookRequest(order.getRestaurantId(), orderService.convert(order));
    }
}
