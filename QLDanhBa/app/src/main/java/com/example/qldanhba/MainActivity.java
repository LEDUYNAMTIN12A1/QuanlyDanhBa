package com.example.qldanhba;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    final String DATABASE_NAME = "DanhBaDB.db";
    SQLiteDatabase database;

    ListView listView;
    ArrayList<DanhBa> list;
    AdapterDanhBa adapter;
    Button btnAdd, btnTim;
    EditText edtTim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addControls();
        readData();
        btnTim = findViewById(R.id.btnTim);
        btnTim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addControls();
                findData();
            }
        });
    }

    private  void addControls() {
        btnAdd = findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddActivity.class);
                startActivity(intent);
            }
        });

        listView = findViewById(R.id.listView);
        list = new ArrayList<>();
        adapter = new AdapterDanhBa(this, list);
        listView.setAdapter(adapter);;

    }

    private void readData() {
        database = Database.initDatabase(this, DATABASE_NAME);
        Cursor cursor = database.rawQuery("SELECT * FROM danhba", null);
        list.clear();
        for(int i=0; i<cursor.getCount(); i++) {
            cursor.moveToPosition(i);
            int id = cursor.getInt(0);
            String ten = cursor.getString(1);
            String sdt = cursor.getString(2);
            byte[] anh = cursor.getBlob(3);
            list.add(new DanhBa(id, ten, sdt, anh));

        }
        adapter.notifyDataSetChanged();
    }
    private void findData() {
        database = Database.initDatabase(this, DATABASE_NAME);
        edtTim = findViewById(R.id.edtTim);
        String string = edtTim.getText().toString();
        Cursor cursor = database.rawQuery("SELECT * FROM danhba where ten like '%"+string+"%'", null);
        list.clear();
        for(int i=0; i<cursor.getCount(); i++) {
            cursor.moveToPosition(i);
            int id = cursor.getInt(0);
            String ten = cursor.getString(1);
            String sdt = cursor.getString(2);
            byte[] anh = cursor.getBlob(3);
            list.add(new DanhBa(id, ten, sdt, anh));

        }
        adapter.notifyDataSetChanged();
    }

}