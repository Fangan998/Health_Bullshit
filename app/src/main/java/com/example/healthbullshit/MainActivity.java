package com.example.healthbullshit;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class MainActivity extends AppCompatActivity {
   //Setting db                                                                                     //重複型動作 的 代號
    private HeDB db = null;                                                                         //動作    loadToList = 載入資料表至 ListView 中
   //Setting all to use Item                                                                        //動作    loadAll    = 載入全部資料
    Button btn_add,btn_edit,btn_sec,btn_del,btn_clear;
    ListView ListV_list;
    EditText editText_id,editText_con;
    long thisId;
    Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //button
        btn_add=(Button)findViewById(R.id.btn_add);
        btn_del=(Button)findViewById(R.id.btn_delete);
        btn_edit=(Button)findViewById(R.id.btn_edit);
        btn_sec=(Button)findViewById(R.id.btn_search);
        btn_clear=(Button)findViewById(R.id.btn_clear);
        //listView
        ListV_list=(ListView)findViewById(R.id.listV_list);
        //EditText
        editText_id=(EditText)findViewById(R.id.edtxt_id);
        editText_con=(EditText)findViewById(R.id.edtxt_con);

        //listener form Button
        btn_add.setOnClickListener(listener_1);
        btn_sec.setOnClickListener(listener_1);
        btn_del.setOnClickListener(listener_1);
        btn_edit.setOnClickListener(listener_1);
        btn_clear.setOnClickListener(listener_1);
        //listener form ListView
        ListV_list.setOnItemClickListener(listener_list);
        // 建立 HeDB 物件
        db =new HeDB(this);
        db.open();
        cursor=db.getAll();                                                                         // loadAll
        UpdateAdapter(cursor);                                                                      //loadToList
        //

    }

    private void ShowData(long id){                                                                 //顯示單筆資料
        Cursor c = db.get(id);
        thisId = id;                                                                                // 取得  _id 欄位
        editText_id.setText(c.getString(1));                                            // name 欄位
        editText_con.setText(c.getString(2));                                           // context 欄位
    }

    View.OnClickListener listener_1 = new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            try {
                switch (v.getId()){
                    case R.id.btn_add:{                                                             //新增資料
                        String name=editText_id.getText().toString();
                        String say=editText_con.getText().toString();
                        if ( db.append(name,say)>0){
                            cursor=db.getAll();                                                     // loadAll
                            UpdateAdapter(cursor);                                                  // loadToList
                            ClearEdit();
                        }
                        break;
                    }

                    case R.id.btn_delete:{                                                          //Delete
                            if (cursor != null && cursor.getCount() >= 0){
                                AlertDialog.Builder builder=new AlertDialog.Builder(MainActivity.this);
                                builder.setTitle("確定刪除");
                                builder.setMessage("確定要刪除這筆資料?");
                                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int i) {
                                    }
                                });
                                builder.setPositiveButton("確定",new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int i) {
                                        if (db.delete(thisId)){
                                            cursor=db.getAll();                                     // loadAll
                                            UpdateAdapter(cursor);                                  // loadToList
                                            ClearEdit();
                                        }
                                    }
                                });
                                builder.show();
                            }
                            break;
                    }

                    case R.id.btn_edit:{
                        String name=editText_id.getText().toString();
                        String say=editText_con.getText().toString();
                        if (db.update(thisId,name,say)){
                            cursor=db.getAll();                                                      // loadAll
                            UpdateAdapter(cursor);                                                   // loadToList
                        }
                        ClearEdit();
                        break;
                    }
                    case R.id.btn_search:{
                        /*

                        String id = editText_id.getText().toString();
                        cursor=db.sec(id);
                        UpdateAdapter(cursor); // 載入資料表至 ListView 中
                                                                                                    //**bug
                         */

                        long id = Integer.parseInt(editText_id.getText().toString());
                        cursor=db.get(id);
                        UpdateAdapter(cursor);                                                      // loadToList
                        break;
                    }
                    case R.id.btn_clear:{

                        cursor=db.getAll();                                                         // loadAll after search
                        UpdateAdapter(cursor);                                                      // loadToList
                        ClearEdit();
                        break;

                        //db.deleteAll();                                                           //**debug use
                    }
                }
            }catch (Exception er){

            }

        }
    };

    AdapterView.OnItemClickListener listener_list = new AdapterView.OnItemClickListener(){           //listener from list
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ShowData(id);
                cursor.moveToPosition(position);
        }
    };

    public void ClearEdit(){                                                                         //清空 all EditText 的 Class
        editText_id.setText("");
        editText_con.setText("");
    }

    public void UpdateAdapter(Cursor cursor){                                                        //loadToList 的 Class
        if (cursor != null && cursor.getCount() >= 0){
            SimpleCursorAdapter adapter = new SimpleCursorAdapter(this,
                    android.R.layout.simple_list_item_2,                                            // 包含兩個資料項
                    cursor,                                                                         // 資料庫的 Cursors 物件
                    new String[] { "name", "say" },                                                 // name、say  欄位
                    new int[] { android.R.id.text1, android.R.id.text2 },
                    0);
            ListV_list.setAdapter(adapter);                                                         // 將adapter增加到listview01中
        }
    }
}
