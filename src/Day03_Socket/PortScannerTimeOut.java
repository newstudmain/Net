package Day03_Socket;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

/*
 * 设定等待建立连接的超时时间
 * 	当客户端的 Socket 构造方法请求与服务器连接时， 可能要等待一段时间。
 *  默认情况下， Socket 构造方法会一直等待下去， 直到连接成功， 或者出现异常。
 *  Socket 构造方法请求连接时， 受底层网络的传输速度的影响， 可能会处于长时间的等待状态。 
 * 
 * 如果希望限定等待连接的时间， 该如何做呢？ 
 * 	此时就需要使用第一个不带参数的构造方法：
 * 	Socket() 通过系统默认类型的 SocketImpl 创建未连接套接字
 * 		Socket socket =new Socket();
 * 		InetSocketAddress address = new InetSocketAddress(host, port);
 * 		socket.connect(address, 60000);
 * 			// Socket 类的 connect(SocketAddress endpoint， int timeout)方法负责连接服务器.
 * 			// 参数 endpoint指定服务器的地址， 参数timeout 设定超时时间， 以毫秒为单位。 
 * 			// 如果参数 timeout 设为 0, 表示永远不会超时
 * 	
 * 用于连接到本地机器 上的监听 1~1024 端口的服务器程序， 等待连接的最长时间为 1分钟。 
 * 如果在 1分钟内连接成功， 则 connect()方法顺利返冋； 
 * 如果在 1 分钟内出现某种异常， 则抛出该异常； 
 * 如果超过了 1 分钟后， 既没有连接成功， 也没有出现其他异常， 那么会抛出 SocketTimeoutException。 
 * 
 * InetSocketAddress
 *  	此类实现 IP 套接字地址（IP 地址 + 端口号）。
 * 		它还可以是一对（主机名 + 端口号）.在此情况下，将尝试解析主机名。如果解析失败，则该地址将被视为未解析 地址，但是其在某些情形下仍然可以使用，比如通过代理连接。 
 * 		它提供不可变对象，供套接字用于绑定、连接或用作返回值。
 * */
public class PortScannerTimeOut {
	public static void main(String[] args) {
		String host ="localhost";
		if(args.length>0)
			host =args[0];
		new PortScanner().scan(host);
	}
	
	public void scan(String host){
		Socket socket =null;
		for(int port=1;port<=1024;port++){
			try {
				socket = new Socket();//创建未连接套接字
				InetSocketAddress address = new InetSocketAddress(host, port);//创建Socket连接地址
				socket.connect(address, 60000);	//等待建立连接的超时时间为1分钟
				System.out.println("There is a server on port: "+port);
			} catch (IOException e) {	//如果因为某些原因连接失败， 就会抛出IOException。
				//e.printStackTrace();
				System.out.println("Can't connect to port: "+port);
			}finally{
				try {
					if(socket!=null)socket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
