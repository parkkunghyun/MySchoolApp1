package org.techtown.myschoolapp1;

import android.content.Context;

import com.google.android.gms.common.api.Api;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class CsvParser {
    public CsvParser() {

    }
    public List<ApiData> getDataList(Context context) throws IOException {
        InputStream is = context.getResources().openRawResource(R.raw.csvdata);    // res/raw/item.csv 파일을 불러오기 위해 해당 코드 작성
        BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));

        String line = "";

        List<ApiData> itemList = new ArrayList<>();

        while ((line = reader.readLine()) != null) {
            String[] tokens = line.split(",");    // 각 행을 순차적으로 돌면서 , 를 기준으로 분리하여 배열에 저장한다.

            ApiData apiData = new ApiData();
            apiData.setYear(tokens[0]);
            apiData.setCode(tokens[1]);
            apiData.setCategory(tokens[2]);
            apiData.setName(tokens[3]);
            apiData.setIsExport(tokens[4]);
            apiData.setKg(tokens[5]);
            apiData.setUsd(tokens[6]);
            itemList.add(apiData); // 반환할 리스트에 파싱된 행 데이터 저장
        }
        reader.close();
        is.close();

        return itemList;
    }
}
