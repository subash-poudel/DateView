package com.subashpoudel;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.TextView;

import com.subashpoudel.date.DateView;

import java.util.Date;

public class MainActivity extends AppCompatActivity {

    TextView textView;
    Button button;
    DateView dateView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = (TextView) findViewById(R.id.text);
        button = (Button) findViewById(R.id.button);
        dateView = (DateView) findViewById(R.id.dateView);
        dateView.setDateEnteredListener(new DateView.DateEnteredListener() {
            @Override
            public void onDateEntered(Date date, boolean isValid) {
                if (isValid) {
                    textView.setText(date.toString());
                } else {
                    textView.setText("null date");
                }
            }
        });
    }
}
