package campus.technique.heh.projetautomate;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.security.NoSuchAlgorithmException;

import campus.technique.heh.projetautomate.sql.DatabaseHelper;

public class UserActivity extends AppCompatActivity {


    //btn lecture
    private Button btn_lecture ;



    //variable pour récupérer l'ip,rack,slot
    private EditText edit_ip;
    private EditText edit_rack;
    private EditText edit_slot;

    private ProgressBar pb_main_progressionS7;
    private Button bt_main_ConnexS7;
    private TextView tv_main_plc;
    private ReadTaskS7 readS7;
    private NetworkInfo network;
    private ConnectivityManager connexStatus;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        Intent intent = getIntent();



        pb_main_progressionS7= (ProgressBar)findViewById(R.id.pb_main_progressionS7);
        bt_main_ConnexS7 = (Button)findViewById(R.id.button_showAutomate);
        tv_main_plc= (TextView)findViewById(R.id.tv_main_plc);
        connexStatus= (ConnectivityManager)this.getSystemService(Context.CONNECTIVITY_SERVICE);
        network = connexStatus.getActiveNetworkInfo();

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







    public void onShowAutomate(View v) throws NoSuchAlgorithmException{

        //récupération des valeurs des edits text rack,slot,ip
        edit_ip = (EditText)findViewById(R.id.edit_text_ip);
        edit_rack = (EditText)findViewById(R.id.edit_text_rack);
        edit_slot = (EditText)findViewById(R.id.edit_text_slot);

        //je récupére la valeur en string des edits text ip,rack,slot

        String edit_ip_string = edit_ip.getText().toString();
        String edit_rack_string = edit_rack.getText().toString();
        String edit_slot_string = edit_slot.getText().toString();
        switch (v.getId()){
            case R.id.button_showAutomate:

                if(network != null && network.isConnectedOrConnecting()) {
                    if (bt_main_ConnexS7.getText().equals("Connexion_S7")){
                    Toast.makeText(this,network.getTypeName(),Toast.LENGTH_SHORT).show();
                    bt_main_ConnexS7.setText("Déconnexion_S7");
                    readS7 = new ReadTaskS7(v,bt_main_ConnexS7, pb_main_progressionS7, tv_main_plc);
                    readS7.Start(edit_ip_string,edit_rack_string, edit_slot_string);

                    //activation du btn de lecture de l'automate !!
                        btn_lecture = (Button)findViewById(R.id.button_lecture);
                        btn_lecture.setEnabled(true);
                } else{
                        btn_lecture = (Button)findViewById(R.id.button_lecture);
                        btn_lecture.setEnabled(false);
                    readS7.Stop();
                    bt_main_ConnexS7.setText("Connexion_S7");
                    Toast.makeText(getApplication(), "Traitement interrompu par l'utilisateur !!! ", Toast.LENGTH_LONG).show();
                }
                } else {
                    Toast.makeText(this,"! Connexion réseau IMPOSSIBLE !",Toast.LENGTH_SHORT).show();
                } break;
            default:
                break;

        }

    }
    
    
    public void onLecture(View v){

        Toast.makeText(this, "lecture de l'automate", Toast.LENGTH_SHORT).show();
    }



    public void onDeconnect(View v ) throws  NoSuchAlgorithmException{

        switch (v.getId()){

            case R.id.button_deco:
                finish();

            default:
                break;
        }
    }


}
