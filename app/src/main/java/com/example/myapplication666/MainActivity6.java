package com.example.myapplication666;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication666.RecyclerView.UserAdapter;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity6 extends AppCompatActivity {
    private FirebaseFirestore db;
    private CollectionReference collectionRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main5);

        Calendar calendar = Calendar.getInstance();
        Date currentDate = calendar.getTime();
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        Date nextDay = calendar.getTime();
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        Date nextnextDay = calendar.getTime();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String formattedCurrentDay;

        // Инициализация Firestore
        db = FirebaseFirestore.getInstance();

        Intent intent = getIntent();
        int receivedIntValue = intent.getIntExtra("my_integer_value", 1);
        formattedCurrentDay = sdf.format(currentDate);
        if(receivedIntValue == 2) {
            formattedCurrentDay = sdf.format(nextDay);
        } else if(receivedIntValue == 3) {
            formattedCurrentDay = sdf.format(nextnextDay);
        }

        String name_o = getIntent().getStringExtra("name_o");
        String surname_o = getIntent().getStringExtra("surname_o");
        String name3_o = getIntent().getStringExtra("name3_o");

        // Получаем ссылку на коллекцию "house3" с нужной датой
        Query query = db.collection("house3").whereEqualTo("currentDateAndTime", formattedCurrentDay)
                .whereEqualTo("name_o", name_o).whereEqualTo("name3_o", name3_o)
                .whereEqualTo("surname_o", surname_o);

        // Инициализация RecyclerView
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        List usersList = new ArrayList<>();
        Context context = this;
        UserAdapter userAdapter = new UserAdapter(context, usersList);
        recyclerView.setAdapter(userAdapter);

        query.get()  // Используем collectionRef (ссылка на "house3")
       // collectionRef.get()  // Используем collectionRef (ссылка на "house3")
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<Users> fetchedUsersList = new ArrayList<>();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Users user = document.toObject(Users.class);
                            fetchedUsersList.add(user);
                        }
                        usersList.clear();
                        usersList.addAll(fetchedUsersList);

                        TextView textView = findViewById(R.id.editText66Text);
                        String originalText = textView.getText().toString();
                        int number = usersList.size();
                        if(receivedIntValue == 1) {
                            String newText = originalText + " на сегодня:" + number;
                            textView.setText(newText);
                        }
                        if(receivedIntValue == 2) {
                            String newText = originalText + " на завтра:" + number;
                            textView.setText(newText);
                        }
                        if(receivedIntValue == 3) {
                            String newText = originalText + " на послезавтра:" + number;
                            textView.setText(newText);
                        }
                        userAdapter.setUsersList(usersList);
                    } else {
                        Exception e = task.getException();
                        if (e instanceof FirebaseFirestoreException) {
                            FirebaseFirestoreException firestoreException = (FirebaseFirestoreException) e;

                        }
                        // Отобразите сообщение об ошибке
                    }
                });
    }
}
