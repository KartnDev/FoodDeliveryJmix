package com.company.restaurantsystem.view.myrestaurants;


import com.company.restaurantsystem.entity.Restaurant;
import com.company.restaurantsystem.service.AttachmentService;
import com.company.restaurantsystem.uicomponents.ListComponents;
import com.company.restaurantsystem.view.main.MainView;
import com.company.useroidcplagin.impl.UserProvider;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.component.avatar.AvatarVariant;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.details.Details;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.virtuallist.VirtualList;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteParameters;
import com.vaadin.flow.server.StreamResource;
import io.jmix.flowui.ViewNavigators;
import io.jmix.flowui.data.items.ContainerDataProvider;
import io.jmix.flowui.model.CollectionContainer;
import io.jmix.flowui.model.CollectionLoader;
import io.jmix.flowui.view.*;
import io.jmix.flowui.view.navigation.ViewNavigationSupport;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.MessageFormat;

@Route(value = "MyRestaurantsView", layout = MainView.class)
@ViewController("MyRestaurantsView")
@ViewDescriptor("my-restaurants-view.xml")
public class MyRestaurantsView extends StandardView {

    @Autowired
    private UserProvider userProvider;
    @Autowired
    private ViewNavigationSupport viewNavigationSupport;
    @Autowired
    private ListComponents listComponents;

    @ViewComponent
    private CollectionLoader<Restaurant> restaurantsDl;
    @ViewComponent
    private VerticalLayout restaurantsListContainer;
    @ViewComponent
    private CollectionContainer<Restaurant> restaurantsDc;

    @Subscribe
    public void onInit(final InitEvent event) {
        restaurantsDl.setParameter("current_user", userProvider.getCurrentUser());
        restaurantsDl.load();
    }

    @Subscribe
    public void onBeforeShow(final BeforeShowEvent event) {
        listComponents.attachListRenderer(restaurantsListContainer, restaurantsDc,
                (rest, infoLayout) -> restaurantsUpdater((Restaurant) rest, infoLayout));
    }

    private void restaurantsUpdater(Restaurant restaurant, ListComponents.ListComponentContext componentContext) {
        componentContext.infoLayout().add(new Html("<strong>" + restaurant.getName() + "</strong>"));
        componentContext.infoLayout().add(new Text(restaurant.getDescription()));

        var detailButton = new Button(new Icon(VaadinIcon.PENCIL));
        detailButton.setText("Edit");
        detailButton.addClickListener(e -> viewNavigationSupport.navigate(MyRestaurantDetailView.class,
                new RouteParameters("id", String.valueOf(restaurant.getId()))));
        detailButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY_INLINE);

        var details = new Details();
        details.setContent(new Text(restaurant.getDescription()));
        details.setSummaryText("Information");

        componentContext.infoLayout().add(details);
        componentContext.rootLayout().add(detailButton);
    }
}