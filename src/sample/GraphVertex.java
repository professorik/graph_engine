/*
 * Copyright (c) professorik  2019.
 * All rights reserved :)
 */

package sample;

import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.shape.Circle;

import java.util.ArrayList;

public class GraphVertex {

    public int id;
    private Circle circle;
    private ArrayList<GraphConnection> connections = new ArrayList<>();
    private double weights;
    private Label name;

    public void AddConnection(GraphConnection connection){
        this.connections.add(connection);
    }

    public GraphVertex(int id, Circle circle, ArrayList<GraphConnection> connections, double weights) {
        this.id = id;
        this.circle = circle;
        this.connections = connections;
        this.weights = weights;
    }

    public GraphVertex(int id, Circle circle, ArrayList<GraphConnection> connections, Label name) {
        this.id = id;
        this.circle = circle;
        this.connections = connections;
        this.name = name;
        weights = 0;
    }


    /**
     * This method using, when you need
     * in node 'circle' - graphic vertex
     * @return Circle
     */
    public Circle getCircle() {
        return circle;
    }

    public void setCircle(Circle circle) {
        this.circle = circle;
    }

    public ArrayList<GraphConnection> getConnections() {
        return connections;
    }

    public Label getName() {
        return name;
    }

    public void setName(Label name) {
        this.name = name;
    }

    public void setConnections(ArrayList<GraphConnection> connections) {
        this.connections = connections;
    }

    public double getWeights() {
        return weights;
    }

    public void setWeights(double weights) {
        this.weights = weights;
    }
}
