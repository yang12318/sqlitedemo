package com.example.yang.sqlite;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private MyDatabaseHelper dbHelper;
    private final String TAG = "MainActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dbHelper = new MyDatabaseHelper(this, "Store.db", null, 1);
        Button btn_new = (Button) findViewById(R.id.btn_new);
        Button btn_create = (Button) findViewById(R.id.btn_create);
        Button btn_delete = (Button) findViewById(R.id.btn_delete);
        Button btn_retrieve = (Button) findViewById(R.id.btn_retrieve);
        Button btn_update = (Button) findViewById(R.id.btn_update);
        Button btn_select_all = (Button) findViewById(R.id.btn_select_all);

        btn_new.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dbHelper.getWritableDatabase();
                Toast.makeText(MainActivity.this, "数据库创建成功", Toast.LENGTH_SHORT).show();
            }
        });

        btn_create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                ContentValues values = new ContentValues();
                //开始组装第一条数据
                values.put("name", "The Da Vinci Code");
                values.put("author", "Dan Brown");
                values.put("pages", 454);
                values.put("price", 16.96);
                db.insert("Book", null, values);
                values.clear();		//清掉现有的内容
                //接着再组装第二条数据
                values.put("name", "基督山伯爵");
                values.put("author", "大仲马");
                values.put("pages", 1200);
                values.put("price", 52);
                db.insert("Book", null, values);
                Toast.makeText(MainActivity.this, "添加完成", Toast.LENGTH_SHORT).show();
            }
        });

        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                db.delete("Book", "pages > ?", new String[] {"500"});
                Toast.makeText(MainActivity.this, "删除完成", Toast.LENGTH_SHORT).show();
            }
        });

        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                ContentValues values  = new ContentValues();
                values.put("price", 1000);
                values.put("pages", 1000);
                db.update("Book", values, "name = ?", new String[] {"The Da Vinci Code"});
                Toast.makeText(MainActivity.this, "修改完成", Toast.LENGTH_SHORT).show();
            }
        });

        btn_retrieve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                Cursor cursor = db.query("Book", new String[] {"name", "price"}, "price > ?", new String[] {"20"}, "name", "count(name) > 5", "price desc");
                if(cursor.moveToFirst()) {
                    do {
                        String name = cursor.getString(cursor.getColumnIndex("name"));
                        double price = cursor.getDouble(cursor.getColumnIndex("price"));
                        Log.d(TAG, name + " " + price);
                    } while(cursor.moveToNext());
                }
                cursor.close();         //用过之后记得调用cursor的close函数
                Toast.makeText(MainActivity.this, "查询完成", Toast.LENGTH_SHORT).show();
            }
        });

        btn_select_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //查询Book表中所有数据
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                Cursor cursor = db.query("Book", null, null, null, null, null,null);
                if(cursor.moveToFirst()) {
                    do {
                        int id = cursor.getInt(cursor.getColumnIndex("id"));
                        String name = cursor.getString(cursor.getColumnIndex("name"));
                        String author = cursor.getString(cursor.getColumnIndex("author"));
                        int pages = cursor.getInt(cursor.getColumnIndex("pages"));
                        double price = cursor.getDouble(cursor.getColumnIndex("price"));
                        Log.d(TAG, id + name + " " + author + " " + pages + " " + price);
                    } while(cursor.moveToNext());
                }
                cursor.close();     //用过之后记得调用cursor的close函数
            }
        });
    }
}
