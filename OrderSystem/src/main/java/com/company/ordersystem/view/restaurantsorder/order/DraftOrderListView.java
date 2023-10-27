package com.company.ordersystem.view.restaurantsorder.order;

import com.company.ordersystem.entity.Order;

import com.company.ordersystem.view.main.MainView;

import com.company.useroidcplagin.impl.UserProvider;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteParameters;
import io.jmix.flowui.component.grid.DataGrid;
import io.jmix.flowui.kit.action.ActionPerformedEvent;
import io.jmix.flowui.kit.action.BaseAction;
import io.jmix.flowui.model.CollectionLoader;
import io.jmix.flowui.view.*;
import io.jmix.flowui.view.navigation.ViewNavigationSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import static com.company.ordersystem.view.OrderSystemPathConstants.*;

@Route(value = "draftOrders", layout = MainView.class)
@ViewController("DraftOrder.list")
@ViewDescriptor("draft-order-list-view.xml")
@LookupComponent("draftOrdersDataGrid")
@DialogMode(width = "64em")
public class DraftOrderListView extends StandardListView<Order> {
    @Autowired
    private ViewNavigationSupport viewNavigationSupport;
    @ViewComponent
    private DataGrid<Order> draftOrdersDataGrid;
    @ViewComponent
    private BaseAction viewHistory;
    @ViewComponent
    private CollectionLoader<Order> draftOrdersDl;
    @Autowired
    private UserProvider userProvider;

    @Subscribe("draftOrdersDataGrid.viewHistory")
    public void onDraftOrdersDataGridViewHistory(final ActionPerformedEvent event) {
        var selectedOrder = draftOrdersDataGrid.getSingleSelectedItem();
        Assert.notNull(selectedOrder, "Selected item is null");
        viewNavigationSupport.navigate(OrderView.class,
                new RouteParameters(ORDER_ID_PATH_PARAM, String.valueOf(selectedOrder.getId())));
    }

    @Subscribe
    public void onBeforeShow(final BeforeShowEvent event) {
        draftOrdersDl.setParameter("currentUser", userProvider.getCurrentUser());
        draftOrdersDl.load();
        draftOrdersDataGrid.addSelectionListener(e -> viewHistory.setEnabled(!e.getAllSelectedItems().isEmpty()));
    }

}