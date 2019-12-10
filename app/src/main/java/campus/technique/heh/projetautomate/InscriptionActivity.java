package campus.technique.heh.projetautomate;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InscriptionActivity extends AppCompatActivity {

    private static Pattern pattern;
    private static Matcher matcher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inscription);

    }



    public void onClickInscription(View v){
        switch (v.getId()){
            case R.id.valider:
                EditText edit_text_login = (EditText) findViewById(R.id.edit_Login_inscription);
                String edit_text_login_string = edit_text_login.getText().toString().toLowerCase();



                Toast.makeText(this, "", Toast.LENGTH_SHORT).show();

        }
    }
}
