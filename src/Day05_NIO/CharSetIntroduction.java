package Day05_NIO;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.util.Map.Entry;
import java.util.Set;
import java.util.SortedMap;

/*		解码[decode]
 * 字节序列 	--->>   字符串
 * 
 * 		 编码[encode]	
 * 字节序列 	<<---   字符串
 * */
public class CharSetIntroduction {

	public static void main(String[] args) throws IOException {
		test2();
	}
	
	//查看支持的字符集
	public static void test1() {
		SortedMap<String, Charset> map = Charset.availableCharsets();
		Set<Entry<String, Charset>> set = map.entrySet();
		//查看支持的字符集
		for (Entry<String, Charset> entry : set) {
			System.out.println(entry.getValue());
		}
	}
	
	//编码与解码
	public static void test2() throws IOException {
		Charset cs = Charset.forName("GBK");
		CharsetEncoder encoder = cs.newEncoder();
		CharsetDecoder decoder = cs.newDecoder();
		
		CharBuffer Cbuf = CharBuffer.allocate(1024);
		Cbuf.put("测试编码字符");
		
		//编码
		Cbuf.flip();
		ByteBuffer Bbuf = encoder.encode(Cbuf);

		for(int i=0;i<12;i++) {
			System.out.println(Bbuf.get());
		}
		
		System.out.println("-------------");
		
		//解码
		Bbuf.flip();
		CharBuffer charBuf = decoder.decode(Bbuf);
		System.out.println(charBuf.toString());
		
		System.out.println("-------------");
		
		//编码不匹配，乱码
		Charset cs2 = Charset.forName("utf-8");
		Bbuf.flip();
		CharBuffer charBuf2 = cs2.decode(Bbuf);
		System.out.println(charBuf2.toString());
	}
}
