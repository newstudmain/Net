package Day04_ServerSocket;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/*
 * 设定客户连接请求队列的长度 
	 * 当服务器进程运行时 Socket socket=new Socket(host,port) 就意味着在远程主机的端口上， 监听到了一个客户的连接 。
	 * 管理客户连接请求的任务是由操作系统来完成的。 操作系统把这些连接请求存储在一个先进先出的队列中。
	 * 许多操作系统限定了队列的最大长度， 一般为 50。 当队列屮的连接请求达到了队列的最大容量时， 服务器进程所在的主机会拒绝新的连接请求。
	 * 使队列腾出空位时， 队列才能继续加入新的连接请求。
 * 
 * 对于客户进程， 如果它发出的连接请求被加入到服务器的队列屮， 就意味着客户与服务器的连接建立成功，客户进程从 Socket构造方法屮正常返回。
 * 如果客户进程发出的连接请求被服务器拒绝， Socket 构造方法就会抛出 ConnectionException。
 * 
 * ServerSocket 构造方法的 backlog 参数用来显式设置连接请求队列的长度， 它将覆盖操作系统限定的队列的最大长度。 
	 * 值得注意的是， 在以下儿种情况中， 仍然会采用操作系统限定的队列的最大长度:
	 * • backlog 参数的值大于操作系统限定的队列的最大长度, 
	 * • backlog 参数的值小于或等于 0。
	 * • 在 ServerSocket 构造方法屮没有设置 backlog 参数。
 * 
 * */
public class SimpleServer {
	private int port =8000;
	private ServerSocket server;
	
	public static void main(String[] args) {
		SimpleServer serverSocket = new SimpleServer();
//		try {
//			Thread.sleep(60000);
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
		serverSocket.service();
	}
	
	public SimpleServer(){
		try {
			server = new ServerSocket(port,3);
			System.out.println("服务器启动");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void service(){
		while(true){
			Socket socket=null;
			try {
				socket = server.accept();
				System.out.println("新的连接请求: "+socket.getInetAddress()+" : "+socket.getPort());
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					if(socket!=null)socket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}

/*
 * (1) //serverSocket.service();
 * 		第1连接成功
		第2连接成功
		第3连接成功
		java.net.ConnectException: Connection refused: connect
		
	>>Client与 Server 连接超过3次，就无法在创建其他连接了。因为服务器的队列已经满了。
 * 
 * (2) //Thread.sleep(60000);
 * 		第1请求成功
		第2请求成功
		第3请求成功
		...
		第100请求成功
		
		>此时Client能顺利与Server建立100次连接。
		
	>>作了以上修改， 服务器与 8000 端口绑定后， 就会在一个 while循环中不断执行serverSocke.accept()方法， 
	>>该方法从队列中取出连接请求， 使得队列能及时腾出空位，以容纳新的连接请求。
	
 * -----------------------------------------------------------------------------------------------------

 * ServerSocket 的 accept()方法从连接请求队列屮取出一个客户的连接请求， 然后创建与客户连接的 Socket 对象， 并将它返冋。 
 * 如果队列中没有连接请求， accept方法就会一直等待， 直到接收到了连接请求才返冋。
 * 接下来， 服务器从 Socket 对象屮获得输入流和输出流， 就能与客户交换数据。 
 * 当服务器正在进行发送数据的操作时， 如果客户端断开了连接， 那么服务器端会抛出一个 IOException 的子类 SocketException 异常：
 * 		java.net.SocketException: Connection reset by peer
 * 这只是服务器与单个客户通信屮出现的异常， 这种异常应该被捕获， 使得服务器能继续与其他客户通信.
 * */
