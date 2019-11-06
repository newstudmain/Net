package Day09_Serializable;

import java.io.Serializable;

/*
 * Serializable采用默认序列化/反，对 非transient/非静态 实例变量进行序列化。 
 * 	按默认方式反序列化时：
 * 		-如果内存中对象所处的类没有被加载 ，那么会先初始化这个类。如果classpath中不存在相应的类文件，抛class not found 异常
 * 		-反序列化时不会调用类的构造方法
 * 	如果一个实例变暈被 transient 修饰符修饰， 那么默认的序列化力方式不会对它序列化。
 * 		-实例变量不代表对象的固有的内部数据， 仅仅代表具有一定逻辑含义的临时数据。 例如， 假定 Customer 类有 firstName、 lastName 和 fUllName(transient) 
 * 		-实例变暈表示一些比较敏感的信息（ 银行账户的口令）， 出于安全方面的原因, 不希望对其序列化。
 * 		-实例变量需要按照用户定义的方式序列化， 如经过加密后再序列化。 在这种情况 ，可以把实例变量定义为 transient 类型， 然后在 writeObject()方法中对其序列化
 * 
 * */
public class CustomerVar implements Serializable{

	private static int count;
	private static final int MAX_COUNT =1000;
	private String name;
	private transient String password;
	
	static {
		System.out.println("class CustomerVar...static block");
	}
	
	public CustomerVar(){
		System.out.println("CustomerVar()...无参构造方法");
		count++;
	}
	public CustomerVar(String name,String password){
		System.out.println("CustomerVar()...带参构造方法");
		this.name =name;
		this.password=password;
		count++;
	}
	@Override
	public String toString() {
		return "CustomerVar [name=" + name 
				+ ", password=" + password + ""
				+ ", count=" + count + ""
				+ ", MAX_COUNT=" + MAX_COUNT + "]";
	}
}
