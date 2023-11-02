package com.company.restaurantsystem.view.cookorderrequest;

import com.company.restaurantsystem.client.OrderClient;
import com.company.restaurantsystem.entity.CookFoodItemEntity;
import com.company.restaurantsystem.entity.CookOrderRequest;

import com.company.restaurantsystem.view.main.MainView;

import com.vaadin.flow.router.Route;
import io.jmix.flowui.model.CollectionLoader;
import io.jmix.flowui.model.InstanceContainer;
import io.jmix.flowui.view.*;
import org.springframework.beans.factory.annotation.Autowired;

@Route(value = "cookOrderRequests/:id/:id", layout = MainView.class)
@ViewController("CookOrderRequest.detail")
@ViewDescriptor("cook-order-request-detail-view.xml")
@EditedEntityContainer("cookOrderRequestDc")
public class CookOrderRequestDetailView extends StandardDetailView<CookOrderRequest> {
    @Autowired
    private OrderClient orderClient;
    @ViewComponent
    private InstanceContainer<CookOrderRequest> cookOrderRequestDc;
    @ViewComponent
    private CollectionLoader<CookFoodItemEntity> itemsDl;

    @Subscribe
    public void onBeforeShow(final BeforeShowEvent event) {
        itemsDl.setParameter("cookRequest", getEditedEntity());
        itemsDl.load();
    }

    @Subscribe
    public void onBeforeSave(final BeforeSaveEvent event) {
        getEditedEntity().setIsDone(true);
    }

    @Install(to = "approveAction", subject = "successHandler")
    private void approveActionSuccessHandler() {
        var order = cookOrderRequestDc.getItem();
        orderClient.publishRestaurantCookDone(order.getRestaurant().getId(), order.getOrderId());
    }

}