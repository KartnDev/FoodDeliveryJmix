package com.company.restaurantsystem.view.myrestaurants;

import com.company.restaurantsystem.entity.Restaurant;
import com.company.restaurantsystem.entity.RestaurantFoodItem;
import com.company.restaurantsystem.entity.RestaurantMenu;
import com.company.restaurantsystem.service.AttachmentService;
import com.company.restaurantsystem.uicomponents.ListComponents;
import com.company.restaurantsystem.view.main.MainView;
import com.company.restaurantsystem.view.restaurantfooditem.RestaurantFoodItemDetailView;
import com.company.restaurantsystem.view.restaurantmenu.RestaurantMenuDetailView;
import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.virtuallist.VirtualList;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamResource;
import io.jmix.core.DataManager;
import io.jmix.core.Messages;
import io.jmix.flowui.DialogWindows;
import io.jmix.flowui.component.SupportsTypedValue;
import io.jmix.flowui.component.grid.DataGrid;
import io.jmix.flowui.component.tabsheet.JmixTabSheet;
import io.jmix.flowui.component.textfield.TypedTextField;
import io.jmix.flowui.component.upload.FileUploadField;
import io.jmix.flowui.kit.action.ActionPerformedEvent;
import io.jmix.flowui.kit.action.BaseAction;
import io.jmix.flowui.kit.component.upload.event.FileUploadFinishedEvent;
import io.jmix.flowui.model.CollectionPropertyContainer;
import io.jmix.flowui.model.DataContext;
import io.jmix.flowui.view.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Route(value = "myrestaurants/:id", layout = MainView.class)
@ViewController("MyRestaurant.detail")
@ViewDescriptor("my-restaurant-detail-view.xml")
@EditedEntityContainer("restaurantDc")
public class MyRestaurantDetailView extends StandardDetailView<Restaurant> {
    @Autowired
    private AttachmentService attachmentService;
    @Autowired
    private DialogWindows dialogWindows;
    @Autowired
    private ListComponents listComponents;
    @Autowired
    private Messages messages;

    @ViewComponent
    private CollectionPropertyContainer<RestaurantFoodItem> menusFoodItemsDc;
    @ViewComponent
    private CollectionPropertyContainer<RestaurantMenu> menusDc;
    @ViewComponent
    private Div menusItemsListContainer;
    @ViewComponent
    private Avatar restaurantAvatarIcon;
    @ViewComponent
    private FileUploadField restaurantAvatarIconUpload;
    @ViewComponent
    private DataGrid<RestaurantMenu> menusDataGrid;
    @ViewComponent
    private H2 restaurantName;
    @ViewComponent
    private Div foodDescription;
    @ViewComponent
    private Div menusDescription;
    @ViewComponent("menusDataGrid.editMenuAction")
    private BaseAction menusDataGridEditMenuAction;
    @ViewComponent("menusDataGrid.removeMenuAction")
    private BaseAction menusDataGridRemoveMenuAction;
    @ViewComponent
    private JmixTabSheet tabSheet;
    @ViewComponent
    private DataContext dataContext;

    private VirtualList<RestaurantFoodItem> restaurantFoodItemList;

    @Subscribe
    public void onBeforeShow(final BeforeShowEvent event) {
        loadTabSheetStyle();
        initRestaurantText();
        initDescriptions();
        initVisibility();
        initIconFields();
        initMenuItemsList();
        initMenusGrid();
        changeMenusActionVisibility();
    }

    private void loadTabSheetStyle(){
        tabSheet.getTabAt(2).getStyle().set("overflow", "initial");
    }

    private void initRestaurantText() {
        restaurantName.setText(getEditedEntity().getName());
    }

    private void initMenusGrid() {
        menusDataGrid.addColumn(new ComponentRenderer<>(e -> new Text(String.join(", ",
                        e.getItems()
                                .stream()
                                .map(RestaurantFoodItem::getName)
                                .toList()
                ))))
                .setHeader(messages.getMessage("menusDataGrid.Items"));
    }


    @Subscribe
    public void onReady(final ReadyEvent event) {
        String listId = "restaurantFoodItemList";
        restaurantFoodItemList.setId(listId);
    }

    private void initDescriptions() {
        foodDescription.add(new Html(messages.getMessage(getClass(), "menusDescription")));
        menusDescription.add(new Html(messages.getMessage(getClass(), "foodDescription")));
    }

    private void initVisibility() {
        changeMenusActionVisibility();
        menusDataGrid.addSelectionListener(e -> changeMenusActionVisibility());
    }

    private void changeMenusActionVisibility() {
        List.of(menusDataGridEditMenuAction, menusDataGridRemoveMenuAction)
                .forEach(e -> e.setEnabled(!menusDataGrid.getSelectedItems().isEmpty()));
    }

    private void initMenuItemsList() {
        restaurantFoodItemList = listComponents.attachListRenderer(menusItemsListContainer, menusFoodItemsDc,
                (item, infoLayout) -> this.foodItemsUpdater((RestaurantFoodItem) item, infoLayout));
    }

    private void foodItemsUpdater(RestaurantFoodItem item, ListComponents.ListComponentContext componentContext) {
        componentContext.infoLayout().add(new Html(messages.formatMessage(getClass(), "foodItemsHeader", item.getName())));

        var horizontalLayout = new HorizontalLayout();
        horizontalLayout.add(new Text(item.getDescription()));
        horizontalLayout.add(new Html(messages.formatMessage(getClass(), "foodItemsDescription", item.getPrice())));
        horizontalLayout.setPadding(false);
        horizontalLayout.setMargin(false);
        horizontalLayout.setAlignItems(FlexComponent.Alignment.CENTER);
        componentContext.infoLayout().add(horizontalLayout);

        var buttonsPanel = new VerticalLayout();
        buttonsPanel.setWidth("AUTO");
        buttonsPanel.setPadding(false);
        buttonsPanel.setMargin(false);
        buttonsPanel.setSpacing(false);

        var detailButton = new Button(new Icon(VaadinIcon.PENCIL));
        detailButton.setText(messages.getMessage("actions.Edit"));
        detailButton.addClickListener(e -> dialogWindows.detail(this, RestaurantFoodItem.class)
                .withViewClass(RestaurantFoodItemDetailView.class)
                .editEntity(item)
                .withParentDataContext(getViewData().getDataContext())
                .withAfterCloseListener(closeEvent -> {
                    if (closeEvent.closedWith(StandardOutcome.SAVE)) {
                        menusFoodItemsDc.replaceItem(closeEvent.getSource().getView().getEditedEntity());
                    }
                })
                .build()
                .open());
        detailButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY_INLINE);

        var removeButton = new Button(new Icon(VaadinIcon.TRASH));
        removeButton.setText(messages.getMessage("actions.Remove"));
        removeButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY_INLINE, ButtonVariant.LUMO_ERROR);
        removeButton.addClickListener(e -> {
            menusFoodItemsDc.getMutableItems().remove(item);
            dataContext.remove(dataContext.merge(item));
        });

        buttonsPanel.add(detailButton, removeButton);
        componentContext.rootLayout().add(buttonsPanel);
    }

    private void initIconFields() {
        //noinspection DuplicatedCode
        if (getEditedEntity().getIcon() != null) {
            try {
                restaurantAvatarIcon.setImageResource(new StreamResource(getEditedEntity().getAttachmentName(),
                        () -> attachmentService.getStreamFromEntityAttachment(getEditedEntity())));
                restaurantAvatarIconUpload.setValue(
                        attachmentService.getStreamFromEntityAttachment(getEditedEntity()).readAllBytes());
            } catch (RuntimeException | IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Subscribe("nameField")
    public void onNameFieldTypedValueChange(final SupportsTypedValue.TypedValueChangeEvent<TypedTextField<String>, String> event) {
        restaurantName.setText(getEditedEntity().getName());
    }


    @Subscribe("restaurantAvatarIconUpload")
    public void onRestaurantAvatarIconUploadFileUploadFinished(final FileUploadFinishedEvent<FileUploadField> event) {
        attachmentService.replaceEntityAttachment(getEditedEntity(), restaurantAvatarIconUpload.getValue());
        getViewData().getDataContext().setModified(getEditedEntity(), true);
        initIconFields();
    }

    @Subscribe("addMenuItemAction")
    public void onAddMenuItemAction(final ActionPerformedEvent event) {
        dialogWindows.detail(this, RestaurantFoodItem.class)
                .withViewClass(RestaurantFoodItemDetailView.class)
                .newEntity()
                .withInitializer(e -> e.setRestaurant(getEditedEntity()))
                .withParentDataContext(getViewData().getDataContext())
                .withAfterCloseListener(e -> {
                    if (e.closedWith(StandardOutcome.SAVE)) {
                        menusFoodItemsDc.replaceItem(e.getSource().getView().getEditedEntity());
                    }
                })
                .build()
                .open();
    }

    @Subscribe("menusDataGrid.addMenuAction")
    private void onMenusDataGridAddMenuAction(final ActionPerformedEvent event) {
        DialogWindow<RestaurantMenuDetailView> dialogWindow = dialogWindows.detail(this, RestaurantMenu.class)
                .withViewClass(RestaurantMenuDetailView.class)
                .newEntity()
                .withInitializer(e -> e.setRestaurant(getEditedEntity()))
                .withParentDataContext(getViewData().getDataContext())
                .withAfterCloseListener(e -> {
                    if (e.closedWith(StandardOutcome.SAVE)) {
                        menusDc.replaceItem(e.getSource().getView().getEditedEntity());
                    }
                })
                .build();
        dialogWindow.getView().setItems(menusFoodItemsDc);
        dialogWindow.open();
    }

    @Subscribe("menusDataGrid.editMenuAction")
    private void onMenusDataGridEditMenuAction(final ActionPerformedEvent event) {
        DialogWindow<RestaurantMenuDetailView> dialogWindow = dialogWindows.detail(this, RestaurantMenu.class)
                .withViewClass(RestaurantMenuDetailView.class)
                .editEntity(Objects.requireNonNull(menusDataGrid.getSingleSelectedItem()))
                .withInitializer(e -> e.setRestaurant(getEditedEntity()))
                .withParentDataContext(getViewData().getDataContext())
                .withAfterCloseListener(e -> {
                    if (e.closedWith(StandardOutcome.SAVE)) {
                        menusDc.replaceItem(e.getSource().getView().getEditedEntity());
                    }
                })
                .build();
        dialogWindow.getView().setItems(menusFoodItemsDc);
        dialogWindow.open();
    }

    @Subscribe("menusDataGrid.removeMenuAction")
    private void onMenusDataGridRemoveMenuAction(final ActionPerformedEvent event) {
        Optional.ofNullable(menusDataGrid.getSingleSelectedItem())
                .ifPresent(e -> {
                    menusDc.getMutableItems().remove(e);
                    getViewData().getDataContext().remove(e);
                });
    }
}