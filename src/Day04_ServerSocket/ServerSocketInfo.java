package Day04_ServerSocket;

import java.io.IOException;
import java.net.ServerSocket;

/*
 * 	getInetAddress() 
          返回此服务器套接字的本地地址。 
	getLocalPort() 
          返回此套接字在其上侦听的端口。 
    
 * 如果把参数 port设为 0, 表示由操作系统来为服务器分配一个任意可用的端口。由操作系统分配的端口也称为匿名端口。
 * 只要调用getLocalPort() 方法 ，就能获知这个端口号。
 * 
 * 多数服务器会监听固定的端口， 这样才便 于客户程序访问服务器。
 * 匿名端口一般适用于服务器与客户之间的临时通信，通信结束， 就断幵连接， 并且ServerSocket 占用的临时端口也被释放。
 
 * FTP（ 文件传输协议） 就使用了匿名端口。 
 * FTP 使用两个并行的连接： 一个是控制连接， 一个是数据连接。 
 * 控制连接用于在客户和服务器之间发送控制信息， 如用户名和口令、 改变远程目录的命令或上传和下载文件的命令。 
 * 数据连接用于传送文件。 TCP服务器在 21 端口上监听控制连接，如果有客户要求上传或卜载文件， 就另外建立一个数据连接， 通过它来传送文件。 
 * 数据连接的建立冇两种方式。
 * 
 * 
 * 
 * */
public class ServerSocketInfo {
	
	public static void main(String[] args) {
		ServerSocket server =null;
		try {
			server = new ServerSocket(0);
			System.out.println("监听的端口为: "+server.getLocalPort());
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if(server!=null) server.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
