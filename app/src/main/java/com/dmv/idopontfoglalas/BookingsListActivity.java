package com.dmv.idopontfoglalas;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;


import java.util.ArrayList;

public class BookingsListActivity extends AppCompatActivity {

    private FirebaseUser user;
    private FirebaseAuth mAuth;

    private RecyclerView mRecyclerView;
    private ArrayList<AppointmentItem> mItemList;
    private ArrayList<AppointmentItem> mItemsData;
    private AppointmentItemAdapter mAdapter;

    private FirebaseFirestore mFirestore;
    private CollectionReference mItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_bookings_list);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        mAuth = FirebaseAuth.getInstance();

        user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            Toast.makeText(BookingsListActivity.this, "Sikeres bejelentkezés.", Toast.LENGTH_LONG).show();
        }
        else {
            //Toast.makeText(BookingsListActivity.this, "Nincs bejelentkezve.", Toast.LENGTH_LONG).show();

            finish();
        }



        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mItemList = new ArrayList<>();

        mItemsData = new ArrayList<>();



        mFirestore = FirebaseFirestore.getInstance();
        mItems = mFirestore.collection("appointments");


    }

    @Override
    protected void onResume() {
        super.onResume();
        queryData();

    }
    private void queryData() {
        mItemsData.clear();

        mItems.orderBy("date").get().addOnSuccessListener(queryDocumentSnapshots -> {
            for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                AppointmentItem item = document.toObject(AppointmentItem.class);
                item.setId(document.getId());
                mItemsData.add(item);
            }
            mAdapter = new AppointmentItemAdapter(this, mItemsData);
            mRecyclerView.setAdapter(mAdapter);
            mAdapter.notifyDataSetChanged();
        });


    }

    public void deleteAppointment(AppointmentItem item) {
        DocumentReference ref = mItems.document(item._getId());

        ref.delete().addOnSuccessListener(success -> {
            Toast.makeText(this, "Időpont törölve.", Toast.LENGTH_LONG).show();
        });

        queryData();
    }

    public void newAppointment(View view) {
        Intent intent = new Intent(this, BookingActivity.class);
        startActivity(intent);
    }
}