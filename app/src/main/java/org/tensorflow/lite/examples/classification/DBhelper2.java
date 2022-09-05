package org.tensorflow.lite.examples.classification;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

//사용자의 설정 상태
//현재 레벨, 배경음, 효과음, 튜토리얼

public class DBhelper2 extends SQLiteOpenHelper {

    private final int databaseSize = 5;

    // 사용자 게임 환경

    // DBHelper 생성자로 관리할 DB 이름과 버전 정보를 받음
    public DBhelper2(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }
    public int getDatabaseSize() {return databaseSize; }
    // DB를 새로 생성할 때 호출되는 함수
    @Override
    public void onCreate(SQLiteDatabase db) {
        // 새로운 테이블 생성
        /* 이름은 STATE, 자동으로 값이 증가하는 _id 정수형 기본키 컬럼과
        checking  boolean 변수 관리*/
        db.execSQL("CREATE TABLE STATE (_id INTEGER, checking INTEGER);");
    }

    // DB 업그레이드를 위해 버전이 변경될 때 호출되는 함수
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void insert() {
        // 읽고 쓰기가 가능하게 DB 열기
        SQLiteDatabase db = getWritableDatabase();
        // DB에 입력한 값으로 행 추가
        // 현재 레벨, 배경음, 효과음, 튜토리얼, new
        //20.08.24_yeaji
        db.execSQL("INSERT INTO STATE VALUES(0, 1);"); //1부터 시작 해야 함
        db.execSQL("INSERT INTO STATE VALUES(1, 1);");
        db.execSQL("INSERT INTO STATE VALUES(2, 0);"); //건들지 말기
        db.execSQL("INSERT INTO STATE VALUES(3, 0);");
        db.execSQL("INSERT INTO STATE VALUES(4, 0);");

        db.close();
    }

    public void update(int id, int checking) {
        SQLiteDatabase db = getWritableDatabase();
        // 입력한 항목과 일치하는 행의 가격 정보 수정
        db.execSQL("UPDATE STATE SET checking=" + checking + " WHERE _id ='" + id + "';");
        db.close();
    }

    public int[] getStageResult() {
        // 읽기가 가능하게 DB 열기
        SQLiteDatabase db = getReadableDatabase();
        int[] result = new int[getDatabaseSize()];

        Cursor cursor = db.rawQuery("SELECT * FROM STATE", null);
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
        db.execSQL("DELETE FROM STATE");
        db.close();
    }
}







