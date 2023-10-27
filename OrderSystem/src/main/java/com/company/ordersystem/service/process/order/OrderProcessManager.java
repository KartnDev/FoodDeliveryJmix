package com.company.ordersystem.service.process.order;

import com.company.ordersystem.entity.Order;
import com.company.ordersystem.repository.OrderRepository;
import lombok.AllArgsConstructor;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.api.Task;
import org.flowable.task.api.TaskQuery;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;

@Service
@AllArgsConstructor
public class OrderProcessManager {
    private static final String ORDER_PROCESS_SCHEMA_ID = "ORDER_PROCESS";

    private final RuntimeService runtimeService;
    private final TaskService taskService;
    private final OrderRepository orderRepository;

    public ProcessInstance startOrderProcess(String orderId) {
        return runtimeService.startProcessInstanceById(ORDER_PROCESS_SCHEMA_ID, orderId);
    }

    public void continueProcessByRestaurantStep(String orderId, String restaurantId) {
        Order order = orderRepository.getById( Long.parseLong(orderId));
        if(!order.getRestaurantId().toString().equals(restaurantId)) {
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
                .processDefinitionKey(orderId)
                .singleResult();
        Task restaurantTask = taskService.createTaskQuery()
                .processInstanceId(processInstance.getId())
                .active()
                .taskDefinitionId(taskDefinitionId)
                .singleResult();
        taskService.complete(restaurantTask.getId());
    }
}
