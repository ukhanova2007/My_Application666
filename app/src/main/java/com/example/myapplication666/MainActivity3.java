package com.example.myapplication666;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity3 extends AppCompatActivity {
    Button next_button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main3);

        TextView textView = findViewById(R.id.editTex9t66Text);
        String name_o = getIntent().getStringExtra("name_o");
        String surname_o = getIntent().getStringExtra("surname_o");
        String name3_o = getIntent().getStringExtra("name3_o");
        String newText = name_o + " " + surname_o + " " + name3_o;
        textView.setText(newText);

        next_button = findViewById(R.id.butten_teacher4);
        next_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent int3 = new Intent(MainActivity3.this, MainActivity6.class);
                int myIntValue = 1; // Ваше значение типа int

                // Используем метод putExtra() для добавления int в Intent
                int3.putExtra("my_integer_value", myIntValue);

                int3.putExtra("name_o", name_o);
                int3.putExtra("surname_o", surname_o);
                int3.putExtra("name3_o", name3_o);
                startActivity(int3);
            }
        });

        next_button = findViewById(R.id.butten_teacher5);
        next_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent int4 = new Intent(MainActivity3.this, MainActivity6.class);
                int myIntValue = 2;
                // Используем метод putExtra() для добавления int в Intent
                int4.putExtra("my_integer_value", myIntValue);
                int4.putExtra("name_o", name_o);
                int4.putExtra("surname_o", surname_o);
                int4.putExtra("name3_o", name3_o);
                startActivity(int4);
            }
        });

        next_button = findViewById(R.id.butten_teacher6);
        next_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent int5 = new Intent(MainActivity3.this, MainActivity6.class);
                int myIntValue = 3;
                // Используем метод putExtra() для добавления int в Intent
                int5.putExtra("my_integer_value", myIntValue);
                int5.putExtra("name_o", name_o);
                int5.putExtra("surname_o", surname_o);
                int5.putExtra("name3_o", name3_o);
                startActivity(int5);
            }
        });
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}