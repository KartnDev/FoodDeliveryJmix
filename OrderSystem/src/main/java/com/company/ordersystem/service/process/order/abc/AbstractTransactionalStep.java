package com.company.ordersystem.service.process.order.abc;

import com.company.ordersystem.entity.OrderEntity;
import com.company.ordersystem.repository.OrderRepository;
import io.jmix.core.DataManager;
import io.jmix.core.FetchPlans;
import io.jmix.core.SaveContext;
import io.jmix.core.security.SystemAuthenticator;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.delegate.DelegateExecution;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.support.TransactionTemplate;

public abstract class AbstractTransactionalStep extends AbstractStep {
    private final Logger log = LoggerFactory.getLogger(AbstractTransactionalStep.class);
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
                                                OrderEntity order,
                                                SaveContext saveContext);

    @Override
    protected void runStep(DelegateExecution execution) {
        TransactionTemplate transactionTemplate = new TransactionTemplate(transactionManager);
        transactionTemplate.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);

        transactionTemplate.executeWithoutResult(transactionStatus ->
                doStepCriticallyWithTransaction(execution, findOrder(execution).orElseThrow()));
    }

    private void doStepCriticallyWithTransaction(DelegateExecution execution, OrderEntity order) {
        try {
            SaveContext criticalSaveContext = new SaveContext();
            doTransactionalStep(execution, order, criticalSaveContext);
            dataManager.save(criticalSaveContext);
        } catch (Exception e) {
            log.error("Catch exception while handling Step [" + this.getClass().getSimpleName() + "] ", e);
            throw e;
        }
    }

    protected void doSomeWork() {
        try {
            Thread.sleep(3000L);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
