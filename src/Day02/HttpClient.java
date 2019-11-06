package Day02;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class HttpClient {
	private String host ="www.javathinker.com";
	private int port =80;
	private Socket socket =null;
	
	public void createSocket(){
		try {
			socket =new Socket(host,port);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void communicate(){
		StringBuffer buffer =new StringBuffer("GET"+" "+"/index.jsp"+" "+"HTTP/1.1"+"\r\n");
		OutputStream socketOut = null;
		InputStream socketIn =null;
		ByteArrayOutputStream baop=null;
		
		buffer.append("Host: javathinker.com"+"\r\n");
		buffer.append("User-Agent: Mozilla/5.0 (Windows NT 10.0; WOW64; rv:56.0) Gecko/20100101 Firefox/56.0"+"\r\n");
		buffer.append("Accept: text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8"+"\r\n");
		buffer.append("Accept-Language: zh-CN,zh;q=0.8,en-US;q=0.5,en;q=0.3"+"\r\n");
		buffer.append("Accept-Encoding: gzip, deflate"+"\r\n");
		buffer.append("Connection: keep-alive"+"\r\n");
		buffer.append("Upgrade-Insecure-Requests: 1"+"\r\n");
		buffer.append("Cache-Control: max-age=0"+"\r\n");
		
		try {
			socketOut =socket.getOutputStream();
			socketOut.write(buffer.toString().getBytes());
			socketOut.flush();
			socket.shutdownOutput();
			
			socketIn =socket.getInputStream();
			baop =new ByteArrayOutputStream(); 
			int len =0;
			byte[] buf =new byte[1024];
			while((len=socketIn.read(buf))!=-1){
				baop.write(buf,0,len);
			}
			baop.flush();
			System.out.println(new String(baop.toByteArray()));
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			try{
				if(socketOut!=null)socketOut.close();
				if(socketIn!=null)socketIn.close();
				if(baop!=null)baop.close();
				if(socket!=null)socket.close();
			}catch(IOException e){
				e.printStackTrace();
			}
		}
	}
	
	public static void main(String[] args){
		HttpClient client =new HttpClient();
		client.createSocket();
		client.communicate();
	}
}
