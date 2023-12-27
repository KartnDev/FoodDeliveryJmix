package com.company.ordersystem.view.restaurantsorder.order;

import com.company.ordersystem.entity.OrderEntity;

import com.company.ordersystem.view.main.MainView;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteParameters;
import io.jmix.core.DataManager;
import io.jmix.core.LoadContext;
import io.jmix.core.Messages;
import io.jmix.core.security.CurrentAuthentication;
import io.jmix.flowui.component.genericfilter.GenericFilter;
import io.jmix.flowui.component.grid.DataGrid;
import io.jmix.flowui.kit.action.ActionPerformedEvent;
import io.jmix.flowui.kit.action.BaseAction;
import io.jmix.flowui.model.CollectionLoader;
import io.jmix.flowui.view.*;
import io.jmix.flowui.view.navigation.ViewNavigationSupport;
import jakarta.annotation.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import java.util.List;
import java.util.function.Function;

import static com.company.ordersystem.view.OrderSystemPathConstants.*;

@Route(value = "orders", layout = MainView.class)
@ViewController("Order.list")
@ViewDescriptor("order-list-view.xml")
@LookupComponent("ordersDataGrid")
@DialogMode(width = "64em")
public class OrderListView extends StandardListView<OrderEntity> {
    @Autowired
    private ViewNavigationSupport viewNavigationSupport;
    @Autowired
    private Messages messages;
    @ViewComponent
    private DataGrid<OrderEntity> ordersDataGrid;
    @ViewComponent("ordersDataGrid.viewHistory")
    private BaseAction viewHistory;

    @Subscribe("ordersDataGrid.viewHistory")
    public void onDraftOrdersDataGridViewHistory(final ActionPerformedEvent event) {
        var selectedOrder = ordersDataGrid.getSingleSelectedItem();
        Assert.notNull(selectedOrder, "Selected item is null");
        viewNavigationSupport.navigate(OrderView.class,
                new RouteParameters(ORDER_ID_PATH_PARAM, String.valueOf(selectedOrder.getId())));
    }

    @Subscribe
    public void onBeforeShow(final BeforeShowEvent event) {
        ordersDataGrid.addSelectionListener(e -> viewHistory.setEnabled(!e.getAllSelectedItems().isEmpty()));
        Grid.Column<OrderEntity> orderEntityColumn = ordersDataGrid.addColumn(new ComponentRenderer<>(e -> switch (e.getStatus()) {
            case NEW_ORDER -> createBarge(VaadinIcon.EXIT_O, messages.getMessage("newOrder"), "primary");
            case PAID -> createBarge(VaadinIcon.CART, messages.getMessage("Paid"), "primary");
            case WAIT_FOR_RESTAURANT -> createBarge(VaadinIcon.CUTLERY, messages.getMessage("WaitForRestaurant"), null);
            case RESTAURANT_COOKING -> createBarge(VaadinIcon.CROSS_CUTLERY, messages.getMessage("WaitForCooking"), "contrast");
            case COURIER_FINDING -> createBarge(VaadinIcon.EXIT_O, messages.getMessage("FindingCourier"), "contrast");
            case DELIVERING -> createBarge(VaadinIcon.CART, messages.getMessage("Delivering"), "primary");
            case DONE -> createBarge(VaadinIcon.CHECK, messages.getMessage("Done"), "success");
        }));
        orderEntityColumn.setHeader(messages.getMessage("status"));
    }

    private Span createBarge(VaadinIcon icon, String text, @Nullable String theme) {
        Span span = new Span(createIcon(icon), new Span(text));
        span.getElement().getThemeList().add("badge");
        if(theme != null) {
            span.getElement().getThemeList().add(theme);
        }
        return span;
    }
    private Icon createIcon(VaadinIcon vaadinIcon) {
        Icon icon = vaadinIcon.create();
        icon.getStyle().set("padding", "var(--lumo-space-xs");
        return icon;
    }
}