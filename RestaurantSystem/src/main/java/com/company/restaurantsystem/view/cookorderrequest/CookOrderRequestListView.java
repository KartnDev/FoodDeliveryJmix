package com.company.restaurantsystem.view.cookorderrequest;

import com.company.restaurantsystem.entity.CookOrderRequest;

import com.company.restaurantsystem.view.main.MainView;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.Route;
import io.jmix.core.Messages;
import io.jmix.flowui.component.grid.DataGrid;
import io.jmix.flowui.view.*;
import org.springframework.beans.factory.annotation.Autowired;

@Route(value = "cookOrderRequests", layout = MainView.class)
@ViewController("CookOrderRequest.list")
@ViewDescriptor("cook-order-request-list-view.xml")
@LookupComponent("cookOrderRequestsDataGrid")
@DialogMode(width = "64em")
public class CookOrderRequestListView extends StandardListView<CookOrderRequest> {
    private static final String IS_DONE_COLUMN = "isDone";

    @Autowired
    private Messages messages;
    @ViewComponent
    private DataGrid<CookOrderRequest> cookOrderRequestsDataGrid;

    @Subscribe
    public void onBeforeShow(final BeforeShowEvent event) {
        if(cookOrderRequestsDataGrid.getColumnByKey(IS_DONE_COLUMN) != null) {
            return;
        }
        Grid.Column<CookOrderRequest> isDone = cookOrderRequestsDataGrid.addColumn(new ComponentRenderer<>(e -> {
            var badge = new Span();
            badge.getElement().getThemeList().add("badge");
            if (e.getIsDone()) {
                badge.getElement().getThemeList().add("success");
                badge.setText(messages.getMessage("cookOrderRequestsDataGrid.Done"));
            } else {
                badge.getElement().getThemeList().add("error");
                badge.setText(messages.getMessage("cookOrderRequestsDataGrid.waitForCook"));
            }

            return badge;
        }));
        isDone.setKey(IS_DONE_COLUMN);
        isDone.setHeader(messages.getMessage(getClass(), IS_DONE_COLUMN));
    }

}