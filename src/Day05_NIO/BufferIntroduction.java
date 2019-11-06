package Day05_NIO;

import java.nio.Buffer;
import java.nio.ByteBuffer;

/*
 * 
 * 
 * 一 缓冲区，在java NIO 中负责数据的存取， 缓冲区是数组。
 * 
 	public abstract class Buffer {
 		
 		public final int capacity( )
		public final int position( )
		public final Buffer position (int newPositio
		public final int limit( )
		public final Buffer limit (int newLimit)
		public final Buffer mark( )
		public final Buffer reset( )
		public final Buffer clear( ) //
		public final Buffer flip( )
		public final Buffer rewind( )
		public final int remaining( ) //返回缓冲区的剩余容量
		public final boolean hasRemaining( ) //
		public abstract boolean isReadOnly( );
 	}
 	

	四个属性来提供关于其所包含的数据元素的信息。它们是：
		容量（ Capacity）
			缓冲区能够容纳的数据元素的最大数量。
			这一容量在缓冲区创建时被设定，并且永远不能被改变。
		上限 （ Limit）
			缓冲区的第一个不能被读或写的元素。或者说，缓冲区中现存元素的计数。
		位置 （ Position）
			下一个要被读或写的元素的索引。位置会自动由相应的 get( )和 put( )函数更新。
		标记 （ Mark）
			一个备忘位置。调用 mark( )来设定 mark = postion。
			调用 reset( )设定 position =mark。标记在设定前是未定义的(undefined)。
	
 	这些函数将引用返回到它们在（this）上被引用的对象。这是一个允许级联调用的类设计方法。
 	
 	clear()这类函数，您通常应当返回 void，而不是 Buffer 引用。
 	isReadOnly()函数。所有的缓冲区都是可读的，但并非所有都可写。
 	每个具体的缓冲区类都通过执行 isReadOnly()来标示其是否允许该缓存区的内容被修改 。 
 	一些类型的缓冲区类可能未使其数据元素存储在一个数组中。例如MappedByteBuffer 的内容可能实际是一个只读文件。
 	您也可以明确地创建一个只读缓冲区 ， 来禁止对内容的意外修改。 对只读的缓冲区的修改尝试将会导致ReadOnlyBufferException 抛出。
 	
 	compact()压缩此缓冲区（可选操作）， 将缓冲区的当前位置和界限之间的字节（如果有）复制到缓冲区的开始处。
 	即将索引 p = position() 处的字节复制到索引 0 处，将索引 p + 1 处的字节复制到索引 1 处，
 	依此类推，直到将索引 limit() - 1 处的字节复制到索引 n = limit() - 1 - p 处。
 	然后将缓冲区的位置设置为 n+1，并将其界限设置为其容量。如果已定义了标记，则丢弃它。

 	
 	上文所列出的的 Buffer API 并没有包括 get()或 put()函数。每一个 Buffer 类都有这两个函数，
 	但它们所采用的参数类型，以及它们返回的数据类型，对每个子类来说都是唯一的，
 	所以它们不能在顶层 Buffer 类中被抽象地声明。它们的定义必须被特定类型的子类所遵从。
 	
 	ByteBuffer
 	CharBuffer
 	ShortBuffer
 	IntBuffer
 	LongBuffer
 	FloatBuffer
 	DoubleBuffer
 	
 	通过 allocate() 获取对应的缓冲区 
 			
 			ByteBuffer.allocate();
 	
 * 
 	public abstract class ByteBufferextends Buffer 
			implements Comparable<ByteBuffer> {
 		// This is a partial API listing
		public abstract byte get( );							//相对读
		public abstract byte get (int index);					//绝对读
		public abstract ByteBuffer put (byte b);				//相对写
		public abstract ByteBuffer put (int index, byte b);		//绝对写
 
 	}
 	
 	//存取数据的方法
 	Get 和 put 可以是相对的或者是绝对的。在前面的程序列表中，相对方案是不带有索引
	参数的函数。当相对函数被调用时，位置在返回时前进一。如果位置前进过多，相对运算就会
	抛 出 异 常 。
	
	put filp get rewind clear
	
	
					|  |  |  |  |  |  |  |  |  |
			position(0)							capatcity(8)
												limit(8)
													
					| a | b | c | d |  |  |  |  |  |   << put a b c d //插值
							position(4)				capatcity(8)
													limit(8)

					| a | b | c | d |  |  |  |  |  |   << filp ...<< put a b c d //读模式
			position(0)		   limit(4)				capatcity(8)
			
			
					| a | b | c | d |  |  |  |  |  |   get..0-4<< filp ...<< put a b c d //取值
					   		   limit(4)				capatcity(8)			
							position(4)				
									
													
					| a | b | c | d |  |  |  |  |  |   rewind..<<get..0-4<< filp ...<< put a b c d //重读
					   		   limit(4)				capatcity(8)			
			position(0)							
			
					| a | b | c | d |  |  |  |  |		clear..<<rewind..<<get..0-4<< filp ...<< put a b c d //清除，数据依然存在
			position(0)							capatcity(8)
												limit(8)
												
----------------------------------------------------------------------------------------------------------------------												
	Mark 标记
					
					| a | b | c | d |  |  |  |  |  |   << put a b c d //插值
							position(4)				capatcity(8)
													limit(8)
													
					| a | b | c | d |  |  |  |  |  |   << filp ...<< put a b c d //读模式
			position(0)		   limit(4)				capatcity(8)
			
			
					| a | b | c | d |  |  |  |  |  |   get..0-2<< filp ...<< put a b c d //取值
					   		   limit(4)				capatcity(8)			
				     position(2)				

					| a | b | c | d |  |  |  |  |  |   Mark..<<get..2<< filp ...<< put a b c d //设置标记位
					   		   limit(4)				capatcity(8)			
				     position(2)					
					     Mark( )
					 
					| a | b | c | d |  |  |  |  |  |   get..0-2<< filp ...<< put a b c d //取值
					   		   limit(4)				capatcity(8)			
				            position(4)				
					 
					| a | b | c | d |  |  |  |  |  |   reset...<<get..0-2<< filp ...<< put a b c d //回到标记位
					   		   limit(4)				capatcity(8)			
				     position(2)				
					 	reset( )
					 
					 
	 对 于 put() ， 如 果 运 算 会 导 致 位 置 超 出 上 界 ， 就 会 抛 出BufferOverflowException 异常。
	 对于 get()，如果位置不小于上界，就会抛出BufferUnderflowException 异常。
	 绝对存取不会影响缓冲区的位置属性，但是如果您所提供的索引超出范围（负数或不小于上界），
	 也将抛出 IndexOutOfBoundsException 异常

	//将代表 Hello 字符串的 ASCII 码载入一个名为 buffer 的ByteBuffer 对象中。
	buffer.put((byte)'H').put((byte)'e').put((byte)'l').put((byte)'l').put((byte)'o');
	
	//假设我们想将缓冲区中的内容从 Hello 的 ASCII 码更改为  Mellow 。我们可以这样实现：
	buffer.put(0,(byte)'M').put((byte)'w');
	这里通过进行一次绝对方案的 put 将 0 位置的字节代替为十六进制数值 0x4d，
	将 0x77放入当前位置（当前位置不会受到绝对 put()的影响）的字节，并将位置属性加一。
	
	-------------------------
	
	我们已经写满了缓冲区，现在我们必须准备将其清空。我们想把这个缓冲区传递给一个通
	道，以使内容能被全部写出。但如果通道现在在缓冲区上执行 get()，那么它将从我们刚刚
	插入的有用数据之外取出未定义数据。如果我们将位置值重新设为 0，通道就会从正确位置开
	始获取，但是它是怎样知道何时到达我们所插入数据末端的呢？这就是上界属性被引入的目
	的。上界属性指明了缓冲区有效内容的末端。我们需要将上界属性设置为当前位置，然后将位
	置重置为 0。我们可以人工用下面的代码实现
 	
 		buffer.limit(buffer.position()).position(0);
 	 	API 为我们提供了一个非常便利的函数：Buffer.flip();//读模式
 	
 *
 *  一  非直接缓冲区与直接缓冲区
 * 非直接缓冲区：通过allocate()分配缓冲区，将缓冲区建立在JVM的内存中
 * 直接缓冲区：通过allocateDirect()分配直接缓冲区，将缓冲区建立在物理内存中，可以提高效率。
 * 		此方法返回的 缓冲区进行分配和取消分配所需成本通常高于非直接缓冲区 。
 *          直接缓冲区的内容可以驻留在常规的垃圾回收堆之外.
 *          将直接缓冲区主要分配给那些易受基础系统的本机 I/O 操作影响的大型、持久的缓冲区。
 *          最好仅在直接缓冲区能在程序性能方面带来明显好处时分配它们。
 *          直接字节缓冲区还可以过 通过FileChannel 的 map() 方法 将文件区域直接映射到内存中来创建 。该方法返回MappedByteBuffe
 *
 * */
public class BufferIntroduction {

	public static void main(String[] args) {
		test1();
	}
	
	public static void test1() {
		//1. 通过 allocate() 获取指定大小的缓冲区 
		ByteBuffer buf = ByteBuffer.allocate(1024);
		
		
		System.out.println("-------------allocate-------------");
		System.out.println("position: "+buf.position());
		System.out.println("limit: "+buf.limit());
		System.out.println("capacity: "+buf.capacity());
		
		String str ="abcd";
		//2. 利用 put存数据
		buf.put(str.getBytes());
		
		System.out.println("-------------put-------------");
		System.out.println("position: "+buf.position());
		System.out.println("limit: "+buf.limit());
		System.out.println("capacity: "+buf.capacity());
		
		//获取读模式
		buf.flip();
		System.out.println("-------------flip-------------");
		System.out.println("position: "+buf.position());
		System.out.println("limit: "+buf.limit());
		System.out.println("capacity: "+buf.capacity());
		
		//3. 利用 get读数据
		System.out.println("-------------get-------------");
		byte[] dst =new byte[buf.limit()];
		buf.get(dst,0,dst.length);
		System.out.println("获取数据: "+new String(dst,0,dst.length));
		
		
		System.out.println("position: "+buf.position());
		System.out.println("limit: "+buf.limit());
		System.out.println("capacity: "+buf.capacity());
		
		//4. rewind  获取读模式，重读
		buf.rewind();
		System.out.println("-------------rewind-------------");
		System.out.println("position: "+buf.position());
		System.out.println("limit: "+buf.limit());
		System.out.println("capacity: "+buf.capacity());
		
		//5. clear 清空缓冲区 ,但缓冲区数据依然存在，处于被遗忘状态
		buf.clear();
		System.out.println("-------------clear-------------");
		System.out.println("position: "+buf.position());
		System.out.println("limit: "+buf.limit());
		System.out.println("capacity: "+buf.capacity());
		
		//6. 标记
		
		//7. 常用判断
		System.out.println("-------------hasRemaining-------------");
		if(buf.hasRemaining())// 缓冲区中是否还用可操作的数据
			System.out.println("缓冲区中还有可操作的数据量: "+buf.remaining());// 还有几个
	}
	
	
	public static void test2() {
        //分配直接缓冲区
        ByteBuffer buffer = ByteBuffer.allocateDirect(1024);
        System.out.println(buffer.isDirect()); //判断是否是直接缓冲
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
