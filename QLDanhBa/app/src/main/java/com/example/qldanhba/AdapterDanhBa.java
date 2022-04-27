package com.example.qldanhba;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class AdapterDanhBa extends BaseAdapter {
    Activity context;
    ArrayList<DanhBa> list;

    public AdapterDanhBa(Activity context, ArrayList<DanhBa> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(R.layout.listview_row, null);
        ImageView imgHinhDaiDien = (ImageView) row.findViewById(R.id.imgHinhDaiDien);
        TextView txtId = (TextView) row.findViewById(R.id.txtId);
        TextView txtTen = (TextView) row.findViewById(R.id.txtTen);
        TextView txtSdt = (TextView) row.findViewById(R.id.txtSdt);
        Button btnXoa = (Button) row.findViewById(R.id.btnXoa);
        Button btnSua = (Button) row.findViewById(R.id.btnSua);

        DanhBa danhBa = list.get(i);
        txtId.setText(danhBa.id + "");
        txtTen.setText(danhBa.ten);
        txtSdt.setText((danhBa.sdt));

        Bitmap bmHinhDaiDien = BitmapFactory.decodeByteArray(danhBa.anh, 0, danhBa.anh.length);
        imgHinhDaiDien.setImageBitmap(bmHinhDaiDien);

        btnSua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, UpdateActivity.class);
                intent.putExtra("ID", danhBa.id);
                context.startActivity(intent);
            }
        });

        btnXoa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setIcon(android.R.drawable.ic_delete);
                builder.setTitle("Xác nhận xóa");
                builder.setMessage("Bạn có chắc chắn muốn xóa người này?");
                builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        delete(danhBa.id);
                    }
                });
                builder.setNegativeButton("Không", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        return row;
    }

    private void delete(int idDanhBa) {
        SQLiteDatabase database = Database.initDatabase(context, "DanhBaDB.db");
        database.delete("DanhBa", "ID = ?", new String[] {idDanhBa +""});

        list.clear();
        Cursor cursor = database.rawQuery("SELECT * FROM danhba", null);
        while (cursor.moveToNext()) {
            int id = cursor.getInt(0);
            String ten = cursor.getString(1);
            String sdt = cursor.getString(2);
            byte[] anh = cursor.getBlob(3);

            list.add(new DanhBa(id, ten, sdt, anh));
        }
        notifyDataSetChanged();
    }
}
