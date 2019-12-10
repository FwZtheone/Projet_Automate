package campus.technique.heh.projetautomate;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.TextView;

import campus.technique.heh.projetautomate.sql.DatabaseHelper;

public class BoardSuperAdActivity extends AppCompatActivity {

    TextView text_email;
    TextView text_login;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board_super_ad);
        text_email = (TextView)findViewById(R.id.TextView_email);
        text_login = (TextView)findViewById(R.id.TextView_Login);
        DatabaseHelper db = new DatabaseHelper(this);
        Cursor res = db.getData("status");
        res.moveToFirst();
        String phrase = res.getString(res.getColumnIndex("EMAIL"));
        text_email.append(phrase);
        String phrase_login = res.getString((res.getColumnIndex("LOGIN")));
        text_login.append(phrase_login);

    }
}
