package com.subashpoudel;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.subashpoudel.dateview.DateView;

import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private TextView textView;
    private DateView dateView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = findViewById(R.id.text);

        dateView = findViewById(R.id.dateView);
        dateView.setDateEnteredListener(new DateView.DateEnteredListener() {
            @Override
            public void onDateEntered(Date date, boolean isValid) {
                if (isValid) {
                    textView.setText(date.toString());
                    textView.setTextColor(Color.BLACK);
                } else {
                    textView.setText("Invalid Date Entered");
                    textView.setTextColor(Color.RED);
                }
            }
        });
    }
}
