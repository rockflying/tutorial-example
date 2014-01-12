import soot.Pack;
import soot.PackManager;
import soot.Transform;

public class MyMain {

	public static void main(String[] args) {
		
		if (args.length == 0)
		{
			System.exit(0);
		}
		
		Pack jtp = PackManager.v().getPack("jtp");
		jtp.add(new Transform("jtp.instrumanter", new InvokeStaticInstrumenter()));
		
		soot.Main.main(args);
	}

}