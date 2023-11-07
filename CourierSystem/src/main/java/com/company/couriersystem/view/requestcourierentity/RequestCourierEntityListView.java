package com.company.couriersystem.view.requestcourierentity;

import com.company.couriersystem.entity.RequestCourierEntity;

import com.company.couriersystem.service.CourierService;
import com.company.couriersystem.view.main.MainView;

import com.company.useroidcplagin.entity.AppUser;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.Route;
import io.jmix.core.DataManager;
import io.jmix.core.Messages;
import io.jmix.core.security.CurrentAuthentication;
import io.jmix.flowui.component.grid.DataGrid;
import io.jmix.flowui.kit.component.ComponentUtils;
import io.jmix.flowui.model.CollectionLoader;
import io.jmix.flowui.view.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

@Route(value = "requestCourierEntities", layout = MainView.class)
@ViewController("RequestCourierEntity.list")
@ViewDescriptor("request-courier-entity-list-view.xml")
@LookupComponent("requestCourierEntitiesDataGrid")
@DialogMode(width = "64em")
public class RequestCourierEntityListView extends StandardListView<RequestCourierEntity> {
    @Autowired
    private CourierService courierService;
    @Autowired
    private CurrentAuthentication currentAuthentication;
    @Autowired
    private DataManager dataManager;
    @ViewComponent
    private DataGrid<RequestCourierEntity> requestCourierEntitiesDataGrid;
    @ViewComponent
    private CollectionLoader<RequestCourierEntity> requestCourierEntitiesDl;
    @Autowired
    private Messages messages;

    @Subscribe
    public void onBeforeShow(final BeforeShowEvent event) {
        Optional<RequestCourierEntity> deliveryForCurrentUser = courierService.findDeliveryForCurrentUser();
        if(deliveryForCurrentUser.isEmpty()) {
            Grid.Column<RequestCourierEntity> requestCourierEntityColumn = requestCourierEntitiesDataGrid.addColumn(new ComponentRenderer<>(e -> {
                var button = new Button(messages.getMessage(getClass(), "assignMyself"));
                button.setIcon(ComponentUtils.convertToIcon(VaadinIcon.EXIT_O));
                button.addClickListener(clickEvent -> {
                    e.setAssingeeCourier((AppUser) currentAuthentication.getUser());
                    dataManager.save(e);
                    courierService.publishAssignRequestForCurrentUser(e);
                    requestCourierEntitiesDl.load();
                });
                return button;
            }));
            requestCourierEntityColumn.setHeader(messages.getMessage("Actions"));
        }

    }
}