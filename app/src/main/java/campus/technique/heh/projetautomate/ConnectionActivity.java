package campus.technique.heh.projetautomate;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.security.NoSuchAlgorithmException;

import campus.technique.heh.projetautomate.sql.DatabaseHelper;










public class ConnectionActivity extends AppCompatActivity  implements View.OnClickListener{


   private String permissionShowUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connection);



    }





    public boolean connectionUser(String email,String password){
        DatabaseHelper mydb = new DatabaseHelper(this);

        Cursor rs = mydb.getContact("*",email,password);

        if(rs.moveToFirst()){
            return true;
        }


        return false;
        
    }





    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_newUser:
                Intent intent = new Intent(this,InscriptionActivity.class);
                startActivity(intent);

                default:
                    break;

        }
    }


    public void onClickConnection(View v) throws NoSuchAlgorithmException {
        switch(v.getId()){
            case R.id.button_connection:
                EditText edit_text_email = (EditText) findViewById(R.id.edit_email) ;
                EditText edit_text_password = (EditText) findViewById(R.id.edit_passwd);

                String edit_text_email_string = edit_text_email.getText().toString();
                String edit_text_password_string = edit_text_password.getText().toString();

                if(connectionUser(edit_text_email_string, InscriptionActivity.toHexString(InscriptionActivity.getSHA(edit_text_password_string)) ))
                {
                    DatabaseHelper mydb = new DatabaseHelper(this);
                    Cursor res =mydb.getContact("*", edit_text_email_string);

                    res.moveToFirst();
                    String status = res.getString(res.getColumnIndex("STATUS"));
                    if(status.equals("SUPERROOT"))
                    {
                        permissionShowUser = "1";
                        Intent intent = new Intent(this,UserActivity.class);
                        intent.putExtra("user_email",edit_text_email_string);
                        intent.putExtra("user_password",edit_text_password_string);
                        intent.putExtra("permissionShowUser", permissionShowUser);
                        startActivity(intent);
                        finish();

                    }
                    else{

                        permissionShowUser ="0";
                   Intent intent = new Intent(this,UserActivity.class);
                   intent.putExtra("user_email",edit_text_email_string);
                   intent.putExtra("user_password",edit_text_password_string);
                   intent.putExtra("permissionShowUser", permissionShowUser);


                        startActivity(intent);
                    finish();
                    }
                }
                else{
                    Toast.makeText(this, "login/mot de passe invalide", Toast.LENGTH_SHORT).show();
                }

         

        }
    }
}
