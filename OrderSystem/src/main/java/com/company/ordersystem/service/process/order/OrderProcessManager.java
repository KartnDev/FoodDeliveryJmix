package com.company.ordersystem.service.process.order;

import com.company.ordersystem.entity.OrderEntity;
import com.company.ordersystem.repository.OrderRepository;
import com.company.useroidcplagin.entity.AppUser;
import io.jmix.core.security.CurrentAuthentication;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.api.Task;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.Map;

@Service
public class OrderProcessManager {
    public static final String ORDER_PROCESS_SCHEMA_ID = "ORDER_PROCESS";
    public static final String  PROCESS_USER_KEY = "startedBy";
    public static final String  SUBJECT_TOKEN = "SUBJECT_TOKEN";

    private final RuntimeService runtimeService;
    private final TaskService taskService;
    private final OrderRepository orderRepository;
    private final CurrentAuthentication currentAuthentication;

    public OrderProcessManager(RuntimeService runtimeService,
                               TaskService taskService,
                               OrderRepository orderRepository,
                               CurrentAuthentication currentAuthentication) {
        this.runtimeService = runtimeService;
        this.taskService = taskService;
        this.orderRepository = orderRepository;
        this.currentAuthentication = currentAuthentication;
    }


    public ProcessInstance startOrderProcess(String orderId) {
        AppUser appUser = (AppUser) currentAuthentication.getUser();

        Map<String, Object> processVariables = Map.of(
                PROCESS_USER_KEY, appUser.getUsername(),
                SUBJECT_TOKEN, appUser.getUserToken()
        );
        return runtimeService.startProcessInstanceByKey(ORDER_PROCESS_SCHEMA_ID, orderId, processVariables);
    }

    public void continueProcessByRestaurantStep(String orderId, String restaurantId) {
        OrderEntity order = orderRepository.getById(Long.parseLong(orderId));
        if (!order.getRestaurantId().toString().equals(restaurantId)) {
            throw new RuntimeException(MessageFormat.format("Illegal access for task(Order No.){0} exception, restaurantId {1}", orderId, restaurantId));
        }
        continueUserTaskInProcess(orderId, "WAIT_RESTAURANT_CALLBACK_TASK");
    }

    public void continueProcessByCourierFoundStep(String orderId) {
        continueUserTaskInProcess(orderId, "COURIER_WAIT_FOUND_TASK");
    }

    public void continueProcessByDeliveredStep(String orderId) {
        continueUserTaskInProcess(orderId, "COURIER_DELIVERED_TASK");
    }

    private void continueUserTaskInProcess(String orderId, String taskDefinitionId) {
        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery()
                .processInstanceBusinessKey(orderId)
                .singleResult();
        Task restaurantTask = taskService.createTaskQuery()
                .processInstanceId(processInstance.getId())
                .active()
                .taskDefinitionKey(taskDefinitionId)
                .singleResult();
        taskService.complete(restaurantTask.getId());
    }
}
