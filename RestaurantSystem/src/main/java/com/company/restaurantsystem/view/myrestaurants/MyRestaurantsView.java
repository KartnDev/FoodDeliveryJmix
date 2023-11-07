package com.company.restaurantsystem.view.myrestaurants;


import com.company.restaurantsystem.entity.Restaurant;
import com.company.restaurantsystem.uicomponents.ListComponents;
import com.company.restaurantsystem.view.main.MainView;
import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.details.Details;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteParameters;
import io.jmix.core.Messages;
import io.jmix.core.security.CurrentAuthentication;
import io.jmix.flowui.model.CollectionContainer;
import io.jmix.flowui.model.CollectionLoader;
import io.jmix.flowui.view.*;
import io.jmix.flowui.view.navigation.ViewNavigationSupport;
import org.springframework.beans.factory.annotation.Autowired;

@Route(value = "MyRestaurantsView", layout = MainView.class)
@ViewController("MyRestaurantsView")
@ViewDescriptor("my-restaurants-view.xml")
public class MyRestaurantsView extends StandardView {

    @Autowired
    private CurrentAuthentication currentAuthentication;
    @Autowired
    private ViewNavigationSupport viewNavigationSupport;
    @Autowired
    private ListComponents listComponents;
    @Autowired
    private Messages messages;
    @ViewComponent
    private CollectionLoader<Restaurant> restaurantsDl;
    @ViewComponent
    private VerticalLayout restaurantsListContainer;
    @ViewComponent
    private CollectionContainer<Restaurant> restaurantsDc;

    @Subscribe
    public void onInit(final InitEvent event) {
        restaurantsDl.setParameter("current_user", currentAuthentication.getUser());
        restaurantsDl.load();
    }

    @Subscribe
    public void onBeforeShow(final BeforeShowEvent event) {
        listComponents.attachListRenderer(restaurantsListContainer, restaurantsDc,
                (rest, infoLayout) -> restaurantsUpdater((Restaurant) rest, infoLayout));
    }

    private void restaurantsUpdater(Restaurant restaurant, ListComponents.ListComponentContext componentContext) {
        componentContext.infoLayout().add(new Html(messages.formatMessage(getClass(), "restaurantItemsHeader", restaurant.getName())));
        componentContext.infoLayout().add(new Text(restaurant.getDescription()));

        var detailButton = new Button(new Icon(VaadinIcon.PENCIL));
        detailButton.setText(messages.getMessage("actions.Edit"));
        detailButton.addClickListener(e -> viewNavigationSupport.navigate(MyRestaurantDetailView.class,
                new RouteParameters("id", String.valueOf(restaurant.getId()))));
        detailButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY_INLINE);

        var details = new Details();
        details.setContent(new Text(restaurant.getDescription()));
        details.setSummaryText(messages.getMessage("information"));

        componentContext.infoLayout().add(details);
        componentContext.rootLayout().add(detailButton);
    }
}