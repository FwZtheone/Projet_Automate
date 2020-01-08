package campus.technique.heh.projetautomate.Regulation;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.concurrent.atomic.AtomicBoolean;

import campus.technique.heh.projetautomate.R;
import campus.technique.heh.projetautomate.simatic_s7.S7;
import campus.technique.heh.projetautomate.simatic_s7.S7Client;
import campus.technique.heh.projetautomate.simatic_s7.S7OrderCode;

public class ReadTaskS7Regulation {


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

    private byte[] datasPLC = new byte[16];
    private byte[] datas_connexion_distance = new byte[16];
    private byte[] datas_consigne_auto = new byte[16];
    private byte[] datas_consigne_manuel = new byte[16];
    private byte[] datas_liquide = new byte[16];
    private byte[] datas_mode = new byte[16];
    private byte[] datas_pilotage = new byte[16];


    private  byte[] statusPLC  = new byte[16];


    private int databloc;






    public ReadTaskS7Regulation(ProgressBar p, TextView txt_reference, TextView txt_connexion_distance, TextView txt_consigne_auto, TextView txt_consigne_manuelle , TextView txt_lvl_liquide, TextView txt_mot_pilotage, TextView txt_mode, TextView txt_vanne1,TextView txt_vanne2,TextView txt_vanne3,TextView txt_vanne4, int databloc){

        pb_main_progressionS7 = p;
        this.txt_reference = txt_reference;
        this.txt_connexion_distance = txt_connexion_distance;
        this.txt_consigne_auto = txt_consigne_auto;
        this.txt_consigne_manuelle = txt_consigne_manuelle;
        this.txt_lvl_liquide = txt_lvl_liquide;
        this.txt_mode  = txt_mode;
        this.txt_mot_pilotage   = txt_mot_pilotage;
        this.txt_vanne1  = txt_vanne1;
        this.txt_vanne2  = txt_vanne2;
        this.txt_vanne3  = txt_vanne3;
        this.txt_vanne4  = txt_vanne4;

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


    private void downloadOnPreExecute(int t, int liquide,int mode, int consigne_auto, int connexion_distance, int mot_pilotage, int consigne_manuel,int vanne1, int vanne2, int vanne3, int vanne4){

        txt_reference.setText(String.valueOf(t));
        txt_lvl_liquide.setText(String.valueOf(liquide));
        txt_consigne_auto.setText(String.valueOf(consigne_auto));
        txt_mot_pilotage.setText( String.valueOf(mot_pilotage));
        txt_consigne_manuelle.setText(String.valueOf(consigne_manuel));
        if(mode == 1){
            txt_mode.setText(R.string.On);
        }
        else{
            txt_mode.setText(R.string.Off);
        }
        if(vanne1 == 1){
            txt_vanne1.setText(R.string.On);
        }
        else{
            txt_vanne1.setText(R.string.Off);
        }
        if(vanne2 == 1){
            txt_vanne2.setText(R.string.On);
        }
        else{
            txt_vanne2.setText(R.string.Off);
        }
        if(vanne3 == 1){
            txt_vanne3.setText(R.string.On);
        }
        else{
            txt_vanne3.setText(R.string.Off);
        }
        if(vanne4 == 1){
            txt_vanne4.setText(R.string.On);
        }
        else{
            txt_vanne4.setText(R.string.Off);
        }
        if(connexion_distance == 1){
            txt_connexion_distance.setText(R.string.On);
        }
        else{
            txt_connexion_distance.setText(R.string.Off);
        }





//        txt_mode.setText(String.valueOf(mode));

    }


    private void downloadOnProgressUpdate(int progress){
        pb_main_progressionS7.setProgress(progress);
    }


    private void downloadOnPostExecute(){
        pb_main_progressionS7.setProgress(0);
        tv_main_plc.setText("PLC: /!\\");
        txt_reference.setText("");
        txt_lvl_liquide.setText("");
        txt_mode.setText("");
        txt_consigne_auto.setText("");
        txt_connexion_distance.setText( "");
        txt_mot_pilotage.setText("");
        txt_consigne_manuelle.setText("");
        txt_vanne1.setText("");
        txt_vanne2.setText("");
        txt_vanne3.setText("");
        txt_vanne4.setText("");

    }

    @SuppressLint("HandlerLeak")
    private Handler monHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MESSAGE_PRE_EXECUTE:


                    Bundle bundle = msg.getData();
                    int liquide = bundle.getInt("liquide",0);

                    int  consigne_auto = bundle.getInt("consigne_auto",0);
                    int  mot_pilotage = bundle.getInt("mot_pilotage",0);
                    int  consigne_manuel = bundle.getInt("consigne_manuel",0);

                    //représente les valves
                    int vanne1 = bundle.getInt("vanne1",0);
                    int vanne2 = bundle.getInt("vanne2",0);
                    int vanne3 = bundle.getInt("vanne3",0);
                    int vanne4 = bundle.getInt("vanne4",0);
                    int  connexion_distance = bundle.getInt("connexion_distance",0);
                    int  mode = bundle.getInt("mode",0);



                    downloadOnPreExecute(msg.arg1,liquide,mode, consigne_auto , connexion_distance, mot_pilotage, consigne_manuel,vanne1,vanne2,vanne3,vanne4);
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
                        int resultDbb16 = comS7.ReadArea(S7.S7AreaDB, databloc, 16, 2, datas_liquide);

                        //
                        int resultDbw18 = comS7.ReadArea(S7.S7AreaDB, databloc, 18, 2, datas_consigne_auto);
                        int resultDbw20 = comS7.ReadArea(S7.S7AreaDB, databloc, 20, 2, datas_consigne_manuel);
                       int resultDbw22 = comS7.ReadArea(S7.S7AreaDB, databloc, 22, 2, datas_pilotage);


                        // a vérifier
                        int resultDbb0 = comS7.ReadArea(S7.S7AreaDB, databloc, 0, 8, statusPLC);
                        int resultDbb1 = comS7.ReadArea(S7.S7AreaDB,databloc, 1, 8, datas_mode );


                        int data_ref = 0;
                        int data_consigne_auto = 0;
                        int data_consigne_manuel = 0;
                        int data_liquide = 0;
                        int data_mot_pilotage = 0;


                        //représente les valves
                        int data_vanne1 = 0;
                        int data_vanne2 = 0;
                        int data_vanne3 = 0;
                        int data_vanne4 = 0;
                        int data_mode = 0;
                        int data_connexion_distance = 0;


                        if (retInfo == 0) {
                            data_ref = S7.GetWordAt(datasPLC, 0);
                            //liquide
                            data_liquide = S7.GetWordAt(datas_liquide,0); //db1
                            //consigne auto
                            data_consigne_auto = S7.GetWordAt(datas_consigne_auto,0);
                            //pilotage
                            data_mot_pilotage = S7.GetWordAt(datas_pilotage,0);
                            //consigne manuelle
                            data_consigne_manuel = S7.GetWordAt(datas_consigne_manuel,0);


                            data_vanne1 = S7.GetBitAt(statusPLC,0,1) ? 1 : 0;
                            data_vanne2 = S7.GetBitAt(statusPLC,0,2) ? 1 : 0;
                            data_vanne3 = S7.GetBitAt(statusPLC,0,3) ? 1 : 0;
                            data_vanne4 = S7.GetBitAt(statusPLC,0,4) ? 1 : 0;
                            data_mode = S7.GetBitAt(statusPLC,0,5) ? 1 : 0;
                            data_connexion_distance= S7.GetBitAt(statusPLC,0,6) ? 1 : 0;



                            sendProgressMessage(data_ref);

                            sendPreExecuteMessage(numCPU,data_liquide,data_mode,data_consigne_auto, data_connexion_distance ,data_mot_pilotage,data_consigne_manuel,data_vanne1,data_vanne2,data_vanne3,data_vanne4);




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

        private void sendPreExecuteMessage(int v, int liquide, int mode, int consigne_auto, int connexion_distance, int mot_pilotage, int consigne_manuel,int vanne1, int vanne2, int vanne3, int vanne4) {
            Message preExecuteMsg = new Message();
            preExecuteMsg.what = MESSAGE_PRE_EXECUTE;
            preExecuteMsg.arg1 = v;
            Bundle bundle = new Bundle();
            bundle.putInt("liquide",liquide);
            bundle.putInt("mode",mode);
            bundle.putInt("consigne_auto",consigne_auto);
            bundle.putInt("connexion_distance",connexion_distance);
            bundle.putInt("mot_pilotage",mot_pilotage);
            bundle.putInt("consigne_manuel",consigne_manuel);
            bundle.putInt("vanne1",vanne1);
            bundle.putInt("vanne2",vanne2);
            bundle.putInt("vanne3",vanne3);
            bundle.putInt("vanne4",vanne4);

            preExecuteMsg.setData(bundle);
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
