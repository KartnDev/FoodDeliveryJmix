package com.company.ordersystem.service.process.order.abc;

import com.company.ordersystem.entity.Order;
import com.company.ordersystem.repository.OrderRepository;
import io.jmix.core.DataManager;
import io.jmix.core.FetchPlans;
import io.jmix.core.SaveContext;
import io.jmix.core.security.SystemAuthenticator;
import lombok.extern.slf4j.Slf4j;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.delegate.DelegateExecution;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.support.TransactionTemplate;

@Slf4j
public abstract class AbstractTransactionalStep extends AbstractStep {
    protected final PlatformTransactionManager transactionManager;
    protected final DataManager dataManager;

    protected AbstractTransactionalStep(SystemAuthenticator systemAuthenticator,
                                        RuntimeService runtimeService,
                                        PlatformTransactionManager transactionManager,
                                        DataManager dataManager,
                                        OrderRepository orderRepository,
                                        FetchPlans fetchPlans) {
        super(systemAuthenticator, runtimeService, orderRepository, fetchPlans);
        this.transactionManager = transactionManager;
        this.dataManager = dataManager;
    }


    protected abstract void doTransactionalStep(DelegateExecution execution,
                                                Order order,
                                                SaveContext saveContext);

    @Override
    protected void runStep(DelegateExecution execution) {
        TransactionTemplate transactionTemplate = new TransactionTemplate(transactionManager);
        transactionTemplate.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);

        transactionTemplate.executeWithoutResult(transactionStatus ->
                doStepCriticallyWithTransaction(execution, findOrder(execution).orElseThrow()));
    }

    private void doStepCriticallyWithTransaction(DelegateExecution execution, Order order) {
        try {
            SaveContext criticalSaveContext = new SaveContext();
            doTransactionalStep(execution, order, criticalSaveContext);
            dataManager.save(criticalSaveContext);
        } catch (Exception e) {
            log.error("Catch exception while handling Step [" + this.getClass().getSimpleName() + "] ", e);
            throw e;
        }
    }
}
