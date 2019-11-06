package Day04_ServerSocket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class EchoServerThreadPool {

	private int port =8000;
	private ServerSocket serverScoket;
	private final int POOL_SIZE	 =4;
	private ExecutorService executorService;
	
	public EchoServerThreadPool() throws IOException{
		serverScoket =new ServerSocket(port);
		executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors()*POOL_SIZE);
		System.out.println("服务器已启动");
	}
	
	public static void main(String[] args) throws IOException{
		new EchoServerThreads().service();
	}
	
	public void service(){
		while(true){
			Socket socket =null;
				try {
					socket =serverScoket.accept();
					//Thread workThread = new Thread(new Handler(socket));
					executorService.execute(new Handler(socket));
					//workThread.start();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
	}
	
	class Handler implements Runnable{
		private Socket socket;
		
		public Handler(Socket socket){
			this.socket=socket;
		}
		
		@Override
		public void run() {
			System.out.println("New connection accepted"+ 
					socket.getInetAddress()+": "+socket.getPort());
			
			BufferedReader br=null;
			PrintWriter pw=null;
			try {
				br = getReader(socket);
				pw =getWriter(socket);
				String msg ="";
				
				while((msg=br.readLine())!=null){
					System.out.println(msg);
					pw.println(echo(msg));
					if(msg.equals("bye")){
						break;
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					if(br!=null) br.close();
					if(pw!=null) pw.close();
					if(socket!=null) socket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
	}
		
	public String echo(String msg){
		return "echo: "+msg;
	}
	public PrintWriter getWriter(Socket socket) throws IOException{
		OutputStream socketOut =socket.getOutputStream();
		return new PrintWriter(socketOut,true);
	}
	public BufferedReader getReader(Socket socket) throws IOException{
		InputStream socketIn =socket.getInputStream();
		return new BufferedReader(new InputStreamReader(socketIn));
	}

	}
}
