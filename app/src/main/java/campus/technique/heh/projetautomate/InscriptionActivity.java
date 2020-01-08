package campus.technique.heh.projetautomate;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import campus.technique.heh.projetautomate.sql.DatabaseHelper;

public class InscriptionActivity extends AppCompatActivity {

    private static Pattern pattern;
    private static Matcher matcher;
    public boolean c = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inscription);

    }


    // validating password with retype password
    public boolean isValidPassword(String pass) {
        String PASSWORD_PATTERN= "[a-zA-Z0-9]";
        Pattern pattern = Pattern.compile(PASSWORD_PATTERN);
        Matcher matcher = pattern.matcher(pass);
        if (pass != null && pass.length() > 4) {
            return true;
        }
        return false;
    }


    //je vérifie si l'email comporte des a ou A de 0 à 9 ainsi qu'il doit commencer de 1 à 256 carract et il doit comparer le @ensuite un hotmail etc.. .com
    public boolean isValidEmail(String email){
        String emailPattern =   "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                "\\@" +
                "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                "(" +
                "\\." +
                "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                ")+";
        Pattern pattern = Pattern.compile(emailPattern);
        Matcher matcher = pattern.matcher(email);

        return matcher.matches();
    }

    // ------------ création de méthode pour le hashage de mdp pour l'injecter dans la DB ----------

    public static byte[] getSHA(String input) throws NoSuchAlgorithmException{
        //on reçoit l'instance avec l'hashage  sha 256
        MessageDigest md = MessageDigest.getInstance("SHA-256");

        return md.digest(input.getBytes(StandardCharsets.UTF_8));

    }

    public static String toHexString(byte[] hash){
        // Convert byte array into signum representation
        // on converti le tableau de byte en signm de représentation
        BigInteger number = new BigInteger(1, hash);

        // converssion du message digest en hexa
        StringBuilder hexString = new StringBuilder(number.toString(16));

        while (hexString.length() < 32)
        {
            hexString.insert(0, '0');
        }

        return hexString.toString();
    }


    //vérification de l'adresse mail si elle est déjà dans la DB

    public boolean ifExistEmail(String email){

        DatabaseHelper mydb = new DatabaseHelper(this);


        ArrayList<String> tableau_email = mydb.getAllContacts("email");

         boolean reponse = tableau_email.contains(email);

        return reponse;


    }



    public void onClickInscription(View v){
        switch (v.getId()){
            case R.id.valider:
                //login --> prenom
                EditText edit_text_login = (EditText) findViewById(R.id.edit_Login_inscription);
                String edit_text_login_string = edit_text_login.getText().toString().toLowerCase();

                EditText edit_text_prenom  =(EditText) findViewById(R.id.edit_prenom_inscription);
                String edit_text_prenom_string = edit_text_prenom.getText().toString().toLowerCase();
                //password
                EditText edit_text_password  = findViewById(R.id.edit_password_inscription);
                String edit_text_password_string = edit_text_password.getText().toString();

                //password2
                EditText edit_text_password2 = findViewById(R.id.edit_password2_inscription);
                String edit_text_password2_string = edit_text_password2.getText().toString();

                //email
                EditText edit_text_email = findViewById(R.id.edit_email_inscription);
                String edit_text_email_string = edit_text_email.getText().toString();



                if(!edit_text_login_string.isEmpty() && !edit_text_email_string.isEmpty() && !edit_text_password_string.isEmpty())
                {
                    if(isValidEmail(edit_text_email_string) && isValidPassword(edit_text_password_string)){

                        if(!ifExistEmail(edit_text_email_string)){

                            if(edit_text_password_string.equals(edit_text_password2_string))
                            {
                            Toast.makeText(this, " Inscription réussie ! ", Toast.LENGTH_SHORT).show();
                            DatabaseHelper mydb = new DatabaseHelper(this);
                            try{
                                mydb.insertContact(edit_text_login_string, edit_text_prenom_string, toHexString(getSHA(edit_text_password_string)),edit_text_email_string,"BASIC" ,false);
                                finish();
                                Intent connection  = new Intent(this,ConnectionActivity.class);
                                startActivity(connection);
                            }
                            catch (NoSuchAlgorithmException e) {
                                Toast.makeText(this, "error insert db !!", Toast.LENGTH_SHORT).show();
                            }
                        }
                            else{
                                edit_text_password2.setError("les mots de passe ne sont pas les mêmes  !");
                            }
                        }



                        else{
                            edit_text_email.setError("email déjà utilisé !!! ");
                        }


                    }
                    else{
                        Toast.makeText(this, " l'email ou le mot de passe ne respectent pas les règles !", Toast.LENGTH_SHORT).show();

                    }
                }
                else {

                    Toast.makeText(this, " un ou plusieurs champs est vide(s) !", Toast.LENGTH_SHORT).show();
                }




        }
    }
}
