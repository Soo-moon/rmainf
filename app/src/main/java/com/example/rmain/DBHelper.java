package com.example.rmain;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.HashSet;
import java.util.Iterator;


class DBHelper extends SQLiteOpenHelper {

    HashSet<Dan> set = new HashSet<Dan>();


    public DBHelper(Context context, String name,  SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE Myword (mword TEXT, mmean);");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void insert(){
        SQLiteDatabase db = getWritableDatabase();
        Iterator<Dan> iterator = set.iterator();
        while (iterator.hasNext()){
            Dan d = iterator.next();
            db.execSQL("INSERT INTO Myword VALUES('"+d.word +"','"+d.mean+"');");
        }
        db.close();
    }

    public String getResult(){
        String result ="";
        Iterator<Dan> iterator = set.iterator();
        while (iterator.hasNext()){
            Dan d = iterator.next();
            result += "단어 : "+d.word+"   뜻 : "+d.mean+"\n";
        }
        return result;
    }

    public void creatset(){
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor =db.rawQuery("SELECT * FROM Myword",null);
        while (cursor.moveToNext()){
            set.add(new Dan(cursor.getString(0),cursor.getString(1)));
        }
    }

    public void add(String mword ,String mmean){
        set.add(new Dan(mword,mmean));
    }
}
