package org.tensorflow.lite.examples.classification;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

public class setDB {

    DBhelper1 dbHelper1;
    DBhelper2 dbHelper2;
    Context con;

    setDB (Context con) {
        this.con = con;
        dbHelper1 = new DBhelper1(con, "GAME.db", null, 1);
        dbHelper2 = new DBhelper2(con, "STATE.db", null, 2);
    }

    public void reset_first() {
        dbHelper1.allClear();
        dbHelper1.insert();
        dbHelper2.allClear();
        dbHelper2.insert();
    }

    public void reset() {
        dbHelper1.allClear();
        dbHelper1.insert();
        dbHelper2.update(0,1);
        dbHelper2.update(3,0);
        dbHelper2.update(4,0);
    }

    public int [] getDB1() {return dbHelper1.getStageResult();}
    public int [] getDB2() {return dbHelper2.getStageResult();}

    public void update(int index,  int data, int num){
        if (num == 1) {
            dbHelper1.update(index,data);
        }
        else if (num == 2) {
            dbHelper2.update(index,data);
        }
    }

    public void printDB(int [] d, int n, int v) {
        if(v == 1) {
//            Log.d("qqwwe","DB1 : \n");
        }
        else if (v == 2) {
//            Log.d("qqwwe","DB2 : \n");
        }
        for(int i= 0; i < n; i++) {
//            Log.d("qqwwe", i + " : " + d[i]);
        }
    }
}