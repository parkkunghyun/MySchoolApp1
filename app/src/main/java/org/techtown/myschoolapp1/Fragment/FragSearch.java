package org.techtown.myschoolapp1.Fragment;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.google.firebase.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.techtown.myschoolapp1.ApiData;
import org.techtown.myschoolapp1.MyAdapter;
import org.techtown.myschoolapp1.MyDBHelper;
import org.techtown.myschoolapp1.R;
import org.techtown.myschoolapp1.SecondActivity;

import java.util.ArrayList;
import java.util.List;

public class FragSearch extends Fragment {
    private View view;
    private String TAG = "search 프래그먼트";
    Spinner year_spinner, isExport_spinner, category_spinner;
    EditText name_edit;

    TextView textView;
    View dialogView;

    ListView listView;
    MyAdapter adapter;

    String selectedYear;
    String selectedIsExport;
    String selectedCategory;
    String selectedName;


    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();


    DatabaseReference databaseReference;


    // 프래그먼트의 UI를 생성하고 반환
    // 검색 필터를 설정하는 버튼에 대한 클릭 리스너를 등록하고, 검색 결과를 표시할 리스트뷰를 설정
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.i(TAG, " onCreateView");
        view = inflater.inflate(R.layout.frag_search, container, false);

        textView = view.findViewById(R.id.tv2);

        databaseReference = FirebaseDatabase.getInstance().getReference();


        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });

        listView = view.findViewById(R.id.listView);
        adapter = new MyAdapter(getActivity(), R.layout.custom_listview, new ArrayList<ApiData>());
        listView.setAdapter(adapter);

        return view;
    }


    // 검색 필터를 설정하는 다이얼로그를 표시
    // 사용자가 검색 조건을 선택하고 '적용' 버튼을 클릭하면,
    // 선택된 조건에 맞는 데이터를 데이터베이스에서 가져와서 리스트뷰를 업데이트
    private void showDialog() {
        Context context = getActivity();
        dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_search, null);

        year_spinner = dialogView.findViewById(R.id.year_spinner);
        isExport_spinner = dialogView.findViewById(R.id.isExport_spinner);
        category_spinner = dialogView.findViewById(R.id.category_spinner);
        name_edit = dialogView.findViewById(R.id.name_edit);

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("검색 필터")
                .setView(dialogView)
                .setPositiveButton("적용", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        selectedYear = year_spinner.getSelectedItem().toString();
                        selectedIsExport = isExport_spinner.getSelectedItem().toString();
                        selectedCategory = category_spinner.getSelectedItem().toString();
                        selectedName = name_edit.getText().toString();

                        // 데이터베이스에서 선택된 조건에 맞는 데이터 가져오기
                        List<ApiData> searchData = fetchDataFromDB(selectedYear, selectedIsExport, selectedCategory, selectedName);


                        if(user.getEmail().toString().equals("12@naver.com")) {
                            updateListView(searchData);
                            Toast.makeText(getContext(), "success 권한.", Toast.LENGTH_LONG).show();
                        }else {
                            noAuthUserListView(searchData);
                            Toast.makeText(getContext(), "권한없습니다.", Toast.LENGTH_LONG).show();
                        }
                        // 가져온 데이터로 리스트뷰 업데이트
                        

                        Toast.makeText(getContext(), "검색 완료했습니다.", Toast.LENGTH_LONG).show();
                    }
                })
                .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        Toast.makeText(getContext(), "취소했습니다.", Toast.LENGTH_LONG).show();
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    // 데이터베이스에서 선택된 조건에 맞는 데이터를 가져오는 메서드
    private List<ApiData> fetchDataFromDB(String year, String isExport, String category, String name) {
        MyDBHelper dbHelper = new MyDBHelper(getActivity());
        return dbHelper.getDataByFilter(year, isExport, category, name);
    }

    // 수정된 데이터를 데이터베이스에서 가져와서 UI를 업데이트하는 메서드
    private void updateDataFromAPI() {
        // 데이터베이스에서 수정된 데이터를 가져오고 UI를 업데이트하는 로직 추가
        new Thread(new Runnable() {
            @Override
            public void run() {
                final List<ApiData> updatedData = fetchDataFromDB(selectedYear, selectedIsExport, selectedCategory, selectedName);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // 수정된 데이터를 어댑터에 설정
                        adapter.setItems(updatedData);
                        // 어댑터에 데이터 설정 후 즉시 반영되도록 notifyDataSetChanged() 호출
                        adapter.notifyDataSetChanged();
                    }
                });
            }
        }).start();
    }

    private void noAuthUserListView(List<ApiData> data) {
        adapter.setItems(data);
        // 어댑터에 데이터가 변경되었음을 알림
    }

        // 리스트뷰를 업데이트하여 검색 결과를 표시하는 메서드
    // 가져온 데이터를 어댑터에 설정하여 리스트뷰를 업데이트
    private void updateListView(List<ApiData> data) {
        adapter.setItems(data);
        adapter.notifyDataSetChanged(); // 어댑터에 데이터가 변경되었음을 알림

        List<ApiData> searchData = fetchDataFromDB(selectedYear, selectedIsExport, selectedCategory, selectedName);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // 클릭한 아이템의 데이터 가져오기
                ApiData selectedItem = (ApiData) parent.getItemAtPosition(position);
                Log.d("selected", selectedItem.getName().toString());

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

                /////////////////////////////////////////////////////////

                // 다이얼로그 생성
                android.app.AlertDialog.Builder dlg = new android.app.AlertDialog.Builder(context);
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

                        dbUpdateData(selectedItem, updatedYear, updatedIsExport, updatedCategory, updatedKg, updatedUsd);

                        updateDataFromAPI();

                        Toast.makeText(getContext(), "수정 완료했습니다.", Toast.LENGTH_LONG).show();
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

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                // 길게 클릭한 아이템의 데이터 가져오기
                final ApiData selectedData = (ApiData) parent.getItemAtPosition(position);

                // AlertDialog를 띄우기 위해 Context 얻기
                Context context = getActivity();

                android.app.AlertDialog.Builder dlg = new android.app.AlertDialog.Builder(context);
                dlg.setTitle("삭제 확인");
                dlg.setMessage("삭제하시겠습니까?");

                dlg.setPositiveButton("삭제", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 데이터베이스에서 삭제
                        deleteData(selectedData);

                        searchData.remove(selectedData);

                        updateDataFromAPI();

                        Toast.makeText(getContext(), "삭제 완료했습니다.", Toast.LENGTH_LONG).show();
                    }
                });

                dlg.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getContext(), "취소했습니다.", Toast.LENGTH_LONG).show();
                    }
                });

                dlg.show();
                return true;
            }
        });
    }

    private void dbUpdateData(ApiData selectedItem, String modifiedYear, String modifiedIsExport, String modifiedCategory, String modifiedKg, String modifiedUsd) {
        MyDBHelper dbHelper = new MyDBHelper(getActivity());

        Log.d("dbUpdateData", "dbUpdateData");

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        String modifiedCode = "";
        if (modifiedCategory.equals("식량작물")){
            modifiedCode = "1";
        }else if(modifiedCategory.equals("사료작물")) {
            modifiedCode = "2";
        } else if(modifiedCategory.equals("과수류")) {
            modifiedCode = "3";
        }else if(modifiedCategory.equals("채소류")) {
            modifiedCode = "4";
        }else if(modifiedCategory.equals("aa")) {
            modifiedCode = "5";
        }else if(modifiedCategory.equals("기타")) {
            modifiedCode = "9";
        }

        ContentValues values = new ContentValues();
        values.put("year", modifiedYear);
        values.put("is_export", modifiedIsExport);
        values.put("category", modifiedCategory);
        values.put("kg", modifiedKg);
        values.put("code", modifiedCode);
        values.put("usd", modifiedUsd);

        String whereClause = MyDBHelper.COLUMN_NAME + "=? AND " +
                MyDBHelper.COLUMN_YEAR + "=? AND " +
                MyDBHelper.COLUMN_IS_EXPORT + "=?";

        String[] whereArgs = {selectedItem.getName(), selectedItem.getYear(), selectedItem.getIsExport()};

        // DB 업데이트
        db.update(MyDBHelper.TABLE_NAME, values, whereClause, whereArgs);

        dbHelper.close();

        // db에 수정을 설정하고 어댑터에 수정된 부분을 넘겨서 수정된 부분이 화면에 나오게 설정
        updateUIWithUpdatedData(selectedItem, modifiedYear, modifiedIsExport, modifiedCategory, modifiedKg,modifiedUsd, modifiedCode);
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

    private void updateUIWithUpdatedData(ApiData selectedItem, String modifiedYear, String modifiedIsExport, String modifiedCategory, String modifiedKg, String modifiedUsd, String modifiedCode) {
        MyDBHelper dbHelper = new MyDBHelper(getActivity());
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        // 데이터베이스에서 수정된 데이터 가져오기
        List<ApiData> updatedDataList = dbHelper.getDataByName(selectedItem.getName());

        dbHelper.close();

        // 수정된 데이터를 찾아서 직접 반영

        for (ApiData updatedData : updatedDataList) {
            if (updatedData.getYear().equals(modifiedYear)
                    && updatedData.getIsExport().equals(modifiedIsExport)
                    && updatedData.getKg().equals(modifiedKg)) {

                // 수정된 데이터로 갱신
                updatedData.setYear(modifiedYear);
                updatedData.setIsExport(modifiedIsExport);
                updatedData.setCategory(modifiedCategory);
                updatedData.setKg(modifiedKg);
                updatedData.setUsd(modifiedUsd);
                updatedData.setCode(modifiedCode);

                break;
            }
        }


        try {
            // 어댑터에 수정된 데이터 설정
            MyAdapter adapter = (MyAdapter) listView.getAdapter();
            adapter.setItems(updatedDataList);

            // 리스트뷰 갱신
            adapter.notifyDataSetChanged();
            Log.d("list view success", "list view success");
        } catch (Exception e) {

        }
    }
}