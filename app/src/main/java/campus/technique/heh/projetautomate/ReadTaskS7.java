package campus.technique.heh.projetautomate;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.concurrent.atomic.AtomicBoolean;

import campus.technique.heh.projetautomate.simatic_s7.S7;
import campus.technique.heh.projetautomate.simatic_s7.S7Client;
import campus.technique.heh.projetautomate.simatic_s7.S7OrderCode;

public class ReadTaskS7 {

    private static final int MESSAGE_PRE_EXECUTE = 1;
    private static final int MESSAGE_PROGRESS_UPDATE = 2;
    private static final int MESSAGE_POST_EXECUTE = 3;



    private AtomicBoolean isRunning = new AtomicBoolean(false);
    private ProgressBar pb_main_progressionS7;
    private Button bt_main_connexS7;
    private View vi_main_ui;
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
    private byte[] datasPLC = new byte[512];

    private byte[] volumePLC = new byte[512];



    public ReadTaskS7( TextView liquide){

        tv_liquide_plc = liquide;

        comS7 = new S7Client();
        plcS7 = new AutomateS7();
        readThread = new Thread(plcS7);

    }


    public ReadTaskS7(View v, Button b, ProgressBar p, TextView t, TextView liquide){
        vi_main_ui = v;
        bt_main_connexS7 = b;
        pb_main_progressionS7 = p;
        tv_main_plc = t;
        tv_liquide_plc = liquide;
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


    private void downloadOnPreExecute(int t,int liquide){
        tv_main_plc.setText(" PLC  : " +   String.valueOf(t) );
        tv_liquide_plc.setText(" LIQUIDE  : " +   String.valueOf(liquide) );


    }


    private void downloadOnProgressUpdate(int progress){
        pb_main_progressionS7.setProgress(progress);
    }


    private void downloadOnPostExecute(){
        pb_main_progressionS7.setProgress(0);
        tv_main_plc.setText("PLC: /!\\");
        tv_liquide_plc.setText("LIQUIDE : 0");
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


    private class AutomateS7  implements Runnable{


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
                        int retInfo= comS7.ReadArea(S7.S7AreaDB,5,9,2,datasPLC);
                        //niveau de liquide
                        int liquideInfo = comS7.ReadArea(S7.S7AreaDB,5,16,2,volumePLC);
                        int data=0;
                         int dataB=0;

                        if (retInfo==0) {
                            data = S7.GetWordAt(datasPLC, 0);
                            dataB = S7.GetWordAt(volumePLC,0);

                            sendProgressMessage(data,dataB);
                            sendPreExecuteMessage(numCPU,dataB);

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
            }

            catch (Exception e) {
                e.printStackTrace();
            }
        }



        private void sendPostExecuteMessage() {
            Message postExecuteMsg= new Message();
              postExecuteMsg.what= MESSAGE_POST_EXECUTE;
               monHandler.sendMessage(postExecuteMsg);
        }


        private void sendPreExecuteMessage(int v,int liquide ) {
            Message preExecuteMsg= new Message();
            preExecuteMsg.what= MESSAGE_PRE_EXECUTE;
            preExecuteMsg.arg1 = v;
            preExecuteMsg.arg2 = liquide;
            monHandler.sendMessage(preExecuteMsg);
        }
        private void sendProgressMessage(int i,int e) {
            Message progressMsg= new Message();
            progressMsg.what= MESSAGE_PROGRESS_UPDATE;
            progressMsg.arg1 = i;
            progressMsg.arg2 = e;
            monHandler.sendMessage(progressMsg);
        }



    }
}



