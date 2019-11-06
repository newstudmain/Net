package Day05_NIO;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;


/*
 * Channel 连接源节点到目标节点的连接，在java NIO 中负责 缓冲区中数据的传输
 * Channel 本身不存储数据，需要配合缓冲区使用。
 * 
	 * |--FileChannel			//从文件中读写数据。
	 * |--SocketChannel			//能通过TCP读写网络中的数据。
	 * |--ServerSocketChannel	//可以监听新进来的TCP连接，像Web服务器那样。对每一个新进来的连接都会创建一个SocketChannel。
	 * |--DatagramChannel		//能通过UDP读写网络中的数据。
 * 
 * 
 * 获取通道
 * 	1. java 对支持通道的类提供了 getChannel() 方法 ，获取通道实例
 * 		本地IO：
 * 			FileInputStream / FileOutputStream
 * 			RandomAccessFile
 * 		网络IO：
 * 			Socket
 * 			ServerSocket
 * 			DatagramSocket
 * 
 * 	2. jdk1.7 中的NIO2  为支持通道的类提供了静态方法 open() 获取通道实例
 * 	3. jdk1.7 中的NIO2  Files 工具类中的 newByteChannel方法 获取通道实例
 * 
 * 四、通道之间的数据传输 （直接缓冲区）
 * transferFrom()
 * transferTo()
 * 
 * 五、分散与聚集
	 * 分散（scatter）读取:  从通道将数据读入多个 Buffer 的操作， 分散 到多个缓冲区中。
	 * 聚集（gather）写入 :   将来自多个缓冲区的数据写入单个通道。
 * 
 * 
 * */
public class ChannelIntroduction {

	public static void main(String[] args) throws IOException {
		test4();
	}
	
	//使用非直接缓冲区读写数据 采用通道方式 ，
	public static void test1() {
		FileInputStream fis =null;
		FileOutputStream fos =null;
		FileChannel inchannel =null;
		FileChannel outchannel =null;
		try {
			// 获取流
			fis =new FileInputStream("G:\\test\\a.txt");
			fos =new FileOutputStream("G:\\test\\b.txt");
			// 获取通道
			inchannel = fis.getChannel();
			outchannel = fos.getChannel();
			// 获取缓冲区,分配大小
			ByteBuffer buf= ByteBuffer.allocate(1024);
			//读写缓冲区数据
			while(inchannel.read(buf)!= -1) {
				buf.flip();//切换读模式
				outchannel.write(buf);//读buf中的数据
				buf.clear();//清空缓冲区，准备下一次读取
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if(inchannel!=null) inchannel.close();
				if(outchannel!=null) outchannel.close();
				if(fis!=null) fis.close();
				if(fos!=null) fos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
	}
}
	
	//使用直接缓冲区读写数据，采用内存映射文件方式 ，数据传输块，直接操作物理内存但不稳定，jvm流回收不及时
	public static void test2() throws IOException {
		long start = System.currentTimeMillis();
		
        FileChannel inChannel = FileChannel.open(Paths.get("G:\\test\\a.txt"), 
        												StandardOpenOption.READ);
        FileChannel outChannel = FileChannel.open(Paths.get("G:\\test\\c.txt"), 
        												StandardOpenOption.WRITE, 
        												StandardOpenOption.READ,
        												StandardOpenOption.CREATE);

        //内存映射文件
        MappedByteBuffer inMappedBuf = inChannel.map(FileChannel.MapMode.READ_ONLY, 0, inChannel.size());
        MappedByteBuffer outMappedBuf = outChannel.map(FileChannel.MapMode.READ_WRITE, 0, inChannel.size());

        //直接对缓冲区进行数据读写操作
        byte[] dst = new byte[inMappedBuf.limit()];
        inMappedBuf.get(dst);
        outMappedBuf.put(dst);

        inChannel.close();
        outChannel.close();

        long end = System.currentTimeMillis();
        System.out.println("耗费的时间为：" + (end - start));
	}

	
	//通道之间的数据传输（直接缓冲区）
	public static void test3() throws IOException {

     FileChannel inChannel = FileChannel.open(Paths.get("1.jpg"), StandardOpenOption.READ);
     
     FileChannel outChannel = FileChannel.open(Paths.get("3.jpg"), StandardOpenOption.WRITE, 
														    		StandardOpenOption.READ, 
														    		StandardOpenOption.CREATE);

     inChannel.transferTo(0, inChannel.size(), outChannel);
     //等价于
     //outChannel.transferFrom(inChannel, 0, inChannel.size());

     inChannel.close();
     outChannel.close();
	}

	//分散与聚集
	public static void test4() throws IOException {
		RandomAccessFile rafScatter = new RandomAccessFile("G:\\test\\a.txt", "rw");
		
		//获取通道
		FileChannel channelScatter = rafScatter.getChannel();
		//分配缓冲区
		ByteBuffer buf1 = ByteBuffer.allocate(1);
		ByteBuffer buf2 = ByteBuffer.allocate(10);
		//分散（scatter）读取
		ByteBuffer[] bufs = {buf1,buf2};
		channelScatter.read(bufs);
		
		for (ByteBuffer byteBuffer : bufs) {
			byteBuffer.flip();
		}
		System.out.println(new String(bufs[0].array(),0,bufs[0].limit()));
		System.out.println("----------");
		System.out.println(new String(bufs[1].array(),0,bufs[1].limit()));
		
		//聚集（gather）写入
		RandomAccessFile rafGather = new RandomAccessFile("G:\\test\\e.txt", "rw");
		FileChannel channelGather = rafGather.getChannel();
		
		channelGather.write(bufs);
		
		
	}
	
}
