package soot.def;

class C<D extends C.A<D>>
{
	static interface A<D>
	{
		public void print();
	}
}



class B implements C.A<B>
{

	@Override
	public void print() 
	{
		System.out.println(this);
	}
	
}

public class MyTag
{
	int value;
	
	public static void main(String[] args)
	{
		B b = new B();
		b.print();
		
		C<B> c = new C<B>();
		System.out.println(c);
		
		MyTag m = new MyTag(123);
		m.getName();
	}
	
	public MyTag(int value)
	{
		this.value = value;
	}
	
	public String getName() 
	{
		return "lu.uni.servel";
	}
}