package org.techtown.myschoolapp1;

import android.content.Context;
import android.net.Uri;

import com.google.android.gms.common.api.Api;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class CSVParser {

    public CSVParser() {
        // 빈 생성자
    }

    public List<ApiData> getItemList(Context context, Uri uri) throws IOException {
        InputStream is = context.getContentResolver().openInputStream(uri);
        BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));

        String line = "";

        List<ApiData> dataList = new ArrayList<>();

        while ((line = reader.readLine()) != null) {
            String[] tokens = line.split(",");    // 각 행을 순차적으로 돌면서 , 를 기준으로 분리하여 배열에 저장한다.

            ApiData data = new ApiData();
            data.setYear(tokens[0]);
            data.setCode(tokens[1]);
            data.setCategory(tokens[2]);
            data.setName(tokens[3]);
            data.setIsExport(tokens[4]);
            data.setKg(tokens[5]);
            data.setUsd(tokens[6]);

            dataList.add(data);
        }

        reader.close();
        is.close();

        return dataList;
    }
}
