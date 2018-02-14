package com.courseproject;

import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Vadim on 15.11.2016.
 */

public class Character extends Pane implements Runnable{
    private static final Logger LOGGER = Logger.getLogger(Doctor.class.getName());

    private static Image image = new Image(Character.class.getResourceAsStream("images\\character.png"));
    private ImageView imageView = new ImageView(image);
    private int count = 4;
    private int columns = 4;
    private int offsetX = 0;
    private int offsetY = 0;
    private int width = 32;
    private int height = 48;
    SpriteAnimation animation;

    int way = 0;
    private int id;

    public Character(int id){
        this.imageView.setViewport(new Rectangle2D(offsetX,offsetY,width,height));
        animation = new SpriteAnimation(imageView,Duration.millis(400),count,columns,offsetX,offsetY,width,height);
        getChildren().addAll(imageView);
        this.id = id;
    }

    private void initLogFile() {
        Handler fileHandler;
        try {
            fileHandler  = new FileHandler("Logs/Character.log");
            LOGGER.addHandler(fileHandler);
            fileHandler.setLevel(Level.ALL);
            LOGGER.setLevel(Level.ALL);
            LOGGER.config("Configuration done.");
            LOGGER.log(Level.FINE, "Finer logged");
        } catch(IOException exception){
            LOGGER.log(Level.SEVERE, "Error occur in FileHandler.", exception);
        }
    }

    private void moveRight(int coordinate)
    {
        while (this.getTranslateX() < coordinate) {
            this.animation.play();//
            this.animation.setOffsetY(96);
            for(int i = 0; i < 2; i++) {
                synchronized (GraphicResources.graphicResources.get(id)) {
                    GraphicResources.graphicResources.get(id).patientsX += 1;
                }
            }

            try {
                Thread.sleep(30);
            } catch (InterruptedException e) {
                LOGGER.log(Level.SEVERE, "Error occur in Characters thread.", e);
            }
        }
        this.animation.stop();//
    }
    private void moveLeft(int coordinate)
    {
        while(this.getTranslateX() > coordinate) {
            this.animation.play();//
            this.animation.setOffsetY(48);
            for(int i = 0; i < 2; i++) {
                synchronized (GraphicResources.graphicResources.get(id)) {
                    GraphicResources.graphicResources.get(id).patientsX -= 1;
                }
            }
            try {
                Thread.sleep(30);
            } catch (InterruptedException e) {
                LOGGER.log(Level.SEVERE, "Error occur in Characters thread.", e);
            }
        }
        this.animation.stop();//
    }
    private void moveUp(int coordinate)
    {
        while(this.getTranslateY() > coordinate) {
            this.animation.play();//
            this.animation.setOffsetY(144);
            for(int i = 0; i < 2; i++) {
                synchronized (GraphicResources.graphicResources.get(id)) {
                    GraphicResources.graphicResources.get(id).patientsY -= 1;
                }
            }
            try {
                Thread.sleep(30);
            } catch (InterruptedException e) {
                LOGGER.log(Level.SEVERE, "Error occur in Characters thread.", e);
            }
        }
        this.animation.stop();//
    }
    private void moveDown(int coordinate)
    {
        while(this.getTranslateY() < coordinate) {
            this.animation.play();//
            this.animation.setOffsetY(0);
            for(int i = 0; i < 2; i++) {
                synchronized (GraphicResources.graphicResources.get(id)) {
                    GraphicResources.graphicResources.get(id).patientsY += 1;
                }
            }
            try {
                Thread.sleep(30);
            } catch (InterruptedException e) {
                LOGGER.log(Level.SEVERE, "Error occur in Characters thread.", e);
            }
        }
        this.animation.stop();
    }

    public void run() {

        initLogFile();

        moveUp(318);

        synchronized (GraphicResources.queueDoctors) {
            GraphicResources.queueDoctors.get(0).offer(this);
        }

        if(!GraphicResources.workDoctors.get(0))
            synchronized (GraphicResources.doctors.get(0)) {
                GraphicResources.doctors.get(0).notify();
            }
        else
            synchronized (GraphicResources.doctors.get(1)) {
                GraphicResources.doctors.get(1).notify();
            }

        synchronized (this) {
            try {
                this.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        moveRight(424);

        if(way == 1) {
            moveUp(160);

            synchronized (GraphicResources.queueDoctors) {
                GraphicResources.queueDoctors.get(1).offer(this);
            }
            if(!GraphicResources.workDoctors.get(1))
                synchronized (GraphicResources.doctors.get(2)) {
                    GraphicResources.doctors.get(2).notify();
                }
            synchronized (this) {
                try {
                    this.wait();
                } catch (InterruptedException e) {
                    LOGGER.log(Level.SEVERE, "Error occur in Characters thread.", e);
                }
            }

            moveDown(600);
        }

        else if(way == 2) {
            moveUp(248);

            moveRight(705);

            synchronized (GraphicResources.queueDoctors) {
                GraphicResources.queueDoctors.get(2).offer(this);
            }
            if(!GraphicResources.workDoctors.get(2))
                synchronized (GraphicResources.doctors.get(3)) {
                    GraphicResources.doctors.get(3).notify();
                }
            synchronized (this) {
                try {
                    this.wait();
                } catch (InterruptedException e) {
                    LOGGER.log(Level.SEVERE, "Error occur in Characters thread.", e);
                }
            }

            moveLeft(426);
        }

        else if(way == 3) {
            moveDown(480);

            moveRight(705);

            synchronized (GraphicResources.queueDoctors) {
                GraphicResources.queueDoctors.get(3).offer(this);
            }
            if(!GraphicResources.workDoctors.get(3))
                synchronized (GraphicResources.doctors.get(4)) {
                    GraphicResources.doctors.get(4).notify();
                }
            synchronized (this) {
                try {
                    this.wait();
                } catch (InterruptedException e) {
                    LOGGER.log(Level.SEVERE, "Error occur in Characters thread.", e);
                }
            }

            moveLeft(426);
        }

        moveDown(630);
    }
}
