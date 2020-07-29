/*
 * Copyright (c) professorik  2019.
 * All rights reserved :)
 */

package sample;

import javafx.scene.Group;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;

public class GraphConnection {

    private Line line;
    private double weight;
    private int to;
    private Polygon polygon;
   // private Group graphicalGroup;
    private int from;
    private boolean isDirected;

    public GraphConnection(Line line, int to, Polygon polygon) {
        this.line = line;
        this.to = to;
        this.polygon = polygon;
    }

    public GraphConnection(Line line, int to, Polygon polygon, int from) {
        this.line = line;
        this.to = to;
        this.polygon = polygon;
        this.from = from;
    }

    public GraphConnection() {
    }

    public GraphConnection(Line line, double weight, int to, Polygon polygon) {
        this.line = line;
        this.weight = weight;
        this.to = to;
        this.polygon = polygon;
    }

    public GraphConnection(Line line, int to) {
        this.line = line;
        this.to = to;
        weight = 0;
    }

    public GraphConnection(int to) {
        this.to = to;
    }

    public GraphConnection(Line line, double weight, int to) {
        this.line = line;
        this.weight = weight;
        this.to = to;
    }

    public GraphConnection(Line line, double weight, int to, int from) {
        this.line = line;
        this.weight = weight;
        this.to = to;
        this.from = from;
    }

    public GraphConnection(Line line, int to, int from) {
        this.line = line;
        this.to = to;
        this.from = from;
        weight = 0;
    }

    public GraphConnection(Line line, double weight, int to, Polygon polygon, int from) {
        this.line = line;
        this.weight = weight;
        this.to = to;
        this.polygon = polygon;
        this.from = from;
    }

    public Polygon getPolygon() {
        return polygon;
    }

    public void setPolygon(Polygon polygon) {
        this.polygon = polygon;
    }

    public Line getLine() {
        return line;
    }

    public void setLine(Line line) {
        this.line = line;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public int getTo() {
        return to;
    }

    public void setTo(int to) {
        this.to = to;
    }

    public int getFrom() {
        return from;
    }

    public void setFrom(int from) {
        this.from = from;
    }

    public boolean isDirected() {
        return isDirected;
    }

    public void setDirected(boolean directed) {
        isDirected = directed;
    }
}
