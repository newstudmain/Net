package Day05_NIO;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

/*
 * 传统的 IO 流都是阻塞式的。也就是说，当一个线程调用 read() 或 write()时，该线程被阻塞，直到有一些数据被读取或写入，
 * 该线程在此期间不能执行其他任务。因此，在完成网络通信进行 IO 操作时，由于线程会阻塞，
 * 所以服务器端必须为每个客户端都提供一个独立的线程进行处理，当服务器端需要处理大量客户端时，性能急剧下降。
 * 一  
 * 1. 通道 (Channel) : 负责连接
 * 		|--SelectableChannel
 * 			|--SocketChannel
 * 			|--ServerSocketChannel
 * 			|--DatagramChannel
 * 
 * 		|--Pip.SinkChannel
 * 		|--Pip.SourceChannel
 * 
 * 	
 * 2. 缓冲区 (Buffer) : 负责数据的存取
 * 3. 选择器 (Selector) : 多个异步I/O操作集中到一个或多个线程中（可以被看成是Unix中select()函数的面向对象版本）。
 * 			选择器（Selector） 是 SelectableChannle 对象的多路复用器，
 * 			Selector 可以同时监控多个 SelectableChannel 的 IO 状况，
 * 			利用 Selector可使一个单独的线程管理多个 Channel。Selector 是非阻塞 IO 的核心。

 * SelectionKey 表示 SelectableChannel 在 Selector 中的注册的标记
	当调用 register(Selector sel, int ops) 将通道注册选择器时，选择器对通道的监听事件，
	需要通过第二个参数 ops 指定可以监听的事件类型（用可使用 SelectionKey 的四个常量 表示）：
		 读 : SelectionKey.OP_READ （1）
		 写 : SelectionKey.OP_WRITE （4）
		 连接 : SelectionKey.OP_CONNECT （8）
		 接收 : SelectionKey.OP_ACCEPT （16）
	若注册时不止监听一个事件，则可以使用  | 位或操作符连接。

	SelectionKey：      表示 SelectableChannel 和 Selector 之间的注册关系。
					每次向选择器注册通道时就会选择一个事件(选择键)。选择键包含两个表示为整数值的操作集。
					操作集的每一位都表示该键的通道所支持的一类可选择操作。
					
		每次向选择器注册通道时就会创建一个选择键。通过调用某个键的 cancel 方法、关闭其通道，
		或者通过关闭其选择器来取消 该键之前，它一直保持有效。
		取消某个键不会立即从其选择器中移除它；相反，会将该键添加到选择器的已取消键集，以便在下一次进行选择操作时移除它。
		可通过调用某个键的 isValid 方法来测试其有效性。 
		
			interest 集合 确定了下一次调用某个选择器的选择方法时，将测试哪类操作的准备就绪信息。
			创建该键时使用给定的值初始化 interest 集合；之后可通过 interestOps(int) 方法对其进行更改。

			ready 集合 标识了这样一类操作，即某个键的选择器检测到该键的通道已为此类操作准备就绪。
			创建该键时 ready 集合被初始化为零；可以在之后的选择操作中通过选择器对其进行更新，但不能直接更新它。 
				选择键的 ready 集合指示，其通道对某个操作类别已准备就绪，该指示只是一个提示，
				并不保证线程可执行此类别中的操作而不导致被阻塞。ready 集合很可能一完成选择操作就是准确的。
				ready 集合可能由于外部事件和在相应通道上调用的 I/O 操作而变得不准确。 






 * */
public class BlockingNIO {

	public static void main(String[] args) {
		
	}
	

}

class Client {
	public static void main(String[] args) throws IOException {
		
        SocketChannel sChannel=SocketChannel.open(new InetSocketAddress("127.0.0.1",9898));
        FileChannel inChannel=FileChannel.open(Paths.get("G:\\test\\a.txt"), StandardOpenOption.READ);
        ByteBuffer buf=ByteBuffer.allocate(1024);
        
        while(inChannel.read(buf)!=-1){
            buf.flip();
            sChannel.write(buf);
            buf.clear();
        }
        
         /*
          * 关闭发送通道，表明发送完毕
          * 如果没有关闭通道，则此时与服务器一直处于连接状态，也没接受打印出服务器响应信息
          * 服务器不知道客户端 是否还有数据 等待write()，处于阻塞状态中
          * */
        sChannel.shutdownOutput();
        
        //接收服务端的反馈
        int len=0;
        while((len=sChannel.read(buf))!=-1){
            buf.flip();
            System.out.println(new String(buf.array(),0,len));
            buf.clear();
        }

        /*
         * 如果没有关闭SocketChannel
         * java.io.IOException: 远程主机强迫关闭了一个现有的连接。
         * 		客户端强制关闭了连接（没有调用SocketChannel的close方法）
         * `	服务端还在read事件中，此时读取客户端的信息时会报错。
         * */
       inChannel.close();
       sChannel.close();
	}
}

//没用Selector，阻塞型的
class BlockingServer {
	public static void main(String[] args) throws IOException {
		
        ServerSocketChannel ssChannel=ServerSocketChannel.open();
        FileChannel outChannel=FileChannel.open(Paths.get("G:\\test\\appect_a.txt"), 
        														StandardOpenOption.WRITE,
        														StandardOpenOption.CREATE);
        ssChannel.bind(new InetSocketAddress(9898));
        SocketChannel sChannel=ssChannel.accept();
        ByteBuffer buf=ByteBuffer.allocate(1024);
        
        while(sChannel.read(buf)!=-1){
            buf.flip();
            outChannel.write(buf);
            buf.clear();
        }
        
        //发送反馈给客户端
        buf.put("服务端接收数据成功".getBytes());
        buf.flip();//给为读模式
        sChannel.write(buf);
        
        sChannel.close();
        outChannel.close();
        ssChannel.close();
	}
}








