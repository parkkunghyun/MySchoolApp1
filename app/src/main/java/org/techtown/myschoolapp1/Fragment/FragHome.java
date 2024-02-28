package org.techtown.myschoolapp1.Fragment;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.opengl.Visibility;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.techtown.myschoolapp1.ApiData;
import org.techtown.myschoolapp1.MyAdapter;
import org.techtown.myschoolapp1.MyDBHelper;
import org.techtown.myschoolapp1.R;
import org.techtown.myschoolapp1.SecondActivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class FragHome extends Fragment {
    private View view;
    private String TAG = "home프래그먼트";
    private SecondActivity activity;
    private List<ApiData> dataList;
    private String key = "9jLPzVx%2BGOW1xmxuT3Zg0ylW%2BAox4i8%2BjBu4xPe%2B2W2r%2BuMwhr1YhfXwmiKFqfFFmOU6aNPMjTFt4g2cECEgSw%3D%3D";
    private TextView text;
    ListView list;

    public static final String TABLE_NAME = "seeds_api_TBL";

    View dialogView;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        activity = (SecondActivity) context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.i(TAG, "onCreateView");

        view = inflater.inflate(R.layout.frag_home, container, false);

        Button apiBtn = view.findViewById(R.id.apiButton);
        LinearLayout l1 = view.findViewById(R.id.linearLayout1);

        apiBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                l1.setVisibility(View.VISIBLE);
                // 앱 최초 실행 여부를 SharedPreferences를 이용해 체크
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
                boolean isFirstRun = prefs.getBoolean("isFirstRun", true);

                if (isFirstRun) {
                    // 최초 실행일 경우에만 API 데이터를 가져와서 DB에 저장
                    fetchDataFromAPI();
                    prefs.edit().putBoolean("isFirstRun", false).apply();  // 최초 실행 여부 갱신
                } else {
                    // 최초 실행이 아닐 경우 저장된 데이터를 불러와서 UI 업데이트
                    updateUIWithData(getAllDataFromDB());
                    //fetchDataFromAPI();
                }
            }
        });

        return view;
    }

    // 이 함수는 처음 앱을 실행했을때 공공api데이터를 불러오는 함수입니다.
    private void fetchDataFromAPI() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String apiData = apiUrlConnection();
                    dataList = parseDataFromAPI(apiData);

                    // 만약에 sqlite에 중복 table이 있다면 삭제 후 현재 불러온 데이터를 저장합니다.
                    clearOldData();
                    saveData(dataList);

                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            updateUIWithData(dataList);
                        }
                    });

                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    // 이 함수는 수정 다이어로그에서 '적용'을 눌렀을때 DB에 적용 후 어댑터를 통해 적용된 데이터를 보여줍니다.
    private void updateDataFromAPI() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        updateUIWithData(getAllDataFromDB());
                    }
                });

            }
        }).start();
    }

    // 기존 데이터를 지우는 메서드
    private void clearOldData() {
        MyDBHelper dbHelper = new MyDBHelper(activity);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // 데이터베이스의 모든 데이터 삭제
        db.delete(MyDBHelper.TABLE_NAME, null, null);

        dbHelper.close();
    }

    // url을 연결하는 함수입니다 이때 데이터를 json형태로 받을 것이며 부를때 ResponseCode는 200~300이어야 성공입니다.
    private String apiUrlConnection() throws IOException, JSONException {

        String apiUrl = "https://api.odcloud.kr/api/15055082/v1/uddi:1c7c80f0-ac7f-4d8b-840d-c552fee2e763?page=1&perPage=219&serviceKey="+key;
        URL url = new URL(apiUrl);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Content-type", "application/json");

        BufferedReader rd;
        if (conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
            rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        } else {
            rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
        }

        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = rd.readLine()) != null) {
            sb.append(line);
        }
        rd.close();
        conn.disconnect();

        return sb.toString();
    }

    // json 데이터를  ApiData에 맞게 변경 후 리스트에 추가 합니다
    private List<ApiData> parseDataFromAPI(String apiData) throws JSONException {
        List<ApiData> dataList = new ArrayList<>();

        JSONObject jsonObject = new JSONObject(apiData);
        JSONArray dataArray = jsonObject.getJSONArray("data");

        for (int i = 0; i < dataArray.length(); i++) {
            JSONObject item = dataArray.getJSONObject(i);
            dataList.add(new ApiData(
                    item.getString("년도"),
                    item.getString("작물코드"),
                    item.getString("작물분류"),
                    item.getString("품목"),
                    item.getString("수출입"),
                    item.getString("물량(kg)"),
                    item.getString("금액(USD)")
            ));
        }
        return dataList;
    }

    // DB에 리스트에 저장한 내용을 추가하는 함수입니다.
    private void saveData(List<ApiData> dataList) {
        MyDBHelper dbHelper = new MyDBHelper(activity);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        for (ApiData data : dataList) {
            dbHelper.insertData(db, data);
        }
        dbHelper.close();
    }

    // 데이터를 보여주고 데이터를 누르면 수정 다이어로그가 나오고, 데이터를 길게 누르면 삭제 다이어로그가 나오는 함수입니다.
    //
    private void updateUIWithData(List<ApiData> dataList) {

        MyAdapter adapter = new MyAdapter(getActivity(), android.R.layout.simple_list_item_1, dataList);
        list = view.findViewById(R.id.listView);

        // 리스트뷰에 어댑터 설정
        list.setAdapter(adapter);

    }

    private List<ApiData> getAllDataFromDB() {
        MyDBHelper dbHelper = new MyDBHelper(activity);
        return dbHelper.getAllData();
    }

}
