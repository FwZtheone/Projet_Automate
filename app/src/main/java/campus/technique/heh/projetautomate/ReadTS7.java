package campus.technique.heh.projetautomate;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.concurrent.atomic.AtomicBoolean;

import campus.technique.heh.projetautomate.simatic_s7.S7;
import campus.technique.heh.projetautomate.simatic_s7.S7Client;
import campus.technique.heh.projetautomate.simatic_s7.S7OrderCode;

public class ReadTS7 {


    private static final int MESSAGE_PRE_EXECUTE = 1;
    private static final int MESSAGE_PROGRESS_UPDATE = 2;
    private static final int MESSAGE_POST_EXECUTE = 3;




    //les valves
    private TextView valve1;
    private TextView valve2;
    private TextView valve3;
    private TextView valve4;
    private TextView valve5;
    private TextView valve6;





    private AtomicBoolean isRunning = new AtomicBoolean(false);

    private TextView tv_main_plc;


    //TextView pour le liquide
    private TextView tv_liquide_plc;
    private TextView tv_status_plc;
    private TextView tv_valve1_plc;


    private TextView tv_setpoint_plc;
    private TextView tv_consigneM_plc;
    private TextView tv_mot_pilotage;



    private AutomateS7 plcS7;
    private Thread readThread;

    private S7Client comS7;
    private String[] param = new String[10];
    //ref
    private byte[] datasPLC = new byte[512];

    private byte[] volumePLC = new byte[512];

    private byte[] consigneAuto = new byte[512];
    private byte[] consigneManu = new byte[512];

    private byte[] motpilotage = new byte[512];
    private byte[] statusPLC = new byte[512];

    private byte[] statusPLC2 = new byte[512];




    public ReadTS7(TextView liquide, TextView valve1, TextView setpoint, TextView consigne, TextView pilotage, TextView ref, TextView valve2, TextView vavle3,TextView vavle4,TextView vavle5,TextView vavle6){

        tv_liquide_plc = liquide;
        tv_valve1_plc = valve1;
        tv_setpoint_plc =setpoint;
        tv_consigneM_plc =consigne;
        tv_mot_pilotage =pilotage;
        tv_main_plc = ref;

        this.valve2 = valve2;
        this.valve3 = vavle3;
        this.valve4 = vavle4;
        this.valve5 = vavle5;
        this.valve6 = vavle6;

        comS7 = new S7Client();
        plcS7 = new AutomateS7();
        readThread = new Thread(plcS7);

    }


    //stopper la lecture
    public void Stop(){
        isRunning.set(false);
        comS7.Disconnect();
        readThread.interrupt();
    }


    public void Start(String a, String r, String s){
        //vérification si le thread n'est pas en cours !
        if(!readThread.isAlive()){
            param[0] = a;
            param[1] = r;
            param[2] = s;
            readThread.start();
            isRunning.set(true);
        }
    }


    private void downloadOnPostExecute()
    {
        tv_liquide_plc.setText("LIQUIDE : 0");
        tv_main_plc.setText("Information automate : 0");
        tv_mot_pilotage.setText(("Mot de pilotage vanne : 0"));
        tv_consigneM_plc.setText("Consigne manuelle : 0");
        tv_setpoint_plc.setText("Consigne automatique : 0 ");
        tv_valve1_plc.setText("Etat de fonctionnement : désactivé");
        valve2.setText("Etat de fonctionnement : désactivé");
        valve3.setText("Etat de fonctionnement : désactivé");
        valve4.setText("Etat de fonctionnement : désactivé");
        valve5.setText("Etat de fonctionnement : désactivé");
        valve6.setText("Etat de fonctionnement : désactivé");




    }

    private void downloadOnPreExecute(int liquide, int option, int consM, int data1 , int ref , int pilot,int data2, int data3, int data4, int data5, int data6 ){
        tv_liquide_plc.setText("LIQUIDE : " +  String.valueOf(liquide));
        tv_setpoint_plc.setText("CONSIGNE AUTO : " +  String.valueOf(option));
        tv_consigneM_plc.setText("Consigne manuelle : " + String.valueOf(consM));
        if(data1 == 0){
            tv_valve1_plc.setText("état Valve 1 : désactivé" );
        }
        else{
            tv_valve1_plc.setText("état Valve 1 : activé" );

        }
        if(data2 == 0){
            valve2.setText("état Valve 2 : désactivé" );
        }
        else{
            valve2.setText("état Valve 2 : activé" );

        }
        if(data3 == 0){
            valve3.setText("état Valve 3 : désactivé" );
        }
        else{
            valve3.setText("état Valve 3 : activé" );

        }
        if(data4 == 0){
            valve4.setText("état Valve 4 : désactivé" );
        }
        else{
            valve4.setText("état Valve 4 : activé" );

        }
        if(data5 == 0){
            valve5.setText("Bouton automatique : désactivé" );
        }
        else{
            valve5.setText("Bouton automatique  : activé" );

        }
        if(data6 == 0){
            valve6.setText("Local Distant : désactivé" );
        }
        else{
            valve6.setText("Local Distant : activé" );

        }
        tv_main_plc.setText(" information de l'automate modèle : " + String.valueOf(ref));
        tv_mot_pilotage.setText("mot de pilotage vanne : " + String.valueOf(pilot));

    }







    @SuppressLint("HandlerLeak")
    private final Handler monHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {
                case MESSAGE_PRE_EXECUTE:

                    //je recup mon tableau de data
                    Bundle data = msg.getData();
                    int consigneM = data.getInt("consigneM",0);

//                    bundle.putInt("consigneM", consigneManuelle);
//                    bundle.putInt("etat", etat);
//                    bundle.putInt("reference", refe);
//                    bundle.putInt("pilotageAuto", pilot);

                    int data1 = data.getInt("etat",0);
                    int reference = data.getInt("reference",0);
                    int pilotageAuto = data.getInt("pilotageAuto",0);
                    int datav2 = data.getInt("datav2",0);
                    int datav3 = data.getInt("datav3",0);
                    int datav4 = data.getInt("datav4",0);
                    int datav5 = data.getInt("datav5",0);
                    int datav6 = data.getInt("datav6",0);


                    downloadOnPreExecute(msg.arg1, msg.arg2, consigneM,data1,reference,pilotageAuto,datav2,datav3,datav4,datav5,datav6);


                    break;

                case MESSAGE_POST_EXECUTE:
                    downloadOnPostExecute();
                    break;

                default:
                    break;
            }

        }

    };


    public class AutomateS7 implements Runnable{

        @Override
        public void run() {
            try{
                comS7.SetConnectionType(S7.S7_BASIC);
                Integer res= comS7.ConnectTo(param[0],Integer.valueOf(param[1]),Integer.valueOf(param[2]));

                S7OrderCode orderCode= new S7OrderCode();
                Integer result= comS7.GetOrderCode(orderCode);
                int numCPU = -1;

                if (res.equals(0) && result.equals(0)){
                    //Quelques exemples : // WinAC: 6ES7 611-4SB00-0YB7
                    // S7-315 2DPP?N : 6ES7 315-4EH13-0AB0
                    // S7-1214C : 6ES7 214-1BG40-0XB0
                    // Récupérer le code CPU  611 OU315 OU21
                    numCPU= Integer.valueOf(orderCode.Code().toString().substring(5, 8));
                }
                else
                {
                    numCPU= 0000;

                }


                while(isRunning.get()){
                    if (res.equals(0)){
                        //ref
                        int retInfo= comS7.ReadArea(S7.S7AreaDB,5,9,2,datasPLC);
                        //niveau de liquide
                        int liquideInfo = comS7.ReadArea(S7.S7AreaDB,5,16,8,volumePLC);

                        int consigneA = comS7.ReadArea(S7.S7AreaDB,5,18,8,consigneAuto);
                        int consigneM = comS7.ReadArea(S7.S7AreaDB,5,20,8,consigneManu);
                        int pilotage = comS7.ReadArea(S7.S7AreaDB,5,22,8,motpilotage);

                        int etat = comS7.ReadArea(S7.S7AreaDB,5,0,2,statusPLC);
                        int etat2 = comS7.ReadArea(S7.S7AreaDB,5,1,2,statusPLC2);

                        int data=0;
                        int dataB=0;
                        int datac= 0;
                        int dataD = 0;
                        int dataE = 0;
                        int dataF =0;


                        //valve;
                        int dataV2 = 0;
                        int dataV3 = 0;
                        int dataV4 = 0;
                        int dataV5 = 0;
                        int dataV6 = 0;

                        if (retInfo==0) {
                            data = S7.GetWordAt(datasPLC,0);
                            dataB = S7.GetWordAt(volumePLC,0);
                            datac = S7.GetWordAt(consigneAuto,0);
                            dataD = S7.GetWordAt(consigneManu,0);
                            dataF = S7.GetWordAt(motpilotage,0);




                            dataV2 = S7.GetBitAt(statusPLC,0,2) ? 1 : 0;
                            dataV3 = S7.GetBitAt(statusPLC,0,3) ? 1 : 0;
                            dataV4 = S7.GetBitAt(statusPLC,0,4) ? 1 : 0;
                            dataV5 = S7.GetBitAt(statusPLC,0,5) ? 1 : 0;
                            dataV6 = S7.GetBitAt(statusPLC,0,6) ? 1 : 0;
                            dataE = S7.GetBitAt(statusPLC,0,1) ? 1 : 0;



                            sendPreExecuteMessage(dataB,datac,dataD,dataE,numCPU,dataF,dataV2,dataV3,dataV4,dataV5,dataV6);




//                            private void sendPreExecuteMessage(int liquide, int ref, int pilotage, int consigneM, int consigneA, int etat ) {


                        }
                        Log.i("Variable A.P.I. -> ", String.valueOf(retInfo));

                        Log.i("Variable A.P.I. -> ", String.valueOf(data));
                        Log.i("Contenu du volume  -> ", String.valueOf(dataB));
                    }
                    try{
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
                sendPostExecuteMessage();
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }


        }


        private void sendPostExecuteMessage() {
            Message postExecuteMsg= new Message();
            postExecuteMsg.what= MESSAGE_POST_EXECUTE;
            monHandler.sendMessage(postExecuteMsg);
        }


        private void sendPreExecuteMessage(int liquide, int consigneAuto, int consigneM, int status, int ref, int pilotage, int data2, int data3, int data4, int data5, int data6) {
            Message preExecuteMsg= new Message();
            preExecuteMsg.what= MESSAGE_PRE_EXECUTE;
            preExecuteMsg.arg1 = liquide;
            preExecuteMsg.arg2 = consigneAuto;



            int consigneManuelle = consigneM;
            int etat = status;
            int refe = ref;
            int pilot = pilotage;
            int datav2 = data2;
            int datav3= data3;
            int datav4 = data4;
            int datav5 = data5;
            int datav6 = data6;

            Bundle bundle = new Bundle();
            bundle.putInt("consigneM", consigneManuelle);
            bundle.putInt("etat", etat);
            bundle.putInt("reference", refe);
            bundle.putInt("pilotageAuto", pilot);
            bundle.putInt("datav2",datav2);
            bundle.putInt("datav3",datav3);
            bundle.putInt("datav4",datav4);
            bundle.putInt("datav5",datav5);
            bundle.putInt("datav6",datav6);


            preExecuteMsg.setData(bundle);


            monHandler.sendMessage(preExecuteMsg);
        }










    }



}
