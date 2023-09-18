package com.example.ds;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
import javafx.scene.layout.VBox;


public class HelloApplication extends Application {
    private List<Shape> shapes = new ArrayList<>(); // 그린 도형들을 저장하는 리스트
    private Shape currentShape; // 현재 그리고 있는 도형
    private GraphicsContext gc; // GraphicsContext를 멤버 변수로 선언

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Draw Shapes");

        Canvas canvas = new Canvas(400, 400);
        gc = canvas.getGraphicsContext2D();

        Button drawCircleButton = new Button("원 그리기");
        Button drawRectangleButton = new Button("사각형 그리기");
        Button drawTriangleButton = new Button("삼각형 그리기");

        drawCircleButton.setOnAction(e -> currentShape = new Circle());
        drawRectangleButton.setOnAction(e -> currentShape = new Rectangle());
        drawTriangleButton.setOnAction(e -> currentShape = new Triangle());

        canvas.setOnMousePressed(this::handleMousePressed);
        canvas.setOnMouseDragged(this::handleMouseDragged);
        canvas.setOnMouseReleased(this::handleMouseReleased);

        Pane pane = new Pane(canvas);
        VBox buttonBox = new VBox(10); // 버튼을 담을 VBox
        buttonBox.getChildren().addAll(drawCircleButton, drawRectangleButton, drawTriangleButton);

        // 버튼과 캔버스를 포함한 전체 레이아웃을 생성
        VBox root = new VBox(buttonBox, pane);

        Scene scene = new Scene(root, 400, 400);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void handleMousePressed(MouseEvent event) {
        if (currentShape != null) {
            currentShape.startX = event.getX();
            currentShape.startY = event.getY();
        }
    }

    private void handleMouseDragged(MouseEvent event) {
        if (currentShape != null) {
            currentShape.endX = event.getX();
            currentShape.endY = event.getY();
            redraw();
        }
    }

    private void handleMouseReleased(MouseEvent event) {
        if (currentShape != null) {
            shapes.add(currentShape);
            currentShape = null;
            redraw();
        }
    }

    private void redraw() {
        gc.clearRect(0, 0, gc.getCanvas().getWidth(), gc.getCanvas().getHeight());
        for (Shape shape : shapes) {
            shape.draw(gc);
        }
        if (currentShape != null) {
            currentShape.draw(gc);
        }
    }

    // 도형을 나타내는 클래스
    abstract class Shape {
        double startX, startY, endX, endY;

        abstract void draw(GraphicsContext gc);
    }

    class Circle extends Shape {
        @Override
        void draw(GraphicsContext gc) {
            double radius = Math.sqrt(Math.pow(endX - startX, 2) + Math.pow(endY - startY, 2));
            gc.setFill(Color.RED);
            gc.fillOval(startX - radius, startY - radius, 2 * radius, 2 * radius);
        }
    }

    class Rectangle extends Shape {
        @Override
        void draw(GraphicsContext gc) {
            gc.setFill(Color.BLUE);
            gc.fillRect(startX, startY, endX - startX, endY - startY);
        }
    }

    class Triangle extends Shape {
        @Override
        void draw(GraphicsContext gc) {
            gc.setFill(Color.GREEN);
            gc.fillPolygon(new double[]{startX, endX, (startX + endX) / 2},
                    new double[]{endY, endY, startY}, 3);
        }
    }
}
