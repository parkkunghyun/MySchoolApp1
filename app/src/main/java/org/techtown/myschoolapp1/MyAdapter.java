package org.techtown.myschoolapp1;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MyAdapter extends BaseAdapter {
    Context mContext;
    LayoutInflater mLayoutInflater = null;
    List<ApiData> items = new ArrayList<ApiData>();

    public MyAdapter(Context context, int simple_list_item_1, List<ApiData> data){
        mContext = context;
        items = data;
        mLayoutInflater = LayoutInflater.from(mContext);
    }


    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public ApiData getItem(int position) {
        return items.get(position);
    }

    public void addItem(ApiData item){
        items.add(item);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        mContext = parent.getContext();
        ApiData listItem = items.get(position);

        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.custom_listview, parent, false);
        }

        TextView yearText = convertView.findViewById(R.id.year);
        TextView codeText = convertView.findViewById(R.id.code);
        TextView categoryText = convertView.findViewById(R.id.category);
        TextView nameText = convertView.findViewById(R.id.name);
        TextView isExportText = convertView.findViewById(R.id.isExport);
        TextView kgText = convertView.findViewById(R.id.kg);
        TextView usdText = convertView.findViewById(R.id.usd);

        yearText.setText(listItem.getYear());
        codeText.setText(listItem.getCode());
        categoryText.setText(listItem.getCategory());
        nameText.setText(listItem.getName());
        isExportText.setText(listItem.getIsExport());
        kgText.setText(listItem.getKg());
        usdText.setText(listItem.getUsd());

        return convertView;
    }

    // setItems 메서드 추가
    public void setItems(List<ApiData> newItems) {
        items.clear(); // 기존 데이터를 모두 지움
        items.addAll(newItems); // 새로운 데이터를 추가
        notifyDataSetChanged(); // 어댑터에게 데이터 변경
    }


    /// 일단 수정도 삭제도 없었네

}
