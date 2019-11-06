package Day04_ServerSocket;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/*
 *  
	SO_REUSEADDR : int
		public void setReuseAddress(boolean on) throws SocketException
		public boolean getReuseAddress() throws SocketException
			SO_REUSEADDR 用来决定如果网络上仍然有数据向旧的  ServerSocket 传输数据，是否允许新的ServerSocket 绑定到与 旧的ServerSocket同样的端口上，
			该选项的默认值与操作系统有关，在某些操作系统中，允许重用端口，而在某些系统中不允许重用端口。
		   	当ServerSocket关闭时，如果网络上还有发送到这个serversocket上的数据，
			这个ServerSocket不会立即释放本地端口，而是等待一段时间，确保接收到了网络上发送过来的延迟数据，然后再释放端口
		    值得注意的是，public void setReuseAddress(boolean on) throws SocketException
			必须在ServerSocket还没有绑定到一个本地端口之前使用，否则执行该方法无效。
			此外，两个公用同一个端口的进程必须都调用serverSocket.setReuseAddress(true)方法，
			才能使得一个进程关闭ServerSocket之后，另一个进程的ServerSocket还能够立刻重用相同的端口


 	SO_RCVBUF : int
	 	public void setReceiveBufferSize(int size) throws SocketException
		public int getReceiveBufferSize() throws SocketException
		 	SO_RCVBUF 表示服务器端的用于接收数据的缓冲区的大小， 以字节为单位。
		 	一般说來， 传输大的连续的数据块（ 基于 HTTP 或 FTP 协议的数据传输）可以使川较大的缓冲区, 这可以减少传输数据的次数， 从而提岛传输数据的效率。 
		 	而对于交互频繁且单次传送数据量比较小的通信( Telnet 和 网络游戏 )， 则应该采用小的缓冲区 , 确保能及时把小批量的数据发送给对方。
		 	S0_ RCVBUF 的默认值与操作系统有关。 
		 	在 Windows 2000 中，显示 SO RCVBUF 的默认值为 8192。
		 	在 Windows 10 中,显示 SO RCVBUF 的默认值为为 65536。
			 	无论绑定到特定端口之前或之后，调用setReceiveBufferSize(int size)方法都有效。 
			 	例外情况下是如果要设置大于64K的缓冲区， 则必须在绑定到特定端口之前进行设置才有效。 
			 	执行setReceiveBufferSize(int size)方法， 相当于对所有由server.accept()法返回的 Socket设置接收数据的缓冲区的大小。
		 	

 	SO_TIMEOUT : int
	 	public void setSoTimeOut(int timeout) throws SocketException
		public int getSoTimeOut() throws IOException
			SO_TIMEOUT 表示 ServerSocket 的 accept()方法等待客户连接的超时时间， 以毫秒为单位。 
			如果 SO_TIMEOUT 的值为 0, 表示永远不会超时， 这是 S0_TIME0UT 的默认值。
			当服务器执行 ServerSocket 的accept() 方 法 时， 如果连接请求队列为空， 服务器就会一直等待， 直到接收到了客户连接才从方法返回。 
			如果设定了超时时间， 那么当服务器等待的时间超过了超时时间， 就会抛出SocketTimeoutException， 它是InterruptedIOException的子类。
		
 * --------------------------------------------------------------------------------------------------------------------------
 
 *  * 设定连接时间、 延迟和带宽的相对重要性
		public void setPerformancePreferences(int connectionTime, int latency, int bandwidth)
			以上方法的 3个参数表示网络传输数据的 3项指标。
			• 参数 connectionTime: 表示用最少时间建立连接。
			• 参数 latency: 表示最小延迟。
			• 参数 bandwidth: 表示最高带宽。
			
	setPerformancePreferences()方法用来设定这 3 项指标之间的相对重要性。
		 可以为这些参数赋于任意的整数， 这些整数之间的相对大小就决定了相应参数的相对重要性。
			例如， 如果参数 connectionTime 为 2, 参 数 latency 为 1， 而参数 bandwidth 为 3， 
			就表示最高带宽最重要，其次是最少连接时间， 最后是最小延迟。
 * 
 * */
public class ServerSocketOption {

	public static void main(String[] args) {
		ServerSocket server =null;
		Socket socket =null;
		try {
			server = new ServerSocket(8000);
			System.out.println("服务器接受数据的缓冲区大小: "+server.getReceiveBufferSize());
			server.setSoTimeout(6000);// 等待客户连接的超时时间为6秒，如果设置为0 或者注释掉此语句 ，服务器将一直等待下去， 直到接收到了客户连接才从accept()方法返回。
			socket = server.accept();// 等待客户连接的过程也成为阻塞
			System.out.println("服务器已关闭");
			System.out.println("-----------");
			//System.out.println("服务器接受数据的缓冲区大小: "+server.getReceiveBufferSize());// 此句执行不到，程序在server.accept() 处阻塞
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if(server!=null) server.close();
				if(socket!=null) socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
