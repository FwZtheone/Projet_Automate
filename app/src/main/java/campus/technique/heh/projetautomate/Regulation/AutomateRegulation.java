package campus.technique.heh.projetautomate.Regulation;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import campus.technique.heh.projetautomate.R;
import campus.technique.heh.projetautomate.simatic_s7.S7Client;

public class AutomateRegulation extends AppCompatActivity {

    //déclaration des textviews

    private TextView txt_reference;
    private TextView txt_mode;
    private  TextView txt_connexion_distance;
    private  TextView txt_vanne1;
    private  TextView txt_vanne2;
    private  TextView txt_vanne3;
    private  TextView txt_vanne4;
    private  TextView txt_lvl_liquide;
    private  TextView txt_consigne_auto;
    private  TextView txt_consigne_manuelle;
    private  TextView txt_mot_pilotage;



    private S7Client coms7;


    private ProgressBar progress_liquide;


    //button pour aller dans l'écriture

    private Button btn_lvl_ecriture;

    private String ip,rack,slot,databloc,permission;



    @Override
    protected  void  onCreate(Bundle savedInstanceStat) {
        super.onCreate(savedInstanceStat);
        setContentView(R.layout.activity_automate_regulation);

        txt_reference  = (TextView)findViewById(R.id.txt_reference);
        progress_liquide = (ProgressBar) findViewById(R.id.progress_liquide);
        txt_connexion_distance = (TextView)findViewById(R.id.txt_liquide_distance);
        txt_consigne_auto = (TextView)findViewById(R.id.txt_consigne_auto);
        txt_consigne_manuelle = (TextView)findViewById(R.id.txt_consigne_m);
        txt_mode = (TextView)findViewById(R.id.txt_liquide_mode);
        txt_lvl_liquide = (TextView)findViewById(R.id.txt_liquide_lvl);
        txt_mot_pilotage = (TextView)findViewById(R.id.txt_pilotage_vanne);
        txt_vanne1 = (TextView)findViewById(R.id.txt_vanne1);
        txt_vanne2 = (TextView)findViewById(R.id.txt_vanne2);
        txt_vanne3 = (TextView)findViewById(R.id.txt_vanne3);
        txt_vanne4 = (TextView)findViewById(R.id.txt_vanne4);

        btn_lvl_ecriture = (Button) findViewById(R.id.btn_level_write);


        Intent intent = getIntent();



        ip  = intent.getStringExtra("ip");
        rack  = intent.getStringExtra("rack");
        slot  = intent.getStringExtra("slot");
        databloc  = intent.getStringExtra("databloc");
        permission  = intent.getStringExtra("permissionEcriture");

        Toast.makeText(this, permission, Toast.LENGTH_SHORT).show();
        if(permission.equals("0"))
        {
            btn_lvl_ecriture.setVisibility(View.INVISIBLE);
        }
        else if(permission.equals("1")){
            btn_lvl_ecriture.setVisibility(View.VISIBLE);

        }




        ReadTaskS7Regulation readTs7 = new ReadTaskS7Regulation(progress_liquide,txt_reference,txt_connexion_distance,txt_consigne_auto,txt_consigne_manuelle,txt_lvl_liquide,txt_mot_pilotage,txt_mode,txt_vanne1,txt_vanne2,txt_vanne3,txt_vanne4,Integer.valueOf(databloc));
        readTs7.Start(ip,rack,slot);




    }



    public void onWriteRegulation(View v){

        switch (v.getId()){
            case R.id.btn_level_write:
                    Intent intent = new Intent(this,AutomatRegulationWrite.class);
                    intent.putExtra("ip", ip);
                    intent.putExtra("rack",rack);
                    intent.putExtra("slot",slot);
                    intent.putExtra("databloc",databloc);
                    intent.putExtra("permisison",permission);
                    startActivity(intent);
                break;

        }


    }


}



