package com.dmv.idopontfoglalas;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;

public class BookingActivity extends AppCompatActivity {

    private FirebaseUser user;
    private FirebaseAuth mAuth;
    private Button setDateButton;
    private Button newApointmentButton;
    private TextView dateText;
    private Date date;
    private Calendar cal;
    private Spinner spinner;
    private String title;
    private FirebaseFirestore mFirestore;
    private CollectionReference mItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_booking);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        mAuth = FirebaseAuth.getInstance();

        user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            finish();
        }

        newApointmentButton = findViewById(R.id.buttonNewAppointment);
        newApointmentButton.setEnabled(false);

        spinner = findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.business,
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long l) {
                title = adapterView.getItemAtPosition(pos).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        dateText = findViewById(R.id.textViewDate);
        Calendar c = Calendar.getInstance();
        dateText.setText(String.valueOf(c.get(Calendar.YEAR)) + ". " + String.valueOf(c.get(Calendar.MONTH) + 1) + ". " + String.valueOf(c.get(Calendar.DAY_OF_MONTH)) + ".");

        mFirestore = FirebaseFirestore.getInstance();
        mItems = mFirestore.collection("appointments");
    }

    public void addAppointment(View view) {
        mItems.add(new AppointmentItem(
                title,
                date
        )).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(BookingActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
        finish();

    }

    public void cancel(View view) {
        finish();
    }

    public void setDate(View view) {
        DatePickerDialog dialog = new DatePickerDialog(BookingActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                dateText.setText(String.valueOf(year) + ". " + String.valueOf(month + 1) + ". " + String.valueOf(dayOfMonth) + ".");
                cal = Calendar.getInstance();
                cal.set(Calendar.YEAR, year);
                cal.set(Calendar.MONTH, month);
                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                setTime(view);

            }
        }, Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH));

        dialog.show();
    }

    public void setTime(View view) {
        TimePickerDialog dialog = new TimePickerDialog(BookingActivity.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                cal.set(Calendar.HOUR_OF_DAY, hourOfDay);
                cal.set(Calendar.MINUTE, minute);
                date = cal.getTime();
                newApointmentButton.setEnabled(true);
                Animation animation = AnimationUtils.loadAnimation(newApointmentButton.getContext(), R.anim.shake);
                newApointmentButton.startAnimation(animation);
            }
        }, 10, 30, true);
        dialog.show();
    }



}