package campus.technique.heh.projetautomate;

import androidx.appcompat.app.AppCompatActivity;


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
                String mail  = res.getString(res.getColumnIndex("EMAIL"));
                if(res.getString(res.getColumnIndex("EMAIL")) == email.getText().toString() &&   res.getString(res.getColumnIndex("PASSWORD")) == password.getText().toString()      )
                {
                    Toast.makeText(getApplicationContext(), "bon login " + email.getText().toString() + " " + password.getText().toString(), Toast.LENGTH_SHORT).show();
                    Toast.makeText(getApplicationContext(),mail , Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "mauvais login "+ email.getText().toString() + " " + password.getText().toString(), Toast.LENGTH_SHORT).show();
                    Toast.makeText(getApplicationContext(),mail , Toast.LENGTH_SHORT).show();

                }

                if (!res.isClosed())  {
                    res.close();
                }




        }
    }
}
