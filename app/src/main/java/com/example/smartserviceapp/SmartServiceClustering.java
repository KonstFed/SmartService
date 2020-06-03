package com.example.smartserviceapp;

import android.os.Debug;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class SmartServiceClustering {
    ArrayList<InfoPrecedent> precedents;
    ArrayList<ArrayList<InfoPrecedent>> distMatrix;
    Boolean trained = false;
    Double thresh;
    int amountCluster;
    public SmartServiceClustering() {
        precedents = new ArrayList<>();
        amountCluster = 0;
    }
    public SmartServiceClustering(ArrayList<InfoPrecedent> precedents) {
        this.precedents = precedents;
        amountCluster = 0;
    }
//    public void addPrecedent(InfoPrecedent precedent)
//    {
//        precedents.add(precedent);
//    }
    public boolean run(Metric metric)
    {
        Double[][] distMatrix = new Double[precedents.size()][precedents.size()];
        ArrayList<Double> distances = new ArrayList<>();
        boolean yes_prec = false;
        boolean no_prec = false;
        for (int i = 0; i < precedents.size()-1; i++) {
            distMatrix[i][i] = 0.0;
            for (int j = i+1; j < precedents.size() ; j++) {
                double curdist = metric.getDist(precedents.get(i),precedents.get(j));
                distances.add(curdist);
                distMatrix[i][j] = curdist;
                distMatrix[j][i] = curdist;
            }
            if (precedents.get(i).label.equals("ok"))
            {
                yes_prec = true;
            }
            else if (precedents.get(i).label.equals("no"))
            {
                no_prec = true;
            }
        }
        if (!yes_prec || !no_prec)
        {
            return false;
        }

        Collections.sort(distances);
        boolean needb = false;
        for (int i = 0; i < distances.size(); i++) {
            thresh = distances.get(i);
            for (int j = 0; j < precedents.size()-1; j++) {
                for (int k = j+1; k < precedents.size(); k++) {
                    if (distMatrix[j][k] <thresh )
                    {
                        if (!precedents.get(k).label.equals(precedents.get(j).label)) {
                            thresh = distances.get(i - 1);
                            needb = true;
                            break;
                        }
                    }
                }
                if (needb)
                {
                    break;
                }
            }
            if (needb)
            {
                break;
            }
        }
        for (int i = 0; i < precedents.size() - 1; i++) {
            InfoPrecedent a = precedents.get(i);
            InfoPrecedent minPreced = null;
            for (int j = i + 1; j < precedents.size(); j++) {
                InfoPrecedent b = precedents.get(j);

                if (a.label == b.label)
                {
                    continue;
                }
                Double curDist = metric.getDist(a,b);
                if (curDist<thresh)
                {
                    if (minPreced == null || metric.getDist(a,minPreced) > curDist)
                    {
                        minPreced = b;
                    }
                }
            }
            if (minPreced == null)
            {
                continue;
            }
            if (a.hostCluster==null)
            {
                if (minPreced.hostCluster==null)
                {
                    Cluster tmp = new Cluster(amountCluster,a.label);
                    tmp.addPrecedent(a);
                    tmp.addPrecedent(minPreced);
                    amountCluster++;
//                            clusters.add(tmp);
                }
                else
                {
                    minPreced.hostCluster.addPrecedent(a);
                }
            }
            else
            {
                if (minPreced.hostCluster==null)
                {
                    a.hostCluster.addPrecedent(minPreced);

                }
                else
                {
                    if (a.hostCluster.id != minPreced.hostCluster.id) {
                        uniteClusters(a.hostCluster, minPreced.hostCluster);
                    }
                }
            }
        }
        for (int i = 0; i < precedents.size(); i++) {
            InfoPrecedent t = precedents.get(i);
            if (t.hostCluster == null)
            {
                Cluster tmp = new Cluster(amountCluster,t.label);
                tmp.addPrecedent(t);
                amountCluster++;
            }
        }
        Log.d("fds","fds");
        trained = true;
        return true;

    }
    private void uniteClusters(Cluster a, Cluster b)
    {
        for (int i = 0; i < b.vectors.size(); i++) {
            a.addPrecedent(b.vectors.get(i));
        }
        b.vectors = new ArrayList<>();
    }
}
