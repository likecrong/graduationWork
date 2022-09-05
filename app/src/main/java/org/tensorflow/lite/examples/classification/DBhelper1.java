package org.tensorflow.lite.examples.classification;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBhelper1 extends SQLiteOpenHelper {
    private final int databaseSize = 60;

    //사용자의 게임 정보

    public DBhelper1(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    // DB를 새로 생성할 때 호출되는 함수
    @Override
    public void onCreate(SQLiteDatabase db) {
        // 새로운 테이블 생성
        db.execSQL("CREATE TABLE GAME (_id INTEGER, checking INTEGER);");
    }

    public int getDatabaseSize() {
        return databaseSize;
    }

    // DB 업그레이드를 위해 버전이 변경될 때 호출되는 함수
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void insert() {

        // 읽고 쓰기가 가능하게 DB 열기
        SQLiteDatabase db = getWritableDatabase();
        // DB에 입력한 값으로 행 추가

        // 금 1 은 2 동 3
        db.execSQL("INSERT INTO GAME VALUES(0, 0);");
        db.execSQL("INSERT INTO GAME VALUES(1, 0);");
        db.execSQL("INSERT INTO GAME VALUES(2, 0);");
        db.execSQL("INSERT INTO GAME VALUES(3, 0);");
        db.execSQL("INSERT INTO GAME VALUES(4, 0);");
        db.execSQL("INSERT INTO GAME VALUES(5, 0);");
        db.execSQL("INSERT INTO GAME VALUES(6, 0);");
        db.execSQL("INSERT INTO GAME VALUES(7, 0);");
        db.execSQL("INSERT INTO GAME VALUES(8, 0);");
        db.execSQL("INSERT INTO GAME VALUES(9, 0);");
        db.execSQL("INSERT INTO GAME VALUES(10, 0);");
        db.execSQL("INSERT INTO GAME VALUES(11, 0);");
        db.execSQL("INSERT INTO GAME VALUES(12, 0);");
        db.execSQL("INSERT INTO GAME VALUES(13, 0);");
        db.execSQL("INSERT INTO GAME VALUES(14, 0);");
        db.execSQL("INSERT INTO GAME VALUES(15, 0);");
        db.execSQL("INSERT INTO GAME VALUES(16, 0);");
        db.execSQL("INSERT INTO GAME VALUES(17, 0);");
        db.execSQL("INSERT INTO GAME VALUES(18, 0);");
        db.execSQL("INSERT INTO GAME VALUES(19, 0);");
        db.execSQL("INSERT INTO GAME VALUES(20, 0);");
        db.execSQL("INSERT INTO GAME VALUES(21, 0);");
        db.execSQL("INSERT INTO GAME VALUES(22, 0);");
        db.execSQL("INSERT INTO GAME VALUES(23, 0);");
        db.execSQL("INSERT INTO GAME VALUES(24, 0);");
        db.execSQL("INSERT INTO GAME VALUES(25, 0);");
        db.execSQL("INSERT INTO GAME VALUES(26, 0);");
        db.execSQL("INSERT INTO GAME VALUES(27, 0);");
        db.execSQL("INSERT INTO GAME VALUES(28, 0);");
        db.execSQL("INSERT INTO GAME VALUES(29, 0);");
        db.execSQL("INSERT INTO GAME VALUES(30, 0);");
        db.execSQL("INSERT INTO GAME VALUES(31, 0);");
        db.execSQL("INSERT INTO GAME VALUES(32, 0);");
        db.execSQL("INSERT INTO GAME VALUES(33, 0);");
        db.execSQL("INSERT INTO GAME VALUES(34, 0);");
        db.execSQL("INSERT INTO GAME VALUES(35, 0);");
        db.execSQL("INSERT INTO GAME VALUES(36, 0);");
        db.execSQL("INSERT INTO GAME VALUES(37, 0);");
        db.execSQL("INSERT INTO GAME VALUES(38, 0);");
        db.execSQL("INSERT INTO GAME VALUES(39, 0);");
        db.execSQL("INSERT INTO GAME VALUES(40, 0);");
        db.execSQL("INSERT INTO GAME VALUES(41, 0);");
        db.execSQL("INSERT INTO GAME VALUES(42, 0);");
        db.execSQL("INSERT INTO GAME VALUES(43, 0);");
        db.execSQL("INSERT INTO GAME VALUES(44, 0);");
        db.execSQL("INSERT INTO GAME VALUES(45, 0);");
        db.execSQL("INSERT INTO GAME VALUES(46, 0);");
        db.execSQL("INSERT INTO GAME VALUES(47, 0);");
        db.execSQL("INSERT INTO GAME VALUES(48, 0);");
        db.execSQL("INSERT INTO GAME VALUES(49, 0);");
        db.execSQL("INSERT INTO GAME VALUES(50, 0);");
        db.execSQL("INSERT INTO GAME VALUES(51, 0);");
        db.execSQL("INSERT INTO GAME VALUES(52, 0);");
        db.execSQL("INSERT INTO GAME VALUES(53, 0);");
        db.execSQL("INSERT INTO GAME VALUES(54, 0);");
        db.execSQL("INSERT INTO GAME VALUES(55, 0);");
        db.execSQL("INSERT INTO GAME VALUES(56, 0);");
        db.execSQL("INSERT INTO GAME VALUES(57, 0);");
        db.execSQL("INSERT INTO GAME VALUES(58, 0);");
        db.execSQL("INSERT INTO GAME VALUES(59, 0);");

        db.close();
    }

    public void update(int id, int checking) {
        SQLiteDatabase db = getWritableDatabase();
        // 입력한 항목과 일치하는 행의 가격 정보 수정
        db.execSQL("UPDATE GAME SET checking=" + checking + " WHERE _id ='" + id + "';");
        db.close();
    }

    public int[] getStageResult() {
        // 읽기가 가능하게 DB 열기
        SQLiteDatabase db = getReadableDatabase();
        int[] result = new int[getDatabaseSize()];

        Cursor cursor = db.rawQuery("SELECT * FROM GAME", null);
        int i = 0;
        while (cursor.moveToNext()) {
            result[i] = cursor.getInt(1);
            i++;
        }
        db.close();
        return result;
    }

    public void allClear() {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM GAME");

        db.close();
    }
}



