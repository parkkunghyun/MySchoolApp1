package org.techtown.myschoolapp1.Fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import org.techtown.myschoolapp1.ApiData;
import org.techtown.myschoolapp1.MyAdapter;
import org.techtown.myschoolapp1.MyDBHelper;
import org.techtown.myschoolapp1.R;

import java.util.ArrayList;
import java.util.List;

public class FragSearch extends Fragment {
    private View view;
    private String TAG = "star 프래그먼트";
    Spinner year_spinner, isExport_spinner, category_spinner;
    EditText name_edit;

    TextView textView;
    View dialogView;

    ListView listView;
    MyAdapter adapter;

    // 프래그먼트의 UI를 생성하고 반환
    // 검색 필터를 설정하는 버튼에 대한 클릭 리스너를 등록하고, 검색 결과를 표시할 리스트뷰를 설정
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.i(TAG, " onCreateView");
        view = inflater.inflate(R.layout.frag_search, container, false);

        textView = view.findViewById(R.id.tv2);
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
                        String selectedYear = year_spinner.getSelectedItem().toString();
                        String selectedIsExport = isExport_spinner.getSelectedItem().toString();
                        String selectedCategory = category_spinner.getSelectedItem().toString();
                        String selectedName = name_edit.getText().toString();

                        // 데이터베이스에서 선택된 조건에 맞는 데이터 가져오기
                        List<ApiData> searchData = fetchDataFromDB(selectedYear, selectedIsExport, selectedCategory, selectedName);

                        // 가져온 데이터로 리스트뷰 업데이트
                        updateListView(searchData);
                    }
                })
                .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
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

    // 리스트뷰를 업데이트하여 검색 결과를 표시하는 메서드
    // 가져온 데이터를 어댑터에 설정하여 리스트뷰를 업데이트
    private void updateListView(List<ApiData> data) {
        adapter.setItems(data);
    }
}