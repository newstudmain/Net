package Day04_ServerSocket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;


public class EchoClientThreads {
	private String host ="localhost";
	private int port =8000;
	private Socket socket;
	
	public EchoClientThreads()throws IOException{
		socket =new Socket(host,port);
	}
	
	public PrintWriter getWriter(Socket socket) throws IOException{
		OutputStream socketOut =socket.getOutputStream();
		return new PrintWriter(socketOut,true);
	}
	public BufferedReader getReader(Socket socket) throws IOException{
		InputStream socketIn =socket.getInputStream();
		return  new BufferedReader(new InputStreamReader(socketIn));
	}
	
	public void talk(){
		try {
			BufferedReader br=getReader(socket);
			PrintWriter pw =getWriter(socket);
			BufferedReader localReader =new BufferedReader(new InputStreamReader(System.in));
			String msg ="";
			while((msg=localReader.readLine())!=null){
				pw.println(msg);
				System.out.println(br.readLine());
				if(msg.equals("bye")){
					break;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
				try {
					if(socket!=null)socket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
	}
	public static void main(String[] args) throws IOException{
		new EchoClientThreads().talk();
	}
}
