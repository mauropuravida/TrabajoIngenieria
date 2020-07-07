package com.example.healthsense.ui.device;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;
import com.example.healthsense.R;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import org.json.JSONObject;


/*
Clase que implementa una grafica con el fin de graficar los datos obtenidos de el sensor de ritmo cardiaco
 */
public class Grafica extends Fragment {
    private LineChart lineChart;
    private LineDataSet lineDataSet;
    public static ArrayList<JSONObject> arrayPulso=new ArrayList<JSONObject>();

     public View onCreateView(@NonNull LayoutInflater inflater,
                      ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.grafica, container, false);

            //grafica
            lineChart = root.findViewById(R.id.lineChart);
            // Creamos un set de datos
            ArrayList<Entry> lineEntries = new ArrayList<Entry>();
            for (int i = 0; i < 11; i++) {
                float y = (int) (Math.random() * 8) + 1;
                lineEntries.add(new Entry((float) i, (float) y));
            }
            // Unimos los datos al data set
            lineDataSet = new LineDataSet(lineEntries, "grafica");
            // Asociamos al gráfico
            LineData lineData = new LineData();
            lineData.addDataSet(lineDataSet);
            lineChart.setData(lineData);


            //grafico de pasos de la caminadora

            BarChart barChart = root.findViewById(R.id.BarChart);
            ArrayList<BarEntry> lineEntries2 = new ArrayList<>();

            lineEntries2.add(new BarEntry(1f, 0));
            lineEntries2.add(new BarEntry(2f, 500));
            lineEntries2.add(new BarEntry(3f, 0));
            lineEntries2.add(new BarEntry(4f,670));
            lineEntries2.add(new BarEntry(5f,0));
            lineEntries2.add(new BarEntry(6f,490));
            lineEntries2.add(new BarEntry(7f,0));

            // Unimos los datos al data set
            BarDataSet datos= new BarDataSet(lineEntries2, "Grafico de pasos");

            // Asociamos al gráfico
            BarData data = new BarData(datos);

            //separacion entre barras
            data.setBarWidth(0.9f);

            //PONE BARRAS CENTRADAS
            barChart.setFitBars(true);

            data.addDataSet(datos);
            barChart.setData(data);

            barChart.invalidate();

            return root;
        }
}