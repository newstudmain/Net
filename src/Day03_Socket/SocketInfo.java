package Day03_Socket;


/*
 * 在一个 Socket 对象中同时包含了远程服务器的 IP 地址和端口信息，以及客户本地的 IP 地址和端口信息。
 * 此外，从 Socket 对象中还可以获得输出流和输入流，分别用于向服务器发送数据，以及接收从服务器端发来的数据。
 * 以 下方法用于获取 Socket 的有关信息：
 * 
 * 	
 *  getInetAddress() 
          返回套接字连接的地址。
    getLocalPort() 
          返回此套接字绑定到的本地端口。
	getLocalAddress() 
          获取套接字绑定的本地地址。 
	getLocalPort() 
          返回此套接字绑定到的本地端口。 
    getOutputStream() 
          返回此套接字的输出流。
    getInputStream() 
          返回此套接字的输入流
          
    -------------------------
    
    boolean isBound() 
          返回套接字的绑定状态。 
 	boolean isClosed() 
          返回套接字的关闭状态。 
 	boolean isConnected() 
          返回套接字的连接状态。 

如果要判断一个 Socket 对象当前是否处于连接状态， 可采用以 下方式：
boolean isConnected=socket.isConnected() &&.!socket.isClosed();

 * 
 * 
 * */
public class SocketInfo {

	public static void main(String[] args) {
		
	}
	
	
}
