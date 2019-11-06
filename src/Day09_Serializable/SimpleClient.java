package Day09_Serializable;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.Socket;

public class SimpleClient {

	public static void main(String[] args) {
		Socket socket = null;
		InputStream in =null;
		ObjectInputStream ois =null;
		try {
			socket = new Socket("localhost",8022);
			in = socket.getInputStream();
			ois = new ObjectInputStream(in);
			Object obj1 = ois.readObject();
			Object obj2 = ois.readObject();
			System.out.println(obj1);
			System.out.println(obj2);
			System.out.println(obj1==obj2);//true
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} finally{
			try {
				if(in!=null)in.close();
				if(ois!=null)ois.close();
				if(socket!=null)socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
