
public class Love {

	public Love filter(Love love)
	{
		return new Love();
	}
	
	public void love(Love one, Love two)
	{
		one = two;
		Object obj = one;
		
		
		//from here to find the whole chain
		startActivity(obj);
		startActivity(obj);
	}
	
	public void startActivity(Object obj)
	{
		System.out.println("obj...");
	}
	
	public static void main(String[] args) 
	{
		Love l = new Love();
		
		Love obj1 = new Love();
		Love obj2 = new Love();
		
		l.love(obj1, obj2);
	}

	
}
