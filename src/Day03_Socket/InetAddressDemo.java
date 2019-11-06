package Day03_Socket;

import java.net.InetAddress;
import java.net.UnknownHostException;

/*
 * 设定服务器的地址
 * 	除了第一个不带参数的构造方法，其他构造方法都需要在参数中设定服务器的地址，包括服务器的 IP 地址或主机名，以及端口。
 * 		
 		Socket(InetAddress address, int port) 
          	创建一个流套接字并将其连接到指定 IP 地址的指定端口号。 
  		Socket(InetAddress address, int port, InetAddress localAddr, int localPort) 
          	创建一个套接字并将其连接到指定远程端口上的指定远程地址。 
  		Socket(String host, int port) 
          	创建一个流套接字并将其连接到指定主机上的指定端口号。 
  		Socket(String host, int port, InetAddress localAddr, int localPort) 
  			创建一个套接字并将其连接到指定远程主机上的指定远程端口。
  			
 * ---------------------------------------------------------------------------------------------

 * IP 地址是 IP 使用的 32 位或 128 位无符号数字，它是一种低级协议，UDP 和 TCP 协议都是在它的基础上构建的。
 * InetAddress 的实例包含 IP 地址，还可能包含相应的主机名（取决于它是否用主机名构造或者是否已执行反向主机名解析）。 
 * 
 * 
 * 设定客户端的地址
 * 	在一个 Socket 对象中，既包含远程服务器的 IP 地址和端 口佶息，也包食本地客户端的 IP 地址和端口信息。
 *	默认惜况下, 客户端的 IP 地址来自客户程序所在的主机,客户端的端口则由操作系统随机分配。
 *	 Socket 类还有两个构造方法允许显式地设置客户端的 IP 地址和端口：
 *
 *		  Socket(InetAddress address, int port, InetAddress localAddr, int localPort) 
          	创建一个套接字并将其连接到指定远程端口上的指定远程地址。 
          Socket(String host, int port, InetAddress localAddr, int localPort) 
  			创建一个套接字并将其连接到指定远程主机上的指定远程端口。
 * 	
 * 如果一个主机同时属于两个以上的网络，它就可能拥有两个以上的 IP 地址。
 * 例如 ,一个主机在 Internet 网络中的 IP 地址为 “222.67.1.34”，在一个局域网中的 IP 地址为“ 112.5.4.3 ”。
 * 假定这个主机上的客户程序希望和同—个局域网中的一个服务器程序(地址为 112.5.4.45: 8000) 通信, 
 * 客户端可按照如下方式构造 Socket 对象:
 * 	InetAddress remoteAddr=InetAddrcss.getByName("112.5.4.45");
 * 	InetAddress localAddr=InetAddress.getByName("112.5.4.3");
 * 	Socket sockct=new Socket(remoteAddr,8000,localAddr,2345); //客户端使用端口 2345
 * 
 * 
 * */
public class InetAddressDemo {
	public static void main(String[] args){
		try {
			InetAddress addr1 =InetAddress.getLocalHost();//返回本地主机地址
			System.out.println(addr1.toString());
			InetAddress addr2 =InetAddress.getByName("14.215.177.39");//返回代表"14.215.177.39"的IP地址
			System.out.println(addr2.toString());
			InetAddress addr3 =InetAddress.getByName("www.baidu.com");//返回域名为"www.baidu.com"的IP地址
			System.out.println(addr3.toString());
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}
}
