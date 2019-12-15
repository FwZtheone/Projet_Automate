package campus.technique.heh.projetautomate;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

import campus.technique.heh.projetautomate.sql.DatabaseHelper;

public class MainActivity extends AppCompatActivity {

    DatabaseHelper myDb ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
       myDb = new DatabaseHelper(this);

       myDb.insertContact("SUPERROOT","123","fabrizio","SUPERADMIN",true);
        ArrayList array_list = myDb.getAllContacts();

        if(array_list.size() != 0 )
        {
            finish();
            Intent connectionSuperRoot = new Intent(getApplicationContext(),ConnectionActivity.class);
            startActivity(connectionSuperRoot);



        }



    }


}
