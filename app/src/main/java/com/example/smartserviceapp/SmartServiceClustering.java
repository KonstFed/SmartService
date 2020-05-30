package com.example.smartserviceapp;

import android.os.Debug;
import android.util.Log;

import java.util.ArrayList;

public class SmartServiceClustering {
    ArrayList<InfoPrecedent> precedents;
    double threshold;
    int amountCluster;
    public SmartServiceClustering(double threshold) {
        precedents = new ArrayList<>();
        amountCluster = 0;
        this.threshold = threshold;
    }
    public boolean isNeedAction(InfoPrecedent precedent)
    {
        for (int i = 0; i < precedents.size(); i++) {
            
        }
    }
    public void addPrecedent(InfoPrecedent precedent)
    {
        precedents.add(precedent);
    }
    public void run(Metric metric)
    {

        for (int i = 0; i < precedents.size() - 1; i++) {
            for (int j = i + 1; j < precedents.size(); j++) {
                InfoPrecedent a = precedents.get(i);
                InfoPrecedent b = precedents.get(j);
                if (metric.getDist(a,b)<threshold)
                {
                    if (a.hostCluster==null)
                    {
                        if (b.hostCluster==null)
                        {
                            amountCluster++;
                            Cluster tmp = new Cluster(amountCluster);
                            tmp.addPrecedent(a);
                            tmp.addPrecedent(b);
//                            clusters.add(tmp);
                        }
                        else
                        {
                            b.hostCluster.addPrecedent(a);
                        }
                    }
                    else
                    {
                        if (b.hostCluster==null)
                        {
                            a.hostCluster.addPrecedent(b);

                        }
                        else
                        {
                            if (a.hostCluster.id != b.hostCluster.id) {
                                uniteClusters(a.hostCluster, b.hostCluster);
                            }
                        }
                    }
                }
            }
        }
        Log.d("fds","fds");
    }
    private void uniteClusters(Cluster a, Cluster b)
    {
        for (int i = 0; i < b.vectors.size(); i++) {
            a.addPrecedent(b.vectors.get(i));
        }
        b.vectors = new ArrayList<>();
    }
}
