package Day09_Serializable;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Date;

/*
 * java在 远程通信/保存到本地文件 中，传递java对象
 * 	java    >>   字节序列 [序列化] >> .obj文件
 * 	.obj文件   >>   字节序列 		   >> java  [反序列化]
 * 
 * 
 * Java的对象序列化是指将那些实现了Serializable接口的对象转换成一个字符序列，
 * 并能够在以后将这个字节序列完全恢复为原来的对象。
 * 这一过程甚至可通过网络进行，这意味着序列化机制能自动弥补不同操作系统之间的差异。 
 * 只要对象实现了Serializable接口(这个接口只是一个标记接口，不包含任何的方法)
 * 
 * 如果我们想要序列化一个对象，首先要创建某些OutputStream(如FileOutputStream、ByteArrayOutputStream等)，
 * 然后将这些OutputStream封装在一个ObjectOutputStream中。
 * 这时候，只需要调用writeObject()方法就可以将对象序列化，
 * 并将其发送给OutputStream（对象的序列化是基于字节的，不能使用Reader和Writer等基于字符的层次结构）。
 * 而反序列的过程（即将一个序列还原成为一个对象），需要将一个InputStream(如FileInputstream、ByteArrayInputStream等)
 * 封装在ObjectInputStream内，然后调用readObject()即可。
 * 
 * implements Serializable(可以采用默认序列化行为) 
 * 				or 
 * 			  Externalizable(完全由自身控制序列化行为)
 * 				[public interface Externalizable extends java.io.Serializable]
 * 
 * Serializable(可以采用默认序列化行为) 
 * 	1. 仅实现 Serializable 
 * 			ObjectOutputStream/ObjectIntputStream 采用默认序列化/反，对 非transient/非静态 实例变量进行序列化。
 * 	2. 实现 Serializable,定义了 readObject(ObjectInputStream in)/writeObject(ObjectOutputStream out)  
 * 			ObjectOutputStream/ObjectIntputStream 调用事先类中已定义的 readObject(in)/writeObject(out) 序列化/反。
 * 
 * 1）反序列化后的对象，需要调用构造函数重新构造吗？
 * 		           不需要。对于Serializable对象，对象完全以它存储的二进制位作为基础来构造，而不调用构造器。
 * 2）序列前的对象与序列化后的对象是什么关系？是("=="还是equal？是浅复制还是深复制？)
 *        	深复制，反序列化还原后的对象地址与原来的的地址不同。
 *        	序列化前后对象的地址不同了，但是内容是一样的，而且对象中包含的引用也相同。
 *        	换句话说，通过序列化操作，我们可以实现对任何可Serializable对象的 深度复制（deep copy）
 *        	这意味着我们复制的是整个对象网，而不仅仅是基本对象及其引用。
 *        	对于同一流的对象，他们的地址是相同，说明他们是同一个对象，但是与其他流的对象地址却不相同。
 *        	也就说，只要将对象序列化到单一流中，就可以恢复出与我们写出时一样的对象网，而且只要在同一流中，对象都是同一个。
 * 
 * Externalizable(完全由自身控制序列化行为)
 * 	1. 实现 Externalizable,必须实现 readExternal(ObjectInput in)/writeExternal(ObjectOutput out)
 * 		ObjectOutputStream 调用事先类中已定义的 writeExternal(ObjectOutput out) 序列化
 * 		ObjectIntputStream 先通过实现类 不带参数的构造方法 创建 当前类对象 ，然后调用类中已定义的readExternal(ObjectInput in) 反序列化。
 * 
 * 与Serizable对象不同，使用Externalizabled，就意味着没有任何东西可以自动序列化， 
 * 为了正常的运行，我们需要在writeExtenal()方法中将自对象的重要信息写入，从而手动的完成序列化。
 * 对于一个Externalizabled对象，对象的默认构造函数都会被调用（包括哪些在定义时已经初始化的字段），
 * 然后调用readExternal()，在此方法中必须手动的恢复数据。
 * 			
 * 对象序列化主要步骤：
 * 	(1)创建对象输出流，new ObjectOutputStream(new FileOutputStream("..."));
 * 	(2)通过输出流的 writeObject()方法写对象，out.writeObject(new XX());
 * 对象反序列化主要步骤：
 * 	(1)创建对象输入流，new ObjectIntputStream(new FileInputStream("..."));
 * 	(2)通过输入流的 readObject()方法读对象，(XX)in.readObject();
 * */
public class ObjectSaver {


	public static void main(String[] args) throws Exception{ 
		//序列化
		ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("G:\\test\\a.obj"));
		
		String str = "Serializable_testStr";
		Date date = new Date();
		CustomerSerializable customer = new CustomerSerializable("jack", 20);
		
		out.writeObject(str);
		out.writeObject(date);
		out.writeObject(customer);
		out.writeInt(123);
		
		out.close();
		
		//反序列化
		ObjectInputStream in = new ObjectInputStream(new FileInputStream("G:\\test\\a.obj"));

		Object obj = in.readObject();
		System.out.println( "obj instanceof Date : "+(obj instanceof String)+"\r\n"+
				"obj.toString(): "+ obj.toString() +"\r\n"+
				"str.toString(): "+ str.toString() +"\r\n"+
				"obj.hashcode(): "+ obj.hashCode() +"\r\n"+
				"str.hashcode(): "+ str.hashCode() +"\r\n"+
				"obj == str : "+(obj == str) +"\r\n"
		);//Serializable_testStr
		
		Object obj2 = in.readObject();
		System.out.println( "obj2 instanceof Date : "+(obj2 instanceof Date)+"\r\n"+
				"obj2.toString(): "+ obj2.toString() +"\r\n"+
				"date.toString(): "+ date.toString() +"\r\n"+
				"obj2.hashcode(): "+ obj2.hashCode() +"\r\n"+
				"date.hashcode(): "+ date.hashCode() +"\r\n"+
				"obj2 == date : "+(obj2 == date) +"\r\n"
		);//Sat Sep 07 13:39:24 CST 2019
		
		Object obj3 = in.readObject();
		System.out.println( "obj3 instanceof CustomerSerializable : "+(obj3 instanceof CustomerSerializable)+"\r\n"+
				"obj3.toString(): "+ obj3.toString() +"\r\n"+
				"customer.toString(): "+ customer.toString() +"\r\n"+
				"obj3.hashcode(): "+ obj3.hashCode() +"\r\n"+
				"customer.hashcode(): "+ customer.hashCode() +"\r\n"+
				"obj3.getName() and getAge(): "+((CustomerSerializable)obj3).getName()+"\r\n"+
				"customer.getName() and getAge(): "+ customer.getName()+" and "+customer.getAge()+"\r\n"+
				"obj3 == customer : "+(obj3 == customer) +"\r\n"
		);
		
		//Object obj4 = In.readObject();
		//System.out.println(obj4.toString());// java.io.OptionalDataException
		
		Object obj4 = in.readInt();
		System.out.println(obj4);
		
		in.close();
	}
	
	/*
	 *  obj instanceof Date : true
		obj.toString(): Serializable_testStr
		str.toString(): Serializable_testStr
		obj.hashcode(): 993043743
		str.hashcode(): 993043743
		obj == str : false
		
		obj2 instanceof Date : true
		obj2.toString(): Thu Oct 24 15:11:44 CST 2019
		date.toString(): Thu Oct 24 15:11:44 CST 2019
		obj2.hashcode(): -56926052
		date.hashcode(): -56926052
		obj2 == date : false
		
		obj3 instanceof CustomerSerializable : true
		obj3.toString(): Day09_Serializable.CustomerSerializable@3b9a45b3
		customer.toString(): Day09_Serializable.CustomerSerializable@75b84c92
		obj3.hashcode(): 999966131
		customer.hashcode(): 1975012498
		obj3.getName() and getAge(): class Day09_Serializable.CustomerSerializable
		customer.getName() and getAge(): jack and 20
		obj3 == customer : false
		
		123
	 * */

//	@Override
//	public void writeExternal(ObjectOutput out) throws IOException {
//		// TODO Auto-generated method stub
//		
//	}
//
//	@Override
//	public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
//		// TODO Auto-generated method stub
//		
//	}
}

class CustomerSerializable implements Serializable{
	private String name =null;
	private int age =0;
	
	public CustomerSerializable(String name,int age){
		this.name=name;
		this.age=age;
	}

	public String getName() {
		return name;
	}

	public int getAge() {
		return age;
	}
	
}
