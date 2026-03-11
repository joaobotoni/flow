package com.botoni.flow.ui.state;

import java.util.List;

public class RouteState {
    private List<String> points;
    private double distance;
    private boolean isFreightVisible;

    public RouteState() {
    }

    public RouteState(List<String> points, double distance, boolean isFreightVisible) {
        this.points = points;
        this.distance = distance;
        this.isFreightVisible = isFreightVisible;
    }

    public List<String> getPoints() {
        return points;
    }

    public double getDistance() {
        return distance;
    }

    public boolean isFreightVisible() {
        return isFreightVisible;
    }
}