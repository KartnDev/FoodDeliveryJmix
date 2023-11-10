package com.company.ordersystem.view.restaurantsorder.order;


import com.company.ordersystem.client.RestaurantClient;
import com.company.ordersystem.entity.OrderEntity;
import com.company.ordersystem.entity.FoodItemCountedEntity;
import com.company.ordersystem.entity.RestaurantFoodItemReplica;
import com.company.ordersystem.repository.OrderRepository;
import com.company.ordersystem.service.process.order.OrderProcessManager;
import com.company.ordersystem.uicomponents.ListComponents;
import com.company.ordersystem.view.main.MainView;
import com.company.restaurantapi.model.RestaurantDTO;
import com.company.restaurantapi.model.RestaurantFoodItemDTO;
import com.company.restaurantapi.model.RestaurantMenuDTO;
import com.company.useroidcplagin.entity.AppUser;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasComponents;
import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.virtuallist.VirtualList;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.QueryParameters;
import com.vaadin.flow.router.Route;
import io.jmix.core.FetchPlan;
import io.jmix.core.FetchPlans;
import io.jmix.core.Messages;
import io.jmix.core.Metadata;
import io.jmix.core.security.AccessDeniedException;
import io.jmix.core.security.CurrentAuthentication;
import io.jmix.flowui.component.tabsheet.JmixTabSheet;
import io.jmix.flowui.kit.action.ActionPerformedEvent;
import io.jmix.flowui.model.*;
import io.jmix.flowui.model.impl.CollectionContainerImpl;
import io.jmix.flowui.view.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import java.text.MessageFormat;
import java.util.Optional;

import static com.company.ordersystem.view.OrderSystemPathConstants.*;

@Route(value = "order/:id", layout = MainView.class)
@ViewController("OrderView")
@ViewDescriptor("order-view.xml")
public class OrderView extends StandardView {

    @Autowired
    private RestaurantClient restaurantClient;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private Messages messages;
    @Autowired
    private FetchPlans fetchPlans;
    @Autowired
    private ListComponents listComponents;
    @Autowired
    private OrderProcessManager orderProcessManager;
    @Autowired
    private DataComponents dataComponents;
    @Autowired
    private CurrentAuthentication currentAuthentication;
    @ViewComponent
    private Div orderContainer;
    @ViewComponent
    private H3 orderTitle;
    @ViewComponent
    private InstanceContainer<RestaurantDTO> restaurantDc;
    @ViewComponent
    private InstanceLoader<RestaurantDTO> restaurantDl;
    @ViewComponent
    private JmixTabSheet menuTabSheetContainer;
    @ViewComponent
    private InstanceContainer<OrderEntity> draftOrderDc;
    @ViewComponent
    private CollectionContainer<RestaurantMenuDTO> menusDc;
    @ViewComponent
    private CollectionLoader<RestaurantMenuDTO> menusDl;
    @ViewComponent
    private DataContext dataContext;
    @ViewComponent
    private Div content;
    @ViewComponent
    private VerticalLayout readonlyContent;

    private VirtualList<FoodItemCountedEntity> restaurantFoodItemList;
    private CollectionContainer<FoodItemCountedEntity> draftOrderItemsDc;

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        // happens before BeforeShowEvent
        String orderId = event.getRouteParameters().get(ORDER_ID_PATH_PARAM).orElse(NEW_ORDER_ID);
        if (NEW_ORDER_ID.equals(orderId)) {
            initNewOrderView(event);
        } else {
            initHistoryOrderView(event);
        }

        super.beforeEnter(event);
    }

    private void initHistoryOrderView(BeforeEnterEvent event) {
        FetchPlan fetchPlan = fetchPlans.builder(OrderEntity.class)
                .addFetchPlan("order-fetch-plan")
                .build();
        Optional<OrderEntity> orderOptional = event.getRouteParameters().get(ORDER_ID_PATH_PARAM)
                .flatMap(orderId -> orderRepository.findById(Long.valueOf(orderId), fetchPlan));
        if (orderOptional.isPresent()) {
            var draft = orderOptional.get();
            draftOrderDc.setItem(draft);
            content.setVisible(false);
            readonlyContent.setVisible(true);
            var totalPriceVal = draft.getOrderItems().stream()
                    .mapToInt(item -> item.getCount() * item.getItem().getPrice())
                    .sum();

            readonlyContent.add(new H3(messages.formatMessage(getClass(), "restaurantHeader", draft.getRestaurantName())));
            readonlyContent.add(new H4(messages.formatMessage(getClass(), "orderPriceHeader", totalPriceVal)));

            var listContainer = new Div();
            listContainer.setWidthFull();
            initMenuItemsList(listContainer);
            readonlyContent.add(listContainer);
            restaurantFoodItemList.setWidth("auto");

        } else {
            throw new AccessDeniedException("view", "OrderView");
        }
    }

    private void initNewOrderView(BeforeEnterEvent event) {
        draftOrderDc.setItem(dataContext.create(OrderEntity.class));
        draftOrderDc.getItem().setBelongsToUser((AppUser) currentAuthentication.getUser());
        QueryParameters queryParameters = event.getLocation().getQueryParameters();
        if (!queryParameters.getParameters().containsKey(RESTAURANT_ID_PATH_PARAM) &&
                queryParameters.getParameters().get(RESTAURANT_ID_PATH_PARAM).size() != 1) {
            throw new AccessDeniedException("view", "OrderView");
        }
        String restaurantId = queryParameters.getParameters()
                .get(RESTAURANT_ID_PATH_PARAM)
                .iterator()
                .next();
        initRestaurantDc(restaurantId);

        initMenuTabsForRestaurant(restaurantId);
        initMenuItemsList(orderContainer);
    }

    private void initMenuItemsList(HasComponents container) {
        draftOrderItemsDc = dataComponents.createCollectionContainer(FoodItemCountedEntity.class);
        CollectionLoader<FoodItemCountedEntity> loader = dataComponents.createCollectionLoader();
        loader.setDataContext(dataContext);
        loader.setLoadDelegate(e -> draftOrderDc.getItem().getOrderItems());
        loader.setContainer(draftOrderItemsDc);

        loader.load();
        ((CollectionContainerImpl<FoodItemCountedEntity>) draftOrderItemsDc).setLoader(loader);

        restaurantFoodItemList = listComponents.attachListRenderer(container, draftOrderItemsDc,
                (item, infoLayout) -> this.foodItemsUpdater((FoodItemCountedEntity) item, infoLayout));
    }

    private void foodItemsUpdater(FoodItemCountedEntity item, ListComponents.ListComponentContext componentContext) {
        var title = new Html(messages.formatMessage(getClass(),
                "foodItemsHeader", item.getItem().getName(), item.getCount()));

        componentContext.infoLayout().add(title);

        var horizontalLayout = new HorizontalLayout();
        horizontalLayout.add(new Text(item.getItem().getDescription()));
        horizontalLayout.add(new Html(messages.formatMessage(getClass(), "foodItemsDescriptionPrice", item.getItem().getPrice())));
        horizontalLayout.add(new Html(messages.formatMessage(getClass(), "foodItemsDescriptionCountedPrice", item.getCount() * item.getItem().getPrice())));
        horizontalLayout.setPadding(false);
        horizontalLayout.setMargin(false);
        horizontalLayout.setAlignItems(FlexComponent.Alignment.CENTER);
        componentContext.infoLayout().add(horizontalLayout);
    }

    private void initRestaurantDc(String restaurantId) {
        AppUser appUser = (AppUser) currentAuthentication.getUser();

        restaurantDl.setLoadDelegate(e -> restaurantClient.getRestaurantById(Long.valueOf(restaurantId), appUser.getUserToken()));
        restaurantDl.load();

        draftOrderDc.getItem().setRestaurantId(restaurantDc.getItem().getId());
        draftOrderDc.getItem().setRestaurantName(restaurantDc.getItem().getName());
    }

    private void initMenuTabsForRestaurant(String restaurantId) {
        AppUser appUser = (AppUser) currentAuthentication.getUser();

        menusDl.setLoadDelegate(e -> restaurantClient.listRestaurantMenus(Long.valueOf(restaurantId), appUser.getUserToken()));
        menusDl.load();
        menusDc.getMutableItems().forEach(menu -> menuTabSheetContainer.add(menu.getName(), generateListItemsForMenu(menu)));
    }

    private Component generateListItemsForMenu(RestaurantMenuDTO menu) {
        var verticalLayout = new VerticalLayout();
        verticalLayout.setPadding(false);
        verticalLayout.setMargin(false);

        CollectionContainer<RestaurantFoodItemDTO> itemsContainer = dataComponents.createCollectionContainer(RestaurantFoodItemDTO.class);
        itemsContainer.setItems(menu.getItems());

        listComponents.attachListRenderer(verticalLayout, itemsContainer,
                (item, infoLayout) -> menuItemsUpdater(infoLayout, (RestaurantFoodItemDTO) item));

        return verticalLayout;
    }

    private void menuItemsUpdater(ListComponents.ListComponentContext componentContext,
                                  RestaurantFoodItemDTO item) {
        componentContext.infoLayout().add(new Html(messages.formatMessage(getClass(), "menusHeader", item.getName())));

        var horizontalLayout = new HorizontalLayout();
        horizontalLayout.add(new Text(item.getDescription()));
        horizontalLayout.add(new Html(messages.formatMessage(getClass(), "menusPrice", item.getPrice())));
        horizontalLayout.setPadding(false);
        horizontalLayout.setMargin(false);
        horizontalLayout.setAlignItems(FlexComponent.Alignment.CENTER);
        componentContext.infoLayout().add(horizontalLayout);

        var buttonsPanel = new VerticalLayout();
        buttonsPanel.setWidth("AUTO");
        buttonsPanel.setPadding(false);
        buttonsPanel.setMargin(false);
        buttonsPanel.setSpacing(false);

        var addButton = new Button(new Icon(VaadinIcon.PLUS));
        addButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY_INLINE);
        addButton.addClickListener(e -> {
            OrderEntity order = draftOrderDc.getItem();
            if (CollectionUtils.isEmpty(order.getOrderItems()) || orderDoesNotContainsAnyFoodItem(order, item)) {
                FoodItemCountedEntity foodItemCountedEntity = dataContext.create(FoodItemCountedEntity.class);
                foodItemCountedEntity.setItem(createReplica(item));
                foodItemCountedEntity.setCount(1);
                foodItemCountedEntity.setOrder(draftOrderDc.getItem());
                foodItemCountedEntity = dataContext.merge(foodItemCountedEntity);
                order.getOrderItems().add(foodItemCountedEntity);
                draftOrderItemsDc.getMutableItems().add(foodItemCountedEntity);
            } else {
                FoodItemCountedEntity foodItemCountedEntity = order.getOrderItems().stream()
                        .filter(foodItem -> foodItem.getItem().getSourceId().equals(item.getId()))
                        .findFirst()
                        .orElseThrow();
                foodItemCountedEntity.setCount(foodItemCountedEntity.getCount() + 1);
                dataContext.merge(foodItemCountedEntity);
                refreshTotalCount();
            }
            refreshTotalCount();
            restaurantFoodItemList.getDataProvider().refreshAll();
        });

        var removeButton = new Button(new Icon(VaadinIcon.MINUS));
        removeButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY_INLINE, ButtonVariant.LUMO_ERROR);
        removeButton.addClickListener(e -> {
            OrderEntity order = draftOrderDc.getItem();
            FoodItemCountedEntity foodItemCountedEntityOptional = order.getOrderItems().stream()
                    .filter(foodItem -> foodItem.getItem().getSourceId().equals(item.getId()))
                    .findFirst()
                    .orElse(null);

            if (foodItemCountedEntityOptional != null) {
                if (foodItemCountedEntityOptional.getCount() == 1) {
                    order.getOrderItems().remove(foodItemCountedEntityOptional);
                    dataContext.remove(foodItemCountedEntityOptional);
                    dataContext.remove(foodItemCountedEntityOptional.getItem());
                    draftOrderItemsDc.getMutableItems().remove(foodItemCountedEntityOptional);
                } else {
                    foodItemCountedEntityOptional.setCount(foodItemCountedEntityOptional.getCount() - 1);
                    dataContext.merge(foodItemCountedEntityOptional);
                }
            }
            refreshTotalCount();
            restaurantFoodItemList.getDataProvider().refreshAll();
        });
        buttonsPanel.add(addButton, removeButton);
        componentContext.rootLayout().add(buttonsPanel);
    }

    private void refreshTotalCount() {
        var totalPrice = draftOrderItemsDc.getMutableItems()
                .stream()
                .mapToInt(item -> item.getCount() * item.getItem().getPrice())
                .sum();
        orderTitle.setText(messages.formatMessage(getClass(), "OrderFormatted", totalPrice));
    }

    private boolean orderDoesNotContainsAnyFoodItem(OrderEntity order, RestaurantFoodItemDTO item) {
        return order.getOrderItems()
                .stream()
                .map(FoodItemCountedEntity::getItem)
                .noneMatch(foodItem -> foodItem.getSourceId().equals(item.getId()));
    }

    private RestaurantFoodItemReplica createReplica(RestaurantFoodItemDTO original) {
        var replica = dataContext.create(RestaurantFoodItemReplica.class);
        replica.setName(original.getName());
        replica.setIcon(original.getIcon());
        replica.setDescription(original.getDescription());
        replica.setBelongsToUser((AppUser) currentAuthentication.getUser());
        replica.setPrice(original.getPrice());
        replica.setSourceId(original.getId());
        return replica;
    }

    @Subscribe("approveOrder")
    public void onApproveOrder(final ActionPerformedEvent event) {
        dataContext.save();
        orderProcessManager.startOrderProcess(draftOrderDc.getItem().getId().toString());
        close(StandardOutcome.SAVE);
    }

    @Subscribe("cancelOrder")
    public void onCancelOrder(final ActionPerformedEvent event) {
        close(StandardOutcome.DISCARD);
    }
}