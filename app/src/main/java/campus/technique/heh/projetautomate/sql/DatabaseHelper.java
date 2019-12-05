package campus.technique.heh.projetautomate.sql;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static  final String DATABASE_NAME = "ProjetAutomate.db";
    private static final String TABLE_NAME = "Database_user";
    private static final String COL_ID = "ID";
    private static final String COL_LOGIN = "LOGIN";
    private static final String COL_PASSWORD = "PASSWORD";
    private static final String COL_EMAIL = "EMAIL";
    private static final String COL_STATUS = "STATUS";






    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 1);
    }



    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME + " (ID INTEGER PRIMARY KEY AUTOINCREMENT,LOGIN text, PASSWORD text, EMAIL text, STATUS text)" );

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE " + TABLE_NAME);
            onCreate(db);
    }



    //renvoit une ligne de colonne sleon l'id
    public Cursor getData(String status) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from Database_user where STATUS="+status+"", null );
//        res.getString(res.getColumnIndex(COL_STATUS)) convert
        return res;
    }


    //affichage de la DB
    public ArrayList<String> getAllContacts(){
        ArrayList<String> array_list = new ArrayList<String>();
        SQLiteDatabase db =this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from Database_user",null);
        res.moveToFirst();
        while(res.isAfterLast() == false){
            array_list.add(res.getString(res.getColumnIndex(COL_STATUS)));
            res.moveToNext();
        }
        return array_list;
    }


    //insert contact
    public boolean insertContact(String LOGIN, String PASSWORD, String EMAIL, String STATUS){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("LOGIN", LOGIN);
        contentValues.put("PASSWORD", PASSWORD);
        contentValues.put("EMAIL", EMAIL);
        contentValues.put("STATUS", STATUS);
        db.insert("Database_user",null,contentValues);
        return true;

    }

}
