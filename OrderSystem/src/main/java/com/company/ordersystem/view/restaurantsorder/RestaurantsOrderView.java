package com.company.ordersystem.view.restaurantsorder;


import com.company.ordersystem.client.RestaurantClient;
import com.company.ordersystem.uicomponents.ListComponents;
import com.company.ordersystem.view.main.MainView;

import com.company.ordersystem.view.restaurantsorder.order.OrderView;
import com.company.restaurantapi.model.RestaurantDTO;
import com.company.useroidcplagin.entity.AppUser;
import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.details.Details;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.QueryParameters;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteParameters;
import io.jmix.core.LoadContext;
import io.jmix.core.security.CurrentAuthentication;
import io.jmix.flowui.model.CollectionContainer;
import io.jmix.flowui.model.CollectionLoader;
import io.jmix.flowui.view.*;
import io.jmix.flowui.view.navigation.ViewNavigationSupport;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static com.company.ordersystem.view.OrderSystemPathConstants.*;

@Route(value = "RestaurantsOrderView", layout = MainView.class)
@ViewController("RestaurantsOrderView")
@ViewDescriptor("restaurants-order-view.xml")
public class RestaurantsOrderView extends StandardView {
    @Autowired
    private RestaurantClient restaurantClient;
    @Autowired
    private ListComponents listComponents;
    @Autowired
    private ViewNavigationSupport viewNavigationSupport;
    @ViewComponent
    private VerticalLayout restaurantsListContainer;
    @ViewComponent
    private CollectionContainer<RestaurantDTO> restaurantsDc;
    @ViewComponent
    private CollectionLoader<RestaurantDTO> restaurantsDl;
    @Autowired
    private CurrentAuthentication currentAuthentication;

    @Install(to = "restaurantsDl", target = Target.DATA_LOADER)
    private List<RestaurantDTO> restaurantsLoaderLoadDelegate(final LoadContext<RestaurantDTO> loadContext) {
        AppUser appUser = (AppUser) currentAuthentication.getUser();
        return restaurantClient.listRestaurants(appUser.getUserToken());
    }

    @Subscribe
    public void onBeforeShow(final BeforeShowEvent event) {
        restaurantsDl.load();
        listComponents.attachListRenderer(restaurantsListContainer, restaurantsDc,
                (rest, infoLayout) -> restaurantsUpdater((RestaurantDTO) rest, infoLayout));
    }

    private void restaurantsUpdater(RestaurantDTO restaurant, ListComponents.ListComponentContext componentContext) {
        componentContext.infoLayout().add(new Html("<strong>" + restaurant.getName() + "</strong>"));
        componentContext.infoLayout().add(new Text(restaurant.getDescription()));

        var detailButton = new Button(new Icon(VaadinIcon.EXIT_O));
        detailButton.setText("Order here");
        detailButton.addClickListener(e -> viewNavigationSupport.navigate(OrderView.class,
                new RouteParameters(ORDER_ID_PATH_PARAM, NEW_ORDER_ID),
                QueryParameters.of(RESTAURANT_ID_PATH_PARAM, String.valueOf(restaurant.getId()))));
        detailButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY_INLINE);

        var details = new Details();
        details.setContent(new Text(restaurant.getDescription()));
        details.setSummaryText("Information");

        componentContext.infoLayout().add(details);
        componentContext.rootLayout().add(detailButton);
    }
}