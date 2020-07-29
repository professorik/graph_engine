/*
 * Copyright (c) professorik  2019.
 * All rights reserved :)
 */


package sample;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.scene.shape.Polygon;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.scene.transform.Rotate;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

//TODO: разнообразить интерфейс, добавить MapAndUnite для arrayList

public class Controller implements Initializable {

    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_WHITE = "\u001B[37m";

    @FXML
    private BorderPane border_pane;
    @FXML
    private Button add_button;
    @FXML
    private Button stop_button;
    @FXML
    private VBox right_box;
    @FXML
    private TextField commandTextField;
    @FXML
    private ChoiceBox typeGraphChoiceBox;
    @FXML
    private ScrollPane textFlowBox;
    @FXML
    private ScrollPane textFlowBox1;
    @FXML
    private Button GetGraphMatrixButton;

    private Pane root = new Pane();

    private double oldX, oldY;
    private double generateX, generateY;

    private boolean isBondMode = false;
    private boolean isDirectedGraphConnection = true;

    private final double radius = 30;

    private int index = -1;

    private GraphVertex vertexMain;

    public static ArrayList<GraphVertex> graph = new ArrayList<>();

    private TextFlow textFlow1;

    private TextFlow textFlow;

    private ExecutorService executorService = Executors.newWorkStealingPool();

    private ContextMenu contextMenu = new ContextMenu();
    private ContextMenu contextLine = new ContextMenu();
    private ContextMenu contextMenuCircle = new ContextMenu();

    private ArrayList<GraphConnection> trash = new ArrayList<>();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        colorTest();
        initChoiceBox();

        AnchorPane anchorPane = new AnchorPane();
        anchorPane.setStyle("-fx-background-color:#2b2b2b");
        anchorPane.setPrefSize(2000, 200);
        textFlow = new TextFlow(new Text("\n"));
        textFlow.setStyle("-fx-font-size:14px");
        anchorPane.getChildren().addAll(textFlow);
        textFlowBox.setContent(anchorPane);

        AnchorPane anchorPane1 = new AnchorPane();
        anchorPane1.setStyle("-fx-background-color:#2b2b2b");
        anchorPane1.setPrefSize(200, 200);
        textFlow1 = new TextFlow(new Text("\n"));
        textFlow1.setStyle("-fx-font-size:14px");
        anchorPane1.getChildren().addAll(textFlow1);
        textFlowBox1.setContent(anchorPane1);

        border_pane.setCenter(root);

        commandTextField.setOnKeyReleased(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                Text command = new Text(commandTextField.getText() + "\n");
                command.setFill(Color.web("#d6bf55"));
                textFlow1.getChildren().add(command);
                handleCommand(commandTextField.getText());
            }
        });

        add_button.setOnAction(event -> {
            // add_button.setStyle("-fx-background-color:#3a3c3d");
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        //Thread.currentThread().sleep(3000);
                        System.out.println(ANSI_BLUE + "Start search way..." + ANSI_RESET);
                        //  System.out.println(ANSI_BLUE + "Result: " + isWay2(graph, 0, 0) + ANSI_RESET);
                        getKruskalAlgorithm();
                    } catch (OutOfMemoryError e) {
                        System.out.println(ANSI_RED + "Out of memory" + ANSI_RESET);
                    }

                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                           /* for (GraphVertex vertex : graph){
                                for (GraphConnection connection: vertex.getConnections()){
                                    root.getChildren().addAll(connection.getPolygon() , connection.getLine());
                                }
                            }*/
                            if (!trash.isEmpty()) {
                                for (GraphConnection connection : trash) {
                                    if (connection.isDirected()) {
                                        root.getChildren().remove(connection.getPolygon());
                                    }
                                    root.getChildren().remove(connection.getLine());
                                }
                                trash.clear();
                            }
                            System.out.println(ANSI_BLUE + "Success" + ANSI_RESET);
                        }
                    });
                }
            });

        });


        //stop_button.setOnAction(event -> executorService.shutdownNow());

        MenuItem menuItem1 = new MenuItem("Add");
        menuItem1.setOnAction(event -> addVertex(generateX, generateY));

        contextMenu.getItems().addAll(menuItem1);

        border_pane.getCenter().setOnContextMenuRequested(event -> {
            generateX = event.getX();
            generateY = event.getY();
            if (!contextMenuCircle.isShowing()) {
                contextMenu.show(border_pane.getCenter(), event.getScreenX(), event.getScreenY());
            }
        });
        border_pane.getCenter().setOnMouseClicked(e -> contextMenu.hide());

        GetGraphMatrixButton.setOnAction(event -> {
            getCopyMatrixWindow();
        });
    }

    private void getCopyMatrixWindow() {
        Scene scene = null;
        try {
            scene = new Scene(FXMLLoader.load(getClass().getClassLoader().getResource("libaryInterface/getMatrixInterface.fxml")));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Stage window = new Stage();
        window.setTitle("Matrix");
        window.setScene(scene);

        window.setX(720);
        window.setY(300);

        window.setResizable(false);
        window.initModality(Modality.WINDOW_MODAL);
        window.initOwner(border_pane.getScene().getWindow());
        window.show();
    }

    public static String getMatrix() {
        String result = "";
        for (int i = 0; i < graph.size(); ++i) {
            for (int j = 0; j < graph.size(); ++j) {
                if (i == j) {
                    result += "0.0";
                    if (j != graph.size() - 1) {
                        result += ", ";
                    }
                    continue;
                }
                boolean fl = true;
                for (GraphConnection connection : graph.get(i).getConnections()) {
                    if (connection.getTo() == j) {
                        result += connection.getWeight() + ", ";
                        fl = false;
                        break;
                    }
                }
                if (fl) {
                    result += "Double.MAX_VALUE, ";
                }
            }
            result += "\n";
        }

        return result;
    }

    public static String getMatrixWithoutWeights() {
        String result = "";
        for (int i = 0; i < graph.size(); ++i) {
            for (int j = 0; j < graph.size(); ++j) {
                if (i == j) {
                    result += "0";
                    if (j != graph.size() - 1) {
                        result += ", ";
                    }
                    continue;
                }
                boolean fl = true;
                for (GraphConnection connection : graph.get(i).getConnections()) {
                    if (connection.getTo() == j) {
                        result += "1, ";
                        fl = false;
                        break;
                    }
                }
                if (fl) {
                    result += "0, ";
                }
            }
            result += "\n";
        }

        return result;
    }

    private void handleCommand(String text) {
        text = text.replaceAll("\\s+", "");
        try {
            if (text.equals("add")) {
                addVertex(Math.random() * (root.getWidth() - 60) + 30, Math.random() * (root.getHeight() - 60) + 30);
            } else if (text.startsWith("delete(")) {
                String[] id = text.substring(7, text.length() - 1).split(",");
                for (int i = 0; i < id.length; ++i) {
                    delete(graph.get(Integer.valueOf(id[i])));
                }
            } else if (isDirectedGraphConnection) {
                if (text.contains("<->")) {
                    String connections = text.split("\\{")[0];
                    String weights = text.split("\\{")[1];
                    weights = weights.substring(0, weights.length() - 1);

                    String[] vertex_id = connections.split("->");
                    String[] connection_weight = weights.split(",");

                    for (int i = 0; i < connection_weight.length; ++i) {
                        int first_id, second_id;
                        if (vertex_id[i].lastIndexOf('<') == -1) {
                            first_id = Integer.valueOf(vertex_id[i]);
                        } else {
                            first_id = Integer.valueOf(vertex_id[i].substring(0, vertex_id[i].length() - 1));
                        }
                        if (vertex_id[i + 1].lastIndexOf('<') == -1) {
                            second_id = Integer.valueOf(vertex_id[i + 1]);
                        } else {
                            second_id = Integer.valueOf(vertex_id[i + 1].substring(0, vertex_id[i + 1].length() - 1));
                        }
                        Double weight = Double.valueOf(connection_weight[i]);
                        addConnectionWithGraphic(graph, first_id, second_id, weight, vertex_id[i].lastIndexOf('<') == -1);
                    }
                } else if (text.contains("->")) {
                    String connections = text.split("\\{")[0];
                    String weights = text.split("\\{")[1];
                    weights = weights.substring(0, weights.length() - 1);

                    String[] vertex_id = connections.split("->");
                    String[] connection_weight = weights.split(",");

                    for (int i = 0; i < connection_weight.length; ++i) {
                        int first_id = Integer.valueOf(vertex_id[i]);
                        int second_id = Integer.valueOf(vertex_id[i + 1]);
                        Double weight = Double.valueOf(connection_weight[i]);
                        addConnectionWithGraphic(graph, first_id, second_id, weight, true);
                    }
                }
            } else if (text.contains("->")) {
                String connections = text.split("\\{")[0];
                String weights = text.split("\\{")[1];
                weights = weights.substring(0, weights.length() - 1);

                String[] vertex_id = connections.split("->");
                String[] connection_weight = weights.split(",");

                for (int i = 0; i < connection_weight.length; ++i) {
                    int first_id = Integer.valueOf(vertex_id[i]);
                    int second_id = Integer.valueOf(vertex_id[i + 1]);
                    Double weight = Double.valueOf(connection_weight[i]);

                    addConnectionWithGraphic(graph, first_id, second_id, weight, true);
                }
            } else if (text.equals("-x>")) {
                String[] arr = text.split("-x>");
                int first_id = Integer.valueOf(arr[0]);
                int second_id = Integer.valueOf(arr[1]);
                deleteConnectionWithGraphic(graph, first_id, second_id);
            }
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
            Text command = new Text("Input right count of weights!" + "\n");
            command.setFill(Color.web("#ff6b68"));
            textFlow1.getChildren().add(command);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            Text command = new Text("IDs and weights must be a numbers!" + "\n");
            command.setFill(Color.web("#ff6b68"));
            textFlow1.getChildren().add(command);
        } catch (Exception e) {
            e.printStackTrace();
            Text command = new Text("Xren znaet v chem problema" + "\n");
            command.setFill(Color.web("#ff6b68"));
            textFlow1.getChildren().add(command);
        }
    }

    private void addVertex(Double x, Double y) {
        ++index;
        Circle circle = new Circle(x, y, radius, Color.AQUA);

        Label label = new Label(String.valueOf(index));

        Group vertexGroup = new Group();
        vertexGroup.getChildren().addAll(circle, label);

        //TODO придумать функцию с делением на аргумент!!! P.s. работает отлично даже при 10004 элементах
        label.setLayoutX(circle.getCenterX() - (3 + 5 * label.getText().length()));
        label.setLayoutY(circle.getCenterY() - (26 - 3 * label.getText().length()));

        label.setTextFill(Color.BLACK);
        //TODO придумать функцию с делением на аргумент (при N >= 10^9 шрифт будет отрицательный)!!! P.s. работает отлично даже при 10004 элементах
        label.setStyle("-fx-font-size:" + (34 - 4 * label.getText().length()) + "px");

        GraphVertex newVertex = new GraphVertex(index, circle, new ArrayList<>(), label);

        circle.setOnMousePressed(event2 -> {
            oldX = circle.getTranslateX() - event2.getSceneX();
            oldY = circle.getTranslateY() - event2.getSceneY();
        });

        circle.setOnMouseDragged(event2 -> {
            if (oldX + event2.getSceneX() + circle.getCenterX() >= 30 && oldX + event2.getSceneX() + circle.getCenterX() <= root.getWidth() - 30
                    && oldY + event2.getSceneY() + circle.getCenterY() >= 30 && oldY + event2.getSceneY() + circle.getCenterY() <= root.getHeight() - 30) {
                circle.setTranslateX(oldX + event2.getSceneX());
                circle.setTranslateY(oldY + event2.getSceneY());
                MyMath myMath = new MyMath();
                for (GraphConnection connection : newVertex.getConnections()) {
                    ArrayList<Double> coordinates = myMath.getCoordinates(circle.getTranslateX() + circle.getCenterX(),
                            circle.getTranslateY() + circle.getCenterY(),
                            graph.get(connection.getTo()).getCircle().getCenterX() + graph.get(connection.getTo()).getCircle().getTranslateX(),//  connection.getLine().getEndX(),
                            graph.get(connection.getTo()).getCircle().getCenterY() + graph.get(connection.getTo()).getCircle().getTranslateY(), radius);

                    if (connection.isDirected()) {
                        //Redraw polygon on line
                        root.getChildren().remove(connection.getPolygon());
                        connection.setPolygon(addPolygon(coordinates));
                        root.getChildren().add(connection.getPolygon());
                        connection.getPolygon().setOnMouseClicked(event3 -> {
                            if (event3.getButton().equals(MouseButton.MIDDLE)) {
                                MenuItem menuItemConnection1 = new MenuItem("Set weight");
                                menuItemConnection1.setOnAction(event4 -> addWeight(connection, event3.getScreenX(), event3.getScreenY()));
                                MenuItem menuItemConnection2 = new MenuItem("Delete weight");
                                menuItemConnection2.setOnAction(event4 -> {
                                    connection.setWeight(0);
                                });
                                MenuItem menuItemConnection3 = new MenuItem("Delete connection");
                                menuItemConnection3.setOnAction(event4 -> {
                                    root.getChildren().removeAll(connection.getPolygon(), connection.getLine());
                                    graph.get(connection.getFrom()).getConnections().remove(connection);
                                    printGraph();
                                });

                                contextLine.getItems().clear();
                                contextLine.getItems().addAll(menuItemConnection1, menuItemConnection2, menuItemConnection3);
                                contextLine.show(connection.getPolygon(), event3.getScreenX(), event3.getScreenY());
                            }
                        });
                    }

                    connection.getLine().setStartX(coordinates.get(0));
                    connection.getLine().setStartY(coordinates.get(1));
                    connection.getLine().setEndX(coordinates.get(2));
                    connection.getLine().setEndY(coordinates.get(3));
                }
                for (GraphVertex vertex : searchVertexTo(newVertex)) {
                    for (GraphConnection connection : vertex.getConnections()) {
                        if (connection.getTo() == newVertex.id) {
                            ArrayList<Double> coordinates = myMath.getCoordinates(vertex.getCircle().getCenterX() + vertex.getCircle().getTranslateX(),//connection.getLine().getStartX(),
                                    vertex.getCircle().getCenterY() + vertex.getCircle().getTranslateY(),
                                    circle.getTranslateX() + circle.getCenterX(),
                                    circle.getTranslateY() + circle.getCenterY(), radius);

                            if (connection.isDirected()) {
                                //Redraw polygon on line
                                root.getChildren().remove(connection.getPolygon());
                                connection.setPolygon(addPolygon(coordinates));
                                root.getChildren().add(connection.getPolygon());
                                connection.getPolygon().setOnMouseClicked(event3 -> {
                                    if (event3.getButton().equals(MouseButton.MIDDLE)) {
                                        MenuItem menuItemConnection1 = new MenuItem("Set weight");
                                        menuItemConnection1.setOnAction(event4 -> addWeight(connection, event3.getScreenX(), event3.getScreenY()));
                                        MenuItem menuItemConnection2 = new MenuItem("Delete weight");
                                        menuItemConnection2.setOnAction(event4 -> {
                                            connection.setWeight(0);
                                        });
                                        MenuItem menuItemConnection3 = new MenuItem("Delete connection");
                                        menuItemConnection3.setOnAction(event4 -> {
                                            root.getChildren().removeAll(connection.getPolygon(), connection.getLine());
                                            graph.get(connection.getFrom()).getConnections().remove(connection);
                                            printGraph();
                                        });

                                        contextLine.getItems().clear();
                                        contextLine.getItems().addAll(menuItemConnection1, menuItemConnection2, menuItemConnection3);
                                        contextLine.show(connection.getPolygon(), event3.getScreenX(), event3.getScreenY());
                                    }
                                });
                            }

                            connection.getLine().setStartX(coordinates.get(0));
                            connection.getLine().setStartY(coordinates.get(1));
                            connection.getLine().setEndX(coordinates.get(2));
                            connection.getLine().setEndY(coordinates.get(3));
                            break;
                        }
                    }

                }

                label.setTranslateX(oldX + event2.getSceneX());
                label.setTranslateY(oldY + event2.getSceneY());
            }
        });

        circle.setOnContextMenuRequested(event2 -> {
            System.out.println(ANSI_PURPLE + typeGraphChoiceBox.getValue() + ANSI_RESET);
            System.out.println(ANSI_PURPLE + isDirectedGraphConnection + ANSI_RESET);
            if (typeGraphChoiceBox.getValue().equals("Mixed graph") && !isBondMode) {
                Menu menu = new Menu("Add bond (first)");
                MenuItem item1 = new MenuItem("Ориентированное");
                MenuItem item2 = new MenuItem("Неориентированное");
                menu.getItems().addAll(item1, item2);
                item1.setOnAction(event -> {
                    vertexMain = newVertex;
                    isBondMode = !isBondMode;
                    isDirectedGraphConnection = true;
                });
                item2.setOnAction(event -> {
                    vertexMain = newVertex;
                    isBondMode = !isBondMode;
                    isDirectedGraphConnection = false;
                });
                MenuItem menuItemCircle2 = new MenuItem("Delete");
                menuItemCircle2.setOnAction(event1 -> delete(newVertex));
                contextMenuCircle.getItems().clear();
                contextMenuCircle.getItems().addAll(menu, menuItemCircle2);
                contextMenuCircle.show(circle, event2.getScreenX(), event2.getScreenY());
            } else {
                MenuItem menuItemCircle = isBondMode ? new MenuItem("Add bond (second)") : new MenuItem("Add bond (first)");

                menuItemCircle.setOnAction(event1 -> {
                    if (isBondMode && !vertexMain.equals(newVertex) && !connectionExist(vertexMain, newVertex)) {
                        MyMath myMath = new MyMath();
                        ArrayList<Double> coordinates = myMath.getCoordinates(vertexMain.getCircle().getTranslateX() + vertexMain.getCircle().getCenterX(),
                                vertexMain.getCircle().getTranslateY() + vertexMain.getCircle().getCenterY(),
                                circle.getCenterX() + circle.getTranslateX(),
                                circle.getCenterY() + circle.getTranslateY(), radius);
                        Line line = new Line(coordinates.get(0), coordinates.get(1), coordinates.get(2), coordinates.get(3));
                        root.getChildren().addAll(line);

                        if (isDirectedGraphConnection) {
                            Polygon polygon1 = addPolygon(coordinates);
                            root.getChildren().addAll(polygon1);
                            GraphConnection graphConnection = new GraphConnection(line, newVertex.id, polygon1, vertexMain.id);
                            graphConnection.setDirected(true);
                            graph.get(vertexMain.id).AddConnection(graphConnection);

                            polygon1.setOnMouseClicked(event3 -> {
                                if (event3.getButton().equals(MouseButton.MIDDLE)) {
                                    MenuItem menuItemConnection1 = new MenuItem("Set weight");
                                    menuItemConnection1.setOnAction(event4 -> addWeight(graphConnection, event3.getScreenX(), event3.getScreenY()));
                                    MenuItem menuItemConnection2 = new MenuItem("Delete weight");
                                    menuItemConnection2.setOnAction(event4 -> {
                                        graphConnection.setWeight(0);
                                        printGraph();
                                    });
                                    MenuItem menuItemConnection3 = new MenuItem("Delete connection");
                                    menuItemConnection3.setOnAction(event4 -> {
                                        root.getChildren().removeAll(polygon1, line);
                                        graph.get(graphConnection.getFrom()).getConnections().remove(graphConnection);
                                        printGraph();
                                    });

                                    contextLine.getItems().clear();
                                    contextLine.getItems().addAll(menuItemConnection1, menuItemConnection2, menuItemConnection3);
                                    contextLine.show(polygon1, event3.getScreenX(), event3.getScreenY());
                                }
                            });
                        } else {
                            GraphConnection graphConnection = new GraphConnection(line, newVertex.id, vertexMain.id);
                            graphConnection.setDirected(false);
                            GraphConnection graphConnection1 = new GraphConnection(line, vertexMain.id, newVertex.id);
                            graphConnection1.setDirected(false);
                            graph.get(vertexMain.id).AddConnection(graphConnection);
                            graph.get(newVertex.id).AddConnection(graphConnection1);
                        }
                        printGraph();
                    } else {
                        vertexMain = newVertex;
                    }
                    isBondMode = !isBondMode;
                });
                MenuItem menuItemCircle2 = new MenuItem("Delete");

                menuItemCircle2.setOnAction(event1 -> delete(newVertex));

                contextMenuCircle.getItems().clear();
                contextMenuCircle.getItems().addAll(menuItemCircle, menuItemCircle2);
                contextMenuCircle.show(circle, event2.getScreenX(), event2.getScreenY());
            }
        });

        root.getChildren().addAll(circle);
        root.getChildren().addAll(label);
        graph.add(newVertex);

        Text command = new Text("Completed" + "\n");
        command.setFill(Color.web("#629755"));
        textFlow1.getChildren().add(command);
    }

    private void deleteConnection(ArrayList<GraphVertex> graph, int first_id, int second_id) {
        if (graph.size() > first_id || graph.size() > second_id) {
            for (GraphConnection connection : graph.get(first_id).getConnections()) {
                if (connection.getTo() == second_id) {
                    graph.get(first_id).getConnections().remove(connection);
                    return;
                }
            }
        }
        System.out.println(ANSI_RED + "Ты совсем дебил?" + ANSI_RESET);
    }

    private void deleteConnectionWithGraphic(ArrayList<GraphVertex> graph, int first_id, int second_id) {
        if (graph.size() > first_id || graph.size() > second_id) {
            for (GraphConnection connection : graph.get(first_id).getConnections()) {
                if (connection.getTo() == second_id) {
                    if (connection.isDirected()) {
                        root.getChildren().removeAll(connection.getLine(), connection.getPolygon());
                        graph.get(first_id).getConnections().remove(connection);
                    } else {
                        root.getChildren().removeAll(connection.getLine());
                        graph.get(first_id).getConnections().remove(connection);
                        deleteConnectionWithGraphic(graph, second_id, first_id);
                    }
                    Text command = new Text("Completed" + "\n");
                    command.setFill(Color.web("#629755"));
                    textFlow1.getChildren().add(command);
                    return;
                }
            }
        }
        Text command = new Text("Ты совсем дебил?" + "\n");
        command.setFill(Color.web("#ff6b68"));
        textFlow1.getChildren().add(command);
        System.out.println(ANSI_RED + "Ты совсем дебил?" + ANSI_RESET);
    }

    private void addConnectionWithGraphic(ArrayList<GraphVertex> graph, int first_id, int second_id, Double weight, boolean isDirectedConnection) {
        try {
            if (first_id != second_id && !connectionExist(graph.get(first_id), graph.get(second_id))) {
                GraphVertex vertexMain = graph.get(first_id);
                GraphVertex newVertex = graph.get(second_id);
                Circle circle = newVertex.getCircle();
                MyMath myMath = new MyMath();
                ArrayList<Double> coordinates = myMath.getCoordinates(vertexMain.getCircle().getTranslateX() + vertexMain.getCircle().getCenterX(),
                        vertexMain.getCircle().getTranslateY() + vertexMain.getCircle().getCenterY(),
                        circle.getCenterX() + circle.getTranslateX(),
                        circle.getCenterY() + circle.getTranslateY(), radius);
                Line line = new Line(coordinates.get(0), coordinates.get(1), coordinates.get(2), coordinates.get(3));
                root.getChildren().addAll(line);
                if (isDirectedGraphConnection && isDirectedConnection) {
                    Polygon polygon1 = addPolygon(coordinates);
                    root.getChildren().addAll(polygon1);
                    GraphConnection graphConnection = new GraphConnection(line, weight, newVertex.id, polygon1, vertexMain.id);
                    graphConnection.setDirected(true);
                    graph.get(vertexMain.id).AddConnection(graphConnection);

                    polygon1.setOnMouseClicked(event3 -> {
                        if (event3.getButton().equals(MouseButton.MIDDLE)) {
                            MenuItem menuItemConnection1 = new MenuItem("Set weight");
                            menuItemConnection1.setOnAction(event4 -> addWeight(graphConnection, event3.getScreenX(), event3.getScreenY()));
                            MenuItem menuItemConnection2 = new MenuItem("Delete weight");
                            menuItemConnection2.setOnAction(event4 -> {
                                graphConnection.setWeight(0);
                                printGraph();
                            });
                            MenuItem menuItemConnection3 = new MenuItem("Delete connection");
                            menuItemConnection3.setOnAction(event4 -> {
                                root.getChildren().removeAll(polygon1, line);
                                graph.get(graphConnection.getFrom()).getConnections().remove(graphConnection);
                                printGraph();
                            });

                            contextLine.getItems().clear();
                            contextLine.getItems().addAll(menuItemConnection1, menuItemConnection2, menuItemConnection3);
                            contextLine.show(polygon1, event3.getScreenX(), event3.getScreenY());
                        }
                    });
                } else {
                    GraphConnection graphConnection = new GraphConnection(line, weight, newVertex.id, vertexMain.id);
                    graphConnection.setDirected(false);
                    graph.get(vertexMain.id).AddConnection(graphConnection);
                    GraphConnection graphConnection2 = new GraphConnection(line, weight, vertexMain.id, newVertex.id);
                    graphConnection2.setDirected(false);
                    graph.get(newVertex.id).AddConnection(graphConnection2);
                }
                printGraph();
                Text command = new Text("Completed" + "\n");
                command.setFill(Color.web("#629755"));
                textFlow1.getChildren().add(command);
                return;
            }
        } catch (IndexOutOfBoundsException e) {
        }
        Text command = new Text("Ты совсем дебил?" + "\n");
        command.setFill(Color.web("#ff6b68"));
        textFlow1.getChildren().add(command);
        System.out.println(ANSI_RED + "Ты совсем дебил?" + ANSI_RESET);
    }

    private void addWeight(GraphConnection graphConnection, Double x, Double y) {
        Label secondLabel = new Label("Please, enter the connection's weight!");
        TextField textField = new TextField();
        Button okButton = new Button("OK");
        StackPane secondaryLayout = new StackPane();
        VBox content = new VBox();
        content.getChildren().addAll(secondLabel, textField, okButton);
        secondaryLayout.getChildren().addAll(content);
        Scene secondScene = new Scene(secondaryLayout, content.getMinWidth(), content.getMinHeight());
        Stage setWeightWindow = new Stage();
        setWeightWindow.setTitle("Set weight");
        setWeightWindow.setScene(secondScene);
        setWeightWindow.setX(x);
        setWeightWindow.setY(y);
        setWeightWindow.setResizable(false);
        setWeightWindow.initModality(Modality.WINDOW_MODAL);
        setWeightWindow.initOwner(border_pane.getScene().getWindow());
        setWeightWindow.show();
        okButton.setOnAction(event5 -> {
            try {
                Double weight = Double.valueOf(textField.getText().trim());
                graphConnection.setWeight(weight);
                setWeightWindow.close();
            } catch (Exception e) {
                Label errorLabel = new Label("Please, enter the NUMBER!");
                errorLabel.setTextFill(Color.RED);
                if (content.getChildren().size() < 4) {
                    content.getChildren().add(1, errorLabel);
                    setWeightWindow.setHeight(setWeightWindow.getHeight() + errorLabel.getHeight() + 10);
                }
            }
        });
        printGraph();
    }

    private Polygon addPolygon(ArrayList<Double> coordinates) {
        Polygon polygon1 = new Polygon();
        MyMath myMath = new MyMath();
        polygon1.getPoints().addAll(myMath.getRightTriangle());
        polygon1.setTranslateX(coordinates.get(2));
        polygon1.setTranslateY(coordinates.get(3));
        Rotate rotate = new Rotate();
        try {
            Double a = (coordinates.get(1) - coordinates.get(3)) / (coordinates.get(0) - coordinates.get(2));
            rotate.setPivotX(0);
            rotate.setPivotY(0);
            if ((a < 0 && (coordinates.get(0) > coordinates.get(2) && coordinates.get(1) < coordinates.get(3)))
                    || (a > 0 && (coordinates.get(0) > coordinates.get(2) && coordinates.get(1) > coordinates.get(3)))
                    || (coordinates.get(1) == coordinates.get(3) && coordinates.get(0) > coordinates.get(2))) {
                rotate.setAngle(180 + Math.toDegrees(Math.atan(a)));
            } else {
                rotate.setAngle(Math.toDegrees(Math.atan(a)));
            }
            polygon1.getTransforms().addAll(rotate);
        } catch (ArithmeticException e) {
            rotate.setPivotX(0);
            rotate.setPivotY(0);
            if (coordinates.get(1) < coordinates.get(3)) {
                rotate.setAngle(-90);
            } else {
                rotate.setAngle(90);
            }
            polygon1.getTransforms().addAll(rotate);
        }
        return polygon1;
    }

    //TODO: сделать стэк
    private void delete(GraphVertex newVertex) {
        for (GraphConnection line : newVertex.getConnections()) {
            root.getChildren().remove(line.getLine());
            root.getChildren().remove(line.getPolygon());
        }
        for (GraphVertex vertex : searchVertexTo(newVertex)) {
            for (GraphConnection connection : vertex.getConnections()) {
                if (connection.getTo() == newVertex.id) {
                    vertex.getConnections().remove(connection);
                    root.getChildren().remove(connection.getLine());
                    root.getChildren().remove(connection.getPolygon());
                    break;
                }
            }

        }
        root.getChildren().remove(newVertex.getCircle());
        root.getChildren().remove(newVertex.getName());

        if (newVertex.equals(vertexMain)) {
            isBondMode = false;
        }

        //graph.remove(newVertex);

        graph.set(newVertex.id, new GraphVertex(newVertex.id, null, new ArrayList<>(), null));
        printGraph();
    }

    private boolean connectionExist(GraphVertex vertexMain, GraphVertex newVertex) {
        for (GraphConnection connection : vertexMain.getConnections()) {
            if (connection.getTo() == newVertex.id) {
                Text text = new Text("Такая связь уже существует! \n");
                text.setFill(Color.web("#ff6b68"));
                textFlow.getChildren().add(0, text);
                System.out.println(ANSI_RED + "Такая связь уже существует!" + ANSI_RESET);
                return true;
            }
        }
        return false;
    }

    private void printGraph() {
        textFlow.getChildren().clear();

        System.out.println(ANSI_GREEN + "----------------------------------------------" + ANSI_RESET);
        System.out.println(ANSI_GREEN + "{to , weight , from}" + ANSI_RESET);
        for (GraphVertex graphVertex : graph) {
            Text text = new Text("ID: " + graphVertex.id + "\n");
            text.setFill(Color.web("#299999"));
            textFlow.getChildren().addAll(text);

            System.out.println(ANSI_CYAN + "ID: " + graphVertex.id + ANSI_RESET);

            String kar = "";
            for (GraphConnection connection : graphVertex.getConnections()) {
                kar += "{" + connection.getTo() + " , " + connection.getWeight() + " , " + connection.getFrom() + "}" + " ";

                System.out.print(ANSI_YELLOW + connection.getTo() + " " + ANSI_RESET);
            }

            text = new Text(kar + "\n");
            text.setFill(Color.web("#d6bf55"));
            textFlow.getChildren().addAll(text);

            System.out.println();
        }
    }

    private ArrayList<GraphVertex> searchVertexTo(GraphVertex vertexMain) {
        ArrayList<GraphVertex> vertexArrayList = new ArrayList<>();
        for (GraphVertex vertex : graph) {
            if (vertex.id == vertexMain.id) continue;
            for (GraphConnection connection : vertex.getConnections()) {
                if (connection.getTo() == vertexMain.id) {
                    vertexArrayList.add(vertex);
                    break;
                }
            }
        }
        return vertexArrayList;
    }

    private void initChoiceBox() {
        ObservableList<String> items = FXCollections.observableArrayList("Directed graph", "Graph isomorphism", "Mixed graph");
        typeGraphChoiceBox.setItems(items);
        typeGraphChoiceBox.setValue("Directed graph");
        isDirectedGraphConnection = true;

        final String[] excValue = {typeGraphChoiceBox.getValue().toString()};

        typeGraphChoiceBox.setOnAction(event -> {
            if (!typeGraphChoiceBox.getValue().toString().equals(excValue[0])) {
                if (graph.size() > 0) {
                    Label secondLabel = new Label("Are you sure? If you choose \'YES\' - graph will have cleaning!");

                    HBox hbox = new HBox();
                    Button yesButton = new Button("YES");
                    Button noButton = new Button("NO");
                    hbox.getChildren().addAll(yesButton, noButton);

                    StackPane secondaryLayout = new StackPane();
                    VBox content = new VBox();
                    content.getChildren().addAll(secondLabel, hbox);
                    secondaryLayout.getChildren().addAll(content);

                    Scene secondScene = new Scene(secondaryLayout, content.getMinWidth(), content.getMinHeight());
                    Stage window = new Stage();
                    window.setTitle("WARNING!");
                    window.setScene(secondScene);
                    window.setX(600);
                    window.setY(300);
                    window.setResizable(false);
                    window.initModality(Modality.WINDOW_MODAL);
                    window.initOwner(border_pane.getScene().getWindow());
                    window.show();
                    yesButton.setOnAction(event1 -> {
                        graph = new ArrayList<>();
                        excValue[0] = typeGraphChoiceBox.getValue().toString();
                        root.getChildren().clear();
                        index = -1;
                        if (excValue[0].equals("Graph isomorphism")) {
                            isDirectedGraphConnection = false;
                        } else {
                            isDirectedGraphConnection = true;
                        }
                        window.close();
                    });
                    noButton.setOnAction(event1 -> {
                        window.close();
                        typeGraphChoiceBox.getSelectionModel().select(excValue[0]);
                        return;
                    });
                } else {
                    excValue[0] = typeGraphChoiceBox.getValue().toString();
                    if (excValue[0].equals("Graph isomorphism")) {
                        isDirectedGraphConnection = false;
                    } else {
                        isDirectedGraphConnection = true;
                    }
                }
            }
        });

    }

    private void colorTest() {
        System.out.println(ANSI_GREEN + "Hello" + ANSI_RESET);
        System.out.println(ANSI_RED + "Hello" + ANSI_RESET);
        System.out.println(ANSI_YELLOW + "Hello" + ANSI_RESET);
        System.out.println(ANSI_BLACK + "Hello" + ANSI_RESET);
        System.out.println(ANSI_BLUE + "Hello" + ANSI_RESET);
        System.out.println(ANSI_CYAN + "Hello" + ANSI_RESET);
        System.out.println(ANSI_PURPLE + "Hello" + ANSI_RESET);
        System.out.println(ANSI_WHITE + "Hello" + ANSI_RESET);
    }

    private Double isWay2(ArrayList<GraphVertex> graph, int start_id, int finish_id) {
        for (GraphVertex vertex : graph) {
            if (vertex.id == start_id) {
                vertex.setWeights(0);
            } else {
                vertex.setWeights(Double.MAX_VALUE);
            }
        }

        ArrayList<GraphVertex> stack = new ArrayList<>();
        stack.add(graph.get(start_id));

        ArrayList<GraphVertex> trash = new ArrayList<>();

        //TODO
        while (true) {
            if (stack.isEmpty()) {
                /*Text text = new Text("Пути не существует!\n");
                text.setFill(Color.web("#299999"));
                textFlow.getChildren().addAll(text);*/
                System.out.println(ANSI_RED + "Пути не существует!" + ANSI_RESET);
                return -1.0;
            }
            if (stack.get(0).id == finish_id) {
                return stack.get(0).getWeights();
            }


            for (GraphConnection connection : stack.get(0).getConnections()) {
                stack.get(0).getCircle().setFill(Color.GREEN);
                connection.getLine().setFill(Color.GREEN);
                if (connection.isDirected()) connection.getPolygon().setFill(Color.GREEN);
               /* try {
                    Thread.currentThread().sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }*/
                System.out.println(stack.get(0).id + " TO " + connection.getTo());
                if (graph.get(connection.getTo()).getWeights() > connection.getWeight() + stack.get(0).getWeights()) {
                    System.out.println(true);
                    graph.get(connection.getTo()).setWeights(connection.getWeight() + stack.get(0).getWeights());
                    if (stack.contains(graph.get(connection.getTo()))) {
                        stack.remove(graph.get(connection.getTo()));
                    }
                    for (int i = 1; i <= stack.size(); ++i) {
                        if (i == stack.size()) {
                            stack.add(graph.get(connection.getTo()));
                            break;
                        } else if (graph.get(connection.getTo()).getWeights() < stack.get(i).getWeights()) {
                            stack.add(i, graph.get(connection.getTo()));
                            break;
                        }
                    }
                }
                connection.getLine().setFill(Color.BLACK);
                if (connection.isDirected()) connection.getPolygon().setFill(Color.BLACK);
            }
            stack.get(0).getCircle().setFill(Color.CYAN);
            trash.add(stack.get(0));
            stack.remove(0);
            System.out.println(stack.size());
        }
    }

    private void getKruskalAlgorithm() {
        // ArrayList<Pair<GraphConnection , GraphConnection>> connections = new ArrayList<>();
        ArrayList<GraphConnection> connections = new ArrayList<>();
        ArrayList<GraphVertex> graphTemp = graph;

        for (GraphVertex vertex : graph) {
            for (GraphConnection connection : vertex.getConnections()) {
                for (int i = 0; i <= connections.size(); ++i) {
                    if (i == connections.size()) {
                        connections.add(connection);
                        break;
                    } else if (connection.getWeight() < connections.get(i).getWeight()) {
                        connections.add(i, connection);
                        break;
                    }
                }
            }
            graphTemp.get(graphTemp.indexOf(vertex)).getConnections().clear();
        }

        for (GraphConnection connection : connections) {
            Double price = isWay2(graphTemp, connection.getFrom(), connection.getTo());
            if (price == -1.0) {
                graphTemp.get(connection.getFrom()).getConnections().add(connection);
                for (GraphConnection graphConnection : graphTemp.get(connection.getTo()).getConnections()) {
                    if (graphConnection.getTo() == connection.getFrom()) {
                        graphTemp.get(connection.getTo()).getConnections().add(graphConnection);
                        break;
                    }
                }
            } else {
                System.out.println("?UUUUUUUUUUUUUU");
                trash.add(connection);
            }
        }

        graph = graphTemp;
    }
}
