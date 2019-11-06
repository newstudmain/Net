package Day02;

import java.io.IOException;
import java.net.BindException;
import java.net.ConnectException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

public class ConnectTester {
	
	public static void main(String[] args){
		String host ="localhaost";
		int port =25;
		if(args.length>1){
			host =args[0];
			port =Integer.parseInt(args[1]);
		}
		new ConnectTester().coonect(host, port);
	}
	public void coonect(String host,int port){
		SocketAddress remoteAddr =new InetSocketAddress(host,port);
		Socket socket =null;
		String result="";
		try {
			socket =new Socket();
			long begin =System.currentTimeMillis();
			socket.connect(remoteAddr,1000);
			long end =System.currentTimeMillis();
			result =(end-begin)+"ms";
		}catch (BindException e) {
			result="Local address and port can't be binded";
		}catch (UnknownHostException e) {
			result="Unknown Host";
		}catch (ConnectException e) {
			result="Connection Refused";
		}catch (SocketTimeoutException e) {
			result="Time Out";
		}catch (IOException e) {
			result="Failure";
		}finally{
			try{
				if(socket!=null)socket.close();
			}catch(IOException e){
				e.printStackTrace();
			}
		}
		System.out.println(remoteAddr+":"+result);
	}
}
