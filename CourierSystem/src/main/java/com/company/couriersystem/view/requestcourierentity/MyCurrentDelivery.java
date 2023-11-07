package com.company.couriersystem.view.requestcourierentity;

import com.company.couriersystem.entity.RequestCourierEntity;

import com.company.couriersystem.service.CourierService;
import com.company.couriersystem.view.main.MainView;

import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.router.Route;
import io.jmix.core.security.CurrentAuthentication;
import io.jmix.flowui.kit.action.ActionPerformedEvent;
import io.jmix.flowui.model.InstanceContainer;
import io.jmix.flowui.model.InstanceLoader;
import io.jmix.flowui.view.*;
import org.springframework.beans.factory.annotation.Autowired;

@Route(value = "myCurrentDelivery", layout = MainView.class)
@ViewController("MyCurrentDelivery.detail")
@ViewDescriptor("my-current-delivery.xml")
@EditedEntityContainer("requestCourierEntityDc")
public class MyCurrentDelivery extends StandardView {
    @Autowired
    private CurrentAuthentication currentAuthentication;
    @Autowired
    private CourierService courierService;

    @ViewComponent
    private InstanceContainer<RequestCourierEntity> requestCourierEntityDc;
    @ViewComponent
    private InstanceLoader<RequestCourierEntity> requestCourierEntityDl;
    @ViewComponent
    private Div ticketContainer;

    @Subscribe
    public void onBeforeShow(final BeforeShowEvent event) {
        requestCourierEntityDl.setParameter("currentUser", currentAuthentication.getUser());
        requestCourierEntityDl.load();
        if(ticketContainer.getChildren().findAny().isEmpty()) {
            ticketContainer.add(new Html(requestCourierEntityDc.getItem().getItemsTicketHtml()));
        }
    }

    @Subscribe("saveAction")
    public void onSaveAction(final ActionPerformedEvent event) {
        close(StandardOutcome.SAVE);
        courierService.publishRequestDeliveredForCurrentUser(requestCourierEntityDc.getItem());
    }
}