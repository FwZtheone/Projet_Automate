package campus.technique.heh.projetautomate;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

public class AutomateRegulation extends AppCompatActivity {
    public static ReadTS7 rest1;


    //variable textview des info de l'automate


        private  TextView tv_liquide;
        private  TextView tv_ref;
        private  TextView tv_etat;
        private  TextView tv_consigneA;
        private  TextView tv_consigneM;
        private  TextView tv_motpilotage;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_automate_regulation);


        tv_liquide = (TextView)findViewById(R.id.tv_liquide_plc);
        tv_ref = (TextView)findViewById(R.id.tv_main_plc);
        tv_etat = (TextView)findViewById(R.id.tv_etat_plc);
        tv_consigneA = (TextView)findViewById(R.id.tv_consigne_plc);
        tv_consigneM = (TextView)findViewById(R.id.tv_consigneM_plc);
        tv_motpilotage = (TextView)findViewById(R.id.tv_pilotage_plc);


        //recup des variables de l'activité useractivité

        Intent intent = getIntent();

        if(intent != null){
            String ip ="";
            String rack = "";
            String slot = "";
            if(intent.hasExtra("ip") && intent.hasExtra("rack") && intent.hasExtra("slot")){
                ip = intent.getStringExtra("ip");
                rack = intent.getStringExtra("rack");
                slot = intent.getStringExtra("slot");
            }
//                public ReadTS7(TextView liquide, TextView status, TextView setpoint, TextView consigne, TextView pilotage, TextView ref){

                rest1 = new ReadTS7(tv_liquide,tv_etat ,tv_consigneA,tv_consigneM,tv_motpilotage,tv_ref);
            rest1.Start(ip,rack,slot);
        }












    }



}
