package Day09_Serializable;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;

public class SimpleServer {

	public static void main(String[] args) {
		new SimpleServer().createObj("customerVarAss_seri");
	}
	
	public void send(Object obj){
		ServerSocket server =null;
		OutputStream out =null; 
		ObjectOutputStream oos =null;
		Socket socket =null; 
		
		try {
			server =new ServerSocket(8022);
			while(true){
				socket = server.accept();
				out = socket.getOutputStream();
				oos = new ObjectOutputStream(out);
				oos.writeObject(obj);
				oos.writeObject(obj);//发送两次序列化对象，验证由一个ObjectOutputStream 多次序列化同一个对象，反序列化时是否也是同一个对象
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		} finally{
				try {
					if(out!=null) out.close();
					if(oos!=null) oos.close();
					if(socket!=null) server.close();
					if(server!=null) server.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
		
	}
	
	public void createObj(String str){
		Object obj = null;
		if(str.equals("date")){
			obj = new Date();
		} else if(str.equals("list")){
			obj = new ArrayList<String>();
		} else if(str.equals("customer")){
			obj = new CustomerSerializable("salry", 20);
		} else if(str.equals("customerVar")){
			obj = new CustomerVar("tom","1234");
		} else if(str.equals("customerVarAss_seri")){
			AssociationClassSeri ASS = new AssociationClassSeri("jack");
			Association ass1 = new Association("num1",ASS);
			Association ass2 = new Association("num2",ASS);
			ASS.addAssociation(ass1);
			ASS.addAssociation(ass2);
			obj = ASS;
		}else{
			obj = "HELLO";
		}
		send(obj);
	}
	
}
