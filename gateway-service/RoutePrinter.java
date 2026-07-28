package com.salon;

import org.springframework.boot.CommandLineRunner;
import org.springframework.cloud.gateway.route.Route;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.stereotype.Component;

@Component
public class RoutePrinter implements CommandLineRunner {

    private final RouteLocator routeLocator;

    public RoutePrinter(RouteLocator routeLocator) {
        this.routeLocator = routeLocator;
    }

    @Override
    public void run(String... args) {

        System.out.println("\n========== LOADED GATEWAY ROUTES ==========\n");

        routeLocator.getRoutes().subscribe(route -> printRoute(route));

        System.out.println("\n===========================================\n");
    }

    private void printRoute(Route route) {
        System.out.println("--------------------------------");
        System.out.println("Route ID : " + route.getId());
        System.out.println("URI      : " + route.getUri());
        System.out.println("Order    : " + route.getOrder());
        System.out.println("--------------------------------");
    }
}