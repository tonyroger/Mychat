package com.anthony.mychat;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.mychat.R;

import java.util.HashMap;
import java.util.Map;

public class chat extends AppCompatActivity {
    LinearLayout layout;
    ImageView sendButton;
    EditText messageArea;
    ScrollView scrollView;
    Firebase reference1, reference2;
    private TextView camera;
    private TextView Location;
    private TextView Text2speech;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        layout = findViewById(R.id.layout1);
        sendButton = findViewById(R.id.sendButton);
        messageArea = findViewById(R.id.messageArea);
        scrollView = findViewById(R.id.scrollView);
        camera =  findViewById(R.id.camera);
        Location =  findViewById(R.id.text_Location);
        Text2speech=  findViewById(R.id.Text2speech);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        Firebase.setAndroidContext(this);
        reference1 = new Firebase("https://mychat-50fec.firebaseio.com/messages/" + userDetail.username + "_" + userDetail.chatWith);
        reference2 = new Firebase("https://mychat-50fec.firebaseio.com/messages/" + userDetail.chatWith + "_" + userDetail.username);

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String messageText = messageArea.getText().toString();

                if(!messageText.equals("")){
                    Map<String, String> map = new HashMap<>();
                    map.put("message", messageText);
                    map.put("user", userDetail.username);

                    reference1.push().setValue(map);
                    reference2.push().setValue(map);
                }
            }
        });

        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.action_camera:
                                camera.setVisibility(View.VISIBLE);
                                Location.setVisibility(View.GONE);
                                Text2speech.setVisibility(View.GONE);

                            case R.id.action_Location:
                                startActivity(new Intent(chat.this, mylocation.class));
                                break;


                            case R.id.action_Text2speech:
                                camera.setVisibility(View.GONE);
                                Location.setVisibility(View.GONE);
                                startActivity(new Intent(chat.this, textReader.class));

                        }
                        return true;
                    }
                });

        reference1.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Map map = dataSnapshot.getValue(Map.class);
                String message = map.get("message").toString();
                String userName = map.get("user").toString();

                if(userName.equals(userDetail.username)){
                    addMessageBox("You:-\n" + message, 1);
                }
                else{
                    addMessageBox(userDetail.chatWith + ":-\n" + message, 2);
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    public void addMessageBox(String message, int type){
        TextView textView = new TextView(chat.this);
        textView.setText(message);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.setMargins(0, 0, 0, 10);
        textView.setLayoutParams(lp);

        if(type == 1) {
            textView.setBackgroundResource(R.drawable.bubble_in);
        }
        else{
            textView.setBackgroundResource(R.drawable.bubble_out);
        }

        layout.addView(textView);
        scrollView.fullScroll(View.FOCUS_DOWN);




    }
}