package Day03_Socket;

import java.io.IOException;
import java.net.Socket;

/*
 *  Socket() 
          	通过系统默认类型的 SocketImpl 创建未连接套接字 
  	Socket(InetAddress address, int port) 
          	创建一个流套接字并将其连接到指定 IP 地址的指定端口号。 
  	Socket(InetAddress address, int port, InetAddress localAddr, int localPort) 
          	创建一个套接字并将其连接到指定远程端口上的指定远程地址。 
  	Socket(String host, int port) 
          	创建一个流套接字并将其连接到指定主机上的指定端口号。 
  	Socket(String host, int port, InetAddress localAddr, int localPort) 
  	
  	除了第一个不带参数的构造方法以外， 其他构造方法都会试图建立与服务器的连接， 
  	如果连接成功， 就返回 Socket对象； 如果因为某些原因连接失败， 就会抛出IOException。

 * */

public class PortScanner {
	public static void main(String[] args) {
		String host ="localhost";
		if(args.length>0)
			host =args[0];
		new PortScanner().scan(host);;
	}
	
	public void scan(String host){
		Socket socket =null;
		for(int port=1;port<=1024;port++){
			try {
				socket = new Socket(host,port);//创建套接字对象，并尝试连接，连接成功就返回Socket对象。
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
