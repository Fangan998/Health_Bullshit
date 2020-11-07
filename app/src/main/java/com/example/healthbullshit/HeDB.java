package com.example.healthbullshit;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class HeDB {
    public SQLiteDatabase db = null;                        // 資料庫類別
    private final static String DATABASE_NAME = "db1.db";   // 資料庫名稱
    private final static String TABLE_NAME = "table01";     // 資料表名稱
    private final static String _ID = "_id";                // 資料表欄位
    private final static String NAME = "name";
    private final static String SAY = "say";

    // 建立資料表的欄位
    private final static String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME +
            " (" + _ID + " INTEGER PRIMARY KEY," + NAME + " TEXT," + SAY + " TEXT)";


    private Context mCtx = null;
    public HeDB(Context ctx) {                              // 建構式
        this.mCtx = ctx;                                    // 傳入 建立物件的 ＭainActivity
    }

    public void open() throws SQLException {                                                // 開啟已存在d資料庫
        db = mCtx.openOrCreateDatabase(DATABASE_NAME, 0, null);
        try {
            db.execSQL(CREATE_TABLE);                                                       // 建立資料表
        } catch (Exception e) {
        }
    }

    public void close() {                                                                   // 關閉資料庫
        db.close();
    }


    public Cursor getAll() {                                                                                    // 查詢all，只取出3個欄位
        return db.query(TABLE_NAME,
                new String[]{_ID, NAME, SAY},
                null, null, null, null, null, null);
    }

    public Cursor get(long rowId) throws SQLException {                                                         // 查詢指定 ID ，只取出3個欄位
        Cursor mCursor = db.query(TABLE_NAME,
                new String[]{_ID, NAME, SAY},
                NAME + "=" + rowId, null, null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

/*
    public Cursor sec(String name) throws SQLException{                                                     //查詢指定NAME
        Cursor sCursor = db.query(TABLE_NAME,
                new String[]{_ID,NAME,SAY},
                NAME + "="+ name,null,null,null,null,null);
        if (sCursor != null) {
            sCursor.moveToFirst();
        }
        return sCursor;
    }                                                                                                       //here more bug
                                                                                                            //error my life
*/
    public long append(String name, String say) {                                                           // 新增資料 *1
        ContentValues args = new ContentValues();
        args.put(NAME, name);
        args.put(SAY, say);
        return db.insert(TABLE_NAME, null, args);
    }

    public boolean delete(long rowId) {                                                                     //刪除指定資料
        return db.delete(TABLE_NAME, _ID + "=" + rowId, null) > 0;
    }

    public void deleteAll(){                                                                                //刪除所有資料
        db.delete(TABLE_NAME,null,null);                                              //debug use
    }

    public boolean update(long rowId, String name, String say) {                                            // 更新指定的資料
        ContentValues args = new ContentValues();
        args.put(NAME, name);
        args.put(SAY, say);
        return db.update(TABLE_NAME, args, _ID + "=" + rowId, null) > 0;
    }
}