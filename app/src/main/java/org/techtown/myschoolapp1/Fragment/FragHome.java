package org.techtown.myschoolapp1.Fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.techtown.myschoolapp1.ApiData;
import org.techtown.myschoolapp1.CSVParser;
import org.techtown.myschoolapp1.MyAdapter;
import org.techtown.myschoolapp1.MyDBHelper;
import org.techtown.myschoolapp1.R;
import org.techtown.myschoolapp1.SecondActivity;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;


public class FragHome extends Fragment {
    private View view;
    private String TAG = "home 프래그먼트";

    private ActivityResultLauncher<String> mGetContent;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.i(TAG, " onCreateView");
        view = inflater.inflate(R.layout.frag_home, container, false);

        Button selectButton = (Button) view.findViewById(R.id.select_button);

        // ActivityResultLauncher를 초기화
        mGetContent = registerForActivityResult(new ActivityResultContracts.GetContent(),
                new ActivityResultCallback<Uri>() {
                    @Override
                    public void onActivityResult(Uri uri) {
                        // 선택한 파일의 URI를 받았을 때 호출되는 콜백
                        handleSelectedFile(uri);
                    }
                });

        selectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 파일 선택을 위한 액티비티를 시작
                mGetContent.launch("text/*");

                // 데이터베이스에서 모든 데이터 삭제
                MyDBHelper dbHelper = new MyDBHelper(requireContext());
                dbHelper.deleteAllData();
            }
        });

        return view;
    }

    // 선택한 파일의 URI를 처리하는 메서드
    private void handleSelectedFile(Uri uri) {
        // URI를 사용하여 CSV 파일 파서를 통해 데이터를 추출
        CSVParser csvParser = new CSVParser();
        try {
            List<ApiData> extractedData = csvParser.getItemList(requireContext(), uri);

            // 추출된 데이터를 MyDBHelper 클래스를 통해 SQLite에 저장
            MyDBHelper dbHelper = new MyDBHelper(requireContext());
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            for (ApiData data : extractedData) {
                dbHelper.insertData(db, data);
            }
            dbHelper.close();

            // SQLite에 데이터가 성공적으로 저장되었음을 사용자에게 알림
            Toast.makeText(requireContext(), "데이터를 불러왔습니다.", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            // 데이터 추출 중 오류가 발생한 경우 사용자에게 알림
            Toast.makeText(requireContext(), "데이터를 불러오는 도중 오류가 발생했습니다.", Toast.LENGTH_SHORT).show();
        }
    }
}
