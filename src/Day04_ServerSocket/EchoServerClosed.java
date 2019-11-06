package Day04_ServerSocket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.TimeUnit;


public class EchoServerClosed {

	private int port =8000;
	private ServerSocket serverScoket;
	private final int POOL_SIZE	 =4;
	private ExecutorService executorService;
	
	private int portForShutdown =8001;
	private ServerSocket serverScoketShutdown;
	private boolean isShutdown =false;
	
	private Thread shutdownThread =new Thread() {
		public void start() {
			this.setDaemon(true);
			super.start();
		}
		
		public void run() {
			while(!isShutdown) {
				Socket scoketShutdown =null;
				try {
					scoketShutdown =serverScoketShutdown.accept();
					System.out.println("[ADMIN] 服务器关闭连接 "+ 
							scoketShutdown.getInetAddress()+": "+scoketShutdown.getPort()+" 已启动");	
					BufferedReader br = new BufferedReader(
											new InputStreamReader(
													scoketShutdown.getInputStream()));
					String command =br.readLine();
					if(command.equals("shutdown")) {
						long beginTime =System.currentTimeMillis();
						scoketShutdown.getOutputStream().write("服务器正在关闭...\r\n".getBytes());
						isShutdown =true;
						executorService.shutdown();
						
						//如果关闭后所有任务都已完成，则返回 true。注意，除非首先调用 shutdown 或 shutdownNow，否则 isTerminated 永不为 true。
						while(!executorService.isTerminated()) 
							executorService.awaitTermination(30, TimeUnit.SECONDS);
							/*	
								当使用awaitTermination时，主线程会处于一种等待的状态，等待线程池中所有的线程都运行完毕后才继续运行。
									timeout-最长等待时间/unit-timeout 参数的时间单位 
										1️.	如果等待的时间超过指定的时间，但是线程池中的线程运行完毕，
											那么awaitTermination()返回true。执行分线程已结束。
										2️.	如果等待的时间超过指定的时间，但是线程池中的线程未运行完毕，
											那么awaitTermination()返回false。不执行分线程已结束。
							*/
						
						serverScoket.close(); // 关闭与客户通讯的 serversocket
						long endTime = System.currentTimeMillis();
						scoketShutdown.getOutputStream().write(
								("服务器已经关闭,关闭服务器用了 "+(endTime-beginTime)+" 秒...").getBytes());
							
						scoketShutdown.close();
						serverScoketShutdown.close();
						
					} else {
						scoketShutdown.getOutputStream().write("错误的命令...\r\n".getBytes());
						scoketShutdown.close();
					}
					
				} catch (IOException e) {
					e.printStackTrace();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	};
	
	public EchoServerClosed() {
		try {
			serverScoket =new ServerSocket(port);
			serverScoket.setSoTimeout(60000);
			/*	m一般用来表示单位时是毫单位，即10-3（10的-3次方）
			如毫米（mm）、毫升（ml）、毫摩尔（mmol）、毫克（mg）	*/
			serverScoketShutdown = new ServerSocket(portForShutdown);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors()*POOL_SIZE);
		
		shutdownThread.start();//
		System.out.println("服务器已启动");
	}
	
	public static void main(String[] args) {
		new EchoServerClosed().service();
	}
	
	public void service(){
		while(!isShutdown){
			Socket socket =null;
				try {
					socket =serverScoket.accept();
					socket.setSoTimeout(60000);
					//Thread workThread = new Thread(new Handler(socket));
					executorService.execute(new Handler(socket));
					//workThread.start();
				} catch (SocketTimeoutException e) {
					System.out.println("服务器: "+serverScoket.getLocalSocketAddress()+"/"+
													serverScoket.getLocalPort()+
														" 等待请求连接超时...60s"+" ,等待重新连接");;//不必处理，等待客户连接超时
				} catch (RejectedExecutionException e) {
					//e.printStackTrace();
					try {
						if(socket!=null) socket.close();
					} catch (IOException e1) {}
					return;
				} catch (SocketException e) {
//					如果是在执行 serversocket.accpet() 方法时
//					serversocket 被 shutdownThread 线程关闭而导致的异常，就退出service() 方法
					if(e.getMessage().indexOf("socket closed")!=-1)
						return;
					//e.printStackTrace();
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
				//e.printStackTrace();
				System.out.println("客户端: "+socket.getInetAddress()+"/"+
						socket.getPort()+
							" 写入数据超时...60s");
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
