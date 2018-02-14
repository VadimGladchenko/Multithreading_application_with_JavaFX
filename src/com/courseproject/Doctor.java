package com.courseproject;


import java.io.IOException;
import java.lang.*;
import java.util.*;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Vadim on 18.11.2016.
 */

public class Doctor implements Runnable{
    private static final Logger LOGGER = Logger.getLogger(Doctor.class.getName());

    private int houseId;
    private int doctorId;

    public Doctor(int houseId, int doctorId) {
        this.houseId = houseId;
        this.doctorId = doctorId;
    }

    private void initLogFile() {
        Handler fileHandler;
        try {
            fileHandler  = new FileHandler("Logs/Doctor.log");
            LOGGER.addHandler(fileHandler);
            fileHandler.setLevel(Level.ALL);
            LOGGER.setLevel(Level.ALL);
            LOGGER.config("Configuration done.");
            LOGGER.log(Level.FINE, "Finer logged");
        } catch(IOException exception){
            LOGGER.log(Level.SEVERE, "Error occur in FileHandler.", exception);
        }
    }

    @Override
    public void run() {

        initLogFile();

        Random random = new Random();
        Character currentPatient;
        while (true) {
            if (!GraphicResources.queueDoctors.get(houseId).isEmpty()) {

                synchronized (GraphicResources.queueDoctors) {
                    currentPatient = GraphicResources.queueDoctors.get(houseId).poll();
                }

                LOGGER.log(Level.FINE, "Doctor " + doctorId + " awoke");

                synchronized (GraphicResources.workDoctors) {
                    GraphicResources.workDoctors.set(houseId, true);
                }

                try {
                    if (doctorId > 2)
                        Thread.sleep(2000 + random.nextInt(13000));
                    else
                        Thread.sleep(2000 + random.nextInt(8000));
                } catch (InterruptedException e) {
                    LOGGER.log(Level.SEVERE, "Error occur in Doctor thread.", e);
                }

                synchronized (currentPatient) {
                    currentPatient.way = random.nextInt(3) + 1;
                    currentPatient.notify();
                }
            } else {
                LOGGER.log(Level.FINE, "Doctor " + doctorId + " sleep");
                if (doctorId != 2) {
                    synchronized (GraphicResources.workDoctors) {
                        GraphicResources.workDoctors.set(houseId, false);
                    }
                }
                synchronized (this) {
                    try {
                        this.wait();
                    } catch (InterruptedException e) {
                        LOGGER.log(Level.SEVERE, "Error occur in Doctor thread.", e);
                    }
                }
            }
        }
    }
}
