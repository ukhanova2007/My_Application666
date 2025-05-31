package com.example.myapplication666;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.example.myapplication666.Users;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class MainActivity5 extends AppCompatActivity {
    Button next_button5, next_button23;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Users users = new Users();
        SimpleDateFormat today = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        users.currentDateAndTime = today.format(new Date());

        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main4);
        String name = getIntent().getStringExtra("name");
        String surname = getIntent().getStringExtra("surname");
        String name3 = getIntent().getStringExtra("name3");
        String formattedCurrentDay = getIntent().getStringExtra("formattedCurrentDay");

        FirebaseFirestore fs = FirebaseFirestore.getInstance();
        next_button5 = findViewById(R.id.butten_teacher2);

        next_button5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                users.currentDateAndTime = formattedCurrentDay;
                users.name3 = name3;
                users.name = name;
                users.surname = surname;
                users.fl = true;
                fs.collection("house3")
                        .add(users)
                        .addOnSuccessListener(documentReference -> {
                            // documentReference содержит автоматически сгенерированный ID
                            String documentId = documentReference.getId();
                            // Используйте documentId
                            System.out.println("Document ID: " + documentId);
                        })
                        .addOnFailureListener(e -> {
                            // Обработка ошибки
                        });
                finish();
            }

            });
        next_button23 = findViewById(R.id.butten_teacher3);

        next_button23.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {finish();
            }

        });
                ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}