package com.example.qldanhba;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class UpdateActivity extends AppCompatActivity {
    final String DATABASE_NAME = "DanhBaDB.db";
    final int RESQUEST_TAKE_PHOTO = 123;
    final int REQUEST_CHOOSE_PHOTO = 321;
    int id =-1;

    Button btnChonHinh, btnChupHinh, btnLuu, btnHuy;
    EditText edtTen, edtSdt;
    ImageView imgHinhDaiDien;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        addControls();
        addEvents();
        initUI();

    }

    private void initUI() {
        Intent intent = getIntent();
        id = intent.getIntExtra("ID", -1);
        SQLiteDatabase database = Database.initDatabase(this, DATABASE_NAME);
        Cursor cursor = database.rawQuery("SELECT * FROM danhba WHERE ID = ?", new String[] { id +""});
        cursor.moveToFirst();
        String ten = cursor.getString(1);
        String sdt = cursor.getString(2);
        byte[] anh = cursor.getBlob(3);

        Bitmap bitmap = BitmapFactory.decodeByteArray(anh, 0, anh.length);
        imgHinhDaiDien.setImageBitmap(bitmap);;
        edtTen.setText(ten);
        edtSdt.setText(sdt);
    }

    private void addControls() {
        btnChonHinh = (Button) findViewById(R.id.btnChonHinh);
        btnChupHinh = (Button) findViewById(R.id.btnChupHinh);
        btnLuu = (Button) findViewById(R.id.btnLuu);
        btnHuy = (Button) findViewById(R.id.btnHuy);
        edtTen = (EditText) findViewById(R.id.edtTen);
        edtSdt = (EditText) findViewById(R.id.edtSdt);
        imgHinhDaiDien = (ImageView) findViewById(R.id.imgHinhDaiDien);

    }

    private void addEvents() {
        btnChonHinh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                choosePhoto();
            }
        });
        btnChupHinh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                takePicture();
            }
        });
        btnLuu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                update();
            }
        });
        btnHuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancel();
            }
        });
    }

    private void takePicture() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, RESQUEST_TAKE_PHOTO);
    }

    private void choosePhoto() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, REQUEST_CHOOSE_PHOTO);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CHOOSE_PHOTO) {
                try {
                    Uri imageUri = data.getData();
                    InputStream is = getContentResolver().openInputStream(imageUri);
                    Bitmap bitmap = BitmapFactory.decodeStream(is);
                    imgHinhDaiDien.setImageBitmap(bitmap);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            } else if (requestCode == RESQUEST_TAKE_PHOTO) {
                Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                imgHinhDaiDien.setImageBitmap(bitmap);
            }

        }
    }

    private void update() {
        String ten = edtTen.getText().toString();
        String sdt = edtSdt.getText().toString();

        byte[]  anh = getByteArrayFromImageView(imgHinhDaiDien);

        ContentValues contentValues = new ContentValues();
        contentValues.put("Ten", ten);
        contentValues.put("SDT", sdt);
        contentValues.put("Anh", anh);

        SQLiteDatabase database = Database.initDatabase(this, "DanhBaDB.db");
        database.update("DanhBa", contentValues, "id = ?", new String[] {id + ""});
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);

    }

    private void cancel() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private byte[] getByteArrayFromImageView(ImageView imgv){

        BitmapDrawable drawable = (BitmapDrawable) imgv.getDrawable();
        Bitmap bmp = drawable.getBitmap();

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        return byteArray;
    }
}