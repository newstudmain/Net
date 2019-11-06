package Day04_ServerSocket;

import java.io.IOException;
import java.net.ServerSocket;

/*
 * ServerSocket 的close() 方法使服务器释放占用的端口， 并且断开与所有客户的连接。 
 * 当 一个服务器程序运行结束时，即使没有执行ServerSocket 的close() 方法，
 * 操作系统也会释放这个服务器占用的端口。 因此，服务器程序并不一定要在结束之前执行ServerSocket 的close() 方法。
 * 
 * 在某些怙况下， 如果希望及时释放服务器的端口， 以便其他程序能占用该端口，可以显式调用的ServerSocket 的close() 方法。 
 * 例如，以下代码用于扫描 1~65535 之 间的端口号。 如果 ServerSocket 成功创建， 意味该端口未被其他服务器进程绑定， 否则说明该端口已经被其他进程占用：

 * ServerSocket的 isClosed()方法判断 ServerSocket 是否关闭， 
	 * 只有执行了 ServerSocket的 close()方法， isClosed()方法才返回 true， 
	 * 否则，即使 ServerSocket 还没有和特定端口绑定， isClosed()方法也会返回 false。
 * ServerSocket 的 isBound()方法判断 ServerSocket 是否已经与一个端口绑定， 
	 * 只要ServerSocket 已经与一个端口绑定， 即使它己经被关闭， isBound()方法也会返冋 true。
 * 
 * 如果需耍确定一个 ServerSocket 己经与特定端口绑定， 并且还没有被关闭， 则可以采川以下方方:
 * 		boolean isOpen =serverSocket.isBound() && serverSocket.isClosed()

 * */
public class ServerSocketClosed {

	public static void main(String[] args) {
		for(int port=1;port<=65535;port++){
			try {
				ServerSocket server = new ServerSocket(port);
				server.close(); //创建了一个 ServerSocket对象后， 就马上关闭它， 以便及时释放它占用的端口， 从而避免程序临时占用系统的大多数端口。
			} catch (IOException e) {
				System.out.println("服务器端口： "+port+" 已被其他程序占用");
			}
		}
	}
}
