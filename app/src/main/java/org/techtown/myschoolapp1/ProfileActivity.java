package org.techtown.myschoolapp1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileActivity extends AppCompatActivity {

    Button modifyBtn;
    TextView back;
    EditText nameEditText, numberEditText;

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    DatabaseReference databaseReference;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        modifyBtn = (Button) findViewById(R.id.modifyBtn);
        back = (TextView) findViewById(R.id.back);
        nameEditText = (EditText) findViewById(R.id.name_edit);
        numberEditText = (EditText) findViewById(R.id.number_edit);

        databaseReference = FirebaseDatabase.getInstance().getReference();
        String uid = user.getUid();

        databaseReference.child("UserAccount").child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UserAccount userAccount = dataSnapshot.getValue(UserAccount.class);

                String name = userAccount.getName();
                nameEditText.setText(name);
                String number = userAccount.getNumber();
                numberEditText.setText(number);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        modifyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String name = nameEditText.getText().toString();
                String number = numberEditText.getText().toString();

                databaseReference.child("UserAccount").child(uid).child("name").setValue(name);
                databaseReference.child("UserAccount").child(uid).child("number").setValue(number);


                Toast.makeText(ProfileActivity.this, "수정 완료했습니다.", Toast.LENGTH_LONG).show();

                finish();
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 뒤로 가기 버튼 클릭 시 현재 액티비티 종료
                finish();
            }
        });
    }
}