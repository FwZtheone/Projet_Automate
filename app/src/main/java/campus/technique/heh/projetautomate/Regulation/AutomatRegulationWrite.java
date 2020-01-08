package campus.technique.heh.projetautomate.Regulation;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import campus.technique.heh.projetautomate.R;

public class AutomatRegulationWrite extends AppCompatActivity {


    //textview

    private EditText dbbw24;
    private EditText dbbw26;
    private EditText dbbw28;
    private EditText dbbw30;

    public  WriteTaskS7Regulation writeS7;

    //je recupére toutes mes checkbox
    //pour dbb2
    private CheckBox ch_lvl_dbb2_0;
    private CheckBox ch_lvl_dbb2_1;
    private CheckBox ch_lvl_dbb2_2;
    private CheckBox ch_lvl_dbb2_3;
    private CheckBox ch_lvl_dbb2_4;
    private CheckBox ch_lvl_dbb2_5;
    private CheckBox ch_lvl_dbb2_6;
    private CheckBox ch_lvl_dbb2_7;

        //pour dbb3
    private CheckBox ch_lvl_dbb3_0;
    private CheckBox ch_lvl_dbb3_1;
    private CheckBox ch_lvl_dbb3_2;
    private CheckBox ch_lvl_dbb3_3;
    private CheckBox ch_lvl_dbb3_4;
    private CheckBox ch_lvl_dbb3_5;
    private CheckBox ch_lvl_dbb3_6;
    private CheckBox ch_lvl_dbb3_7;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_automat_regulation_write);


        //edit text pour l'écriture
        dbbw24 = (EditText)findViewById(R.id.et_level_dbw24);
        dbbw26 = (EditText)findViewById(R.id.et_level_dbw26);
        dbbw28 = (EditText)findViewById(R.id.et_level_dbw28);
        dbbw30 = (EditText)findViewById(R.id.et_level_dbw30);

        //checkbox dbb2 et dbb3

        ch_lvl_dbb2_0 = (CheckBox)findViewById(R.id.ch_level_dbb2_0);
        ch_lvl_dbb2_1 = (CheckBox)findViewById(R.id.ch_level_dbb2_1);
        ch_lvl_dbb2_2 = (CheckBox)findViewById(R.id.ch_level_dbb2_2);
        ch_lvl_dbb2_3 = (CheckBox)findViewById(R.id.ch_level_dbb2_3);
        ch_lvl_dbb2_4 = (CheckBox)findViewById(R.id.ch_level_dbb2_4);
        ch_lvl_dbb2_5 = (CheckBox)findViewById(R.id.ch_level_dbb2_5);
        ch_lvl_dbb2_6 = (CheckBox)findViewById(R.id.ch_level_dbb2_6);
        ch_lvl_dbb2_7 = (CheckBox)findViewById(R.id.ch_level_dbb2_7);

        ch_lvl_dbb3_0 = (CheckBox)findViewById(R.id.ch_level_dbb3_0);
        ch_lvl_dbb3_1 = (CheckBox)findViewById(R.id.ch_level_dbb3_1);
        ch_lvl_dbb3_2 = (CheckBox)findViewById(R.id.ch_level_dbb3_2);
        ch_lvl_dbb3_3 = (CheckBox)findViewById(R.id.ch_level_dbb3_3);
        ch_lvl_dbb3_4 = (CheckBox)findViewById(R.id.ch_level_dbb3_4);
        ch_lvl_dbb3_5 = (CheckBox)findViewById(R.id.ch_level_dbb3_5);
        ch_lvl_dbb3_6 = (CheckBox)findViewById(R.id.ch_level_dbb3_6);
        ch_lvl_dbb3_7 = (CheckBox)findViewById(R.id.ch_level_dbb3_7);

        Intent intent = getIntent();

        String ip,rack,slot,databloc,permission;

        ip  = intent.getStringExtra("ip");
        rack = intent.getStringExtra("rack");
        slot = intent.getStringExtra("slot");
        databloc = intent.getStringExtra("databloc");
        permission = intent.getStringExtra("permissionEcriture");



        writeS7  = new WriteTaskS7Regulation(Integer.valueOf(databloc));
        writeS7.start(ip,rack,slot);

       dbbw24.addTextChangedListener(new TextWatcher() {
           @Override
           public void beforeTextChanged(CharSequence s, int start, int count, int after) {

           }

           @Override
           public void onTextChanged(CharSequence s, int start, int before, int count) {
               try{
                   String valeur = dbbw24.getText().toString();
                   if(valeur.equals(""))
                   {
                       valeur = "0";
                   }
                   writeS7.setWordAtDbb24(0,Integer.valueOf(valeur));
               }
               catch (NumberFormatException er)
               {
                   Toast.makeText(AutomatRegulationWrite.this, "mauvais nombre", Toast.LENGTH_SHORT).show();
               }

           }

           @Override
           public void afterTextChanged(Editable s) {

           }
       });

        dbbw26.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try{
                    String valeur = dbbw26.getText().toString();
                    if(valeur.equals(""))
                    {
                        valeur = "0";
                    }
                    writeS7.setWordAtDbb26(0,Integer.valueOf(valeur));
                }
                catch (NumberFormatException er)
                {
                    Toast.makeText(AutomatRegulationWrite.this, "mauvais nombre", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        dbbw28.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try{
                    String valeur = dbbw28.getText().toString();
                    if(valeur.equals(""))
                    {
                        valeur = "0";
                    }
                    writeS7.setWordAtDbb28(0,Integer.valueOf(valeur));
                }
                catch (NumberFormatException er)
                {
                    Toast.makeText(AutomatRegulationWrite.this, "mauvais nombre", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        dbbw30.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {


            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try{
                    String valeur = dbbw30.getText().toString();
                    if(valeur.equals(""))
                    {
                        valeur = "0";
                    }
                    writeS7.setWordAtDbb30(0,Integer.valueOf(valeur));
                }
                catch (NumberFormatException er)
                {
                    Toast.makeText(AutomatRegulationWrite.this, "mauvais nombre", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


    }


    public void bonjour(View v ) {
        switch (v.getId()){
            case R.id.btn_level_write:
                break;
        }
    }


    public void onWriteRegulation(View v){
        switch (v.getId()){
            case R.id.btn_level_goWrite:
                break;

            case R.id.ch_level_dbb2_0:
                writeS7.setWriteBooldb2(128, ch_lvl_dbb2_0.isChecked()? 1 : 0);
                break;

            case R.id.ch_level_dbb2_1:
                writeS7.setWriteBooldb2(64,ch_lvl_dbb2_1.isChecked()? 1:0);
                break;

            case R.id.ch_level_dbb2_2:
                writeS7.setWriteBooldb2(32,ch_lvl_dbb2_2.isChecked()?1:0);
                break;
            case R.id.ch_level_dbb2_3:
                writeS7.setWriteBooldb2(16, ch_lvl_dbb2_3.isChecked()? 1 : 0);
                break;

            case R.id.ch_level_dbb2_4:
                writeS7.setWriteBooldb2(8,ch_lvl_dbb2_4.isChecked()? 1:0);
                break;

            case R.id.ch_level_dbb2_5:
                writeS7.setWriteBooldb2(4,ch_lvl_dbb2_5.isChecked()?1:0);
                break;
            case R.id.ch_level_dbb2_6:
                writeS7.setWriteBooldb2(2,ch_lvl_dbb2_6.isChecked()?1:0);
                break;
            case R.id.ch_level_dbb2_7:
                writeS7.setWriteBooldb2(1,ch_lvl_dbb2_7.isChecked()?1:0);
                break;
            case R.id.ch_level_dbb3_0:
                writeS7.setWriteBooldb3(128, ch_lvl_dbb3_0.isChecked()? 1 : 0);
                break;

            case R.id.ch_level_dbb3_1:
                writeS7.setWriteBooldb3(64,ch_lvl_dbb3_1.isChecked()? 1:0);
                break;

            case R.id.ch_level_dbb3_2:
                writeS7.setWriteBooldb3(32,ch_lvl_dbb3_2.isChecked()?1:0);
                break;
            case R.id.ch_level_dbb3_3:
                writeS7.setWriteBooldb3(16, ch_lvl_dbb3_3.isChecked()? 1 : 0);
                break;

            case R.id.ch_level_dbb3_4:
                writeS7.setWriteBooldb3(8,ch_lvl_dbb3_4.isChecked()? 1:0);
                break;

            case R.id.ch_level_dbb3_5:
                writeS7.setWriteBooldb3(4,ch_lvl_dbb3_5.isChecked()?1:0);
                break;
            case R.id.ch_level_dbb3_6:
                writeS7.setWriteBooldb3(2,ch_lvl_dbb3_6.isChecked()?1:0);
                break;
            case R.id.ch_level_dbb3_7:
                writeS7.setWriteBooldb3(1,ch_lvl_dbb3_7.isChecked()?1:0);
                break;



        }
    }


}
