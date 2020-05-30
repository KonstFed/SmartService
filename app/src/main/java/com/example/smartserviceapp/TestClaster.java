package com.example.smartserviceapp;

import android.util.Log;

public class TestClaster {
    public static void main()
    {
        InfoPrecedent precedent1 = new InfoPrecedent(10,1000,45,57.1,45);
        InfoPrecedent precedent2 = new InfoPrecedent(1.5,56.8,44.9,57.2,45.11);
        InfoPrecedent precedent3 = new InfoPrecedent(1.3,56.65,44.999,57.213,45.234);

        SmartServiceClustering smartServiceClustering = new SmartServiceClustering(15.0);
        smartServiceClustering.addPrecedent(precedent1);
        smartServiceClustering.addPrecedent(precedent2);
        smartServiceClustering.addPrecedent(precedent3);

        TimeMetric timeMetric = new TimeMetric();

        smartServiceClustering.run(timeMetric);
        for (int i = 0; i < smartServiceClustering.precedents.size(); i++) {
            if (smartServiceClustering.precedents.get(i).hostCluster!=null) {
                Log.d("meow", Integer.toString(smartServiceClustering.precedents.get(i).hostCluster.id));
            }
        }
    }
}
