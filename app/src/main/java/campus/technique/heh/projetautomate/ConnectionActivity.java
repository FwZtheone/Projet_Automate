package campus.technique.heh.projetautomate;

import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import campus.technique.heh.projetautomate.sql.DatabaseHelper;




public class ConnectionActivity extends AppCompatActivity  implements View.OnClickListener{

    EditText email;
    EditText password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connection);



    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_connection:
                EditText email = (EditText) findViewById(R.id.edit_email) ;
                EditText password = (EditText) findViewById(R.id.edit_passwd);

                DatabaseHelper myDb = new DatabaseHelper(this);
                Cursor res =  myDb.getData("status");

                res.moveToFirst();
                String mail_db  = res.getString(res.getColumnIndex("EMAIL"));
                String passwd_db = res.getString(res.getColumnIndex("PASSWORD"));
                String e = email.getText().toString().toLowerCase();
                String p = password.getText().toString().toLowerCase();
                if(e.equals(mail_db) && p.equals(passwd_db))
                {
                    Toast.makeText(getApplicationContext(), "bienvenu superAdmin" , Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(this, BoardSuperAdActivity.class);
                    startActivity(intent);
                    finish();
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "mauvais login/password" , Toast.LENGTH_SHORT).show();


                }

                if (!res.isClosed())  {
                    res.close();
                }




        }
    }
}
