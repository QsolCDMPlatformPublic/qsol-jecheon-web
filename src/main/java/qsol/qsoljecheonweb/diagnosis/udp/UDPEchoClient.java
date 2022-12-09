package qsol.qsoljecheonweb.diagnosis.udp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.List;


public class UDPEchoClient {

    /*private String str;
    private BufferedReader file;*/
    private int step;
    private static int SERVERPORT = 20038;
    private byte[] buffer;
    DatagramSocket ds = new DatagramSocket(); // 소켓에 포트 저장 ?

    public static void clear(Buffer buffer) {
        buffer.clear();
    }
    public static void putUnsignedByte (ByteBuffer bb, int value){
        bb.put ((byte)(value & 0xff));
    }

    public UDPEchoClient(String ip, int port, int locid, int testid, String stepCheck) throws SocketException {
        try{
            if(stepCheck.equals("start")) {
                step = 1; // 진단 시작
            } else {
                step = 2; // 진단 강제 중지
            }
            InetAddress ia = InetAddress.getByName(ip); // IP 저장 ?
            ds = new DatagramSocket(port);
            //ds.connect(ia, port);

           // 안해도 0으로 초기화되어있음
           // byte[] filler = new byte[] {0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00};

            ByteBuffer buffer = ByteBuffer.allocate(16);
            clear(buffer);
            //buffer.clear();
            buffer = buffer.order(ByteOrder.BIG_ENDIAN);
            buffer.putInt(locid);
            buffer.putInt(testid);
            buffer.put((byte) step);
            //buffer.put(filler);

            byte[] bufferData = buffer.array();

            DatagramPacket dp = new DatagramPacket(
                    bufferData,bufferData.length,ia,SERVERPORT);

            ds.send(dp); // 데이터 전달
            ds.close(); // 소켓 연결 해제
        }catch(IOException ioe){
            ds.close(); // 소켓 연결 해제
            ioe.printStackTrace();
        }
    }

    /*public static void main(String[] args){
        new UDPEchoClient("localhost",20034);
    }*/
};