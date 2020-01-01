package campus.technique.heh.projetautomate;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import campus.technique.heh.projetautomate.sql.DatabaseHelper;

public class BoardUser extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board_user);

        Intent intent = getIntent();

        if(intent != null)
        {
            String email = "";
            if(intent.hasExtra("key_edit_text_email"))
            {
                email = intent.getStringExtra("key_edit_text_email");
                TextView vue_account = (TextView)findViewById(R.id.text_view_account_login_user);
                vue_account.append(email);

           




            }
        }
    }



}
