package com.company.restaurantsystem.view.restaurant;

import com.company.restaurantsystem.entity.Restaurant;

import com.company.restaurantsystem.service.AttachmentService;
import com.company.restaurantsystem.view.main.MainView;

import com.vaadin.flow.router.Route;
import io.jmix.flowui.component.upload.FileUploadField;
import io.jmix.flowui.kit.component.upload.event.FileUploadSucceededEvent;
import io.jmix.flowui.view.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;

@Route(value = "restaurants/:id", layout = MainView.class)
@ViewController("Restaurant.detail")
@ViewDescriptor("restaurant-detail-view.xml")
@EditedEntityContainer("restaurantDc")
public class RestaurantDetailView extends StandardDetailView<Restaurant> {
    @Autowired
    private AttachmentService attachmentService;
    @ViewComponent
    private FileUploadField iconField;

    @Subscribe
    private void onBeforeShow(final BeforeShowEvent event) {
        if(getEditedEntity().getIcon() != null) {
            try (var stream = attachmentService.getStreamFromEntityAttachment(getEditedEntity())){
                iconField.setValue(stream.readAllBytes());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Subscribe("iconField")
    public void onIconFieldFileUploadSucceeded(final FileUploadSucceededEvent<FileUploadField> event) {
        attachmentService.replaceEntityAttachment(getEditedEntity(), iconField.getValue());
        getViewData().getDataContext().setModified(getEditedEntity(), true);
    }
}