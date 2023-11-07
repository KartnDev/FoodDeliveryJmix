package com.company.restaurantsystem.view.cookorderrequest;

import com.company.restaurantsystem.client.OrderClient;
import com.company.restaurantsystem.entity.CookFoodItemEntity;
import com.company.restaurantsystem.entity.CookOrderRequest;

import com.company.restaurantsystem.view.main.MainView;

import com.vaadin.flow.router.Route;
import io.jmix.core.DataManager;
import io.jmix.core.LoadContext;
import io.jmix.flowui.model.CollectionLoader;
import io.jmix.flowui.model.InstanceContainer;
import io.jmix.flowui.view.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Route(value = "cookOrderRequests/:id/:id", layout = MainView.class)
@ViewController("CookOrderRequest.detail")
@ViewDescriptor("cook-order-request-detail-view.xml")
@EditedEntityContainer("cookOrderRequestDc")
public class CookOrderRequestDetailView extends StandardDetailView<CookOrderRequest> {
    @Autowired
    private OrderClient orderClient;
    @Autowired
    private DataManager dataManager;
    @ViewComponent
    private InstanceContainer<CookOrderRequest> cookOrderRequestDc;
    @Install(to = "itemsDl", target = Target.DATA_LOADER)
    private List<CookFoodItemEntity> itemsDlLoadDelegate(final LoadContext<CookFoodItemEntity> loadContext) {
        //noinspection DataFlowIssue
        loadContext.getQuery().setParameter("cookRequest", getEditedEntity());
        return dataManager.loadList(loadContext);
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