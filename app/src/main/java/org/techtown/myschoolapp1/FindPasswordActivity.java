package org.techtown.myschoolapp1;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class FindPasswordActivity extends AppCompatActivity {
    Button findPwBtn, cancelBtn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_findpw);

        findPwBtn = (Button) findViewById(R.id.findPwBtn);
        cancelBtn = (Button) findViewById(R.id.cancelBtn);

        findPwBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FindPasswordActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FindPasswordActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
}
