/*
 * Copyright (c) professorik  2019.
 * All rights reserved :)
 */

package sample;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.PixelWriter;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Pair;

public class Main2 extends Application {

    private static int centerX = 2500;
    private static int centerY = 500;
    private static int radiusX = 1500;
    private static int radiusY = 200;
    private static int circleRadius = 50;
    private static int delta = circleRadius;
    private double a = 2;
    private double b = 1;

    @Override
    public void start(Stage primaryStage) throws Exception {
        AnchorPane anchorPane = new AnchorPane();
        ScrollPane scrollPane = new ScrollPane(anchorPane);
        Scene scene = new Scene(scrollPane, 1700, 1000);
        primaryStage.setTitle("Graphic Graph Editor (GGE)");
        primaryStage.setScene(scene);
        primaryStage.show();

        Group circles2 = new Group();Group circles3 = new Group();Group circles = new Group();

        for (double x = 0.0; x <= radiusX;) {
            if (radiusX - 1000 <= x){
                x+=0.1;
            }else{
                x+=1.0;
            }
            double x2 = ((double) x) / radiusX;
            double y2 = Math.sqrt(1 - x2 * x2);
            int y = (int) (y2 * radiusY);
            System.out.println(x + " " + y);

                /*Rectangle rectangle01 = new Rectangle(x + centerX, y + centerY, 1, 1);
                Rectangle rectangle02 = new Rectangle(x + centerX, centerY - y, 1, 1);
                Rectangle rectangle03 = new Rectangle(centerX - x, y + centerY, 1, 1);
                Rectangle rectangle04 = new Rectangle(centerX - x, centerY - y, 1, 1);
            Group firstEllipse = new Group(rectangle01, rectangle02, rectangle03, rectangle04);*/
                Circle circle11 = new Circle(x + centerX, y + centerY, delta,Color.CYAN);
                Circle circle12 = new Circle(x + centerX, centerY - y, delta, Color.CYAN);
                Circle circle13 = new Circle(centerX - x, y + centerY, delta,Color.CYAN);
                Circle circle14 = new Circle(centerX - x, centerY - y, delta,Color.CYAN);
             circles2.getChildren().addAll(circle11, circle12, circle13, circle14);

            Circle circle01 = new Circle(x + centerX, y + centerY, 3);
            Circle circle02 = new Circle(x + centerX, centerY - y, 3);
            Circle circle03 = new Circle(centerX - x, y + centerY, 3);
            Circle circle04 = new Circle(centerX - x, centerY - y, 3);
             circles.getChildren().addAll(circle01, circle02, circle03, circle04);
             if (x < 3){
                 x-=0.999;
             }
        }

        for (double x = 0.0; x <= radiusX + delta;) {
            if (radiusX + delta - 1000 <= x){
                x+=0.1;
            }else{
                x+=1.0;
            }
            double x2 = ((double) x) / (radiusX + delta);
            double y2 = Math.sqrt(1 - x2 * x2);
            double y =  y2 * (radiusY + delta);
            System.out.println(x + " " + y);

            /*Rectangle rectangle01 = new Rectangle(x + centerX, y + centerY, 1, 1);
            Rectangle rectangle02 = new Rectangle(x + centerX, centerY - y, 1, 1);
            Rectangle rectangle03 = new Rectangle(centerX - x, y + centerY, 1, 1);
            Rectangle rectangle04 = new Rectangle(centerX - x, centerY - y, 1, 1);
            Group firstEllipse = new Group(rectangle01, rectangle02, rectangle03, rectangle04);*/

            Circle circle01 = new Circle(x + centerX, y + centerY, 3,Color.RED);
            Circle circle02 = new Circle(x + centerX, centerY - y, 3, Color.RED);
            Circle circle03 = new Circle(centerX - x, y + centerY, 3,Color.RED);
            Circle circle04 = new Circle(centerX - x, centerY - y, 3,Color.RED);
            circles3.getChildren().addAll(circle01, circle02, circle03, circle04);

        }

        anchorPane.getChildren().add(circles2);
        anchorPane.getChildren().add(circles);
        anchorPane.getChildren().add(circles3);

      /*  Ellipse ellipse = new Ellipse(centerX , centerY , radiusX , radiusY);
        ellipse.setFill(Color.RED);

        MyMath myMath = new MyMath();
        Pair<Double, Double> pair = myMath.getK(centerX - radiusX , centerY, centerX , centerY + radiusY);
        a = pair.getKey(); b = pair.getValue();

        for (int x = centerX - radiusX; x < centerX + radiusX; ++x){
            System.out.println(Math.sqrt((b / a) * (a - x*x)));
            Circle circle = new Circle(x, Math.sqrt((b / a) * (a - x*x)) , circleRadius , Color.AQUA);
            Circle circle2 = new Circle(x ,  - Math.sqrt((b / a) * (a - x*x)) , circleRadius , Color.AQUA);
            anchorPane.getChildren().addAll(circle , circle2);
        }

        Ellipse ellipse2 = new Ellipse(750 , 500 , ellipse.getRadiusX() + delta , ellipse.getRadiusY() + delta);
        ellipse2.setFill(Color.GREEN); anchorPane.getChildren().add(ellipse2); anchorPane.getChildren().add(ellipse);*/
    }

    public static void main(String[] args) {
        launch(args);
    }
}
