package Day05_NIO;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServerBlocking {

	private int port =8000;
	private ServerSocketChannel serverSocketChannel;
	private ExecutorService executorService;
	private static final int POOL_MULTIPLE =4;
	
	public ServerBlocking() {
		//创建线程池
		executorService =Executors.newFixedThreadPool(POOL_MULTIPLE * (Runtime.getRuntime().availableProcessors()));
		
		try {
			//创建ServerSocket 连接通道
			serverSocketChannel = ServerSocketChannel.open();
			
			/*SO_REUSEADDR 用来决定如果网络上仍然有数据向旧的  ServerSocket 传输数据，是否允许新的ServerSocket 绑定到与 旧的ServerSocket同样的端口上，
			该选项的默认值与操作系统有关，在某些操作系统中，允许重用端口，而在某些系统中不允许重用端口。
		   	当ServerSocket关闭时，如果网络上还有发送到这个serversocket上的数据，
			这个ServerSocket不会立即释放本地端口，而是等待一段时间，确保接收到了网络上发送过来的延迟数据，然后再释放端口
		    setReuseAddress(boolean on) 必须在ServerSocket还没有绑定到一个本地端口之前使用，否则执行该方法无效。
			两个公用同一个端口的进程必须都调用serverSocket.setReuseAddress(true)方法，
			才能使得一个进程关闭ServerSocket之后，另一个进程的ServerSocket还能够立刻重用相同的端口
			 */
			serverSocketChannel.socket().setReuseAddress(true);//同一个主机上关闭了服务器程序，紧接着再启动时可以绑定到相同端口
		
			//为ServerSocket 绑定端口
			serverSocketChannel.socket().bind(new InetSocketAddress(port));
			
		} catch (IOException e) {
			e.printStackTrace();
		}  
		System.out.println("服务器启动成功...");
	}
	
	
	public void service() {
		while(true) {
			SocketChannel socketChannel =null;
			try {
				socketChannel = serverSocketChannel.accept();
				executorService.execute(new Handler(socketChannel));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	} 
	
	//使用ServerSocketChannel .read() 读数据
	public void serviBuf() {
		
	}
	
	class Handler implements Runnable{
		private SocketChannel socketChannel;
		
		public Handler(SocketChannel socket){
			this.socketChannel=socket;
		}
		
		@Override
		public void run() {
			handle(socketChannel);
		}
		
		/*
		 * [注意] Socket socket =socketChannel.socket() 在多线程时的同步情况
		 * 		 Socket socket 为成员变量，分开赋值时 有空值针异常
		 * */
		public void handle(SocketChannel socketChannel) {
			Socket socket =socketChannel.socket();
			if(socket!=null)
				System.out.println("New connection accepted"+ 
						socket.getInetAddress()+": "+socket.getPort());
			
			BufferedReader br=null;
			PrintWriter pw=null;
			try {
				br = getReader(socket);
				pw = getWriter(socket);
				
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
				System.out.println("客户端: "+socketChannel.socket().getInetAddress()+"/"+
						socketChannel.socket().getPort()+
							" 写入数据超时...60s");
			} finally {
				try {
					if(br!=null) br.close();
					if(pw!=null) pw.close();
					if(socketChannel!=null) socketChannel.close();
					if(socket!=null) socket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		//使用ServerSocketChannel .read() 一次读一行数据
		public String handleBuf(SocketChannel socketChannel) throws IOException {
			//存放读到的数据
			ByteBuffer Bbuf = ByteBuffer.allocate(1024);
			//存放每次读到的数据，一次读一个
			ByteBuffer Tmpbuf = ByteBuffer.allocate(1);
			
			boolean isLine =false;	//标记是否读到了一行字符串
			boolean isEnd =false;	//标记是否达到输入流末尾
			
			while(!isLine && !isEnd) {
				Tmpbuf.clear();
				
				int n = socketChannel.read(Tmpbuf);
				if(n == -1) {
					isEnd =true;
					break;
				}
				if(n == 0) 
					continue;
				
				Tmpbuf.flip();
				Bbuf.put(Tmpbuf);
				Bbuf.flip();
				
				Charset charset = Charset.forName("utf-8");
				CharBuffer Cbuf = charset.decode(Bbuf);
				String data = Cbuf.toString();
				
				if(data.indexOf("\r\n")!=-1) {
					isLine =true;
					data = data.substring(0, data.indexOf("\\r\\n"));
					break;
				}
				
				Bbuf.position(Bbuf.limit());
				Bbuf.limit(Bbuf.capacity());
			}
			
			return data;
			
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
	
	public static void main(String[] args) {
		new ServerBlocking().service();
	}
	
	
}
