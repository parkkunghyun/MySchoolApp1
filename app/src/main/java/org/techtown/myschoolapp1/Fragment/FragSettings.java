package org.techtown.myschoolapp1.Fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import org.techtown.myschoolapp1.ProfileActivity;
import org.techtown.myschoolapp1.R;

public class FragSettings extends Fragment {
    private View view;
    private String TAG = "설정 프래그먼트";

    TextView manage_profiles, logout;

    // 설정 관리 및 로그아웃 버튼에 대한 클릭 리스너를 등록
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.i(TAG, " onCreateView");
        view = inflater.inflate(R.layout.frag_settings, container, false);

        manage_profiles = (TextView) view.findViewById(R.id.manage_profiles);
        logout = (TextView) view.findViewById(R.id.logout);

        // 로그아웃 버튼을 클릭했을 때 실행(미구현)
        // 사용자에게 로그아웃 여부를 확인하는 다이얼로그를 표시하고,
        // 확인을 누르면 로그인 액티비티로 이동(미구현)
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = getActivity();

                AlertDialog.Builder dlg = new AlertDialog.Builder(context);
                dlg.setTitle("로그아웃 확인");
                dlg.setMessage("로그아웃 하시겠습니까?");

                dlg.setPositiveButton("예", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                dlg.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getActivity(), "취소했습니다", Toast.LENGTH_SHORT).show();
                    }
                });

                dlg.show();
            }
        });

        // 프로필 관리 버튼을 클릭했을 때 실행
        // ProfileActivity를 시작하여 프로필 관리 화면으로 이동
        manage_profiles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = getActivity();
                Intent intent = new Intent(context, ProfileActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }

}
