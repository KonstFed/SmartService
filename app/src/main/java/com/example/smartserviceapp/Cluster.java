package com.example.smartserviceapp;

import java.util.ArrayList;

public class Cluster {
    int id;
    ArrayList<InfoPrecedent> vectors;
    public Cluster(int id) {
        this.id = id;
        vectors = new ArrayList<>();
    }
    public void addPrecedent(InfoPrecedent a)
    {
        a.hostCluster = this;
        vectors.add(a);
    }
}
