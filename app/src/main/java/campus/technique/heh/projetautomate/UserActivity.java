package campus.technique.heh.projetautomate;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Html;
import android.widget.TextView;
import android.widget.Toast;

import campus.technique.heh.projetautomate.sql.DatabaseHelper;

public class UserActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        Intent intent = getIntent();

        if(intent != null){
            String email ="";
            String password = "";

            if(intent.hasExtra("user_email")){
                email = intent.getStringExtra("user_email");
                TextView textView_user = findViewById(R.id.TextView_user_login);
                DatabaseHelper mydb = new DatabaseHelper(this);

                Cursor res = mydb.getContact("*",email);
                res.moveToFirst();
                String login = res.getString(res.getColumnIndex("LOGIN"));
                TextView textView = (TextView) findViewById(R.id.text_view_title);
                textView.setText(Html.fromHtml(getString(R.string.title_user)));
                textView_user.append(login);

                //création des textview pour le password etc..
                TextView textView_user_email = findViewById(R.id.TextView_user_email);
                TextView textView_user_password = findViewById(R.id.TextView_user_password);
                TextView textView_user_ecriture = findViewById(R.id.TextView_user_ecriture);

                String user_email = res.getString(res.getColumnIndex("EMAIL"));
                password = intent.getStringExtra("user_password");
                String user_ecriture = res.getString(res.getColumnIndex("ECRITURE"));
                //modification de l'affichage du mode écriture
                if(user_ecriture.equals("0")){
                    user_ecriture = "lecture";
                }
                else{
                    user_ecriture = "lecture et ecriture";
                }


                //insertion des données dans les textview
                textView_user_email.append(user_email);
                textView_user_password.append(password);
                textView_user_ecriture.append(user_ecriture);




            }

        }





    }







}
