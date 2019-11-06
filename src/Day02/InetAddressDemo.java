package Day02;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class InetAddressDemo {
	public static void main(String[] args){
		try {
			InetAddress addr1 =InetAddress.getLocalHost();
			System.out.println(addr1.toString());
			InetAddress addr2 =InetAddress.getByName("14.215.177.39");
			System.out.println(addr2.toString());
			InetAddress addr3 =InetAddress.getByName("www.baidu.com");
			System.out.println(addr3.toString());
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}
}
