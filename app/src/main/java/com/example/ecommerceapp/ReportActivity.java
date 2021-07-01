package com.example.ecommerceapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Cartesian;
import com.example.ecommerceapp.models.Order;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class ReportActivity extends AppCompatActivity {

    TextView uname, norders;
    AnyChartView anyChartView;
    FirebaseFirestore fStore;
    FirebaseAuth fAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        anyChartView = (AnyChartView) findViewById(R.id.any_chart_view);
        uname = findViewById(R.id.uNameTxt);
        norders = findViewById(R.id.ordersTotalTxt);
        fStore = FirebaseFirestore.getInstance();
        fAuth = FirebaseAuth.getInstance();

        get_orders();
    }

    @Override
    protected void onStart() {
        super.onStart();
        get_orders();
    }

    private HashMap<String, Integer> get_orders(){
        HashMap<String, Integer> result = new HashMap<>();
        String uID = fAuth.getCurrentUser().getUid();

        DocumentReference docRef = fStore.collection("user").document(uID);
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                String name = documentSnapshot.get("name").toString();
                uname.setText(name);
            }
        });

        fStore.collection("order")
                .whereEqualTo("uID", uID)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                List<Order> Orders = queryDocumentSnapshots.toObjects(Order.class);
                //Helper.report_orders = order;

                for (Order order : Orders) {
                    String[] date = order.getDate().split("-");
                    String month = date[1];
                    int new_cnt = (result.containsKey(month)? result.get(month)+1 : 1);
                    result.put(month, new_cnt);
                }

                Cartesian pie = AnyChart.column();
                String[] months = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
                int total = 0;

                List<DataEntry> data = new ArrayList<>();
                for (String key: result.keySet()) {
                    int m_ind = Integer.valueOf(key);
                    String month = months[m_ind-1];
                    int cnt = result.get(key);
                    data.add(new ValueDataEntry(month, cnt));
                    total += cnt;
                }

                norders.setText("Total orders: " + String.valueOf(total));
                pie.data(data);
                pie.yAxis(0).labels().format("{%Value}{decimalsCount:0}");
                anyChartView.setChart(pie);
            }
        });

        return result;
    }
}