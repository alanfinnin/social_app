package com.example.week2ex1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ServerValue;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static android.widget.Toast.makeText;
import static com.google.firebase.Timestamp.now;

public class PostActivity extends AppCompatActivity {
    int numberOfPosts = 0;
    //The maximum number of chars a post can have
    private final int maxPostChars = 128;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("posts")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                numberOfPosts++;
                            }
                        } else {
                            Log.w("tag", "Error getting document", task.getException());
                        }
                    }
                });


        final EditText postText = (EditText) findViewById(R.id.post_text);
        final TextView textView = (TextView) findViewById(R.id.textViewPostLength);
        textView.setText("0/" + maxPostChars);

        postText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                int postLength = postText.getText().length();
                textView.setText(postLength + "/" + maxPostChars);
            }
            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    public void sendData(View view){
        //Boolean value of whether post string is valid or not
        boolean validPostString = true;

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        TextView postText = (TextView) findViewById(R.id.post_text);
        String postString = postText.getText().toString();

        if(postString.length() > maxPostChars || postString.trim().equals(""))
            validPostString = false;

        Context context = getApplicationContext();
        CharSequence text = "Post failed!";
        int duration = Toast.LENGTH_SHORT;

        Toast postToast = Toast.makeText(context, text, duration);

        if(validPostString) {
            FirebaseFirestore db = FirebaseFirestore.getInstance();

            CollectionReference posts = db.collection("posts");
            Timestamp postTime = now();
            System.out.println(ServerValue.TIMESTAMP.toString());

            Map<String, Object> post = new HashMap<>();
            post.put("username", user.getDisplayName());
            post.put("post", postString);
            post.put("time", postTime);

            String postNumber = String.valueOf(numberOfPosts);
            posts.document(postNumber).set(post);

            postToast.setText("Successfully posted");
            postToast.show();

            Intent intent = new Intent(this, LoggedIn.class);
            startActivity(intent);
        }else{
            postToast.setText("Post failed - too long!");
            postToast.show();
        }
    }


}