package com.company.restaurantsystem.view.restaurantfooditem;

import com.company.restaurantsystem.entity.RestaurantFoodItem;
import com.company.restaurantsystem.service.AttachmentService;
import com.company.restaurantsystem.view.main.MainView;
import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamResource;
import io.jmix.core.Metadata;
import io.jmix.flowui.component.SupportsTypedValue;
import io.jmix.flowui.component.textfield.TypedTextField;
import io.jmix.flowui.component.upload.FileUploadField;
import io.jmix.flowui.kit.action.BaseAction;
import io.jmix.flowui.kit.component.upload.event.FileUploadFinishedEvent;
import io.jmix.flowui.view.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;

@Route(value = "restaurantFoodItems/:id", layout = MainView.class)
@ViewController("RestaurantFoodItem.detail")
@ViewDescriptor("restaurant-food-item-detail-view.xml")
@EditedEntityContainer("restaurantFoodItemDc")
public class RestaurantFoodItemDetailView extends StandardDetailView<RestaurantFoodItem> {

    @Autowired
    private AttachmentService attachmentService;
    @ViewComponent
    private Avatar itemAvatarIcon;
    @ViewComponent
    private FileUploadField itemAvatarIconUpload;
    @ViewComponent
    private H2 itemName;

    @Subscribe
    public void obBeforeShow(final BeforeShowEvent event) {
        initIconFields();
    }

    @Subscribe("nameField")
    public void onNameFieldTypedValueChange(final SupportsTypedValue.TypedValueChangeEvent<TypedTextField<String>, String> event) {
        itemName.setText(getEditedEntity().getName());
    }

    @Subscribe("itemAvatarIconUpload")
    public void onRestaurantAvatarIconUploadFileUploadFinished(final FileUploadFinishedEvent<FileUploadField> event) {
        attachmentService.replaceEntityAttachment(getEditedEntity(), itemAvatarIconUpload.getValue());
        getViewData().getDataContext().setModified(getEditedEntity(), true);
        initIconFields();
    }

    @SuppressWarnings("DuplicatedCode")
    private void initIconFields() {
        if (getEditedEntity().getName() != null) {
            itemName.setText(getEditedEntity().getName());
        }
        if (getEditedEntity().getIcon() != null) {
            try {
                itemAvatarIcon.setImageResource(new StreamResource(getEditedEntity().getAttachmentName(), () -> attachmentService.getStreamFromEntityAttachment(getEditedEntity())));
                itemAvatarIconUpload.setValue(attachmentService.getStreamFromEntityAttachment(getEditedEntity()).readAllBytes());
            } catch (RuntimeException | IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}