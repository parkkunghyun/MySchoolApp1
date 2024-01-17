package org.techtown.myschoolapp1.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.techtown.myschoolapp1.Data;
import org.techtown.myschoolapp1.R;
import org.techtown.myschoolapp1.SecondActivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;


public class FragHome extends Fragment {
    private View view;
    private String TAG = "home프래그먼트";
    private SecondActivity activity;

    //
    public static ArrayList<Data> treeDataList;
    String key = "9jLPzVx%2BGOW1xmxuT3Zg0ylW%2BAox4i8%2BjBu4xPe%2B2W2r%2BuMwhr1YhfXwmiKFqfFFmOU6aNPMjTFt4g2cECEgSw%3D%3D";
    TextView text;
    Button btn;
    String string_data;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        activity = (SecondActivity) context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.i(TAG, " onCreateView");

        view = inflater.inflate(R.layout.frag_home, container, false);
        btn = view.findViewById(R.id.button);
        text = view.findViewById(R.id.textView);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            string_data = data();
                            StringBuilder sb = new StringBuilder();
                            for (int i = 0; i< treeDataList.size(); i++) {
                                Data d = treeDataList.get(i);
                                sb.append("{" + "\n");
                                sb.append(" 금액(USD) : " + d.getUsd() + "\n");
                                sb.append(" 년도 : " + d.getYear() + "\n");
                                sb.append(" 수출입 : " + d.getIsExport()+ "\n");
                                sb.append(" 작물분류 : " + d.getCategory() + "\n");
                                sb.append(" 품목 : " + d.getName() + "\n");
                                sb.append("}," + "\n");
                            }

                            activity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    text.setText(sb.toString());
                                }
                            });

                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        });

        return view;
    }



    private String data() throws IOException, JSONException {
        // 각각의 정보를 넣어주기
        String urlInfo = "http://api.odcloud.kr/api/15055082/v1/uddi:0a5f4af0-3d39-4510-b26a-e53c24ea3b1e_201909051519";
        String type ="json";
        StringBuilder apiUrl = new StringBuilder(urlInfo);
        apiUrl.append("?page=1&perPage=10");
        apiUrl.append("&" + URLEncoder.encode("serviceKey", "UTF-8") + "=" + key);

        apiUrl.append("&" + URLEncoder.encode("dataType", "UTF-8") + "=" + URLEncoder.encode(type, "UTF-8"));

        URL url = new URL(apiUrl.toString());
        System.out.println("url" + url);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Content-type", "application/json");

        BufferedReader rd;
        if(conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
            rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        } else {
            rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
        }
        StringBuilder sb = new StringBuilder();
        String line;
        while((line = rd.readLine()) != null) {
            sb.append(line);
        }
        rd.close();
        conn.disconnect();
        String result = sb.toString();

        JSONObject jsonObject = new JSONObject(result);
        JSONArray row = jsonObject.getJSONArray("data");

        treeDataList = new ArrayList<>();
        for (int i = 0; i<row.length(); i++) {
            JSONObject item = row.getJSONObject(i);
            treeDataList.add(new Data(
                    item.getString("금액(USD)"),
                    item.getString("년도"),
                    item.getString("수출입"),
                    item.getString("작물분류"),
                    item.getString("품목")
            ));
        }

        Log.d("map", treeDataList.toString());
        return result;

    }
}
