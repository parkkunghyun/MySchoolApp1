package org.techtown.myschoolapp1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_EXTERNAL_STORAGE = 1;

    private FirebaseAuth mFirebaseAuth; // 파이어베이스 인증
    private DatabaseReference mDatabaseRef; // 실시간 데이터베이스
    private EditText mEtEmail, mEtPwd;

    Button loginBtn;
    TextView findIdTextView, findPwTextView, signUpTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mFirebaseAuth = FirebaseAuth.getInstance();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference();

        mEtEmail = findViewById(R.id.login_idEditText);
        mEtPwd = findViewById(R.id.login_pwEditText);

        loginBtn = (Button) findViewById(R.id.loginBtn);
        findIdTextView = (TextView) findViewById(R.id.findIdTextView);
        findPwTextView = (TextView) findViewById(R.id.findPwTextView);
        signUpTextView = (TextView) findViewById(R.id.signUpTextView);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String strEmail = mEtEmail.getText().toString();
                String strPwd = mEtPwd.getText().toString();

                mFirebaseAuth.signInWithEmailAndPassword(strEmail, strPwd).addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Intent intent = new Intent(MainActivity.this, SecondActivity.class);
                            Log.d("넘어가는지" , "Dd");
                            startActivity(intent);
                        } else {
                            Toast.makeText(MainActivity.this, "Login에 실패", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
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