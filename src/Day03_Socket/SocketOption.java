package Day03_Socket;

import java.net.SocketException;
import java.net.SocketOptions;

/*java.net.SocketOptions [接口]
 * 
 *  TCP_NODELAY : int
  		setTcpNoDelay(boolean) [java.net.Socket 类]
		getTcpNoDelay()
			在默认情况下，客户端向服务器发送数据时，会根据数据包的大小决定是否立即发送。
			当数据包中的数据很少时，如只有1个字节，而数据包的头却有几十个字节（IP头+TCP头）时，
			系统会在发送之前先将较小的包合并到软大的包后，一起将数据发送出去。
			在发送下一个数据包时，系统会等待服务器对前一个数据包的响应，当收到服务器的响应后，再发送下一个数据包，
			这就是所谓的Nagle算法；在默认情况下，Nagle算法是开启的。
	    	这种算法虽然可以有效地改善网络传输的效率，但对于网络速度比较慢，而且对实现性的要求比较高的情况下（如游戏、Telnet等），
	    	使用这种方式传输数据会使得客户端有明显的停顿现象。因此，最好的解决方案就是需要Nagle算法时就使用它，不需要时就关闭它。
	    	而使用setTcpToDelay正好可以满足这个需求。当使用setTcpNoDelay(true) 将Nagle算法关闭后，客户端每发送一次数据，无论数据包的大小都会将这些数据发送出去。
    	
	SO_BINDADDR : int
	
	SO_REUSEADDR : int
		public boolean getReuseAddress() throws SocketException           
		public void setReuseAddress(boolean on) throws SocketException
			如果端口忙，但TCP状态位于 TIME_WAIT ，可以重用端口。
			如果端口忙，而TCP状态位于其他状态，重用端口时依旧得到一个错误信息， 抛出“Address already in use： JVM_Bind”。
			如果你的服务程序停止后想立即重启，不等60秒，而新套接字依旧使用同一端口，此时 SO_REUSEADDR 选项非常有用。
			必须意识到，此时任何非期望数据到达，都可能导致服务程序反应混乱，不过这只是一种可能，事实上很不可能。
				使用SO_REUSEADDR选项时有两点需要注意：
    				1. 必须在调用bind方法之前使用setReuseAddress方法来打开SO_REUSEADDR选项。
    				        因此，要想使用SO_REUSEADDR选项，就不能通过Socket类的构造方法来绑定端口。
    				2. 必须将绑定同一个端口的所有的Socket对象的SO_REUSEADDR选项都打开才能起作用。
    				   socket1和socket2都使用了setReuseAddress方法打开了各自的SO_REUSEADDR选项。
    				   
    				    ServerSocket socket1 = new ServerSocket();
						ServerSocket socket2 = new ServerSocket();
						
						socket1.setReuseAddress(true);
						socket1.bind(new InetSocketAddress("127.0.0.1", 8899));
						
						socket2.setReuseAddress(true);
						socket2.bind(new InetSocketAddress("127.0.0.1", 8899));
						
						这个参数在Windows平台与Linux平台表现的特点不一样。在Windows平台表现的特点是不正确的， 在Linux平台表现的特点是正确的。
						在Windows平台，多个Socket新建立对象可以绑定在同一个端口上，这些新连接是非TIME_WAIT状态的。这样做并没有多大意义。
						在Linux平台，只有TCP状态位于 TIME_WAIT ，才可以重用 端口。这才是正确的行为。
						
	SO_BROADCAST : int
		为套接字设置 SO_BROADCAST。此选项启用和禁用发送广播消息的处理能力。
		它仅用于数据报套接字和支持广播消息概念的网络上（例如，以太网、令牌网等），
		默认情况下为 DatagramSocket 设置此选项.
			为什么说程序在发送广播消息之前需要设定这个选项？
				那是为了防止一些程序并不是设计用来发送广播消息，因为用户的错误输入，而发送了广播消息。
				比如一个UDP 程序接受一个目标IP地址做为命令行参数，但用户却把这个地址写成了一个广播地址。
				这时候这个选项的作用就体现出来了，与其让这个UDP程序检验用户输入的是不是个广播地址，不如让内核来检测，
				如果是一个广播地址，但SO_BROADCAST 选项却没有被设定，EACCES错误就会被返回。
		
	IP_MULTICAST_IF : int
	IP_MULTICAST_IF2 : int
	IP_MULTICAST_LOOP : int
	IP_TOS : int
	
	SO_LINGER : int
		public int getSoLinger() throws SocketException
		public void setSoLinger(boolean on, int linger) throws SocketException//参数为秒
			 这个Socket选项可以影响close方法的行为。在默认情况下，当调用close方法后，将立即返回；
			 如果这时仍然有未被送出的数据包，那么这些数据包将被丢弃。如果将linger参数设为一个正整数n时（n的值最大是65535），在调用close方法后，将最多被阻塞n秒。
			 在这n秒内，系统将尽量将未送出的数据包发送出去；如果超过了n秒，如果还有未发送的数据包，这些数据包将全部被丢弃；而close方法会立即返回。
			 如果将linger设为0，和关闭SO_LINGER选项的作用是一样的。如果底层的Socket实现不支持SO_LINGER都会抛出SocketException例外。
			 当给linger参数传递负数值时，setSoLinger还会抛出一个IllegalArgumentException例外。
			 可以通过getSoLinger方法得到延迟关闭的时间，如果返回-1，则表明SO_LINGER是关闭的。例如，下面的代码将延迟关闭的时间设为1分钟： 
			 	socket1.setSoTimeout(30 * 1000);
		 	
	
	SO_TIMEOUT : int
		public int getSoTimeout() throws SocketException
		public void setSoTimeout(int timeout) throws SocketException
			可以通过这个选项来设置读取数据超时。当输入流的read方法被阻塞时（读入数据时还没有数据，等待中），如果设置timeout（timeout的单位是毫秒），
			那么系统在等待了timeout毫秒后会抛出一个InterruptedIOException例外。在抛出例外后，输入流并未关闭，你可以继续通过read方法读取数据。
			如果将timeout设为0，就意味着read将会无限等待下去，直到服务端程序关闭这个Socket.这也是timeout的默认值。如下面的语句将读取数据超时设为30秒。
				socket1.setSoTimeout(30 * 1000);
				Socket 的 setTimeout()方法必须在接收数据之前执行才有效。
			
	SO_SNDBUF : int
		public int getSendBufferSize() throws SocketException
		public void setSendBufferSize(int size) throws SocketException
			 在默认情况下，输出流的发送缓冲区是8096个字节（8K）。这个值是Java所建议的输出缓冲区的大小。
			 如果这个默认值不能满足要求，可以用setSendBufferSize()方法来重新设置缓冲区的大小。
			 但最好不要将输出缓冲区设得太小，否则会导致传输数据过于频繁，从而降低网络传输的效率。
			 如果底层的Socket实现不支持SO_SENDBUF选项，这两个方法将会抛出SocketException例外。
			 必须将size设为正整数，否则setSendBufferedSize方法将抛出IllegalArgumentException例外。
	
	SO_RCVBUF : int
		public int getReceiveBufferSize() throws SocketException
		public void setReceiveBufferSize(int size) throws SocketException
			在默认情况下，输入流的接收缓冲区是8096个字节（8K）。这个值是Java所建议的输入缓冲区的大小。
			如果这个默认值不能满足要求，可以用setReceiveBufferSize()方法来重新设置缓冲区的大小。
			但最好不要将输入缓冲区设得太小，否则会导致传输数据过于频繁，从而降低网络传输的效率。
			如果底层的Socket实现不支持SO_RCVBUF选项，这两个方法将会抛出SocketException例外。
			必须将size设为正整数，否则setReceiveBufferSize方法将抛出IllegalArgumentException例外。
		 
	SO_KEEPALIVE : int
		public boolean getKeepAlive() throws SocketException
		public void setKeepAlive(boolean on) throws SocketException
			如果将这个Socket选项打开，客户端Socket每隔段的时间（大约两个小时）就会利用空闲的连接向服务器发送一个数据包。
			这个数据包并没有其它的作用，只是为了检测一下服务器是否仍处于活动状态。
			如果服务器未响应这个数据包，在大约11分钟后，客户端Socket再发送一个数据包，如果在12分钟内，服务器还没响应，那么客户端Socket将关闭。
			如果将Socket选项关闭，客户端Socket在服务器无效的情况下可能会长时间不会关闭。SO_KEEPALIVE选项在默认情况下是关闭的.
				socket1.setKeepAlive(true);
		
	SO_OOBINLINE : int
			如果这个Socket选项打开，可以通过Socket类的sendUrgentData()方法向服务器发送一个单字节的数据。
			这个单字节数据并不经过输出缓冲区，而是立即发出。
			虽然在客户端并不是使用OutputStream向服务器发送数据，但在服务端程序中这个单字节的数据是和其它的普通数据混在一起的。
			因此，在服务端程序中并不知道由客户端发过来的数据是由OutputStream还是由sendUrgentData发过来的。
			值得注意的是，除非使用一些更高层次的协议， 否则接收方处理紧急数据的能力非常有限， 
			当紧急数据到来时， 接收方不会得到任何通知， 因此接收方很难区分普通数据与紧急数据， 只好按照同样的方式处理它们.
				下面是sendUrgentData方法的声明：
					public void sendUrgentData(int data) throws IOException
    					虽然sendUrgentData的参数data是int类型，但只有这个int类型的低字节被发送，其它的三个字节被忽略。

---------------------------------------------------------------------------------------------------------------
 * 在 Internet上传输数据也分为不同的服务类型， 它们有不同的定价。 用户可以根据自己的需求， 选择不同的服务类型。
 * 例如， 发送视频盖要较高的带宽， 快速到达目的地， 以保证接收方看到连续的画面。
 * 而发送电子邮件可以使用较低的带宽， 延迟几个小时到达目的地也没关系。
 * 
	 * IP规定了4种服务类型，用来定性的描述服务的质量
		  * 低成本：发送成本低
		  * 高可靠性：保证把数据可靠的送到目的地
		  * 最高吞吐量：一次可以接收或者发送大批量的数据
		  * 最小延迟：传输数据的速度快，把数据快速送达目的地

	当然这四种服务类型还可以进行相应的组合，例如，可以同时要求获得高可靠性和最小延迟。Socket类中提供了设置和读取服务类型的方法
	   * 设置服务类型: public void setTrafficClass(int trafficClass) throws SocketException;
	   * 读取服务类型： public int getTrafficClass() throws SocketException;

	Socket类用4个整数表示服务类型；
	   * 低成本： 0x02
	   * 高可靠性： 0x04
	   * 最高吞吐量： 0x08
	   * 最小延迟： 0x10
	
	例如下面代码请求高可靠性传输服务：
	   Socket socket = new Socket("www.javathinker.org",  80);
	   socket.setTrafficClass(0x04);
	
	要求高可靠性和最小延迟传输服务：
	    Socket socket = new Socket("www.javathinker.org", 80);
	    socket.setTrafficClass(0x04 | 0x10);
-----------------------------------------------------------------------------------------------------------------

 * 设定连接时间、 延迟和带宽的相对重要性
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
 * 
 * */
public class SocketOption implements SocketOptions{

    public final static int TCP_NODELAY = 0x0001;
    public final static int SO_REUSEADDR = 0x04;
    public final static int SO_LINGER = 0x0080;
    public final static int SO_TIMEOUT = 0x1006;
    public final static int SO_SNDBUF = 0x1001;
    public final static int SO_RCVBUF = 0x1002;
    public final static int SO_KEEPALIVE = 0x0008;
    public final static int SO_OOBINLINE = 0x1003;
    
	@Override
	public void setOption(int optID, Object value) throws SocketException {
		// TODO Auto-generated method stub
	}

	@Override
	public Object getOption(int optID) throws SocketException {
		// TODO Auto-generated method stub
		return null;
	}

	
}
