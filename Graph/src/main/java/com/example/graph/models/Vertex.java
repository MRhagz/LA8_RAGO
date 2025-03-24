package com.example.graph.models;

import java.io.Serializable;

public class Vertex implements Serializable {
    private double xOffSet;
    private double yOffSet;
    private String text;

    public Vertex(String text) {
        xOffSet = 0;
        yOffSet = 0;
        this.text = text;
    }

    @Override
    public String toString() {
        return "Vertex: " + text + String.format("(%.2f, %.2f)", xOffSet, yOffSet);
    }

    public void setText(String text) {
        this.text = text;
    }

    public double getxOffSet() {
        return xOffSet;
    }

    public void setxOffSet(double xOffSet) {
        this.xOffSet = xOffSet;
    }

    public void setyOffSet(double yOffSet) {
        this.yOffSet = yOffSet;
    }

    public double getyOffSet() {
        return yOffSet;
    }

    public String getText() {
        return text;
    }
}
