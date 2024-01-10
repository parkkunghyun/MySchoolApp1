package org.techtown.myschoolapp1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    Button loginBtn;
    TextView findIdTextView, findPwTextView, signUpTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loginBtn = (Button) findViewById(R.id.loginBtn);
        findIdTextView = (TextView) findViewById(R.id.findIdTextView);
        findPwTextView = (TextView) findViewById(R.id.findPwTextView);
        signUpTextView = (TextView) findViewById(R.id.signUpTextView);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, SecondActivity.class);
                Log.d("넘어가는지" , "Dd");
                startActivity(intent);
            }
        });

        findIdTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // findIdTextView를 클릭했을 때의 동작
                Intent intent = new Intent(MainActivity.this, FindIdActivity.class);
                startActivity(intent);
            }
        });

        findPwTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // findPwTextView를 클릭했을 때의 동작
                Intent intent = new Intent(MainActivity.this, FindPasswordActivity.class);
                startActivity(intent);
            }
        });

        signUpTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // signUpTextView를 클릭했을 때의 동작
                Intent intent = new Intent(MainActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });
    }
}