package org.techtown.myschoolapp1;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class MyDBHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "seeds_DB";

    public static final String TABLE_NAME = "seeds_api_TBL";
    public static final String COLUMN_YEAR = "year";
    public static final String COLUMN_CODE = "code";
    public static final String COLUMN_CATEGORY = "category";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_IS_EXPORT = "is_export";
    public static final String COLUMN_KG = "kg";
    public static final String COLUMN_USD = "usd";

    public MyDBHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // 테이블 생성 쿼리
        String createTableQuery = "CREATE TABLE " + TABLE_NAME + " ("
                + COLUMN_YEAR + " TEXT, "
                + COLUMN_CODE + " TEXT, "
                + COLUMN_CATEGORY + " TEXT, "
                + COLUMN_NAME + " TEXT, "
                + COLUMN_IS_EXPORT + " TEXT, "
                + COLUMN_KG + " TEXT, "
                + COLUMN_USD + " TEXT)";
        db.execSQL(createTableQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    // OpenAPI로부터 받은 데이터를 SQLite에 삽입하는 메서드
    public void insertData(SQLiteDatabase db, ApiData data) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_YEAR, data.getYear());
        values.put(COLUMN_CODE, data.getCode());
        values.put(COLUMN_CATEGORY, data.getCategory());
        values.put(COLUMN_NAME, data.getName());
        values.put(COLUMN_IS_EXPORT, data.getIsExport());
        values.put(COLUMN_KG, data.getKg());
        values.put(COLUMN_USD, data.getUsd());

        db.insert(TABLE_NAME, null, values);
    }

    // SQLite에서 데이터를 조회하는 메서드
    public List<ApiData> getAllData() {
        List<ApiData> dataList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);

        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range")
                ApiData data = new ApiData(
                        cursor.getString(cursor.getColumnIndex(COLUMN_YEAR)),
                        cursor.getString(cursor.getColumnIndex(COLUMN_CODE)),
                        cursor.getString(cursor.getColumnIndex(COLUMN_CATEGORY)),
                        cursor.getString(cursor.getColumnIndex(COLUMN_NAME)),
                        cursor.getString(cursor.getColumnIndex(COLUMN_IS_EXPORT)),
                        cursor.getString(cursor.getColumnIndex(COLUMN_KG)),
                        cursor.getString(cursor.getColumnIndex(COLUMN_USD))
                );
                dataList.add(data);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return dataList;
    }

    public List<ApiData> getDataByName(String name) {
        List<ApiData> dataList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {COLUMN_YEAR, COLUMN_CODE, COLUMN_CATEGORY, COLUMN_NAME, COLUMN_IS_EXPORT, COLUMN_KG, COLUMN_USD};
        String selection = COLUMN_NAME + "=?";
        String[] selectionArgs = {name};
        Cursor cursor = db.query(TABLE_NAME, columns, selection, selectionArgs, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range")
                ApiData data = new ApiData(
                        cursor.getString(cursor.getColumnIndex(COLUMN_YEAR)),
                        cursor.getString(cursor.getColumnIndex(COLUMN_CODE)),
                        cursor.getString(cursor.getColumnIndex(COLUMN_CATEGORY)),
                        cursor.getString(cursor.getColumnIndex(COLUMN_NAME)),
                        cursor.getString(cursor.getColumnIndex(COLUMN_IS_EXPORT)),
                        cursor.getString(cursor.getColumnIndex(COLUMN_KG)),
                        cursor.getString(cursor.getColumnIndex(COLUMN_USD))
                );
                dataList.add(data);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return dataList;
    }


    // 필터로 데이터를 조회하여 리스트로 반환함
    // 연도, 수출 여부, 작물 분류, 품목에 따라 데이트를 필터링하여 조회함
    public List<ApiData> getDataByFilter(String year, String isExport, String category, String name) {
        List<ApiData> dataList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {COLUMN_YEAR, COLUMN_CODE, COLUMN_CATEGORY, COLUMN_NAME, COLUMN_IS_EXPORT, COLUMN_KG, COLUMN_USD};
        StringBuilder selectionBuilder = new StringBuilder();
        List<String> selectionArgsList = new ArrayList<>();

        // 필터 조건 추가
        if (!year.equals("전체 기간")) {
            selectionBuilder.append(COLUMN_YEAR).append("=? ");
            selectionArgsList.add(year);
        }
        if (!isExport.equals("전체")) {
            if (selectionBuilder.length() > 0) {
                selectionBuilder.append("AND ");
            }
            selectionBuilder.append(COLUMN_IS_EXPORT).append("=? ");
            selectionArgsList.add(isExport);
        }
        if (!category.equals("전체")) {
            if (selectionBuilder.length() > 0) {
                selectionBuilder.append("AND ");
            }
            selectionBuilder.append(COLUMN_CATEGORY).append("=? ");
            selectionArgsList.add(category);
        }
        if (!name.equals("전체")) {
            if (selectionBuilder.length() > 0) {
                selectionBuilder.append("AND ");
            }
            selectionBuilder.append(COLUMN_NAME).append("=? ");
            selectionArgsList.add(name);
        }

        String[] selectionArgs = new String[selectionArgsList.size()];
        selectionArgsList.toArray(selectionArgs);

        Cursor cursor = db.query(TABLE_NAME, columns, selectionBuilder.toString(), selectionArgs, null, null, null);

        // 커서를 사용하여 필터 값에 해당하는 데이터를 가져옴
        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range")
                ApiData data = new ApiData(
                        cursor.getString(cursor.getColumnIndex(COLUMN_YEAR)),
                        cursor.getString(cursor.getColumnIndex(COLUMN_CODE)),
                        cursor.getString(cursor.getColumnIndex(COLUMN_CATEGORY)),
                        cursor.getString(cursor.getColumnIndex(COLUMN_NAME)),
                        cursor.getString(cursor.getColumnIndex(COLUMN_IS_EXPORT)),
                        cursor.getString(cursor.getColumnIndex(COLUMN_KG)),
                        cursor.getString(cursor.getColumnIndex(COLUMN_USD))
                );
                dataList.add(data);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return dataList;
    }
}
