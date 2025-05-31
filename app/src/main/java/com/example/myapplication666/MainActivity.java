package com.example.myapplication666;
import android.content.SharedPreferences;
import android.widget.EditText;
import android.os.Bundle;
import java.text.SimpleDateFormat;
import java.util.Date;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Calendar;
import android.widget.Button;
import android.view.View;
import android.content.Intent;
import android.widget.Toast;

import com.example.myapplication666.Users;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class MainActivity extends AppCompatActivity {
    private FirebaseFirestore db;
    Button next_button2, next_button3;
    private SharedPreferences sharedPreferences;
    private static final String PREFS_NAME = "MyPrefs";
    private static final String NAME_KEY = "name";
    private static final String NAME3_KEY = "name3";
    private static final String SURNAME_KEY = "surname";
    EditText myEditText, myE0ditText, myE0ditTe0xt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Users users = new Users();
        db = FirebaseFirestore.getInstance();



        next_button2 = findViewById(R.id.butten_student);
        next_button3 = findViewById(R.id.butten_teacher);


        myEditText = findViewById(R.id.myEditText);
        //String myEditText1 = myEditText.getText().toString();
        myE0ditText = findViewById(R.id.myE0ditText);
        //String myE0ditText1 = myE0ditText.getText().toString();
        myE0ditTe0xt = findViewById(R.id.myE0ditTe0xt);

        // Получаем SharedPreferences
        sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);

        // Загружаем сохраненное имя
        String savedName = sharedPreferences.getString(NAME_KEY, "");// "" - значение по умолчанию, если нет сохраненного имени
        String savedName3 = sharedPreferences.getString(NAME3_KEY, "");
        String savedSurname = sharedPreferences.getString(SURNAME_KEY, "");
        myEditText.setText(savedName3);
        myE0ditText.setText(savedName);
        myE0ditTe0xt.setText(savedSurname);



        next_button2.setOnClickListener(new View.OnClickListener() { // участник
            @Override
            public void onClick(View view) {
                Intent int1 = new Intent(MainActivity.this, MainActivity2.class);
                //myEditText = findViewById(R.id.myEditText);
                String myEditText1 = myEditText.getText().toString();
                //myE0ditText = findViewById(R.id.myE0ditText);
                String myE0ditText1 = myE0ditText.getText().toString();
                //myE0ditTe0xt = findViewById(R.id.myE0ditTe0xt);
                String myE0ditTe0xt1 = myE0ditTe0xt.getText().toString();
               // users.fl = true;
                String name3 = myEditText.getText().toString();
                String surname = myE0ditTe0xt.getText().toString();
                String name = myE0ditText.getText().toString();
                int1.putExtra("name", name);
                int1.putExtra("surname", surname);
                int1.putExtra("name3", name3);
                if(myEditText1.isEmpty() || myE0ditText1.isEmpty() || myE0ditTe0xt1.isEmpty()){
                    myEditText.setError("Пожалуйста, введите отчество");
                    myE0ditText.setError("Пожалуйста, введите имя");
                    myE0ditTe0xt.setError("Пожалуйста, введите фамилию");
                    return;
                }


                startActivity(int1);
            }
        });
        next_button3.setOnClickListener(new View.OnClickListener() { // организатор
            @Override
            public void onClick(View view) {
                Intent int2 = new Intent(MainActivity.this, MainActivity3.class);
                //myEditText = findViewById(R.id.myEditText);
                String myEditText1 = myEditText.getText().toString();
                //myE0ditText = findViewById(R.id.myE0ditText);
                String myE0ditText1 = myE0ditText.getText().toString();
                //myE0ditTe0xt = findViewById(R.id.myE0ditTe0xt);
                String myE0ditTe0xt1 = myE0ditTe0xt.getText().toString();
                String name3_o = myEditText.getText().toString();
                String surname_o = myE0ditTe0xt.getText().toString();
                String name_o = myE0ditText.getText().toString();
                int2.putExtra("name_o", name_o);
                int2.putExtra("surname_o", surname_o);
                int2.putExtra("name3_o", name3_o);
                if(myEditText1.isEmpty() || myE0ditText1.isEmpty() || myE0ditTe0xt1.isEmpty()){
                    myEditText.setError("Пожалуйста, введите отчество");
                    myE0ditText.setError("Пожалуйста, введите имя");
                    myE0ditTe0xt.setError("Пожалуйста, введите фамилию");
                   //Toast.makeText(this, "Заполните обязательные поля!", Toast.LENGTH_SHORT).show();
                   return;
                }

                String cur_name_o = myE0ditText.getText().toString();
                String cur_surname_o = myE0ditTe0xt.getText().toString();
                String cur_name3_o = myEditText.getText().toString();
                users.currentDateAndTime = "";
                users.name3 = "";
                users.name  = "";
                users.surname  = "";
                users.fl = false;
                users.surname_o = cur_surname_o;
                users.name_o = cur_name_o;
                users.name3_o= cur_name3_o;

                //проверка на уникальность записи
                Query query_all = db.collection("house3").whereEqualTo("currentDateAndTime", "")
                        .whereEqualTo("name", "")
                        .whereEqualTo("name3", "")
                        .whereEqualTo("surname", "")
                        .whereEqualTo("name_o", cur_name_o)
                        .whereEqualTo("name3_o", cur_name3_o)
                        .whereEqualTo("surname_o", cur_surname_o)
                        .whereEqualTo("fl", false);
                query_all.get().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        if (task.getResult().isEmpty()) {


                            // вставка уникальной записи в БД
                            db.collection("house3")
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
                        }
                    }
                });



                startActivity(int2);
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();

        // Сохраняем имя пользователя при выходе из Activity
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(NAME_KEY, myE0ditText.getText().toString());
        editor.putString(NAME3_KEY, myEditText.getText().toString());
        editor.putString(SURNAME_KEY, myE0ditTe0xt.getText().toString());
        editor.apply(); // Асинхронное сохранение
    }

}