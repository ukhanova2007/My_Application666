package com.example.myapplication666;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;

import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication666.RecyclerView.UserAdapter;
import com.example.myapplication666.Users;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity2 extends AppCompatActivity {
    private FirebaseFirestore db;
    ArrayAdapter<String> adapter;
    Button next_button4, next_button5, next_button6, next_button10;
    private ToggleButton todayToggleButton, tomorrowToggleButton, afterTomorrowToggleButton;
    private TextView attendanceStatusTextView;
    private SharedPreferences sharedPreferences;
    private static final String PREFS_NAME = "AttendancePrefs";
    private static final String TODAY_KEY = "today";
    private static final String TOMORROW_KEY = "tomorrow";
    private static final String AFTER_TOMORROW_KEY = "afterTomorrow";

  //  Calendar calendar;
    // Date currentDate, nextDay, nextnextDay;

    String cur_name_o = "";
    String cur_surname_o = "";
    String cur_name3_o = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main2);
        Users users = new Users();

       /*
        SimpleDateFormat today = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        users.currentDateAndTime = today.format(new Date());
        */



        // Инициализация Firestore
        db = FirebaseFirestore.getInstance();

        // Получаем ссылку на коллекцию "house3" с нужной датойformattedCurrentDay
        Query query = db.collection("house3").
                whereEqualTo("currentDateAndTime", "")
                .whereEqualTo("name", "")
                .whereEqualTo("name3", "")
                .whereEqualTo("surname", "")
                .whereEqualTo("fl", false);

        // Инициализация RecyclerView
        Spinner organizerSpinner = findViewById(R.id.organizerSpinner);
       // List usersList = new ArrayList<>();
        List<String> organizerNames = new ArrayList<>();

        // Создаем адаптер
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, organizerNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // Стиль для выпадающего списка
        organizerSpinner.setAdapter(adapter);


        query.get()  // Используем collectionRef (ссылка на "house3")
                // collectionRef.get()  // Используем collectionRef (ссылка на "house3")
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        organizerNames.clear();  // Очищаем списки перед заполнением
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String organizerName = document.getString("surname_o") +" "+ document.getString("name_o") + " "+ document.getString("name3_o");
                            String organizerId = document.getId(); // Получаем ID документа
                            organizerNames.add(organizerName);
                          //  organizerIds.add(organizerId);  // Сохраняем ID организатора
                        }
                        adapter.notifyDataSetChanged(); // Обновляем Spinner
                    } else {
                        Exception e = task.getException();
                        if (e instanceof FirebaseFirestoreException) {
                            FirebaseFirestoreException firestoreException = (FirebaseFirestoreException) e;
                        }
                        // Отобразите сообщение об ошибке
                    }
                });


        Calendar calendar1 = Calendar.getInstance(); // Новый экземпляр
        Date currentDate = calendar1.getTime();

        Calendar calendar2 = Calendar.getInstance(); // Новый экземпляр
        calendar2.add(Calendar.DAY_OF_YEAR, 1);
        Date nextDay = calendar2.getTime();

        Calendar calendar3 = Calendar.getInstance(); // Новый экземпляр
        calendar3.add(Calendar.DAY_OF_YEAR, 2);
        Date nextnextDay = calendar3.getTime();


        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String formattedCurrentDay = sdf.format(currentDate);
        String formattedNextDay = sdf.format(nextDay);
        String formattedNextNextDay = sdf.format(nextnextDay);

        // Инициализация Views
        todayToggleButton = findViewById(R.id.todayToggleButton);
        tomorrowToggleButton = findViewById(R.id.tomorrowToggleButton);
        afterTomorrowToggleButton = findViewById(R.id.afterTomorrowToggleButton);
        attendanceStatusTextView = findViewById(R.id.attendanceStatusTextView);

        // Получаем SharedPreferences
        sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);

        // Загружаем сохраненные значения
        //todayToggleButton.setChecked(sharedPreferences.getBoolean(TODAY_KEY, false));
        //tomorrowToggleButton.setChecked(sharedPreferences.getBoolean(TOMORROW_KEY, false));
        //afterTomorrowToggleButton.setChecked(sharedPreferences.getBoolean(AFTER_TOMORROW_KEY, false));

        todayToggleButton.setChecked(false);
        tomorrowToggleButton.setChecked(false);
        afterTomorrowToggleButton.setChecked(false);

        // Обновляем статус
        updateAttendanceStatus();

        organizerSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedOrganizerName = organizerNames.get(position);
                //String selectedOrganizerId = organizerIds.get(position); // Получаем ID выбранного организатора

                // Здесь можно выполнить действия с выбранным организатором (например, передать ID в другую Activity)
                //Log.d(TAG, "Выбран организатор: " + selectedOrganizerName + ", ID: " + selectedOrganizerId);

                Toast.makeText(MainActivity2.this, "Выбран: " + selectedOrganizerName, Toast.LENGTH_SHORT).show();

                String[] words = selectedOrganizerName.split(" ");  // Разделяем строку по пробелам
                cur_surname_o = words[0];
                cur_name_o = words[1];
                cur_name3_o = words[2];
                String name = getIntent().getStringExtra("name");
                String surname = getIntent().getStringExtra("surname");
                String name3 = getIntent().getStringExtra("name3");
                getEventStatuses(selectedOrganizerName,surname, name, name3); // меняем статусы прихода
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Обработка случая, когда ничего не выбрано (необязательно)
            }
        });


        /* Calendar calendar = Calendar.getInstance();
        Date currentDate = calendar.getTime();
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        Date nextDay = calendar.getTime();
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        Date nextnextDay = calendar.getTime();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String formattedCurrentDay = sdf.format(currentDate);
        String formattedNextDay = sdf.format(nextDay);
        String formattedNextNextDay = sdf.format(nextnextDay);

        // Инициализация Views
        todayToggleButton = findViewById(R.id.todayToggleButton);
        tomorrowToggleButton = findViewById(R.id.tomorrowToggleButton);
        afterTomorrowToggleButton = findViewById(R.id.afterTomorrowToggleButton);
        attendanceStatusTextView = findViewById(R.id.attendanceStatusTextView);

        // Получаем SharedPreferences
        sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);

        // Загружаем сохраненные значения
        todayToggleButton.setChecked(sharedPreferences.getBoolean(TODAY_KEY, false));
        tomorrowToggleButton.setChecked(sharedPreferences.getBoolean(TOMORROW_KEY, false));
        afterTomorrowToggleButton.setChecked(sharedPreferences.getBoolean(AFTER_TOMORROW_KEY, false));

        // Обновляем статус
        updateAttendanceStatus();
*/
        // Устанавливаем слушатели на ToggleButton
        todayToggleButton.setOnCheckedChangeListener((buttonView, isChecked) -> {
            saveAttendance(TODAY_KEY, isChecked);
            updateAttendanceStatus();
            /*String name_o = getIntent().getStringExtra("name_o");
            String surname_o = getIntent().getStringExtra("surname_o");
            String name3_o = getIntent().getStringExtra("name3_o");
            String name = getIntent().getStringExtra("name");
            String surname = getIntent().getStringExtra("surname");
            String name3 = getIntent().getStringExtra("name3");
            if(todayToggleButton.isChecked() == true){
                users.currentDateAndTime = formattedCurrentDay;
                users.name3 = name3;
                users.name = name;
                users.surname = surname;
                users.fl = true;
                users.surname_o = cur_surname_o;
                users.name_o = cur_name_o;
                users.name3_o= cur_name3_o;


                //проверка на уникальность записи
                Query query_all = db.collection("house3").whereEqualTo("currentDateAndTime", formattedCurrentDay)
                        .whereEqualTo("name", name)
                        .whereEqualTo("name3", name3)
                        .whereEqualTo("surname", surname)
                        .whereEqualTo("name_o", name_o)
                        .whereEqualTo("name3_o", name3_o)
                        .whereEqualTo("surname_o", surname_o)
                                                .whereEqualTo("fl", true);
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
            }
            else {
                users.currentDateAndTime = formattedCurrentDay;
                users.name3 = name3;
                users.name = name;
                users.surname = surname;
                users.fl = true;
                users.surname_o = cur_surname_o;
                users.name_o = cur_name_o;
                users.name3_o = cur_name3_o;
                Query query_all = db.collection("house3").whereEqualTo("currentDateAndTime", formattedNextDay)
                        .whereEqualTo("name", name)
                        .whereEqualTo("name3", name3)
                        .whereEqualTo("surname", surname)
                        .whereEqualTo("name_o", name_o)
                        .whereEqualTo("name3_o", name3_o)
                        .whereEqualTo("surname_o", surname_o)
                        .whereEqualTo("fl", true);
                query_all.get().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document :
                                task.getResult()) {
                            document.getReference().delete()
                                    .addOnSuccessListener(aVoid -> Log.d("DELETE",
                                            "Запись на мероприятие удалена"))
                                    .addOnFailureListener(e -> Log.e("DELETE", "Ошибка: " + e.getMessage()));
                        }
                    } else {
                        Log.e("FIREBASE", "Ошибка запроса: " + task.getException());
                    }
                });
            }*/

            String name = getIntent().getStringExtra("name");
            String surname = getIntent().getStringExtra("surname");
            String name3 = getIntent().getStringExtra("name3");

            users.name3 = name3;
            users.name = name;
            users.surname = surname;
            users.fl = true;
            users.surname_o = cur_surname_o;
            users.name_o = cur_name_o;
            users.name3_o= cur_name3_o;
            if(todayToggleButton.isChecked() == true){
                users.currentDateAndTime = formattedCurrentDay;
                //проверка на уникальность записи
                Query query_all = db.collection("house3").whereEqualTo("currentDateAndTime",
                                formattedCurrentDay
                        )
                        .whereEqualTo("name", name)
                        .whereEqualTo("name3", name3)
                        .whereEqualTo("surname", surname)
                        .whereEqualTo("name_o", cur_name_o)
                        .whereEqualTo("name3_o", cur_name3_o)
                        .whereEqualTo("surname_o", cur_surname_o)
                        .whereEqualTo("fl", true);
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
            }
            else {
                users.currentDateAndTime = formattedCurrentDay;
                Query query_all = db.collection("house3").whereEqualTo("currentDateAndTime", formattedCurrentDay)
                        .whereEqualTo("name", name)
                        .whereEqualTo("name3", name3)
                        .whereEqualTo("surname", surname)
                        .whereEqualTo("name_o", cur_name_o)
                        .whereEqualTo("name3_o", cur_name3_o)
                        .whereEqualTo("surname_o", cur_surname_o)
                        .whereEqualTo("fl", true);
                query_all.get().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document :
                                task.getResult()) {
                            document.getReference().delete()
                                    .addOnSuccessListener(aVoid -> Log.d("DELETE",
                                            "Запись на мероприятие удалена"))
                                    .addOnFailureListener(e -> Log.e("DELETE", "Ошибка: " + e.getMessage()));
                        }
                    } else {
                        Log.e("FIREBASE", "Ошибка запроса: " + task.getException());
                    }
                });
            }





        });

        tomorrowToggleButton.setOnCheckedChangeListener((buttonView, isChecked) -> {
            saveAttendance(TOMORROW_KEY, isChecked);
            updateAttendanceStatus();
            /*String name_o = getIntent().getStringExtra("name_o");
            String surname_o = getIntent().getStringExtra("surname_o");
            String name3_o = getIntent().getStringExtra("name3_o");
            String name = getIntent().getStringExtra("name");
            String surname = getIntent().getStringExtra("surname");
            String name3 = getIntent().getStringExtra("name3");

            if(tomorrowToggleButton.isChecked() == true) {
                users.currentDateAndTime = formattedNextDay;
                users.name3 = name3;
                users.name = name;
                users.surname = surname;
                users.fl = true;
                users.surname_o = cur_surname_o;
                users.name_o = cur_name_o;
                users.name3_o= cur_name3_o;
                Query query_all = db.collection("house3").whereEqualTo("currentDateAndTime", formattedNextDay)
                        .whereEqualTo("name", name)
                        .whereEqualTo("name3", name3)
                        .whereEqualTo("surname", surname)
                        .whereEqualTo("name_o", name_o)
                        .whereEqualTo("name3_o", name3_o)
                        .whereEqualTo("surname_o", surname_o)
                        .whereEqualTo("fl", true);
                query_all.get().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        if (task.getResult().isEmpty()) {
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
            }else {
                users.currentDateAndTime = formattedNextDay;
                users.name3 = name3;
                users.name = name;
                users.surname = surname;
                users.fl = true;
                users.surname_o = cur_surname_o;
                users.name_o = cur_name_o;
                users.name3_o= cur_name3_o;
                Query query_all = db.collection("house3").whereEqualTo("currentDateAndTime", formattedNextDay)
                        .whereEqualTo("name", name)
                        .whereEqualTo("name3", name3)
                        .whereEqualTo("surname", surname)
                        .whereEqualTo("name_o", name_o)
                        .whereEqualTo("name3_o", name3_o)
                        .whereEqualTo("surname_o", surname_o)
                        .whereEqualTo("fl", true);
                query_all.get().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                       for (QueryDocumentSnapshot document :
                                task.getResult()) {
                            document.getReference().delete()
                                    .addOnSuccessListener(aVoid -> Log.d("DELETE",
                                            "Запись на мероприятие удалена"))
                                    .addOnFailureListener(e -> Log.e("DELETE", "Ошибка: " + e.getMessage()));
                        }
                    } else{
                            Log.e("FIREBASE", "Ошибка запроса: " + task.getException());
                        }
                    });

            }*/
            String name = getIntent().getStringExtra("name");
            String surname = getIntent().getStringExtra("surname");
            String name3 = getIntent().getStringExtra("name3");

            users.name3 = name3;
            users.name = name;
            users.surname = surname;
            users.fl = true;
            users.surname_o = cur_surname_o;
            users.name_o = cur_name_o;
            users.name3_o= cur_name3_o;
            if (tomorrowToggleButton.isChecked() == true) {
                users.currentDateAndTime = formattedNextDay;

                Query query_all = db.collection("house3").whereEqualTo("currentDateAndTime",formattedNextDay)
                        .whereEqualTo("name", name)
                        .whereEqualTo("name3", name3)
                        .whereEqualTo("surname", surname)
                        .whereEqualTo("name_o", cur_name_o)
                        .whereEqualTo("name3_o", cur_name3_o)
                        .whereEqualTo("surname_o", cur_surname_o)
                        .whereEqualTo("fl", true);
                query_all.get().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        if (task.getResult().isEmpty()) {
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
            }
            else {
                users.currentDateAndTime = formattedNextDay;

                Query query_all = db.collection("house3").whereEqualTo("currentDateAndTime", formattedNextDay)
                        .whereEqualTo("name", name)
                        .whereEqualTo("name3", name3)
                        .whereEqualTo("surname", surname)
                        .whereEqualTo("name_o", cur_name_o)
                        .whereEqualTo("name3_o", cur_name3_o)
                        .whereEqualTo("surname_o", cur_surname_o)
                        .whereEqualTo("fl", true);
                query_all.get().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document :
                                task.getResult()) {
                            document.getReference().delete()
                                    .addOnSuccessListener(aVoid -> Log.d("DELETE",
                                            "Запись на мероприятие удалена"))
                                    .addOnFailureListener(e -> Log.e("DELETE", "Ошибка: " + e.getMessage()));
                        }
                    } else{
                        Log.e("FIREBASE", "Ошибка запроса: " + task.getException());
                    }
                });

            }




        });

        afterTomorrowToggleButton.setOnCheckedChangeListener((buttonView, isChecked) -> {
            saveAttendance(AFTER_TOMORROW_KEY, isChecked);
            updateAttendanceStatus();
            /*String name_o = getIntent().getStringExtra("name_o");
            String surname_o = getIntent().getStringExtra("surname_o");
            String name3_o = getIntent().getStringExtra("name3_o");
            String name = getIntent().getStringExtra("name");
            String surname = getIntent().getStringExtra("surname");
            String name3 = getIntent().getStringExtra("name3");
            if (afterTomorrowToggleButton.isChecked() == true) {
                users.currentDateAndTime = formattedNextNextDay;
                users.name3 = name3;
                users.name = name;
                users.surname = surname;
                users.fl = true;
                users.surname_o = cur_surname_o;
                users.name_o = cur_name_o;
                users.name3_o= cur_name3_o;
                Query query_all = db.collection("house3").whereEqualTo("currentDateAndTime", formattedNextNextDay)
                        .whereEqualTo("name", name)
                        .whereEqualTo("name3", name3)
                        .whereEqualTo("surname", surname)
                        .whereEqualTo("name_o", name_o)
                        .whereEqualTo("name3_o", name3_o)
                        .whereEqualTo("surname_o", surname_o)
                        .whereEqualTo("fl", true);
                query_all.get().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        if (task.getResult().isEmpty()) {
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
            }
            else {
                users.currentDateAndTime = formattedNextNextDay;
                users.name3 = name3;
                users.name = name;
                users.surname = surname;
                users.fl = true;
                users.surname_o = cur_surname_o;
                users.name_o = cur_name_o;
                users.name3_o= cur_name3_o;
                Query query_all = db.collection("house3").whereEqualTo("currentDateAndTime", formattedNextDay)
                        .whereEqualTo("name", name)
                        .whereEqualTo("name3", name3)
                        .whereEqualTo("surname", surname)
                        .whereEqualTo("name_o", name_o)
                        .whereEqualTo("name3_o", name3_o)
                        .whereEqualTo("surname_o", surname_o)
                        .whereEqualTo("fl", true);
                query_all.get().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document :
                                task.getResult()) {
                            document.getReference().delete()
                                    .addOnSuccessListener(aVoid -> Log.d("DELETE",
                                            "Запись на мероприятие удалена"))
                                    .addOnFailureListener(e -> Log.e("DELETE", "Ошибка: " + e.getMessage()));
                        }
                    } else{
                        Log.e("FIREBASE", "Ошибка запроса: " + task.getException());
                    }
                });

            }*/
            String name = getIntent().getStringExtra("name");
            String surname = getIntent().getStringExtra("surname");
            String name3 = getIntent().getStringExtra("name3");

            users.name3 = name3;
            users.name = name;
            users.surname = surname;
            users.fl = true;
            users.surname_o = cur_surname_o;
            users.name_o = cur_name_o;
            users.name3_o= cur_name3_o;


            if (afterTomorrowToggleButton.isChecked() == true) {
                users.currentDateAndTime = formattedNextNextDay;

                Query query_all = db.collection("house3").whereEqualTo("currentDateAndTime",formattedNextNextDay)
                        .whereEqualTo("name", name)
                        .whereEqualTo("name3", name3)
                        .whereEqualTo("surname", surname)
                        .whereEqualTo("name_o", cur_name_o)
                        .whereEqualTo("name3_o", cur_name3_o)
                        .whereEqualTo("surname_o", cur_surname_o)
                        .whereEqualTo("fl", true);
                query_all.get().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        if (task.getResult().isEmpty()) {
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
            }
            else {
                users.currentDateAndTime = formattedNextNextDay;

                Query query_all = db.collection("house3").whereEqualTo("currentDateAndTime", formattedNextNextDay)
                        .whereEqualTo("name", name)
                        .whereEqualTo("name3", name3)
                        .whereEqualTo("surname", surname)
                        .whereEqualTo("name_o", cur_name_o)
                        .whereEqualTo("name3_o", cur_name3_o)
                        .whereEqualTo("surname_o", cur_surname_o)
                        .whereEqualTo("fl", true);
                query_all.get().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document :
                                task.getResult()) {
                            document.getReference().delete()
                                    .addOnSuccessListener(aVoid -> Log.d("DELETE",
                                            "Запись на мероприятие удалена"))
                                    .addOnFailureListener(e -> Log.e("DELETE", "Ошибка: " + e.getMessage()));
                        }
                    } else{
                        Log.e("FIREBASE", "Ошибка запроса: " + task.getException());
                    }
                });

            }



        });


        next_button4 = findViewById(R.id.butten_teacher7);
        next_button5 = findViewById(R.id.butten_teacher8);
        next_button6 = findViewById(R.id.butten_teacher9);

       // next_button10 = findViewById(R.id.butten_hteacher7);

        /*next_button10.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity2.this, "Выбор сохранен", Toast.LENGTH_SHORT).show();
              //  String name_o = getIntent().getStringExtra("name_o");
              //  String surname_o = getIntent().getStringExtra("surname_o");
               // String name3_o = getIntent().getStringExtra("name3_o");
                String name = getIntent().getStringExtra("name");
                String surname = getIntent().getStringExtra("surname");
                String name3 = getIntent().getStringExtra("name3");

                users.name3 = name3;
                users.name = name;
                users.surname = surname;
                users.fl = true;
                users.surname_o = cur_surname_o;
                users.name_o = cur_name_o;
                users.name3_o= cur_name3_o;
                if(todayToggleButton.isChecked() == true){
                    users.currentDateAndTime = formattedCurrentDay;
                    //проверка на уникальность записи
                    Query query_all = db.collection("house3").whereEqualTo("currentDateAndTime",
                                    formattedCurrentDay
                            )
                            .whereEqualTo("name", name)
                            .whereEqualTo("name3", name3)
                            .whereEqualTo("surname", surname)
                            .whereEqualTo("name_o", cur_name_o)
                            .whereEqualTo("name3_o", cur_name3_o)
                            .whereEqualTo("surname_o", cur_surname_o)
                            .whereEqualTo("fl", true);
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
                }
                else {
                    users.currentDateAndTime = formattedCurrentDay;
                    Query query_all = db.collection("house3").whereEqualTo("currentDateAndTime", formattedCurrentDay)
                            .whereEqualTo("name", name)
                            .whereEqualTo("name3", name3)
                            .whereEqualTo("surname", surname)
                            .whereEqualTo("name_o", cur_name_o)
                            .whereEqualTo("name3_o", cur_name3_o)
                            .whereEqualTo("surname_o", cur_surname_o)
                            .whereEqualTo("fl", true);
                    query_all.get().addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document :
                                    task.getResult()) {
                                document.getReference().delete()
                                        .addOnSuccessListener(aVoid -> Log.d("DELETE",
                                                "Запись на мероприятие удалена"))
                                        .addOnFailureListener(e -> Log.e("DELETE", "Ошибка: " + e.getMessage()));
                            }
                        } else {
                            Log.e("FIREBASE", "Ошибка запроса: " + task.getException());
                        }
                    });
                }

                if (tomorrowToggleButton.isChecked() == true) {
                    users.currentDateAndTime = formattedNextDay;

                    Query query_all = db.collection("house3").whereEqualTo("currentDateAndTime",formattedNextDay)
                            .whereEqualTo("name", name)
                            .whereEqualTo("name3", name3)
                            .whereEqualTo("surname", surname)
                            .whereEqualTo("name_o", cur_name_o)
                            .whereEqualTo("name3_o", cur_name3_o)
                            .whereEqualTo("surname_o", cur_surname_o)
                            .whereEqualTo("fl", true);
                    query_all.get().addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            if (task.getResult().isEmpty()) {
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
                }
                else {
                    users.currentDateAndTime = formattedNextDay;

                    Query query_all = db.collection("house3").whereEqualTo("currentDateAndTime", formattedNextDay)
                            .whereEqualTo("name", name)
                            .whereEqualTo("name3", name3)
                            .whereEqualTo("surname", surname)
                            .whereEqualTo("name_o", cur_name_o)
                            .whereEqualTo("name3_o", cur_name3_o)
                            .whereEqualTo("surname_o", cur_surname_o)
                            .whereEqualTo("fl", true);
                    query_all.get().addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document :
                                    task.getResult()) {
                                document.getReference().delete()
                                        .addOnSuccessListener(aVoid -> Log.d("DELETE",
                                                "Запись на мероприятие удалена"))
                                        .addOnFailureListener(e -> Log.e("DELETE", "Ошибка: " + e.getMessage()));
                            }
                        } else{
                            Log.e("FIREBASE", "Ошибка запроса: " + task.getException());
                        }
                    });

                }


                if (afterTomorrowToggleButton.isChecked() == true) {
                    users.currentDateAndTime = formattedNextNextDay;

                    Query query_all = db.collection("house3").whereEqualTo("currentDateAndTime",formattedNextNextDay)
                            .whereEqualTo("name", name)
                            .whereEqualTo("name3", name3)
                            .whereEqualTo("surname", surname)
                            .whereEqualTo("name_o", cur_name_o)
                            .whereEqualTo("name3_o", cur_name3_o)
                            .whereEqualTo("surname_o", cur_surname_o)
                            .whereEqualTo("fl", true);
                    query_all.get().addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            if (task.getResult().isEmpty()) {
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
                }
                else {
                    users.currentDateAndTime = formattedNextNextDay;

                    Query query_all = db.collection("house3").whereEqualTo("currentDateAndTime", formattedNextNextDay)
                            .whereEqualTo("name", name)
                            .whereEqualTo("name3", name3)
                            .whereEqualTo("surname", surname)
                            .whereEqualTo("name_o", cur_name_o)
                            .whereEqualTo("name3_o", cur_name3_o)
                            .whereEqualTo("surname_o", cur_surname_o)
                            .whereEqualTo("fl", true);
                    query_all.get().addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document :
                                    task.getResult()) {
                                document.getReference().delete()
                                        .addOnSuccessListener(aVoid -> Log.d("DELETE",
                                                "Запись на мероприятие удалена"))
                                        .addOnFailureListener(e -> Log.e("DELETE", "Ошибка: " + e.getMessage()));
                            }
                        } else{
                            Log.e("FIREBASE", "Ошибка запроса: " + task.getException());
                        }
                    });

                }



            }

        });*/

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
    private void saveAttendance(String key, boolean isChecked) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(key, isChecked);
        editor.apply(); // Асинхронное сохранение
    }

    private void updateAttendanceStatus() {
        boolean today = todayToggleButton.isChecked();
        boolean tomorrow = tomorrowToggleButton.isChecked();
        boolean afterTomorrow = afterTomorrowToggleButton.isChecked();

        StringBuilder status = new StringBuilder("Статус: \n");
        status.append("Сегодня: ").append(today ? "Приду\n" : "Не приду\n");
        status.append("Завтра: ").append(tomorrow ? "Приду\n" : "Не приду\n");
        status.append("Послезавтра: ").append(afterTomorrow ? "Приду\n" : "Не приду\n");

        attendanceStatusTextView.setText(status.toString());
    }
    private void getEventStatuses(String organizer, String surname, String name, String name3) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        String[] words = organizer.split(" ");  // Разделяем строку по пробелам
        cur_surname_o = words[0];
        cur_name_o = words[1];
        cur_name3_o = words[2];

        // Получите даты для сегодня, завтра и послезавтра
      /*  Calendar calendar = Calendar.getInstance();
        Date today = calendar.getTime();
        calendar.add(Calendar.DAY_OF_YEAR, 1);
        Date tomorrow = calendar.getTime();
        calendar.add(Calendar.DAY_OF_YEAR, 1);
        Date afterTomorrow = calendar.getTime();
*/
        Calendar calendar1 = Calendar.getInstance(); // Новый экземпляр
        Date today = calendar1.getTime();

        Calendar calendar2 = Calendar.getInstance(); // Новый экземпляр
        calendar2.add(Calendar.DAY_OF_YEAR, 1);
        Date tomorrow = calendar2.getTime();

        Calendar calendar3 = Calendar.getInstance(); // Новый экземпляр
        calendar3.add(Calendar.DAY_OF_YEAR, 2);
        Date afterTomorrow = calendar3.getTime();



        // Форматируйте даты в нужный формат (если в Firebase даты хранятся как строки)
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()); // Замените формат, если необходимо
        String todayString = dateFormat.format(today);
        String tomorrowString = dateFormat.format(tomorrow);
        String afterTomorrowString = dateFormat.format(afterTomorrow);


        // Создайте запросы для каждой даты
        Query todayQuery = db.collection("house3").whereEqualTo("currentDateAndTime", todayString)
                .whereEqualTo("name", name)
                .whereEqualTo("name3", name3)
                .whereEqualTo("surname", surname)
                .whereEqualTo("name_o", cur_name_o)
                .whereEqualTo("name3_o", cur_name3_o)
                .whereEqualTo("surname_o", cur_surname_o)
                .whereEqualTo("fl", true);

        todayQuery.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                if (task.getResult().isEmpty()) {
                    todayToggleButton.setChecked(false);
                    // установить toggleButton "Не приду"
                }else {
                    // установить toggleButton "Приду"
                    todayToggleButton.setChecked(true);

                }
            }
        });

        Query tomorrowQuery = db.collection("house3").whereEqualTo("currentDateAndTime", tomorrowString)
                .whereEqualTo("name", name)
                .whereEqualTo("name3", name3)
                .whereEqualTo("surname", surname)
                .whereEqualTo("name_o", cur_name_o)
                .whereEqualTo("name3_o", cur_name3_o)
                .whereEqualTo("surname_o", cur_surname_o)
                .whereEqualTo("fl", true);
        tomorrowQuery.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                if (task.getResult().isEmpty()) {
                    tomorrowToggleButton.setChecked(false);
                    // установить toggleButton "Не приду"
                }else {
                    // установить toggleButton "Приду"
                    tomorrowToggleButton.setChecked(true);

                }
            }
        });

        Query afterTomorrowQuery = db.collection("house3").whereEqualTo("currentDateAndTime", afterTomorrowString)
                        .whereEqualTo("name", name)
                        .whereEqualTo("name3", name3)
                        .whereEqualTo("surname", surname)
                        .whereEqualTo("name_o", cur_name_o)
                        .whereEqualTo("name3_o", cur_name3_o)
                        .whereEqualTo("surname_o", cur_surname_o)
                        .whereEqualTo("fl", true);

        afterTomorrowQuery.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                if (task.getResult().isEmpty()) {
                    afterTomorrowToggleButton.setChecked(false);
                    // установить toggleButton "Не приду"
                }else {
                    // установить toggleButton "Приду"
                    afterTomorrowToggleButton.setChecked(true);

                }
            }
        });
        updateAttendanceStatus();

    }
}