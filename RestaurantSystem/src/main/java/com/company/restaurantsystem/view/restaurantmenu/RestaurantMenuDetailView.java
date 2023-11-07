package com.company.restaurantsystem.view.restaurantmenu;

import com.company.restaurantsystem.entity.RestaurantFoodItem;
import com.company.restaurantsystem.entity.RestaurantMenu;

import com.company.restaurantsystem.service.DataProviderHelper;
import com.company.restaurantsystem.view.main.MainView;

import com.vaadin.flow.data.provider.CallbackDataProvider;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.router.Route;
import io.jmix.flowui.Actions;
import io.jmix.flowui.action.entitypicker.EntityLookupAction;
import io.jmix.flowui.action.multivaluepicker.MultiValueSelectAction;
import io.jmix.flowui.component.multiselectcomboboxpicker.JmixMultiSelectComboBoxPicker;
import io.jmix.flowui.data.items.ContainerDataProvider;
import io.jmix.flowui.kit.action.Action;
import io.jmix.flowui.model.CollectionContainer;
import io.jmix.flowui.model.CollectionPropertyContainer;
import io.jmix.flowui.model.impl.CollectionContainerImpl;
import io.jmix.flowui.model.impl.CollectionPropertyContainerImpl;
import io.jmix.flowui.view.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;

@Route(value = "restaurantMenus/:id", layout = MainView.class)
@ViewController("RestaurantMenu.detail")
@ViewDescriptor("restaurant-menu-detail-view.xml")
@EditedEntityContainer("restaurantMenuDc")
@DialogMode(resizable = true, width = "50em", height = "40em")
public class RestaurantMenuDetailView extends StandardDetailView<RestaurantMenu> {
    @Autowired
    private Actions actions;
    @Autowired
    private DataProviderHelper dataProviderHelper;
    @ViewComponent
    private JmixMultiSelectComboBoxPicker<RestaurantFoodItem> itemsMultiSelectComboBoxPicker;
    @ViewComponent
    private CollectionPropertyContainer<RestaurantFoodItem> itemsDc;
    private CollectionContainer<RestaurantFoodItem> variantItems;

    @Subscribe
    public void onBeforeShow(final BeforeShowEvent event) {

        MultiValueSelectAction<RestaurantFoodItem> multiValueSelectAction = actions.create(MultiValueSelectAction.ID);
        multiValueSelectAction.setItems(dataProviderHelper.createCallbackDataProvider(variantItems.getItems()));
        multiValueSelectAction.setJavaClass(RestaurantFoodItem.class);
        multiValueSelectAction.setUseComboBox(true);
        multiValueSelectAction.setTarget(itemsMultiSelectComboBoxPicker);
        itemsMultiSelectComboBoxPicker.addAction(multiValueSelectAction, 0);

        //noinspection unchecked
        itemsMultiSelectComboBoxPicker.setItems(dataProviderHelper.createCallbackDataProvider(variantItems.getItems()));
        itemsMultiSelectComboBoxPicker.addValueChangeListener(e -> itemsDc.setItems(new ArrayList<>(e.getValue())));
        if(!itemsDc.getMutableItems().isEmpty()) {
            itemsMultiSelectComboBoxPicker.setValue(itemsDc.getMutableItems());
        }
    }

    public void setItems(CollectionContainer<RestaurantFoodItem> items) {
        this.variantItems = items;
    }
}