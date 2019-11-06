package Day03_Socket;

import java.io.IOException;
import java.net.BindException;
import java.net.ConnectException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;


/*
 * 客户连接服务器时可能拋出的异常 
 * 当Socket的构造方法请求连接服务器时，可能会抛出以下异常：
 * 		UnknownHostException
 * 			如果无法识别主机的名字或IP 地址，就会抛出这种异常
 * 		ConnectException
 * 			如果没有服务器进程监听指定的端口，或者服务器进程拒绝连接，就会抛出这种异常
 * 		SocketTimeoutException
 * 			如果等待连接超时，就会抛出这种异常
 * 		BindException
 * 			如果无法把 Socket 对象与指定的本地 IP 地址或端口绑定，就会抛出这种异常
 * 				sockct.bind(new InetSocketAddress(InetAddress.getByName("222.34.5.7"),5432));
 * 				Socket 的本地 IP 地址设为 “222.34.5.7”，把本地端口设为5432。
 * 				如果本地主机不具有 IP 地址 “222.34.5.7”, 或者端口 5678 已经被占用，那么bind()方法或构造方法就会抛出 BindException
 * 
 * IOException
		UnknownHostException
		InterruptedException
			SocketTimeoutException
		SocketException
			BindException
			ConnectException
 * 
 * 
 * */

public class SocketException {

	public static void main(String[] args) {
		String host ="localhost";//baidu.com,localhost
		int port =8082;
		new SocketException().connect(host, port);//baidu.com/39.156.69.79:80:61ms
	}
	
	public void connect(String host,int port){
		InetSocketAddress remoteAddr = new InetSocketAddress(host,port);
		Socket socket = null;
		String result ="";
		
		try {
			long begin =System.currentTimeMillis();
			socket= new Socket();
			socket.connect(remoteAddr, 1000); //设置超时为1秒
			long end =System.currentTimeMillis();
			result =(end-begin)+"ms";
		} catch (BindException e) {
			result ="Local address and port can't be binded";
		} catch (ConnectException e) {
			result ="Connection Refused";// localhost/127.0.0.1:8089:Connection Refused
		} catch (SocketTimeoutException e) {
			result ="Timeout";// localhost/127.0.0.1:80:Timeout
		} catch (UnknownHostException e) {
			result ="Unknown Host";// everhost:80:Unknown Host
		} catch (IOException e) {
			result ="failure";
		} finally{
			try {
				if(socket!=null) socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		System.out.println(remoteAddr +":"+result);
	}
}
