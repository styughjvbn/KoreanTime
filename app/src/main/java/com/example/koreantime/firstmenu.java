package com.example.koreantime;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.fonts.Font;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.koreantime.DTO.DTO_user;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class firstmenu extends AppCompatActivity {

    DTO_user user_info;
    FirebaseFirestore db= FirebaseFirestore.getInstance();
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {//그룹만들고서 그룹리스트 업데이트
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case 0:
            {
                if(resultCode==1){
                    String searchemail=user_info.getEmail();
                    db.collection("group").whereArrayContains("participation", searchemail)
                            .get()
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful()) {
                                        for (QueryDocumentSnapshot document : task.getResult()) {//유저가 소속되어있는 그룹들 리턴
                                            Log.d("added_group", document.getId() + " => " + document.getData());
                                        }
                                    } else {
                                        Log.d("added_group", "Error getting documents: ", task.getException());
                                    }
                                }
                            });
                }
            }
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firstmenu);

        Intent Intent = getIntent();
        user_info=(DTO_user) Intent.getSerializableExtra("user_info");
        LinearLayout edit = findViewById(R.id.edit);
        LinearLayout makeGroup = findViewById(R.id.makeGroup);
        TextView name = findViewById(R.id.name);
        GridLayout grid = findViewById(R.id.grid);
        Intent fcm = new Intent(getApplicationContext(), MyFirebaseMessaging.class);
        startService(fcm);

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(firstmenu.this, GroupMaking.class);
//                startActivity(intent);
                createNew();
            }
        });

        makeGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(firstmenu.this, "Asdf", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(firstmenu.this, GroupMaking.class);
                intent.putExtra("user_info", user_info);
                startActivityForResult(intent,0);
            }
        });
    }

    private void createNew(){
        GridLayout grid = findViewById(R.id.grid);

        LinearLayout group = new LinearLayout(this);
        group.setBackground(Drawable.createFromPath("@drawable/frame_shadow"));
        group.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams groupParams = new LinearLayout.LayoutParams(160, 160,Gravity.CENTER);
        groupParams.setMargins(10,10,10,10);
        group.setGravity(Gravity.FILL);
        group.setLayoutParams(groupParams);
        group.setId(0);

        TextView view1 = new TextView(this);
        view1.setText("그룹명");
        view1.setTextColor(Color.parseColor("#42C2FF"));
        view1.setGravity(Gravity.CENTER);
        view1.setPadding(0,10,0,0);
        view1.setTextSize(18);
        view1.setTypeface(Typeface.createFromFile("@font/cafe"), Typeface.BOLD);

        ImageView view2 = new ImageView(this);
        LinearLayout.LayoutParams ImgParams = new LinearLayout.LayoutParams(160, 160);
        ImgParams.setMargins(0,15,0,0);
        view2.setLayoutParams(ImgParams);
        view2.setImageResource(R.drawable.user);


        group.addView(view1);
        group.addView(view2);

        grid.addView(group);
    }
}