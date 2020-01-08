package campus.technique.heh.projetautomate.Comprime;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.concurrent.atomic.AtomicBoolean;

import campus.technique.heh.projetautomate.simatic_s7.S7;
import campus.technique.heh.projetautomate.simatic_s7.S7Client;
import campus.technique.heh.projetautomate.simatic_s7.S7OrderCode;

public class ReadTaskS7Comprime {
    private static final int MESSAGE_PRE_EXECUTE = 1;
    private static final int MESSAGE_PROGRESS_UPDATE = 2;
    private static final int MESSAGE_POST_EXECUTE = 3;



    private AtomicBoolean isRunning = new AtomicBoolean(false);
    private ProgressBar pb_main_progressionS7;
    private TextView tv_main_plc;
    private AutomateS7 plcS7;
    private Thread readThread;
    private S7Client comS7;
    private String[] param = new String[10];
//
//    ISRUN(0,0),
//    FIVEPILLS(0,1),
//    TENPILLS(0,2),
//    FIFTEENPILLS(0,3),
//    CAPREMPSTATE(0,4),
//    CAPBOUCHSTATE(0,5),
//    INPLULSPIL(0,6),
//    SA0(0,7),
//    SA1(1,0),
//    RESETCP(1,2),
//    GENFLACONS(1,3),
//    LOCALDISTANT(1,6),
//    DISTRIBPILLS(4,0),
//    MOTBANDE(4,1),
//    ABOUCH(4,2),
//    LAMPFIVEPILLS(4,3),
//    LAMPTENPILLS(4,4),
//    LAMPFIFTEENPILLS(4,5),
//    COUNTPILLS(15,0),
//    COUNTBOTTLE(16,0),
//    DBB5(5,0),
//    DBB6(6,0),
//    DBB7(7,0),
//    DBB8(8,0),
//    DBW18(18,0);

    //déclarations des tableaux de byte
    private byte[] dbb0 = new byte[16];
    private byte[] dbb1 = new byte[16];

    private byte[] dbb4 = new byte[16];
    private byte[] dbb15 = new byte[16];
    private byte[] dbb16 = new byte[16];

    private byte[] dbb5 = new byte[16];
    private byte[] dbb6 = new byte[16];
    private byte[] dbb7 = new byte[16];
    private byte[] dbb8 = new byte[16];
    private byte[] dbb18 = new byte[16];



    private byte[] datasPLC = new byte[512];


    //récupération des textviews
    private TextView tv_service;
    private TextView tv_connexion_distance;
    private TextView tv_gen_flacons;
    private TextView tv_moteur_distributeur;
    private TextView tv_moteur_bande;
    private TextView tv_bouchonne;
    private TextView tv_nombre_flacons;
    private TextView tv_pilule_dem;
    private TextView tv_pilule;


    public int databloc;

    public ReadTaskS7Comprime(ProgressBar p, TextView t, TextView tv_service, TextView tv_connexion_distance, TextView tv_gen_flacons, TextView tv_moteur_distributeur, TextView tv_moteur_bande, TextView tv_bouchonne, TextView tv_nombre_flacons, TextView tv_pilule_dem, TextView tv_pilule, int databloc){

        pb_main_progressionS7 = p;
        tv_main_plc = t;
        this.tv_service = tv_service;
        this.tv_connexion_distance = tv_connexion_distance;
        this.tv_gen_flacons = tv_gen_flacons;
        this.tv_moteur_distributeur = tv_moteur_distributeur;
        this.tv_moteur_bande = tv_moteur_bande;
        this.tv_bouchonne  = tv_bouchonne;
        this.tv_nombre_flacons = tv_nombre_flacons;
        this.tv_pilule_dem  = tv_pilule_dem;
        this.tv_pilule  = tv_pilule;
        this.databloc = databloc;
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


    private void downloadOnPreExecute(int t, int nombre_bouteille){
        tv_main_plc.setText(String.valueOf(t));
        tv_nombre_flacons.setText(String.valueOf(nombre_bouteille));
    }


    private void downloadOnProgressUpdate(int progress){
        pb_main_progressionS7.setProgress(progress);
    }


    private void downloadOnPostExecute(){
        pb_main_progressionS7.setProgress(0);
        tv_main_plc.setText("PLC: /!\\");
    }

    @SuppressLint("HandlerLeak")
    private Handler monHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MESSAGE_PRE_EXECUTE:
                    downloadOnPreExecute(msg.arg1,msg.arg2);
                    break;

                case MESSAGE_PROGRESS_UPDATE:
                    downloadOnProgressUpdate(msg.arg1);
                    break;

                case MESSAGE_POST_EXECUTE:
                    downloadOnPostExecute();
                    break;

                default:
                    break;
            }

        }

    };

    private class AutomateS7 implements Runnable {
        @Override
        public void run() {
            try {
                comS7.SetConnectionType(S7.S7_BASIC);
                Integer res = comS7.ConnectTo(param[0], Integer.valueOf(param[1]), Integer.valueOf(param[2]));
                S7OrderCode orderCode = new S7OrderCode();
                Integer result = comS7.GetOrderCode(orderCode);
                int numCPU = -1;
                if (res.equals(0) && result.equals(0)) {
                    numCPU = Integer.valueOf(orderCode.Code().toString().substring(5, 8));
                } else {
                    numCPU = 0000;
                }


                while (isRunning.get()) {
                    if (res.equals(0)) {
                        int retInfo = comS7.ReadArea(S7.S7AreaDB, databloc, 9, 2, datasPLC);
                        int data = 0;
                        if (retInfo == 0) {
                            data = S7.GetWordAt(datasPLC, 0);
                            sendProgressMessage(data);
                            //    ISRUN(0,0),
//    FIVEPILLS(0,1),
//    TENPILLS(0,2),
//    FIFTEENPILLS(0,3),
//    CAPREMPSTATE(0,4),
//    CAPBOUCHSTATE(0,5),
//    INPLULSPIL(0,6),
//    SA0(0,7),
//    SA1(1,0),
//    RESETCP(1,2),
//    GENFLACONS(1,3),
//    LOCALDISTANT(1,6),
//    DISTRIBPILLS(4,0),
//    MOTBANDE(4,1),
//    ABOUCH(4,2),
//    LAMPFIVEPILLS(4,3),
//    LAMPTENPILLS(4,4),
//    LAMPFIFTEENPILLS(4,5),
//    COUNTPILLS(15,0),
//    COUNTBOTTLE(16,0),

 //ecriture
//    DBB5(5,0),
//    DBB6(6,0),
//    DBB7(7,0),
//    DBB8(8,0),
////    DBW18(18,0);
//
                            comS7.ReadArea(S7.S7AreaDB,databloc,16,2,dbb16);
                            comS7.ReadArea(S7.S7AreaDB,databloc,15,2,dbb15);

                            int nombre_pilule = S7.GetWordAt(dbb15,0);
                            int nombre_bouteille = S7.GetWordAt(dbb16,0);

                            sendPreExecuteMessage(numCPU, nombre_bouteille);
                            Log.i("DDB15 " , String.valueOf(nombre_pilule));
                            Log.i("DDB16 " , String.valueOf(nombre_bouteille));
                        }
                    }
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException ie) {
                        ie.printStackTrace();
                    }
                }
                sendPostExecuteMessage();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        private void sendPreExecuteMessage(int v, int nombre_bouteille) {
            Message preExecuteMsg = new Message();
            preExecuteMsg.what = MESSAGE_PRE_EXECUTE;
            preExecuteMsg.arg1 = v;
            preExecuteMsg.arg2 = nombre_bouteille;
            monHandler.sendMessage(preExecuteMsg);
        }

        private void sendProgressMessage(int i) {
            Message progressMsg = new Message();
            progressMsg.what = MESSAGE_PROGRESS_UPDATE;
            progressMsg.arg1 = i;
            monHandler.sendMessage(progressMsg);
        }

        private void sendPostExecuteMessage() {
            Message postExecuteMsg = new Message();
            postExecuteMsg.what = MESSAGE_POST_EXECUTE;
            monHandler.sendMessage(postExecuteMsg);
        }
    }
}
