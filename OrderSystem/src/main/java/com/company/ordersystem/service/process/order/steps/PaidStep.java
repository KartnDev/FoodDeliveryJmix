package com.company.ordersystem.service.process.order.steps;

import com.company.ordersystem.entity.DraftOrderStatus;
import com.company.ordersystem.entity.OrderEntity;
import com.company.ordersystem.repository.OrderRepository;
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
public class PaidStep extends AbstractTransactionalStep {
    protected PaidStep(SystemAuthenticator systemAuthenticator, RuntimeService runtimeService, PlatformTransactionManager transactionManager, DataManager dataManager, OrderRepository orderRepository, FetchPlans fetchPlans) {
        super(systemAuthenticator, runtimeService, transactionManager, dataManager, orderRepository, fetchPlans);
    }

    @Override
    protected void doTransactionalStep(DelegateExecution execution, OrderEntity order, SaveContext saveContext) {
        order.setStatus(DraftOrderStatus.PAID);
        saveContext.saving(order);
    }
}
