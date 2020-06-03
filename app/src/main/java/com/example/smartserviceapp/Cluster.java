package com.example.smartserviceapp;

import java.util.ArrayList;

public class Cluster {
    int id;
    String label;
    ArrayList<InfoPrecedent> vectors;
    public Cluster(int id, String label) {
        this.id = id;
        this.label = label;
        vectors = new ArrayList<>();
    }
    public void addPrecedent(InfoPrecedent a)
    {
        a.hostCluster = this;
        vectors.add(a);
    }
}
