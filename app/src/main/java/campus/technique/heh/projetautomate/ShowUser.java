package campus.technique.heh.projetautomate;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import campus.technique.heh.projetautomate.sql.DatabaseHelper;

public class ShowUser extends AppCompatActivity {

    //tableau qui va contenir les données email,login,écriture de chaque user
    private ArrayList<String> data_email = new ArrayList<String>();
    private ArrayList<String> data_login = new ArrayList<String>();
    private ArrayList<String> data_ecriture = new ArrayList<String>();
    private   Spinner combo_email ;
    private TableLayout table;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_user);


        final DatabaseHelper mydb = new DatabaseHelper(this);

        Map<Integer,String> tableau_user = mydb.getShowUser();


        table = (TableLayout)findViewById(R.id.table_user);

        int size_tableau_user = tableau_user.size();

        int index = 0;
        while(index < size_tableau_user){

            //curseur me donne le premier user
            Cursor res = mydb.getContact("*",tableau_user.get(index));
            res.moveToFirst();
            data_email.add(res.getString(res.getColumnIndex("EMAIL")));
            data_login.add(res.getString(res.getColumnIndex("LOGIN")));
            data_ecriture.add(res.getString(res.getColumnIndex("ECRITURE")));
            res.close();
            //création de la ligne pour afficher les users
            TableRow ligne = new TableRow(this);


            //je stocke les valeurs des users qui sont dans le tableau
            String email = data_email.get(index);
            String login = data_login.get(index);
            String ecriture = data_ecriture.get(index);

            if(ecriture.equals("0")){
                ecriture = "R";
            }
            else{
                ecriture = "R/W";
            }
            //création de la view pour stocker les données des users

            /*

            Caractéristique de la textview !

                 <TextView
                android:layout_width="wrap_content"
                android:text="test"
                android:layout_marginRight="1dp"
                android:layout_marginBottom="1dp"
                android:background="#ffff"
                android:layout_weight="1"
                android:gravity="center"
                android:layout_height="wrap_content">
            </TextView>


             */


            //combobox
             combo_email = (Spinner)findViewById(R.id.combobox_user);
            ArrayAdapter<String> adp1 = new ArrayAdapter<String>(this,
                    android.R.layout.simple_list_item_1, data_email);

            adp1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            combo_email.setAdapter(adp1);






            //tableau
            TextView view_login = new TextView(this);
            view_login.setText(login);
            view_login.setLayoutParams(new TableRow.LayoutParams(
                    TableRow.LayoutParams.WRAP_CONTENT,
                    TableRow.LayoutParams.WRAP_CONTENT,1f) );
            view_login.setGravity(Gravity.CENTER);
            view_login.setBackgroundColor(Color.WHITE);

            TextView view_email = new TextView(this);
            view_email.setText(email);
            view_email.setLayoutParams(new TableRow.LayoutParams(
                    TableRow.LayoutParams.WRAP_CONTENT,
                    TableRow.LayoutParams.WRAP_CONTENT,1f) );
            view_email.setGravity(Gravity.CENTER);
            view_email.setBackgroundColor(Color.WHITE);

            TextView view_ecriture = new TextView(this);
            view_ecriture.setText(ecriture);
            view_ecriture.setLayoutParams(new TableRow.LayoutParams(
                    TableRow.LayoutParams.WRAP_CONTENT,
                    TableRow.LayoutParams.WRAP_CONTENT,1f) );
            view_ecriture.setGravity(Gravity.CENTER);
            view_ecriture.setBackgroundColor(Color.WHITE);






            ligne.addView(view_login);
            ligne.addView(view_email);
            ligne.addView(view_ecriture);


            table.addView(ligne);


            index++;
        }






    }





    public void onDeleteUser(View v){


        int item_id = combo_email.getSelectedItemPosition()+1;
        DatabaseHelper mydb = new DatabaseHelper(this);

        String email = combo_email.getSelectedItem().toString();
        if(item_id == 1){
            Toast.makeText(this, "tu ne peux pas te supprimer super root !", Toast.LENGTH_LONG).show();

        }
        else{
            mydb.deletUser(email);
            Toast.makeText(this, "SUPPRIME", Toast.LENGTH_LONG).show();

        }








    }


    
    
}
