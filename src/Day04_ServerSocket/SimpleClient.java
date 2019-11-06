package Day04_ServerSocket;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

/*
 * 
 * 
 * */
public class SimpleClient {

	public static void main(String[] args) {
		final int length =100;
		String host ="localhost";
		int port =8000;
		
		Socket[] sockets =new Socket[length];
		
		
		try {
			for(int i=0;i<sockets.length;i++){
			sockets[i] = new Socket(host,port);
			System.out.println("第"+(i+1)+"请求成功");
			}
			Thread.sleep(3000);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			try {
				for(int i=0;i<sockets.length;i++){
					if(sockets[i]!=null) 
						sockets[i].close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
