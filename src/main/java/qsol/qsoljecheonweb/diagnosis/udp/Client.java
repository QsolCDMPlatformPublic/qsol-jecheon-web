package qsol.qsoljecheonweb.diagnosis.udp;



import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.HashMap;

import org.apache.commons.lang3.StringUtils;

/*-------------------------------------------------------------------------------------*
 *      Author: byeong-soo Kim
 *      Created: 2010. 6. 4.
 *      Mail: seban21@naver.com
 *      Description: 패킷 제어
 *-------------------------------------------------------------------------------------*/



public class Client
{
    private InetAddress        addr                = null;
    private String                host                = null;
    private String                port                = null;

    private int                        packetSendSize        = 232;
    private int                        packetRcvSize        = 232;

    public Client( )
    {}

    public HashMap< String , Object > send( int msgID , String seq , String val00 , String val01 , String val02,
                                            int flag0 , String val03 , int flag1 , int sEndian , int rEndian )
    {
        Socket socket = null;
        BufferedOutputStream bout = null;
        BufferedInputStream bin = null;
        HashMap< String , Object > result = null;

        this.host = "192.168.0.2";
        this.port = "2222";
        try
        {
            System.out.println( "host" + this.host );
            addr = InetAddress.getByName( this.host );
            socket = new Socket( this.addr , Integer.parseInt( this.port ) );
            socket.setSoTimeout( 10000 ); // Timeout : 10 secs.
            System.out.println( "socket = " + socket );
            bout = new BufferedOutputStream( socket.getOutputStream( ) );
            bin = new BufferedInputStream( socket.getInputStream( ) );
            ByteBuffer buffer = ByteBuffer.allocate( this.packetSendSize );
            if ( sEndian == 0 )
            {
                buffer.order( ByteOrder.LITTLE_ENDIAN ); // c언어는 little_endian방식
            }
            else
            {
                buffer.order( ByteOrder.BIG_ENDIAN ); // 자바는 big_endian방식
            }
            System.out.println( "Byte order : " + buffer.order( ) ); // 엔디안 방식 보여줌
            String ret00 = "";
            String ret01 = "";
            String ret02 = "";
            if ( val00.getBytes( ).length > 40 )
            {
                System.out.println( "alert !!! val00 over 40!!!" );
            }
            ret00 = StringUtils.rightPad( val00 , 40 , ( char ) 0x00 );

            if ( val01.getBytes( ).length > 40 )
            {
                System.out.println( "alert !!! val01 over 40!!!" );
            }
            ret01 = StringUtils.rightPad( val01 , 40 , ( char ) 0x00 );

            if ( val02.getBytes( ).length > 40 )
            {
                System.out.println( "alert !!! val02 over 40!!!" );
            }
            ret02 = StringUtils.rightPad( val02 , 40 , ( char ) 0x00 );

            if ( val03.getBytes( "euc-kr" ).length > 80 )
            {
                System.out.println( "alert !!! val03 over 80!!! cut!!!!" );
            }
            byte[] cotent = new byte[84]; // 84바이트 공간이지만 실제로는 80바이트만 사용
            System.arraycopy( val03.getBytes( "euc-kr" ) , 0 , cotent , 0 , val03.getBytes( "euc-kr" ).length );

            buffer.putInt( ( int ) msgID ); // 4
            buffer.putShort( ( short ) 0xfefe ); // 2
            buffer.putShort( ( short ) this.packetSendSize ); // 2바이트 -총 길이(바디+헤더)
            buffer.putInt( ( int ) 0x0000 ); // 4 -0x0000:요청 -0x0001:응답
            buffer.putInt( Integer.parseInt( seq ) ); // 4
            buffer.putInt( ( int ) 0x0100 ); // 4
            buffer.put( ret00.getBytes( ) ); // 40
            buffer.put( ret01.getBytes( ) ); // 40
            buffer.put( ret02.getBytes( ) ); // 40
            buffer.putInt( ( int ) flag0 ); // 4
            buffer.put( cotent ); // 84
            buffer.putInt( ( int ) flag1 ); // 4

            buffer.flip( );
            byte[] results = buffer.array( ); // 버퍼에 들어있는 내용을 바이트 배열에 집어 넣는다

            System.out.println( "send packet--->" + new String( results ) );
            System.out.println( "send size--->" + results.length );
            bout.write( results ); // 신식send 스트림을 바이트배열 형식으로 서버에 보낸다
            bout.flush( ); // 버퍼 내용을 비운다

            // 리시브 시작
            byte[] rBuf = new byte[ this.packetRcvSize ];
//                        bin.read( rBuf , 0 , rBuf.length );
            DatagramPacket packet = new DatagramPacket( rBuf , bin.read( rBuf , 0 , rBuf.length ) ); // 길이 체크를 위함
            System.out.println( "rcv packet length: " + packet.getLength( ) );

            ByteBuffer buffer2 = ByteBuffer.allocate( rBuf.length );
            if ( rEndian == 0 )
            {
                buffer2.order( ByteOrder.LITTLE_ENDIAN );
            }
            else
            {
                buffer2.order( ByteOrder.BIG_ENDIAN );
            }
//                        byte[] sub0 = getbytes( rBuf , 12 , 4 );
//                        buffer2.put( sub0 );
            buffer2.put( getbytes( rBuf , 0 , 4 ) );
            buffer2.put( getbytes( rBuf , 4 , 2 ) );
            buffer2.put( getbytes( rBuf , 6 , 2 ) );
            buffer2.put( getbytes( rBuf , 8 , 4 ) );
            buffer2.put( getbytes( rBuf , 12 , 4 ) );
            buffer2.put( getbytes( rBuf , 16 , 4 ) );
            byte[] sub0 = getbytes( rBuf , 20 , 40 );
            byte[] sub1 = getbytes( rBuf , 60 , 40 );
            byte[] sub2 = getbytes( rBuf , 100 , 40 );
            buffer2.put( getbytes( rBuf , 140 , 4 ) );
            byte[] sub3 = getbytes( rBuf , 144 , 84 );
            buffer2.put( getbytes( rBuf , 228 , 4 ) );

            int i0 = buffer2.getInt( 0 );
            short i1 = buffer2.getShort( 4 );
            short i2 = buffer2.getShort( 6 );
            int i3 = buffer2.getInt( 8 );
            int i4 = buffer2.getInt( 12 );
            int i5 = buffer2.getInt( 16 );

            String i6 = new String( sub0 , 0 , sub0.length );
            String i7 = new String( sub1 , 0 , sub1.length );
            String i8 = new String( sub2 , 0 , sub2.length );
            int i9 = buffer2.getInt( 20 );
            String i10 = new String( sub3 , 0 , sub3.length );
            int i11 = buffer2.getInt( 24 );

            System.out.println( "0: " + i0 );
            System.out.println( "1: " + i1 );
            System.out.println( "2: " + i2 );
            System.out.println( "3: " + i3 );
            System.out.println( "4: " + i4 );
            System.out.println( "5: " + i5 );
            System.out.println( "6: " + i6 );
            System.out.println( "7: " + i7 );
            System.out.println( "8: " + i8 );
            System.out.println( "9: " + i9 );
            System.out.println( "10: " + i10 );
            System.out.println( "11: " + i11 );

            result = new HashMap< String , Object >( );
            result.put( "0" , i0 + "" );
            result.put( "1" , i1 + "" );
            result.put( "2" , i2 + "" );
        }
        catch ( SocketException e0 )
        {
            System.out.println( "SOCKET EXCEPTION : " + e0.toString( ) );
        }
        catch ( UnknownHostException e1 )
        {
            System.out.println( "Server Address Error" );
        }
        catch ( Exception e2 )
        {
            System.out.println( "EXCEPTION :" + e2.toString( ) );
        }
        finally
        {
            try
            {
                if ( bout != null )
                {
                    bout.close( );
                }
                if ( bin != null )
                {
                    bin.close( );
                }
                socket.close( );
                System.out.println( "Socket close" );
            }
            catch ( Exception e3 )
            {}
        }
        return result;
    }

    public static final byte[] getbytes( byte src[] , int offset , int length )
    {
        byte dest[] = new byte[ length ];
        System.arraycopy( src , offset , dest , 0 , length );
        return dest;
    }

/*    public static void main( String[] args )
    {
        new Client( ).send( 7 , "1" , "aaaaaaaa" , "bbbbbbbbbbb" , "cccccccccc" , 2 , "ddddddddd" , 1 , 1 , 1 );
    }*/
}