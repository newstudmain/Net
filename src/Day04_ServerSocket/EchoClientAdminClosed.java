package Day04_ServerSocket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

public class EchoClientAdminClosed {

	private String host ="localhost";
	private int port =8001;
	private Socket socket;
	
	public EchoClientAdminClosed()throws IOException{
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
			pw.println(localReader.readLine());
			
			String msg ="";
			while((msg=br.readLine())!=null){
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
		new EchoClientAdminClosed().talk();
	}
}
