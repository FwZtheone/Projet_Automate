package campus.technique.heh.projetautomate;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;

import java.util.ArrayList;

import campus.technique.heh.projetautomate.sql.DatabaseHelperSuperAdmin;

public class MainActivity extends AppCompatActivity {

    DatabaseHelperSuperAdmin myDb ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
       myDb = new DatabaseHelperSuperAdmin(this);

    }








}
