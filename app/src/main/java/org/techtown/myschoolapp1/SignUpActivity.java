package org.techtown.myschoolapp1;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpActivity extends AppCompatActivity {
    Button signUpBtn;

    // 아이디 비번 이름을 치면 회원가입 되게 만들자
    private FirebaseAuth mFirebaseAuth; // 파이에베이스 인증
    private DatabaseReference mDatabaseRef; // 실시간 데이터베이스
    private EditText mEtEmail, mEtPwd, mName, checkEmail, checkPwd;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        mFirebaseAuth = FirebaseAuth.getInstance();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference();

        mEtEmail = findViewById(R.id.signUp_idEditText);
        mEtPwd = findViewById(R.id.signUp_pwEditText);
        mName = findViewById(R.id.signUp_nameEditText);

        checkEmail = findViewById(R.id.signUp_emailEditText);
        checkPwd = findViewById(R.id.signUp_pwChkEditText);

        // 확인은 나중에 하자

        signUpBtn = (Button) findViewById(R.id.signUpBtn);

        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String strEmail = mEtEmail.getText().toString();
                String strPwd = mEtPwd.getText().toString();
                String strName = mName.getText().toString();

                if (strEmail.equals(checkEmail.getText().toString()) && strPwd.equals(checkPwd.getText().toString())) {
                    mFirebaseAuth.createUserWithEmailAndPassword(strEmail,strPwd)
                            .addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if(task.isSuccessful()) {
                                        FirebaseUser firebaseUser = mFirebaseAuth.getCurrentUser();
                                        UserAccount account = new UserAccount();
                                        account.setName(strName);
                                        account.setEmail(firebaseUser.getEmail());
                                        account.setPassword(strPwd);
                                        account.setIdToken(firebaseUser.getUid());

                                        mDatabaseRef.child("UserAccount").child(firebaseUser.getUid()).setValue(account);
                                        Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                                        startActivity(intent);
                                    } else {
                                        Toast.makeText(SignUpActivity.this, "회원가입에 실패했습니다", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                } else {
                    Toast.makeText(SignUpActivity.this, "이메일과 비밀번호를 정확히 입력해주세요", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
