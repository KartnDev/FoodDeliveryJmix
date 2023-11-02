package com.company.ordersystem.service.process.order.steps;

import com.company.ordersystem.client.RestaurantClient;
import com.company.ordersystem.entity.DraftOrderStatus;
import com.company.ordersystem.entity.OrderEntity;
import com.company.ordersystem.repository.OrderRepository;
import com.company.ordersystem.service.OrderService;
import com.company.ordersystem.service.process.order.abc.AbstractTransactionalStep;
import io.jmix.core.DataManager;
import io.jmix.core.FetchPlans;
import io.jmix.core.SaveContext;
import io.jmix.core.security.SystemAuthenticator;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.delegate.DelegateExecution;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;

import static com.company.ordersystem.service.process.order.OrderProcessManager.PROCESS_USER_KEY;
import static com.company.ordersystem.service.process.order.OrderProcessManager.SUBJECT_TOKEN;

@Service
public class RequestRestaurantCookStep extends AbstractTransactionalStep {
    private final Logger log = LoggerFactory.getLogger(RequestRestaurantCookStep.class);
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
    protected void doTransactionalStep(DelegateExecution execution, OrderEntity order, SaveContext saveContext) {
        String username = getVariable(execution, PROCESS_USER_KEY);
        String subjectToken = getVariable(execution, SUBJECT_TOKEN);
        String result = systemAuthenticator.withUser(username,
                () -> restaurantClient.publishRestaurantCookRequest(order.getRestaurantId(), orderService.convert(order), subjectToken));
        log.info("Result from restaurant system for cook request: " + result);
        order.setStatus(DraftOrderStatus.WAIT_FOR_RESTAURANT);
        saveContext.saving(order);
        doSomeWork();
    }
}
