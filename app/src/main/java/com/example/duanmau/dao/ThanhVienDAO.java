package com.example.duanmau.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.duanmau.database.DbHelper;
import com.example.duanmau.model.ThanhVien;

import java.util.ArrayList;

public class ThanhVienDAO {
    DbHelper dbHelper;
    public ThanhVienDAO(Context context){
        dbHelper = new DbHelper(context);
    }
    public ArrayList<ThanhVien> getDSThanhVien(){
        ArrayList<ThanhVien> list = new ArrayList<>();
        SQLiteDatabase sqLiteDatabase = dbHelper.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM THANHVIEN",null);
        if (cursor.getCount()!=0){
            cursor.moveToFirst();
            do {
                list.add(new ThanhVien(cursor.getInt(0),cursor.getString(1),cursor.getString(2)));
            }while (cursor.moveToNext());
        }
        return list;
    }
    public boolean themThanhVien(String hoten, String namsinh){
        SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("hoten",hoten);
        contentValues.put("namsinh",namsinh);
        long check = sqLiteDatabase.insert("THANHVIEN",null,contentValues);
        if (check == -1)
            return false;
        return true;
    }
    public boolean capNhatThongTinTV(int matv, String hoten, String namsinh){
        SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("hoten", hoten);
        contentValues.put("namsinh",namsinh);
        long check = sqLiteDatabase.update("THANHVIEN",contentValues,"matv = ?", new String[]{String.valueOf(matv)});
        if (check == -1)
            return false;
        return true;
    }
    // int 1: xóa thành công. 0: thất bại, -1: tìm thấy thành viên đang có phiếu mượn
    public int xoaThongTinTV(int matv){
        SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM PHIEUMUON WHERE matv = ?", new String[]{String.valueOf(matv)});
        if (cursor.getCount() != 0){
            return -1;
        }
        long check = sqLiteDatabase.delete("THANHVIEN","matv = ?", new String[]{String.valueOf(matv)});
        if (check == -1)
            return 0;
        return 1;
    }
}
