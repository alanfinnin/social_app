package com.example.week2ex1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;

public class LoggedIn extends AppCompatActivity {

    int numberOfPosts = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logged_in);
        display();
    }
    public void post(View view){
        Intent postIntent = new Intent(this, PostActivity.class);
        startActivity(postIntent);
    }
    public void display(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        final ArrayList<String> postList = new ArrayList<>();
        //Get database posts
        db.collection("posts").orderBy("time")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>(){
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task){
                        if(task.isSuccessful()){
                            for(QueryDocumentSnapshot document : task.getResult()){
                                numberOfPosts++;
                                //System.out.println(document.getId());
                                //System.out.println(document.getData());
                                postList.add("\n" + document.get("username") + "\n" + document.get("post") + "\n");
                                //TextView info = (TextView) findViewById(R.id.info);
                                //info.append("\n" + document.get("username") + "\n" + document.get("post") + "\n");
                            }
                        }else{
                            Log.w("tag", "Error getting document", task.getException());
                        }

                        TextView info = (TextView) findViewById(R.id.info);

                        String concat = "";
                        Collections.reverse(postList);
                        //System.out.println("First call");
                        for(int i = 0; i < postList.size(); i++){
                            if(i > 4)
                                break;

                            concat += postList.get(i);
                            //System.out.println(postList.get(i));

                        }
                        info.setText(concat);
                    }
                });
    }
}