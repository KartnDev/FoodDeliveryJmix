package com.company.ordersystem.service.process.order.abc;

import com.company.ordersystem.entity.OrderEntity;
import com.company.ordersystem.repository.OrderRepository;
import io.jmix.core.FetchPlans;
import io.jmix.core.security.SystemAuthenticator;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;

import java.util.Map;
import java.util.Optional;

import static com.company.ordersystem.service.process.order.OrderProcessManager.PROCESS_USER_KEY;

public abstract class AbstractStep implements JavaDelegate {
    protected final SystemAuthenticator systemAuthenticator;
    protected final RuntimeService runtimeService;
    protected final OrderRepository orderRepository;
    protected final FetchPlans fetchPlans;

    protected AbstractStep(SystemAuthenticator systemAuthenticator,
                           RuntimeService runtimeService,
                           OrderRepository orderRepository,
                           FetchPlans fetchPlans) {
        this.systemAuthenticator = systemAuthenticator;
        this.runtimeService = runtimeService;
        this.orderRepository = orderRepository;
        this.fetchPlans = fetchPlans;
    }

    protected abstract void runStep(DelegateExecution execution);

    @Override
    public void execute(DelegateExecution execution) {
        systemAuthenticator.runWithSystem(() -> runStep(execution));
    }

    protected void persistVariable(DelegateExecution execution, String variableName, Object value) {
        runtimeService.setVariable(execution.getId(), variableName, value);
    }

    protected void persistVariables(DelegateExecution execution, Map<String, Object> variables) {
        runtimeService.setVariables(execution.getId(), variables);
    }

    @SuppressWarnings("unchecked")
    protected <T> T getVariable(DelegateExecution execution, String variableName) {
        return (T) runtimeService.getVariable(execution.getId(), variableName);
    }

    protected Optional<OrderEntity> findOrder(DelegateExecution execution) {
        var fetchPlan = fetchPlans.builder(OrderEntity.class)
                .addFetchPlan("order-fetch-plan")
                .build();

        String orderId = execution.getProcessInstanceBusinessKey();
        return orderRepository.findById(Long.valueOf(orderId), fetchPlan);
    }
}
