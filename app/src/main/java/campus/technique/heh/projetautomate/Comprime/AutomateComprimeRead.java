package campus.technique.heh.projetautomate.Comprime;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import campus.technique.heh.projetautomate.R;

public class AutomateComprimeRead extends AppCompatActivity {

    private ReadTaskS7Comprime readS7;
    private ProgressBar p;
    private TextView tv_service;
    private TextView tv_connexion_distance;
    private TextView tv_gen_flacons;
    private TextView tv_moteur_distributeur;
    private TextView tv_moteur_bande;
    private TextView tv_bouchonne;
    private TextView tv_nombre_flacons;
    private TextView tv_pilule_dem;
    private TextView tv_pilule;

    private TextView tv_affichage_ref;


    private String ip,rack,slot,permission,databloc;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_automate_comprime_read);

        p = (ProgressBar)findViewById(R.id.progress_liquide);
        tv_service  = (TextView)findViewById(R.id.txt_service);
        tv_connexion_distance  = (TextView)findViewById(R.id.txt_comprime_distance);
        tv_gen_flacons  = (TextView)findViewById(R.id.txt_gen_flacons);
        tv_moteur_distributeur  = (TextView)findViewById(R.id.txt_moteur_distrib);
        tv_moteur_bande  = (TextView)findViewById(R.id.txt_moteur_bande);
        tv_bouchonne  = (TextView)findViewById(R.id.txt_bouchonne);
        tv_pilule_dem  = (TextView)findViewById(R.id.txt_pilule_dem);
        tv_pilule  = (TextView)findViewById(R.id.txt_pilule);
        tv_affichage_ref= (TextView)findViewById(R.id.txt_reference) ;
        tv_nombre_flacons = (TextView)findViewById(R.id.txt_nb_flacons);

        Intent intent = getIntent();
        int DATABLOC = Integer.valueOf(intent.getStringExtra("databloc"));
        ip = intent.getStringExtra("ip");
        rack = intent.getStringExtra("rack");
        slot = intent.getStringExtra("slot");

        readS7 = new ReadTaskS7Comprime(p,tv_affichage_ref,tv_service,tv_connexion_distance,tv_gen_flacons,tv_moteur_distributeur,tv_moteur_bande,tv_bouchonne,tv_nombre_flacons,tv_pilule_dem,tv_pilule,DATABLOC);
        readS7.Start(ip,rack,slot);
    }



    public void onWriteComprime(View v){
        switch (v.getId()){
            case R.id.btn_comp_write:
                break;
        }
    }
}
