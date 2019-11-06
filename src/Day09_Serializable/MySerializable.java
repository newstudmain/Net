package Day09_Serializable;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/*
 * 如果用户希望控制类的序列化方式 ， 可以在可序列化类中提供自定义的
 * writeObject()方法和readObject()方法
 * 
 * 值 得 注 意 的 是 ， 以 上 writeObject()方法和 readObject()方法并不是在 java.io.Serializable 接口中定义的。 
 * 当一个软件系统希望扩展第三方提供的 Java 类库（ JDK 类库） 的功能时， 最常见的方式是实现第三方类库的一些接口， 或创建类库中抽象类的子类。 
 * 但是以上 writeObject()方法和 readObjectO方法并不是在 java.io.Serializable 接口中定义的。
 * JDK 类库的设计人员没有把这两个方法放在 Serializable 接口中， 这样做的优点在于：
 * 
 * (1) 不必公幵这两个方法的访问权限，以便封装序列化的细节。 如果把这两个方法放在 Serializable 接口中， 就必须定义为 public 类型。
 * (2) 不必强迫用户定义的可序列化类实现这两个方法。 如果把这两个方法放在Serializable 接口中， 它的实现类就必须实现这些方法， 否则就只能声明为抽象类。
 * 
 * 以下情况， 可以考虑采用用户自定义的序列化方式， 从而控制序列化的行为：
 * 	(1) 确保序列化的安全性， 对敏感的信息加密后再序列化， 在反序列化时则需要解密;
 * 
 * 	(2) 确保对象的成员变暈符合正确的约柬条件；
 * 			通常， 在 一个类的构造方法屮， 会对用于赋值给成员变暈的参数进行合法性检杳，
 * 			而默认的序列化方式不会调用类的构造方法， 直接由对象的序列化数据来构造出一个
 * 			对象， 这使得不法分子有吋能会提供串廿法的序列化数据， 由它来构造出一个不符合约束条件的对象。
 * 	(3) 优化序列化的性能； 
 * 			默认的序列化方式会序列化整个对象图， 这需要递归遍历对象图。 如果对象图很复杂，
 *  		递归遍历操作需要消耗很多的空间和时间， 甚至会导致虚拟机的堆栈溢出。
 * 	(4) 便于更好地封装类的内部数据结构， 确保类的接口不会被类的内部实现所束缚
 * 
 * */
public class MySerializable implements Serializable{

	//当 ObjectOutputStream 对个 MySerializable 对象进行序列化时， 如果该 对象具有writeObject()方法， 那么就会执行这一方法， 否则就按默认力方式序列化。 
	private void writeObject(ObjectOutputStream out)throws IOException{
		
	}
	//当 ObjectInputStream 对个 MySerializable 对象进行序列化时， 如果该 对象具有readObject()方法， 那么就会执行这一方法， 否则就按默认力方式序列化。
	private void readObject(ObjectInputStream in)throws IOException,ClassNotFoundException{
		
	}
}
