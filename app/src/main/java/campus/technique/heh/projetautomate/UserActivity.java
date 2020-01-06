package campus.technique.heh.projetautomate;

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
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.security.NoSuchAlgorithmException;

import campus.technique.heh.projetautomate.sql.DatabaseHelper;

public class UserActivity extends AppCompatActivity {


    //btn lecture
    private Button btn_lecture ;




    //variable pour récupérer l'ip,rack,slot
    private EditText edit_ip;
    private EditText edit_rack;
    private EditText edit_slot;

    private Button bt_main_ConnexS7;
    private TextView tv_main_plc;
//    private ReadTaskS7 readS7;
    private NetworkInfo network;
    private ConnectivityManager connexStatus;


    private String user_ecriture;

    private String  user_ecritureOnAutomate;


    private EditText edit_databloc;

    //variable qui récupére les données de l'automate (1)



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        Intent intent = getIntent();

        bt_main_ConnexS7 = (Button)findViewById(R.id.button_showAutomate);

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
                 user_ecriture = res.getString(res.getColumnIndex("ECRITURE"));
                user_ecritureOnAutomate = user_ecriture;
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




        String permissionShowUser = intent.getStringExtra("permissionShowUser");

        showBtnShowUser(permissionShowUser);


    }


    public void showBtnShowUser(String permission){
        View btn_showUser = (Button)findViewById(R.id.button_show_user);

        if(permission.equals("0")){
                    btn_showUser.setVisibility(View.GONE);
        }
    }





    public void onShowAutomate(View v) throws NoSuchAlgorithmException{

        //récupération des valeurs des edits text rack,slot,ip,Databloc
        edit_ip = (EditText)findViewById(R.id.edit_text_ip);
        edit_rack = (EditText)findViewById(R.id.edit_text_rack);
        edit_slot = (EditText)findViewById(R.id.edit_text_slot);
        edit_databloc = (EditText)findViewById(R.id.edit_text_databloc);

        //je récupére la valeur en string des edits text ip,rack,slot

        String edit_ip_string = edit_ip.getText().toString();
        String edit_rack_string = edit_rack.getText().toString();
        String edit_slot_string = edit_slot.getText().toString();
        String edit_databloc_string = edit_databloc.getText().toString();
        switch (v.getId()){
            case R.id.button_showAutomate:
                if(network != null && network.isConnectedOrConnecting()) {
                    Toast.makeText(this,edit_ip_string + " " + edit_rack_string + " "+ edit_slot_string + " " + edit_databloc_string, Toast.LENGTH_SHORT).show();
                    if(edit_ip_string.equals("") || edit_databloc_string.equals("") || edit_rack_string.equals("") || edit_slot_string.equals(""))
                    {
                        Toast.makeText(this, "Un champ manquant ! ", Toast.LENGTH_SHORT).show();

                    }
                    else{
                        if (bt_main_ConnexS7.getText().equals("Connexion_AutomateLiquide")) {
                            bt_main_ConnexS7.setText("Déconnexion_AutomateLiquide");
                            Intent intent = new Intent(this, AutomateRegulation.class);
                            intent.putExtra("ip", edit_ip_string);
                            intent.putExtra("rack", edit_rack_string);
                            intent.putExtra("slot", edit_slot_string);
                            intent.putExtra("permissionEcriture",user_ecritureOnAutomate);
                            intent.putExtra("databloc",edit_databloc_string);

                            startActivity(intent);



                        }
                        else{ bt_main_ConnexS7.setText("Connexion_AutomateLiquide"); }
                    }
                }
              

                else
                    {
                    Toast.makeText(this,"! Connexion réseau IMPOSSIBLE !",Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;

        }

    }



    public void onShowUser(View v)throws  NoSuchAlgorithmException{
        switch (v.getId()){
            case R.id.button_show_user:
                Intent intent = new Intent(this,ShowUser.class);
                startActivity(intent);


            default:
                break;

        }
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
