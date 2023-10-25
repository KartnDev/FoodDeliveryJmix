package com.company.ordersystem.view.restaurantsorder.order;


import com.company.ordersystem.client.RestaurantClient;
import com.company.ordersystem.uicomponents.ListComponents;
import com.company.ordersystem.view.main.MainView;

import com.company.restaurantapi.model.RestaurantFoodItemDTO;
import com.company.restaurantapi.model.RestaurantMenuDTO;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.QueryParameters;
import com.vaadin.flow.router.Route;
import io.jmix.core.Metadata;
import io.jmix.core.security.AccessDeniedException;
import io.jmix.flowui.component.tabsheet.JmixTabSheet;
import io.jmix.flowui.model.CollectionContainer;
import io.jmix.flowui.model.CollectionLoader;
import io.jmix.flowui.model.DataContext;
import io.jmix.flowui.model.InstanceContainer;
import io.jmix.flowui.model.impl.CollectionContainerImpl;
import io.jmix.flowui.view.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.MessageFormat;

import static com.company.ordersystem.view.OrderSystemPathConstants.*;

@Route(value = "order/:id", layout = MainView.class)
@ViewController("OrderView")
@ViewDescriptor("order-view.xml")
public class OrderView extends StandardView {
    @ViewComponent
    private InstanceContainer draftOrderDc;
    @ViewComponent
    private CollectionContainer<RestaurantMenuDTO> menusDc;
    @ViewComponent
    private CollectionLoader<RestaurantMenuDTO> menusDl;
    @ViewComponent
    private DataContext dataContext;
    @Autowired
    private RestaurantClient restaurantClient;
    @ViewComponent
    private JmixTabSheet menuTabSheetContainer;
    @Autowired
    private ListComponents listComponents;
    @Autowired
    private Metadata metadata;

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        // happens before BeforeShowEvent
        String orderId = event.getRouteParameters().get(ORDER_ID_PATH_PARAM).orElse(NEW_ORDER_ID);
        if(NEW_ORDER_ID.equals(orderId)) {
            initNewOrderView(event);
        } else {
            initHistoryOrderView(event);
        }

        super.beforeEnter(event);
    }

    private void initHistoryOrderView(BeforeEnterEvent event) {

    }

    private void initNewOrderView(BeforeEnterEvent event) {
        QueryParameters queryParameters = event.getLocation().getQueryParameters();
        if(!queryParameters.getParameters().containsKey(RESTAURANT_ID_PATH_PARAM) &&
                queryParameters.getParameters().get(RESTAURANT_ID_PATH_PARAM).size() != 1) {
            throw new AccessDeniedException("view", "OrderView");
        }
        String restaurantId = queryParameters.getParameters()
                .get(RESTAURANT_ID_PATH_PARAM)
                .iterator()
                .next();

        initMenuTabsForRestaurant(restaurantId);
    }

    private void initMenuTabsForRestaurant(String restaurantId) {
        menusDl.setLoadDelegate(e -> restaurantClient.listRestaurantMenus(Long.valueOf(restaurantId)));
        menusDl.load();
        menusDc.getMutableItems().forEach(menu -> menuTabSheetContainer.add(menu.getName(), generateListItemsForMenu(menu)));
    }

    private Component generateListItemsForMenu(RestaurantMenuDTO menu) {
        var verticalLayout = new VerticalLayout();
        verticalLayout.setPadding(false);
        verticalLayout.setMargin(false);

        var itemsContainer = new CollectionContainerImpl<RestaurantFoodItemDTO>(metadata.getClass(RestaurantFoodItemDTO.class));
        itemsContainer.setItems(menu.getItems());

        listComponents.attachListRenderer(verticalLayout, itemsContainer,
                (item, infoLayout) -> menuItemsUpdater(infoLayout, (RestaurantFoodItemDTO) item, menu));

        return verticalLayout;
    }

    private void menuItemsUpdater(ListComponents.ListComponentContext componentContext,
                                  RestaurantFoodItemDTO item,
                                  RestaurantMenuDTO menu) {
        componentContext.infoLayout().add(new Html(MessageFormat.format("<strong>{0}</strong>", item.getName())));

        var horizontalLayout = new HorizontalLayout();
        horizontalLayout.add(new Text(item.getDescription()));
        horizontalLayout.add(new Html(MessageFormat.format("<div><strong>Price: </strong>{0}$</div>", item.getPrice())));
        horizontalLayout.setPadding(false);
        horizontalLayout.setMargin(false);
        horizontalLayout.setAlignItems(FlexComponent.Alignment.CENTER);
        componentContext.infoLayout().add(horizontalLayout);

        var buttonsPanel = new VerticalLayout();
        buttonsPanel.setWidth("AUTO");
        buttonsPanel.setPadding(false);
        buttonsPanel.setMargin(false);
        buttonsPanel.setSpacing(false);

        var detailButton = new Button(new Icon(VaadinIcon.PLUS));
        detailButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY_INLINE);

        var removeButton = new Button(new Icon(VaadinIcon.MINUS));
        removeButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY_INLINE, ButtonVariant.LUMO_ERROR);

        buttonsPanel.add(detailButton, removeButton);
        componentContext.rootLayout().add(buttonsPanel);
    }

}