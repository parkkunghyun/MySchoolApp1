package org.techtown.myschoolapp1;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class FindIdActivity extends AppCompatActivity {
    Button findIdBtn, cancelBtn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_findid);

        findIdBtn = (Button) findViewById(R.id.findIdBtn);
        cancelBtn = (Button) findViewById(R.id.cancelBtn);

        findIdBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FindIdActivity.this, FindIdActivity2.class);
                startActivity(intent);
            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FindIdActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
}
