package com.courseproject;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;
import java.lang.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * Created by Vadim on 15.11.2016.
 */
public class Main extends Application implements Runnable{
    private static Main applicationWindow;
    private static final Logger LOGGER = Logger.getLogger(Main.class.getName());

    private volatile boolean doProgram = false;
    private static Pane root = new Pane();

    private List<ImageView> ivHomes;

    private int patientsCount = 0;

    private List<Boolean> workingDoctors;
    private List<Thread> doctorsThreads = new ArrayList<>();
    private List<Thread> patientsThreads = new ArrayList<>();
    public Thread mainThread;

    private Label sizeQueue1;
    private Label sizeQueue2;
    private Label sizeQueue3;
    private Label sizeQueue4;


    public static void main(String[] args) {
        applicationWindow = new Main();
        launch(args);
    }

    void initLogFile() {
        Handler fileHandler;
        try {
            fileHandler  = new FileHandler("Logs/Main.log");
            LOGGER.addHandler(fileHandler);
            fileHandler.setLevel(Level.ALL);
            LOGGER.setLevel(Level.ALL);
            LOGGER.config("Configuration done.");
            LOGGER.log(Level.FINE, "Finer logged");
        } catch(IOException exception){
            LOGGER.log(Level.SEVERE, "Error occur in FileHandler.", exception);
        }
    }

    public void start(Stage primaryStage) {

        initLogFile();

        root.setPrefSize(900, 600);

        MenuBar menuBar = new MenuBar();
        Menu program = new Menu("Program");
        final MenuItem itemStart = new MenuItem("Start");
        menuBar.setStyle("-fx-base: white");
        menuBar.prefWidthProperty().bind(primaryStage.widthProperty());
        program.getItems().addAll(itemStart);
        menuBar.getMenus().addAll(program);
        root.getChildren().add(menuBar);

        Image image = new Image(getClass().getResourceAsStream("images\\background.png"));
        ImageView img = new ImageView(image);
        img.setFitHeight(600);
        img.setFitWidth(900);
        img.setTranslateY(27);
        root.getChildren().add(img);

        Scene scene = new Scene(root, 890, 615);
        scene.setFill(Color.rgb(240, 240, 240));

        primaryStage.setResizable(false);

        primaryStage.setTitle("Course project");
        primaryStage.setScene(scene);
        primaryStage.show();

        ivHomes = new ArrayList<>();
        ivHomes.add(new ImageView(new Image(Character.class.getResourceAsStream("images\\house1.png"))));
        ivHomes.add(new ImageView(new Image(Character.class.getResourceAsStream("images\\house2.png"))));
        ivHomes.add(new ImageView(new Image(Character.class.getResourceAsStream("images\\house3.png"))));
        ivHomes.add(new ImageView(new Image(Character.class.getResourceAsStream("images\\house4.png"))));

        ivHomes.get(0).setTranslateX(109);
        ivHomes.get(0).setTranslateY(180);
        ivHomes.get(0).setViewport(new Rectangle2D(128,0,128,162));
        root.getChildren().add(ivHomes.get(0));

        ivHomes.get(1).setTranslateX(391);
        ivHomes.get(1).setTranslateY(27);
        ivHomes.get(1).setViewport(new Rectangle2D(128,0,128,162));
        root.getChildren().add(ivHomes.get(1));

        ivHomes.get(2).setTranslateX(672);
        ivHomes.get(2).setTranslateY(108);
        ivHomes.get(2).setViewport(new Rectangle2D(128,0,128,162));
        root.getChildren().add(ivHomes.get(2));

        ivHomes.get(3).setTranslateX(671);
        ivHomes.get(3).setTranslateY(342);
        ivHomes.get(3).setViewport(new Rectangle2D(128,0,128,162));
        root.getChildren().add(ivHomes.get(3));

        GraphicResources.initialization();

        workingDoctors = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            workingDoctors.add(false);
        }

        sizeQueue1 = new Label("0");
        sizeQueue2 = new Label("0");
        sizeQueue3 = new Label("0");
        sizeQueue4 = new Label("0");

        sizeQueue1.setTranslateX(176);
        sizeQueue1.setTranslateY(372);
        sizeQueue1.setFont(new Font("Arial", 30));
        sizeQueue1.setStyle("-fx-padding: 1 10 0 10;"
                + "-fx-border-color: rgb(0, 0, 0);"
                + "-fx-background-color: rgb(207, 150, 76);"
                + "-fx-background-radius: 10;"
                + "-fx-border-radius: 10;");
        root.getChildren().add(sizeQueue1);

        sizeQueue2.setTranslateX(459);
        sizeQueue2.setTranslateY(188);
        sizeQueue2.setFont(new Font("Arial", 30));
        sizeQueue2.setStyle("-fx-padding: 1 10 0 10;"
                + "-fx-border-color: rgb(0, 0, 0);"
                + "-fx-background-color: rgb(207, 150, 76);"
                + "-fx-background-radius: 10;"
                + "-fx-border-radius: 10;");
        root.getChildren().add(sizeQueue2);

        sizeQueue3.setTranslateX(742);
        sizeQueue3.setTranslateY(271);
        sizeQueue3.setFont(new Font("Arial", 30));
        sizeQueue3.setStyle("-fx-padding: 1 10 0 10;"
                + "-fx-border-color: rgb(0, 0, 0);"
                + "-fx-background-color: rgb(207, 150, 76);"
                + "-fx-background-radius: 10;"
                + "-fx-border-radius: 10;");
        root.getChildren().add(sizeQueue3);

        sizeQueue4.setTranslateX(742);
        sizeQueue4.setTranslateY(506);
        sizeQueue4.setFont(new Font("Arial", 30));
        sizeQueue4.setStyle("-fx-padding: 1 10 0 10;"
                + "-fx-border-color: rgb(0, 0, 0);"
                + "-fx-background-color: rgb(207, 150, 76);"
                + "-fx-background-radius: 10;"
                + "-fx-border-radius: 10;");
        root.getChildren().add(sizeQueue4);

        itemStart.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent actionEvent) {
                if (!doProgram) {
                    applicationWindow.run();
                    doProgram = true;
                    animationTimer.start();
                }
            }
        });

        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent t) {
                Platform.exit();
                System.exit(0);
            }
        });
    }

    private AnimationTimer animationTimer = new AnimationTimer(){
        @Override
        public void handle(long now) {
            synchronized (GraphicResources.workDoctors) {
                sizeQueue1.setText(Integer.toString(GraphicResources.queueDoctors.get(0).size()));
                sizeQueue2.setText(Integer.toString(GraphicResources.queueDoctors.get(1).size()));
                sizeQueue3.setText(Integer.toString(GraphicResources.queueDoctors.get(2).size()));
                sizeQueue4.setText(Integer.toString(GraphicResources.queueDoctors.get(3).size()));
            }

            synchronized (GraphicResources.graphicResources) {
                int i = 0;
                if(GraphicResources.graphicResources.size() > 0) {
                    for (GraphicResources g : GraphicResources.graphicResources) {
                        GraphicResources.patients.get(i).setTranslateX(g.patientsX);
                        GraphicResources.patients.get(i).setTranslateY(g.patientsY);
                        i++;
                    }
                }
            }

            synchronized (GraphicResources.workDoctors) {
                boolean temp;
                for (int j = 0; j < 4; j++) {
                    temp = GraphicResources.workDoctors.get(j);
                    if (temp && !workingDoctors.get(j))
                        ivHomes.get(j).setViewport(new Rectangle2D(0, 0, 128, 162));
                    else if(!temp && workingDoctors.get(j))
                        ivHomes.get(j).setViewport(new Rectangle2D(128, 0, 128, 162));
                    workingDoctors.set(j, temp);
                }
            }
        }
    };

    @Override
    public void run() {
        for (int i = 0; i < 5; i++) {
            doctorsThreads.add(new Thread(GraphicResources.doctors.get(i)));
            doctorsThreads.get(i).start();
        }

        mainThread = new Thread(taskAddNewPerson);
        mainThread.start();
    }

    private Task taskAddNewPerson = new Task() {
        @Override protected Void call() {
            while(true) {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        GraphicResources.createNewPatient();
                        GraphicResources.patients.get(GraphicResources.patientsCount - 1).setTranslateX(GraphicResources
                                .graphicResources.get(GraphicResources.patientsCount - 1).patientsX);
                        GraphicResources.patients.get(GraphicResources.patientsCount - 1).setTranslateY(GraphicResources
                                .graphicResources.get(GraphicResources.patientsCount - 1).patientsY);
                        root.getChildren().add(GraphicResources.patients.get(GraphicResources.patientsCount - 1));

                        synchronized (GraphicResources.patients){
                            patientsThreads.add(new Thread(GraphicResources.patients.get(patientsCount)));
                        }
                        patientsThreads.get(patientsCount).
                        start();
                        patientsCount++;
                        LOGGER.log(Level.FINE,"New process created! New patient is going to the doctor!");
                    }
                });

                Random random = new Random();
                int sleepTime = random.nextInt(3000) + 1000;
                try {
                    Thread.sleep(sleepTime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    };
}