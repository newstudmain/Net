package Day09_Serializable;

import java.io.Serializable;

/*
 * 可序列化类的不同版本的序列化兼容性
 * 	假定 SerialVision 类有两个版本 1.0 和 2.0, 如果要把基于 1.0 的序列化数据反序列
 * 	化为 2.0 的 SerialVision 对象, 或者把基于2.0 的序列化数据反序列化为 1.0 的 SerialVision对象， 
 * 	会出现什么情况呢？ 如果可以成功地反序列化， 则意味着不同版本之间对序列化兼容， 反之， 则意味着不同版本之间对序列化不兼容。
 * 		凡足实现 Serializable 接口的类都有一个表示序列化版本标识符的静态常量：
 * 			private static final long serialVersionUID;
 * 	以上 serialVersionUID 的取值是 Java 运行时环境根据类的内部细节白动生成的。
 * 	如果对类的源代码作了修改， 再重新编译， 新生成的类文件的 serialVersionUID 的取值有可能也会发生变化。
 * 
 * 
 * 
 * */
//vision 1.0
public class SerialVision implements Serializable{

	//private static final long serialVersionUID = 1L;
	
	private String name;
	private int age;
	
	public SerialVision(String name, int age) {
		super();
		this.name = name;
		this.age = age;
	}

	@Override
	public String toString() {
		return "SerialVision [name=" + name + ", age=" + age + "]";
	}
	
}


/*
 * 分别对两个类编译， 把它们的类文件分別放在 server 和 client 目录 下, 此外把
 * SimpleServer 和 SimpleClient 的类文件也分别复制到 server 和 client 目录下。
 * 
 * bin 目录下有一个 serialver.exe 程序，用于査看实现了Serializable 接口的类的 serialVersionUlD。 
 * 		
 * 		static final long serialVersionUlD = -1443651131474384429L;
		static final long serialVersionUlD = -5448724816396875494L;
		
		类的 serialVersionUID 的默认值依赖于 Java 编泽器的实现， 
		对于同—个类， 用不同的 Java 编译器编译， 有可能会导致不N的 serialVersionUID， 也有可能相同。 
		为了提高 serialVersionUID 的独立性和确定性， 强烈建议在一个可序列化类中显式地定义serialVersionUID, 为它赋予明确的值。 
		
			(1) 在某些场合， 希望类的不同版本对序列化兼容， 因此需耍确保类的不同版本具有相同的 serialVersionUID；
			(2) 在某些场合， 不希望类的不同版本对序列化兼容， 因此需要确保类的不同版木具有不同的 serialVersionUID。

 * Customer5 类的两个版本有着不同的 serialVersionUlD。目录下运行命令 “java SimpleServer SerialVision”， 然后在 client 目录下
 * 运行命令 “ java SimpleClient ”。 SimpleServer 按照 SerialVision 类的 1.0 版本对一个
 * SerialVision 对象进行序列化， 而  SimpleClient 按照 SerialVision 类的 2.0 版本进行反序列化， 
 * 由于两个类的版本不一样， SimpleClient 在执行反序列化操作时， 会抛出以下异常。
 * 
 * 用 来 控 制 序 列 化 兼 容 性 的 能 力 是 很 有 限 的。 当 一 个 类 的 不同 版 本 的 serialVersionUID 相 同， 仍 然 有 可 能 出 现 序 列 化 不 兼 容 的 情 况
 * 因 为 序 列 化 兼 容 性 不 仅 取 决 于 还 取 决 于 类 的 不 同 版 本 的 实 现 细 节 和 序 列 化 细 节。
 * 
 * 
 * */


//vision 2.0
/*
public class SerialVision implements Serializable{

	//private static final long serialVersionUID = 1L;
	
	private String name;
	private boolean isHave;//
	
	public SerialVision(String name, boolean isHave) {
		super();
		this.name = name;
		this.isHave = isHave;
	}

	@Override
	public String toString() {
		return "SerialVision [name=" + name + ", isHave=" + isHave + "]";
	}
	
}
*/