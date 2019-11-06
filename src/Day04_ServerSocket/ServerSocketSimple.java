package Day04_ServerSocket;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.UnknownHostException;

/*
 * 
    ServerSocket() 
          创建非绑定服务器套接字。 
	ServerSocket(int port) 
	创建绑定到特定端口的服务器套接字。 
	ServerSocket(int port, int backlog) 
	利用指定的 backlog 创建服务器套接字并将其绑定到指定的本地端口号。 
	ServerSocket(int port, int backlog, InetAddress bindAddr) 
	 使用指定的端口、侦听 backlog 和要绑定到的本地 IP 地址创建服务器。 
 
  	backlog:客户端请求队列的长度
  	bindAddr:指定服务器要绑定的IP
  
 	除了第一个不带参数的构造方法以外，其他构造方法都会使服务器与特定端口绑定， 该端口由参数port行指定。 
 	如果把参数 port设为 0, 表示由操作系统来为服务器分配一个任意可用的端口。由操作系统分配的端口也称为匿名端口。 
 	对于多数服务器， 会使用明确的端口， 而不会使用匿名端口， 闪为客户程序需要事先知道服务器的端口， 才能方便地访问服务器。
 	在某些场合， 匿名端口有着特殊的用途。
 * 
 		ServerSocket serverSocket=new ServerSocket(80);
 * 
 * BindException —般 是 由 以 下原因造成的：
	• 端口已经被其他服务器进程占用；
	• 在某些操作系统中， 如果没有以超级用户的身份来运行服务器程序， 那么操作系统不允许服务器绑定到 1~1023 之间的端口。
 * 
 * 
 * 
 * 
 * */
public class ServerSocketSimple {

	/*
	 * 如果主机只有一个 IP 地址， 那么默认情况下， 服务器程序就与该 IP 地址绑定。
	 * ServerSocket 的第 4 个构造方法 ServerSocket(int port, int backlog, InetAddress bindAddr) 
	 * 有一个bindAddr参数， 它显式指定服务器要绑定的 IP 地址， 该构造方法适用于有多个  IP 地址的主机。
	 * 假定一个主机有两个网卡， 一个网卡连接到 Internet, IP 地址为 222.67.5.94, 还有一个网卡用于连接到本地局域网, IP地址为 192.168.0.110。
	 * 如果服务器仅仅被本地局域网中的客户访问， 那么可以按如下方式创建 ServerSocket：
	 * */
	@SuppressWarnings("resource")
	public static void main(String[] args) {
		try {
			//
			ServerSocket serverSocket = new ServerSocket(8000,10,InetAddress.getByName("192.168.0.110"));
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
	/*
	 * ServerSocket 不带参数的默认构造方法。通过该方法不与任何端口绑定，接下来还需要用bind()方法与特定端口绑定。
	 * 其主要用途是，在绑定到特定端口前，先设置ServerSocket 的一些选项。【如果先绑定端口，则设置不起作用】
			ServerSocket serverSocket = new ServerSocket();
			scrverSocket.setReuseAddress(true);//设置ServerSocket的选项
			scrverSocket.bind(new InetSocketAddress(8000));//与 8000端口绑定
	 * 
	 * */	
		
	}
}
