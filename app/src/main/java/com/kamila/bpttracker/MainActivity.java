package com.kamila.bpttracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {


    Double tempWeek,tempDaily;
    int countWeek,countDaily;
    TextView tvBtWeek, tvBtDaily,tvBpWeek, tvBpDaily;
    String currentDate;
    DateFormat df = new SimpleDateFormat("dd/MM/yyyy");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvBtWeek = (TextView) findViewById(R.id.tvBpWeek);
        tvBtDaily = (TextView) findViewById(R.id.tvBpDaily);
        tvBpWeek = (TextView) findViewById(R.id.tvBpWeek);
        tvBpDaily = (TextView) findViewById(R.id.tvBpDaily);

        tempWeek = 0.0;
        tempDaily = 0.0;
        countDaily = 0;
        countWeek = 0;


        FirebaseApp.initializeApp(this);

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("kamilatferreira@gmail.com").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        System.out.println("KKKKKKKKKKKKK" + document.getId());
                        System.out.println("firestore" + document.getData());
                        String docDate = (String) document.get("date");
                        currentDate = df.format(new Date());
                        if (currentDate.equals(docDate)) {
                            countDaily++;
                            tempDaily = tempDaily + Double.parseDouble((String) document.get("temperature"));
                        }
                        tempWeek = tempWeek + Double.parseDouble((String) document.get("temperature"));
                        countWeek++;
                    }
                    tvBtWeek.setText(String.valueOf(tempWeek / countWeek));
                    tvBtDaily.setText(String.valueOf(tempDaily / countDaily));
                } else {
                    System.out.println("error" + task.getException());
                }

            }
        });
    }
}