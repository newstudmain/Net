package Day03_Socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

public class MailSocketDemo {

	private static String smtpServer ="smtp.163.com";
	private static int smtpPort =25;
	private static String username ="ktwoow";
	private static String password ="Emper_above001";
	
	public static void main(String[] args) {
		Socket socket =null;
		PrintWriter pw =null;
		BufferedReader br =null;
//		String response=null;
		try {
			socket = new Socket(smtpServer,smtpPort);
			String hostName = InetAddress.getLocalHost().getHostName();
			pw = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()),true);
			br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			sendReceive("test",pw,br);
			
			
			sendReceive("HELO "+hostName,pw,br);
			//if(br.readLine()!=null)
//			pw.println("");//需要/r/n 表示命令结束
//			response = br.readLine();
//			System.out.println(response);
//			pw.println("HELO "+hostName);
//			response = br.readLine();
//			System.out.println(response);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if(pw!=null)pw.close();
				if(br!=null)br.close();
				if(socket!=null)socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void sendReceive(String str,PrintWriter pw,BufferedReader br) throws IOException{
		if (str!= null){
			System.out.println("Client>"+str);
			pw.println(str); //会发  \r\n.
			
			String response;
			if ((response = br.readLine())!= null)
			System.out.println("Server>"+response);
		}
	}
}
