package Day09_Serializable;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
/*
 * 在默认方式下,对象输出流会对整个对象图进行序列化。 当程序执行 writeObject(AssociationClassSeri)方法时， 该方法不仅序列化 AssociationClassSeri对象， 
 * 还会把两个与它关联的 Association对象也进行序列化。 当通过 ObjectlnputStream对象的 readObject()方法反序列化 Customer2 对象时，
 * 实际上会对整个对象图反序列化。
 * 
 * 按照默认方式序列化对象 A时， 到底会序列化由哪些对象构成的对象图呢？ 或者说，对象A持行对象B的引用，在内存屮可以从对象A导航到对象B。 
 * 序列化对象A时， 实际上会序列化对象A， 以及所有可以从对象A直接或间接导航到的对象。
 * 因此序列化对象A时， 实际上被序列化的对象包括： 对象A、 对象B、 对象C、 对象D、 对象 E、 对象F和对象G
 * 关系为 由A>>B>>...
 * 
 * 
 * */
public class AssociationClassSeri implements Serializable{
	private String name;
	private Set<Association> ASS =new HashSet<Association>();//关联对象Association
	/*
	 * AssociationClassSeri...static block
		AssociationClassSeri [name=jack, ass=[Day09_Serializable.Association@4891d863, Day09_Serializable.Association@5f1570cd]]
		AssociationClassSeri [name=jack, ass=[Day09_Serializable.Association@4891d863, Day09_Serializable.Association@5f1570cd]]
		true
	 * */
	
	static{
		System.out.println("AssociationClassSeri...static block");
	}
	
	public AssociationClassSeri(){
		System.out.println("AssociationClassSeri...AssociationClassSeri()");
	}
	public AssociationClassSeri(String name){
		System.out.println("AssociationClassSeri...AssociationClassSeri(String name)");
		this.name =name;
	}
	
	public void addAssociation(Association ass){
		 ASS.add(ass);
	}
	@Override
	public String toString() {
		return "AssociationClassSeri [name=" + name + ", ass=" + ASS + "]";
	}
	
}

class Association implements Serializable{
	private String number;
	private AssociationClassSeri cus;
	
	public Association(){
		System.out.println("Association...Association()");
	}
	
	public Association(String number,AssociationClassSeri cus){
		System.out.println("Association...Association(String number,CustomerVar cus)");
		this.number=number;
		this.cus=cus;
	}

//	@Override
//	public String toString() {
//		return "Association [number=" + number + ", cus=" + cus + "]";
//	}
	
}