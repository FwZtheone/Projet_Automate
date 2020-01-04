package campus.technique.heh.projetautomate;

import android.annotation.SuppressLint;
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







    private AtomicBoolean isRunning = new AtomicBoolean(false);

    private TextView tv_main_plc;


    //TextView pour le liquide
    private TextView tv_liquide_plc;
    private TextView tv_status_plc;
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




    public ReadTS7(TextView liquide, TextView status, TextView setpoint, TextView consigne, TextView pilotage, TextView ref){

        tv_liquide_plc = liquide;
        tv_status_plc = status;
        tv_setpoint_plc =setpoint;
        tv_consigneM_plc =consigne;
        tv_mot_pilotage =pilotage;
        tv_main_plc = ref;

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

//    //après affichage
//    private void downloadOnPreExecute(int liquide, int ref,int pilotage, int consigne, int setpoint, int status){
//        tv_liquide_plc.setText("LIQUIDE : " + String.valueOf(liquide) );
//        tv_main_plc.append(String.valueOf(ref));
//        tv_mot_pilotage.setText("Mot de pilotage vanne : " +String.valueOf(pilotage));
//        tv_consigneM_plc.setText("Consigne manuelle :" + String.valueOf(consigne));
//        tv_setpoint_plc.setText("Consigne automatique : " +String.valueOf(setpoint));
//        tv_status_plc.setText("Etat de fonctionnement :" + String.valueOf(status));
//
//
//    }
//
//    //avant affichage
//    private void downloadOnPostExecute()
//    {
//        tv_liquide_plc.setText("LIQUIDE : 0");
//        tv_main_plc.setText("Information automate : ");
//        tv_mot_pilotage.setText(("Mot de pilotage vanne : "));
//        tv_consigneM_plc.setText("Consigne manuelle :");
//        tv_setpoint_plc.setText("Consigne automatique : ");
//        tv_status_plc.setText("Etat de fonctionnement :");
//
//
//    }

    private void downloadOnPreExecute(int liquide){
        tv_liquide_plc.setText("LIQUIDE : " +  String.valueOf(liquide));

    }

    private void downloadOnPostExecute(){
        tv_liquide_plc.setText("LIQUIDE : 0");
    }




    @SuppressLint("HandlerLeak")
    private Handler monHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MESSAGE_PRE_EXECUTE:
                    Message msg2 = msg;
                    downloadOnPreExecute(msg.arg1);
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
                int numCPU;
                numCPU = -1;
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
                        int liquideInfo = comS7.ReadArea(S7.S7AreaDB,5,16,2,volumePLC);

                        int consigneA = comS7.ReadArea(S7.S7AreaDB,5,18,2,consigneAuto);
                        int consigneM = comS7.ReadArea(S7.S7AreaDB,5,20,2,consigneManu);
                        int pilotage = comS7.ReadArea(S7.S7AreaDB,5,22,2,motpilotage);

                        int etat = comS7.ReadArea(S7.S7AreaDB,5,0,2,statusPLC);

                        int data=0;
                        int dataB=0;
                        int datac= 0;
                        int dataD = 0;
                        int dataE = 0;
                        int dataF =0;

                        if (retInfo==0) {
                            data = S7.GetWordAt(datasPLC,0);
                            dataB = S7.GetWordAt(volumePLC,0);
                            datac = S7.GetWordAt(consigneAuto,0);
                            dataD = S7.GetWordAt(consigneManu,0);
                            dataE = S7.GetWordAt(statusPLC,0);
                            dataF = S7.GetWordAt(motpilotage,0);

//                            private void sendPreExecuteMessage(int liquide, int ref, int pilotage, int consigneM, int consigneA, int etat ) {
                            sendPreExecuteMessage(dataB,data,dataF,dataD,datac,dataE);

                        }
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


        private void sendPreExecuteMessage(int liquide, int ref, int pilotage, int consigneM, int consigneA, int etat ) {
            Message preExecuteMsg= new Message();
            preExecuteMsg.what= MESSAGE_PRE_EXECUTE;
            preExecuteMsg.arg1 = liquide;
//            preExecuteMsg.arg2 = ref;
//            preExecuteMsg.obj = pilotage;
//            preExecuteMsg.obj = consigneM;
//            preExecuteMsg.obj = consigneA;
//            preExecuteMsg.obj = etat;


//            tv_liquide_plc.setText("LIQUIDE : 0");
//            tv_main_plc.setText("Information automate : ");
//            tv_mot_pilotage.setText(("Mot de pilotage vanne : "));
//            tv_consigneM_plc.setText("Consigne manuelle :");
//            tv_setpoint_plc.setText("Consigne automatique : ");
//            tv_status_plc.setText("Etat de fonctionnement :");
            monHandler.sendMessage(preExecuteMsg);
        }




    }



}
