package Day05_NIO;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

/*
 *  为什么传统的监控多个 socket 的 Java 解决方案是为每个 socket 创建一个线程并使得线
	程可以在 read( )调用中阻塞，直到数据可用?
	
		这事实上将每个被阻塞的线程当作了 socket 监控器，并将 Java 虚拟机的线程调度当作了通知机制。
		这两者本来都不是为了这种目的而设计的。程序员和 Java 虚拟机都为管理所有这些线程的复杂性和性能损耗付出了代价，
		这在线程数量的增长失控时表现得更为突出。真正的就绪选择必须由操作系统来做。
		操作系统的一项最重要的功能就是处理 I/O 请求并通知各个线程它们的数据已经准备好了。
		选择器类提供了这种抽象，使得 Java 代码能够以可移植的方式，请求底层的操作系统提供就绪选择服务。
 * 
 * 
 * 
 * 
 3. 选择器 (Selector) : 多个异步I/O操作集中到一个或多个线程中（可以被看成是Unix中select()函数的面向对象版本）。
		选择器类管理着一个被注册的通道集合的信息和它们的就绪状态。
		通道是和选择器一起被注册的，并且使用选择器来更新通道的就绪状态。
	 	当这么做的时候，可以选择将被激发的线程挂起，直到有就绪的的通道.
		 
	  		选择器（Selector） 是 SelectableChannle 对象的多路复用器，
	 		Selector 可以同时监控多个 SelectableChannel 的 IO 状况，
	  		利用 Selector可使一个单独的线程管理多个 Channel。Selector 是非阻塞 IO 的核心。
	  		
	  		public abstract class Selector{
				
				public static Selector open( ) throws IOException
				public abstract boolean isOpen( );
				public abstract void close( ) throws IOException;
				public abstract SelectionProvider provider( );
				public abstract int select( ) throws IOException;
				public abstract int select (long timeout) throws IOException;
				public abstract int selectNow( ) throws IOException;
				public abstract void wakeup( );
				public abstract Set keys( );	//返回此选择器的键集。
				public abstract Set selectedKeys( );	// 返回此选择器的已选择键集。
			}
	  		
	  	SelectableChannle	
	  		这个抽象类提供了实现通道的可选择性所需要的公共方法。它是所有支持就绪检查的通道类的父类。
	  		FileChannel 对象不是可选择的，因为它们没有继承 SelectableChannel。
			所有 socket 通道都是可选择的，包括从管道(Pipe)对象的中获得的通道。
			SelectableChannel 可以被注册到 Selector 对象上，同时可以指定对那个选择器而言，那种操作是感兴趣的。
			一个通道可以被注册到多个选择器上，但对每个选择器而言只能被注册一次
	
			SelectableChannel 类上定义了 register()方法， 
			将 register()放 在SelectableChannel 上而不是 Selector 上，这种做法看起来有点随意。

 * 
 * * SelectionKey 表示 SelectableChannel 在 Selector 中的注册的标记
 * 		选择键封装了特定的通道与特定的选择器的注册关系。
 * 		选择键对象被SelectableChannel.register()返回
 * 		并提供一个表示这种注册关系的标记。选择键包含了两个比特集（以整数的形式进行编码），
 * 		指示了该注册关系所关心的通道操作，以及通道已经准备好的操作
 * 
 * 					通道在被注册到一个选择器上之前，必须先设置为非阻塞模式
 * 					通过调用 configureBlocking(false)
 * 
					 	调用可选择通道的 register()方法会将它注册到一个选择器上。
					 	如果您试图注册一个处于阻塞状态的通道， register()将抛出未检查的 IllegalBlockingModeException 异常。
					 	此外，通道一旦被注册，就不能回到阻塞状态。
					 	试图这么做的话，将在调用 configureBlocking()方法时将抛出IllegalBlockingModeException异常。
						并且，理所当然地，试图注册一个已经关闭的 SelectableChannel 实例的话，也将抛出ClosedChannelException 异常。
 * 
		当调用 register(Selector sel, int ops) 将通道注册选择器时，选择器对通道的监听事件，
		需要通过第二个参数 ops 指定可以监听的事件类型（用可使用 SelectionKey 的四个常量 表示）：
			 读 : SelectionKey.OP_READ （1） 		//输入流中已经有可读数据，可以执行读操作
			 写 : SelectionKey.OP_WRITE （4）		//已经可以向输出流中写数据  | SocketChannel 提供了读和写的方法
			 连接 : SelectionKey.OP_CONNECT （8） 	//客户与服务器的连接已经经历成功
			 接收 : SelectionKey.OP_ACCEPT （16）	//
		若注册时不止监听一个事件，则可以使用  | 位或操作符连接。
		
		https://blog.csdn.net/tennysonsky/article/details/45621341
		

	SelectionKey：      表示 SelectableChannel 和 Selector 之间的注册关系。
					每次向选择器注册通道时就会选择一个事件(选择键)。选择键包含两个表示为整数值的操作集。
					操作集的每一位都表示该键的通道所支持的一类可选择操作。
					
		每次向选择器注册通道时就会创建一个选择键。通过调用某个键的 cancel 方法、关闭其通道，
		或者通过关闭其选择器来取消 该键之前，它一直保持有效。
		取消某个键不会立即从其选择器中移除它；相反，会将该键添加到选择器的已取消键集，以便在下一次进行选择操作时移除它。
		可通过调用某个键的 isValid 方法来测试其有效性。 
		
		- 	对于键的 interest（感兴趣的操作）集合和 ready（已经准备好的操作）集合的解释是和特定的
			通道相关的。每个通道的实现，将定义它自己的选择键类。在 register( )方法中构造它并将它传递给
			所提供的选择器对象
			
				interest 集合 确定了下一次调用某个选择器的选择方法时，将测试哪类操作的准备就绪信息。
				创建该键时使用给定的值初始化 interest 集合；之后可通过 interestOps(int) 方法对其进行更改。
	
				ready 集合 标识了这样一类操作，即某个键的选择器检测到该键的通道已为此类操作准备就绪。
				创建该键时 ready 集合被初始化为零；可以在之后的选择操作中通过选择器对其进行更新，但不能直接更新它。 
					选择键的 ready 集合指示，其通道对某个操作类别已准备就绪，该指示只是一个提示，
					并不保证线程可执行此类别中的操作而不导致被阻塞。ready 集合很可能一完成选择操作就是准确的。
					ready 集合可能由于外部事件和在相应通道上调用的 I/O 操作而变得不准确。 
				
				
					SelectionKey		SelectionKey		SelectionKey
					interest：WRITE		interest：READ		interest：READ&WRITE
						|————————————————————|——————————————————————|
										  Selector
										  
									选择器维护了一个需要监控的通道的集合。
									
								一个给定的通道可以被注册到多于一个 的选择器上，
								而且不需要知道它被注册了那个Selector对象上 。
								它将返回一个封装了两个对象的关系的选择键对象。
								重要的是选择器对象控制了被注册到它之上的通道的选择过程
			
								【选择器才是提供管理功能的对象，而不是可选择通道对象。
								选择器对象对注册到它之上的通道执行就绪选择，并管理选择键】
								
					
			public abstract class SelectionKey{
				
				public static final int OP_READ
				public static final int OP_WRITE
				public static final int OP_CONNECT
				public static final int OP_ACCEPT
				public abstract SelectableChannel channel( );
				public abstract Selector selector( );
				public abstract void cancel( );
				public abstract boolean isValid( );
				public abstract int interestOps( );
				public abstract void interestOps (int ops);
				public abstract int readyOps( );	
													返回当前的 interest 集合   .. [一个用于指示那些通道/选择器组合体所关心的操作]
													最初，这应该是通道被注册时传进来的值。这个 interset 集永远不会被选择器改变，
													但您可以通过调用 interestOps( )方法并传入一个新的比特掩码参数来改变它。 
				public final boolean isReadable( )
				public final boolean isWritable( )
				public final boolean isConnectable( )
				public final boolean isAcceptable( )
				public final Object attach (Object ob)
				public final Object attachment( )
			}
 * 	
 * 
 * 	 
	           
	           
	           通过 SelectionKey 对象来表示可选择通道到选择器的注册。选择器维护了三种选择键集： 

				- 键集 包含的键表示当前通道到此选择器的注册。此集合由 keys 方法返回。 
				- 已选择键集 是这样一种键的集合，即在前一次选择操作期间，
				     检测每个键的通道是否已经至少为该键的相关操作集所标识的一个操作准备就绪。
				     此集合由 selectedKeys 方法返回。已选择键集始终是键集的一个子集。 
				- 已取消键集 是已被取消但其通道尚未注销的键的集合。不可直接访问此集合。已取消键集始终是键集的一个子集。 
				
					[ 在新创建的选择器中，这三个集合都是空集合。 ]
					



		三次握手的连接队列:
		
			1、未完成连接队列（incomplete connection queue），
					每个这样的 SYN 分节对应其中一项：已由某个客户发出并到达服务器，
					而服务器正在等待完成相应的 TCP 三次握手过程。这些套接口处于 SYN_RCVD 状态。
			
			2、已完成连接队列（completed connection queue），
					每个已完成 TCP 三次握手过程的客户对应其中一项。这些套接口处于 ESTABLISHED 状态。
							当来自客户的 SYN 到达时，TCP 在未完成连接队列中创建一个新项，然后响应以三次握手的第二个分节：
							服务器的 SYN 响应，其中稍带对客户 SYN 的 ACK（即SYN+ACK），这一项一直保留在未完成连接队列中，
							直到三次握手的第三个分节（客户对服务器 SYN 的 ACK ）到达或者该项超时为止。

			 * connect()函数
					对于客户端的 connect() 函数，该函数的功能为客户端主动连接服务器，建立连接是通过三次握手，
					而这个连接的过程是由内核完成，不是这个函数完成的，这个函数的作用仅仅是通知 Linux 内核，
					让 Linux 内核自动完成 TCP 三次握手连接（三次握手详情，请看《浅谈 TCP 三次握手》），
					最后把连接的结果返回给这个函数的返回值（成功连接为0， 失败为-1）。
					
				listen()函数
					对于服务器，它是被动连接的。举一个生活中的例子，通常的情况下，
					移动的客服（相当于服务器）是等待着客户（相当于客户端）电话的到来。而这个过程，需要调用listen()函数。
					listen() 函数的主要作用就是将套接字( sockfd )变成被动的连接监听套接字（被动等待客户端的连接），
					至于参数 backlog 的作用是设置内核中连接队列的长度（这个长度有什么用，后面做详细的解释），
					TCP 三次握手也不是由这个函数完成，listen()的作用仅仅告诉内核一些信息。
					
					这里需要注意的是，listen()函数不会阻塞，它主要做的事情为，
							将该套接字和套接字对应的连接队列长度告诉 Linux 内核，然后，listen()函数就结束。
							这样的话，当有一个客户端主动连接（connect()），
							Linux 内核就自动完成TCP 三次握手，
							将建立好的链接自动存储到队列中，如此重复。
							所以，只要 TCP 服务器调用了 listen()，
							客户端就可以通过 connect() 和服务器建立连接，
							而这个连接的过程是由内核完成。
			
				accept()函数
					accept()函数功能是，从处于 established 状态的连接队列头部取出一个已经完成的连接，
					如果这个队列没有已经完成的连接，accept()函数就会阻塞，直到取出队列中已完成的用户连接为止。
							TCP 的连接队列满后，Linux 不会如书中所说的拒绝连接，只是有些会延时连接，
							而且accept()未必能把已经建立好的连接全部取出来（如：当队列的长度指定为 0 ），
							写程序时服务器的 listen() 的第二个参数最好还是根据需要填写，写太大不好
							（具体可以看cat /proc/sys/net/core/somaxconn，默认最大值限制是 128），浪费资源，写太小也不好，延时建立连接。

 * 
 * 
 * */
public class NonBlockingNIO {
//	public static void main(String[] args) {
//		
//	}

}




class NonBlockingServer {
	
	public static void main(String[] args) throws IOException {
        //1.获取通道
        ServerSocketChannel ssChannel=ServerSocketChannel.open();

        //2.切换非阻塞式模式
        ssChannel.configureBlocking(false);

        //3.绑定连接
        ssChannel.bind(new InetSocketAddress(9898));

        //4.获取选择器
        Selector selector=Selector.open();

        //5.将通道注册到选择器上，并且指定“监听接收事件”
        ssChannel.register(selector,SelectionKey.OP_ACCEPT);
        //6.轮询式的获取选择器上已经“准备就绪”的事件
        /*	选择一组键，其相应的通道已为 I/O 操作准备就绪。 
			此方法执行处于阻塞模式的选择操作。仅在至少选择一个通道、调用此选择器的 wakeup 方法，
			或者当前的线程已中断（以先到者为准）后此方法才返回。 
			
			返回：
				已更新其准备就绪操作集的键的数目，该数目可能为零 
         */
        System.out.println("Keys: "+selector.keys().size());
        //System.out.println("selects: "+selector.select());
        
       for (SelectionKey key : selector.keys()) {
    	   System.out.println(key.interestOps());//16...SelectionKey.OP_ACCEPT
       }
       
        while(selector.select()>0){

            //7.获取当前选择器中所有注册的“选择键（已就绪的监听事件）”
            Iterator<SelectionKey> it=selector.selectedKeys().iterator();

            while(it.hasNext()){
                //8.获取准备“就绪”的事件
                SelectionKey sk=it.next();

                //9.判断具体是什么时间准备就绪, 测试此键的通道是否已准备好接受新的套接字连接。
                if(sk.isAcceptable()){//该调用与以下调用的作用完全相同： k.readyOps() & OP_ACCEPT != 0

                    //10.若“接收就绪”，获取客户端连接
                    SocketChannel sChannel=ssChannel.accept();

                    //11.切换非阻塞模式
                    sChannel.configureBlocking(false);

                    //12.将该通道注册到选择器上
                    sChannel.register(selector, SelectionKey.OP_READ);
                    
                    
                    	//测试此键的通道是否已准备好进行读取。 
                }else if(sk.isReadable()){// sk.readyOps() & OP_READ != 0
                	
                    //13.获取当前选择器上“读就绪”状态的通道
                    SocketChannel sChannel=(SocketChannel)sk.channel();
                    //14.读取数据
                    ByteBuffer buf=ByteBuffer.allocate(1024);
                    int len=0;
                    while((len=sChannel.read(buf))>0){
                        buf.flip();
                        System.out.println(new String(buf.array(),0,len));
                        buf.clear();
                    }
                }
                //15.取消选择键SelectionKey
                it.remove();
            }
        }
    }
}
