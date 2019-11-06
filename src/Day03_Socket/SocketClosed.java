package Day03_Socket;

/*
 * 进程 A 与进程 B 通过 Socket 通信，假定进程 A 输出数据，进程 B 读入数据。
 * 进程 A 如何告诉进程 B 所有数据己经输出完毕呢？有几种处理办法：
 * 	(1) 当 进 程 A 与进程 B 交换的是字符流，并都一行一行地读/写数据时，
 * 		可以事先约定以一个特殊的标志作为结束标忐，如以字符串 “ bye” 作为结束标志。
 * 		当进程A 向进程 B 发送一行字符串 “bye”，进程 B 读到这一行数据后，就停止读数据。
 * 	(2) 进程A先发送一个消息，告诉进程 B 所发送的正文的长度，然后再发送正文。
 * 		进程 B 先获知进程A将发送的正文的长度，接下来只要读取完该长度的字符或字节，就停止读数据。
 * 	(3) 进程 A 发完所有数据后，关闭 Socket。当进程 B 读入了进程 A 发送的所有数据后，再次执行输入流的 read()方法时，该方法返回-1。
 * 		如果执行 BufferedReader 的readLine()方法，那么该方法返冋 null。
 * 	(4) 当调用 Socket 的 close()方法关闭 Socket 时，它的输出流和输入流也都被关闭。
 * 		有的时候，可能仅仅希望关闭输出流或输入流之一。此时可以采用 Socket 类提供的半关闭方法：
 * 
 			• shutdownInput():	关闭输入流。
			• shutdownOutput(): 关闭输出流。
			
		Socket 类还提供了两个状态测试方法，用来判断输入流和输出流是否关闭：
			• public boolean isInputShutdown()：如果输入流关闭，则返回 true, 否则返冋false。
			• public boolean isOutputShutdown()：如果输出流关闭，则返回  true，否则返冋false。
			
		值得注意的是，先后调用 Socket 的 shutdownlnputO和 shutdownOutput()方法，仅仅关闭了输入流和输出流，
		并不等价于调用 Socket 的 close()方法。在通信结束后，仍然要调用 Socket 的 close()方法，
		因为只有该方法才会释放 Socket 占用的资源，如占用的本地端口等。
		
		
 * 
 * 
 * */
public class SocketClosed {


}
