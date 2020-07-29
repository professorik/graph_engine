/*
 * Copyright (c) professorik  2019.
 * All rights reserved :)
 */

package sample;

import javafx.scene.shape.Polygon;
import javafx.util.Pair;

import java.util.ArrayList;

public class MyMath {

    private Double[] rightTriangle = new Double[]{
            0.0, 0.0,
            -40.0, 6.0,
            -40.0, -6.0
    };

    public Double[] getRightTriangle() {
        return rightTriangle;
    }

    public Double getY(ArrayList<Double> coordinates , Double x){
        Double a = (coordinates.get(2) - coordinates.get(0)) / (coordinates.get(3) - coordinates.get(1));
        Double b = coordinates.get(1) - a * coordinates.get(0);
        return a * x + b;
    }

    public ArrayList<Double> getCoordinates(double x0, double y0, double x1, double y1, double R) {
        ArrayList<Double> result = new ArrayList<>();
        Double a = (y1 - y0) / (x1 - x0);
        Double b = y0 - a * x0;

        Double firstSum = x0 + a * y0 - a * b;
        Double secondSum = 1 + a * a;
        Double D = firstSum * firstSum - secondSum * (x0 * x0 - R * R + (b - y0) * (b - y0));

        if (x0 < x1) {
            result.add((firstSum + Math.sqrt(D)) / secondSum);
        } else if (x0 > x1) {
            result.add((firstSum - Math.sqrt(D)) / secondSum);
        } else {
            result.add(x0);
        }

        result.add(a * result.get(0) + b);

        firstSum = x1 + a * y1 - a * b;
        secondSum = 1 + a * a;
        D = firstSum * firstSum - secondSum * (x1 * x1 - R * R + (b - y1) * (b - y1));

        if (x0 < x1) {
            result.add((firstSum - Math.sqrt(D)) / secondSum);
        } else if (x0 > x1) {
            result.add((firstSum + Math.sqrt(D)) / secondSum);
        } else {
            result.add(x0);
        }

        result.add(a * result.get(2) + b);

        // System.out.println(result);
        return result;
    }

    public ArrayList<Double> getSimpleCoordinates(double x0, double y0, double x1, double y1, double R) {
        ArrayList<Double> result = new ArrayList<>();
        Double a = (y1 - y0) / (x1 - x0);
        Double b = y0 - a * x0;

        Double firstSum = x0 + a * y0 - a * b;
        Double secondSum = 1 + a * a;
        Double D = firstSum * firstSum - secondSum * (x0 * x0 - R * R + (b - y0) * (b - y0));

        if (x0 < x1) {
            result.add((firstSum + Math.sqrt(D)) / secondSum);
        } else if (x0 > x1) {
            result.add((firstSum - Math.sqrt(D)) / secondSum);
        } else {
            result.add(x0);
        }

        result.add(a * result.get(0) + b);
        // System.out.println(result);
        return result;
    }

    public Polygon getTriangle(ArrayList<Double> coordinates) {
        Polygon polygon = new Polygon();
        Double x0 = coordinates.get(0);
        Double x1 = coordinates.get(2);
        Double y0 = coordinates.get(1);
        Double y1 = coordinates.get(3);
        for (Double a : coordinates) {
            System.out.println(a);
        }
        System.out.println("-------------------------------");
        Double delta = (x0 - x1 + y0 - y1) / 100;
        Double a1 = (y1 - y0) / (x1 - x0);
        Double a2 = (-1) / a1; //(x0 - x1) / (y1 - y0);
        Double b1 = y0 - a1 * x0;
        Double b2 = (a1 * (delta + (x0 + x1) / 2) + b1) - a2 * (delta + (x0 + x1) / 2);

        System.out.println(a1 + " " + b1);
        System.out.println(a2 + " " + b2);

        Double firstX = -Math.signum(delta) * 10.0 + (x0 + x1) / 2;
        Double secondX = (-3.0 + delta + (x0 + x1) / 2);
        Double thirdX = (3.0 + delta + (x0 + x1) / 2);


        polygon.getPoints().addAll(new Double[]{
                firstX, a1 * firstX + b1,
                secondX, a2 * secondX + b2,
                thirdX, a2 * thirdX + b2});
        System.out.println(polygon.getPoints());
        return polygon;
    }

    public Polygon getTriangle2(ArrayList<Double> coordinates) {
        Polygon polygon = new Polygon();

        Double x0 = coordinates.get(0);
        Double x1 = coordinates.get(2);
        Double y0 = coordinates.get(1);
        Double y1 = coordinates.get(3);
        Double a1 = (y1 - y0) / (x1 - x0);
        Double b1 = y0 - a1 * x0;
        double R = 20;
        double angle = 30;// (0;180)

        for (Double a : coordinates) {
            System.out.println(a);
        }
        System.out.println("-------------------------------");

        Double a2 = Math.tan(((180 - angle) / 180) * Math.PI);
        Double a3 = Math.tan(((180 - 2 * angle) / 180) * Math.PI);
        Double b2 = a1 * (x0 + x1 + 2 * R) / 2 + b1 - a2 * (x0 + x1 + 2 * R) / 2;
        Double b3 = a1 * (x0 + x1) / 2 + b1 - a3 * (x0 + x1) / 2;

        Double a2_2 = Math.tan((angle / 180) * Math.PI);
        Double a3_2 = Math.tan((angle / 90) * Math.PI);
        Double b2_2 = a1 * (x0 + x1 + 2 * R) / 2 + b1 - a2_2 * (x0 + x1 + 2 * R) / 2;
        Double b3_2 = a1 * (x0 + x1) / 2 + b1 - a3_2 * (x0 + x1) / 2;

        Double pX1 = (x0 + x1 + 2 * R) / 2, pY1 = a1 * pX1 + b1;
        Double pX2 = (b2 - b3) / (a3 - a2), pY2 = a3 * pX2 + b3;
        Double pX3 = (b2_2 - b3_2) / (a3_2 - a2_2), pY3 = a3_2 * pX3 + b3_2;

        polygon.getPoints().addAll(new Double[]{
                pX1, pY1,
                pX2, pY2,
                pX3, pY3});
        return polygon;
    }

    public Pair<Double, Double> getK(int x0, int y0, int x1, int y1){
        double a = 5.3 , b = (x0*x0*y1*y1 - x1*x1*y0*y0)/(x0*x0 - x1*x1);
        a = (b*b*x0*x0)/(b*b - y0*y0);
        return new Pair<Double, Double>(a , b);
    }
}
