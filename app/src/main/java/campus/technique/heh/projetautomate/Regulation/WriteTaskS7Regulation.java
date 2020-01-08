package campus.technique.heh.projetautomate.Regulation;

import android.widget.EditText;

import java.util.concurrent.atomic.AtomicBoolean;

import campus.technique.heh.projetautomate.simatic_s7.S7;
import campus.technique.heh.projetautomate.simatic_s7.S7Client;

public class WriteTaskS7Regulation {


    private AtomicBoolean isRunning = new AtomicBoolean(false);

    private Thread writeThread;
    private AutomateS7 plcS7;

    private S7Client comS7;
    private String[] parConnexion = new String[10];



    public byte[] dbbw24 = new byte[10];
    public byte[]  dbbw26 = new byte[10];
    public  byte[] dbbw28 = new byte[10];
    public byte[] dbbw30 = new byte[10];

    public  byte[] dbbw2 = new byte[10];
    public byte[] dbbw3 = new byte[10];

    private S7 s7;

    //textview

    private EditText et_dbbw24;
    private EditText Et_dbbw26;
    private EditText Et_dbbw28;
    private EditText Et_dbbw30;

    private int databloc;








    public WriteTaskS7Regulation(int databloc){
        this.databloc = databloc;
        comS7 = new S7Client();
        plcS7 = new AutomateS7();
        writeThread = new Thread(plcS7);
    }



    public void start(String a, String r, String s) {
        if (!writeThread.isAlive()) {
            parConnexion[0] = a;
            parConnexion[1] = r;
            parConnexion[2] = s;
            writeThread.start();
            isRunning.set(true);
        }
    }

    public void stop() {
        isRunning.set(false);
        comS7.Disconnect();
        writeThread.interrupt();
    }











    private class AutomateS7 implements Runnable {

        @Override
        public void run() {
            try {
                comS7.SetConnectionType(S7.S7_BASIC);
                Integer res = comS7.ConnectTo(parConnexion[0], Integer.valueOf(parConnexion[1]), Integer.valueOf(parConnexion[2]));
                while (isRunning.get() && res.equals(0)) {
////                    Integer writePLC = comS7.WriteArea(S7.S7AreaDB, 5, 0, 1, motCommande);
//
                    comS7.WriteArea(S7.S7AreaDB,databloc,24,2,dbbw24);
                    comS7.WriteArea(S7.S7AreaDB,databloc,26,2,dbbw26);
                    comS7.WriteArea(S7.S7AreaDB,databloc,28,2,dbbw28);
                    comS7.WriteArea(S7.S7AreaDB,databloc,30,2,dbbw30);
                    comS7.WriteArea(S7.S7AreaDB,databloc,2,1,dbbw2);
                    comS7.WriteArea(S7.S7AreaDB,databloc,3,1,dbbw3);

//




                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void setWriteBooldb2(int b, int v) {
        if (v == 1)
            dbbw2[0] = (byte) (b | dbbw2[0]);
        else
            // ~b = complément à 1 de b
            dbbw2[0] = (byte) (~b & dbbw2[0]);
    }

    public void setWriteBooldb3(int b, int v) {
        if (v == 1)
            dbbw3[0] = (byte) (b | dbbw3[0]);
        else
            // ~b = complément à 1 de b
            dbbw3[0] = (byte) (~b & dbbw3[0]);
    }







    public void setBitAtdb2(int b, int v) {
        byte[] powerOf2 = {(byte) 0x01, (byte) 0x02, (byte) 0x04, (byte) 0x08, (byte) 0x16,
                (byte) 0x32, (byte) 0x64, (byte) 0xA0};

        b = b < 0 ? 0 : b;
        b = b > 7 ? 7 : b;

        if (v == 1)
            dbbw2[0] = (byte) (dbbw3[0] | powerOf2[b]);
        else
            dbbw2[0] = (byte) (dbbw3[0] & ~powerOf2[b]);
    }


    public void setBitAtdb3(int b, int v) {
        byte[] powerOf2 = {(byte) 0x01, (byte) 0x02, (byte) 0x04, (byte) 0x08, (byte) 0x16,
                (byte) 0x32, (byte) 0x64, (byte) 0xA0};

        b = b < 0 ? 0 : b;
        b = b > 7 ? 7 : b;

        if (v == 1)
            dbbw3[0] = (byte) (dbbw3[0] | powerOf2[b]);
        else
            dbbw3[0] = (byte) (dbbw3[0] & ~powerOf2[b]);
    }



    public void setWordAtDbb24(int p, int v) {
        S7.SetWordAt(dbbw24,p,v);

    }
    public void setWordAtDbb26(int p, int v) {
        S7.SetWordAt(dbbw26,p,v);

    }
    public void setWordAtDbb28(int p, int v) {
        S7.SetWordAt(dbbw28,p,v);

    }
    public void setWordAtDbb30(int p, int v) {
        S7.SetWordAt(dbbw30,p,v);

    }



//    public static void SetWordAt(byte[] Buffer, int Pos, int Value)
//    {
//        int Word = Value & 0x0FFFF;
//        Buffer[Pos]   = (byte) (Word >> 8);
//        Buffer[Pos+1] = (byte) (Word & 0x00FF);
//    }



}
