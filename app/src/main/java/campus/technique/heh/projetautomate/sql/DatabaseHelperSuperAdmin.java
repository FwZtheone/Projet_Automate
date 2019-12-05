package campus.technique.heh.projetautomate.sql;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DatabaseHelperSuperAdmin extends SQLiteOpenHelper {

    public static  final String DATABASE_NAME = "ProjetAutomate.db";
    private static final String TABLE_NAME = "SuperAdmin";
    private static final String COL_1 = "ID";
    private static final String COL_2 = "LOGIN";
    private static final String COL_3 = "PASSWORD";
    private static final String COL_4 = "EMAIL";






    public DatabaseHelperSuperAdmin(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 1);
    }



    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME );

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("drop table " + TABLE_NAME);
    }
}
