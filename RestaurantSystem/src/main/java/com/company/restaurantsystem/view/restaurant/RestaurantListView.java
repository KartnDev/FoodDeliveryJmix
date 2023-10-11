package com.company.restaurantsystem.view.restaurant;

import com.company.restaurantsystem.entity.Restaurant;

import com.company.restaurantsystem.view.main.MainView;

import com.vaadin.flow.router.Route;
import io.jmix.flowui.view.*;

@Route(value = "restaurants", layout = MainView.class)
@ViewController("Restaurant.list")
@ViewDescriptor("restaurant-list-view.xml")
@LookupComponent("restaurantsDataGrid")
@DialogMode(width = "64em")
public class RestaurantListView extends StandardListView<Restaurant> {
}