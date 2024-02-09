package org.techtown.myschoolapp1.Fragment;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
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
        }

        return view;
    }


    private void fetchDataFromAPI() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String apiData = apiUrlConnection();
                    dataList = parseDataFromAPI(apiData);

                    // 기존 데이터를 지우고 새로운 데이터를 저장
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

    // 기존 데이터를 지우는 메서드
    private void clearOldData() {
        MyDBHelper dbHelper = new MyDBHelper(activity);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // 데이터베이스의 모든 데이터 삭제
        db.delete(MyDBHelper.TABLE_NAME, null, null);

        dbHelper.close();
    }

    private String apiUrlConnection() throws IOException, JSONException {
        /*
        String urlInfo = "http://api.odcloud.kr/api/15055082/v1/uddi:0a5f4af0-3d39-4510-b26a-e53c24ea3b1e_201909051519";
        String type = "json";
        StringBuilder apiUrl = new StringBuilder(urlInfo);
        apiUrl.append("?page=1&perPage=10");
        apiUrl.append("&" + URLEncoder.encode("serviceKey", "UTF-8") + "=" + key);
        apiUrl.append("&" + URLEncoder.encode("dataType", "UTF-8") + "=" + URLEncoder.encode(type, "UTF-8"));

        URL url = new URL(apiUrl.toString());

         */
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

    private void saveData(List<ApiData> dataList) {
        MyDBHelper dbHelper = new MyDBHelper(activity);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        for (ApiData data : dataList) {
            dbHelper.insertData(db, data);
        }

        dbHelper.close();
    }

    private void updateUIWithData(List<ApiData> dataList) {

        final MyAdapter adapter = new MyAdapter(getActivity(), android.R.layout.simple_list_item_1, dataList);
        list = view.findViewById(R.id.listView);

        // 리스트뷰에 어댑터 설정
        list.setAdapter(adapter);

        // 리스트뷰 아이템 클릭 이벤트 처리
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // 클릭한 아이템의 데이터 가져오기
                ApiData selectedItem = (ApiData) parent.getItemAtPosition(position);

                // AlertDialog를 띄우기 위해 Context 얻기
                Context context = getActivity();

                dialogView = (View) View.inflate(context, R.layout.dialog_modify, null);

                EditText edtYear = dialogView.findViewById(R.id.year);
                edtYear.setText(selectedItem.getYear());
                EditText edtIsExport = dialogView.findViewById(R.id.isExport);
                edtIsExport.setText(selectedItem.getIsExport());
                EditText edtCategory = dialogView.findViewById(R.id.category);
                edtCategory.setText(selectedItem.getCategory());
                EditText edtKg = dialogView.findViewById(R.id.kg);
                edtKg.setText(selectedItem.getKg());
                EditText edtUsd = dialogView.findViewById(R.id.usd);
                edtUsd.setText(selectedItem.getUsd());

                TextView tvName = dialogView.findViewById(R.id.name);
                tvName.setText("품목명 : "+selectedItem.getName());

                // 다이얼로그 생성
                AlertDialog.Builder dlg = new AlertDialog.Builder(context);
                dlg.setTitle("데이터 수정");
                dlg.setView(dialogView);
                dlg.setPositiveButton("적용", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 수정된 데이터를 가져오기
                        String updatedYear = edtYear.getText().toString();
                        String updatedIsExport = edtIsExport.getText().toString();
                        String updatedCategory = edtCategory.getText().toString();
                        String updatedKg = edtKg.getText().toString();
                        String updatedUsd = edtUsd.getText().toString();

                        // 수정된 데이터를 데이터베이스에 반영
                        updateData(selectedItem, updatedYear, updatedIsExport, updatedCategory, updatedKg, updatedUsd);

                        // 수정된 데이터를 데이터베이스에서 가져와서 UI 업데이트
                        updateUIWithUpdatedData(selectedItem.getName(), selectedItem.getYear(), selectedItem.getIsExport(), selectedItem.getCategory(), selectedItem.getKg(), selectedItem.getUsd());

                        // 데이터를 다시 불러와서 UI 업데이트
                        fetchDataFromAPI();
                    }
                });
                dlg.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getActivity(), "취소했습니다", Toast.LENGTH_SHORT).show();
                    }
                });
                dlg.show();
            }
        });

        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                // 길게 클릭한 아이템의 데이터 가져오기
                final ApiData selectedData = (ApiData) parent.getItemAtPosition(position);

                // AlertDialog를 띄우기 위해 Context 얻기
                Context context = getActivity();

                AlertDialog.Builder dlg = new AlertDialog.Builder(context);
                dlg.setTitle("삭제 확인");
                dlg.setMessage("삭제하시겠습니까?");

                dlg.setPositiveButton("삭제", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 데이터베이스에서 삭제
                        deleteData(selectedData);

                        // 리스트뷰에서 삭제
                        dataList.remove(selectedData);
                        adapter.notifyDataSetChanged();
                    }
                });

                dlg.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });

                dlg.show();

                return true;
            }
        });

    }

    private List<ApiData> getAllDataFromDB() {
        MyDBHelper dbHelper = new MyDBHelper(activity);
        return dbHelper.getAllData();
    }

    private void updateData(ApiData selectedItem, String modifiedYear, String modifiedIsExport, String modifiedCategory, String modifiedKg, String modifiedUsd) {
        MyDBHelper dbHelper = new MyDBHelper(activity);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("year", modifiedYear);
        values.put("is_export", modifiedIsExport);
        values.put("category", modifiedCategory);
        values.put("kg", modifiedKg);
        values.put("usd", modifiedUsd);

        String whereClause = MyDBHelper.COLUMN_NAME + "=? AND " +
                MyDBHelper.COLUMN_YEAR + "=? AND " +
                MyDBHelper.COLUMN_IS_EXPORT + "=?";

        String[] whereArgs = {selectedItem.getName(), selectedItem.getYear(), selectedItem.getIsExport()};

        // DB 업데이트
        db.update(MyDBHelper.TABLE_NAME, values, whereClause, whereArgs);

        dbHelper.close();

        updateUIWithUpdatedData(selectedItem.getName(), selectedItem.getYear(), selectedItem.getIsExport(), selectedItem.getCategory(), selectedItem.getKg(), selectedItem.getUsd());
    }

    private void deleteData(ApiData data) {
        MyDBHelper dbHelper = new MyDBHelper(getActivity());
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        String whereClause = MyDBHelper.COLUMN_NAME + "=? AND " +
                MyDBHelper.COLUMN_YEAR + "=? AND " +
                MyDBHelper.COLUMN_IS_EXPORT + "=?";

        String[] whereArgs = {data.getName(), data.getYear(), data.getIsExport()}; // 품목명, 년도, 수출입을 기준으로 삭제

        // 데이터베이스에서 삭제
        db.delete(MyDBHelper.TABLE_NAME, whereClause, whereArgs);

        dbHelper.close();
    }

    private void updateUIWithUpdatedData(String updatedItemName, String updatedYear, String updatedIsExport, String updatedCategory, String updatedKg, String updatedUsd) {
        MyDBHelper dbHelper = new MyDBHelper(activity);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        // 데이터베이스에서 수정된 데이터 가져오기
        List<ApiData> updatedDataList = dbHelper.getDataByName(updatedItemName);

        dbHelper.close();

        // 수정된 데이터를 찾아서 직접 반영
        for (ApiData updatedData : updatedDataList) {
            if (updatedData.getName().equals(updatedItemName)
                    && updatedData.getYear().equals(updatedYear)
                    && updatedData.getIsExport().equals(updatedIsExport)) {

                // 수정된 데이터로 갱신
                updatedData.setYear(updatedYear);
                updatedData.setIsExport(updatedIsExport);
                updatedData.setCategory(updatedCategory);
                updatedData.setKg(updatedKg);
                updatedData.setUsd(updatedUsd);

                break;
            }
        }

        // 어댑터에 수정된 데이터 설정
        MyAdapter adapter = (MyAdapter) list.getAdapter();
        adapter.setItems(updatedDataList);

        // 리스트뷰 갱신
        adapter.notifyDataSetChanged();
    }
}
