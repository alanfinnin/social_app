package com.example.week2ex1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class LoggedIn extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logged_in);
    }
    public void sendData(View view){
            FirebaseFirestore db = FirebaseFirestore.getInstance();

            CollectionReference drinks = db.collection("drinks");

            Map<String, Object> drink1 = new HashMap<>();
            drink1.put("name","Espresso");
            drink1.put("description", "It's coffee");
            drink1.put("price",1.95);
            drinks.document("espresso").set(drink1);

            Map<String, Object> drink2 = new HashMap<>();
            drink2.put("name","Latte");
            drink2.put("description", "It's coffee");
            drink2.put("price",2.85);
            drinks.document("latte").set(drink2);

            Map<String, Object> drink3 = new HashMap<>();
            drink3.put("name","Cappaccino");
            drink3.put("description", "It's coffee");
            drink3.put("price",2.95);
            drinks.document("cappaccino").set(drink3);

            Map<String, Object> drink4 = new HashMap<>();
            drink4.put("name","Americano");
            drink4.put("description", "It's coffee");
            drink4.put("price",2.20);
            drinks.document("americano").set(drink4);

    }

    public void getData(View view){
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("drinks")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>(){
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task){
                        if(task.isSuccessful()){
                            for(QueryDocumentSnapshot document : task.getResult()){
                                System.out.println(document.getId());
                                System.out.println(document.getData());

                                TextView info = (TextView) findViewById(R.id.info);
                                info.append("\n" + document.getData() + "\n");
                            }
                        }else{
                            Log.w("tag", "Error getting document", task.getException());
                        }
                    }
                });
    }
}
