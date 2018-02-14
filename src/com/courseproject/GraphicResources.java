package com.courseproject;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * Created by Vadim on 28.11.2016.
 */
public class GraphicResources {

    static public List<Doctor> doctors = new ArrayList<>();
    static public List<GraphicResources> graphicResources = new ArrayList<>();
    static public List<Character> patients = new ArrayList<>();
    static public List<Boolean> workDoctors = new ArrayList<>();
    static public List<Queue<Character>> queueDoctors = new ArrayList<>();
    static public int patientsCount = 0;

    public int patientsX;
    public int patientsY;

    public GraphicResources(int x, int y)
    {
        this.patientsX = x;
        this.patientsY = y;
    }

    public static void initialization()
    {
        for (int i = 0; i < 4; i++) {
            workDoctors.add(false);
            Queue<com.courseproject.Character> queue = new LinkedList<>();
            queueDoctors.add(queue);
        }

        for (int i = 0; i < 5; i++) {
            if(i == 0)
                doctors.add(new Doctor(0, i+1));
            else
                doctors.add(new Doctor(i-1, i + 1));
        }
    }

    public static void createNewPatient() {
        graphicResources.add(new GraphicResources(143, 630));
        patients.add(new Character(patientsCount++));
    }
}
