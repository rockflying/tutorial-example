package soot.def;
import soot.options.Options;


public class HerosTest {

	public static void main(String[] args) 
	{
		String[] args2 = 
		{
			"-process-dir", "res",
			"-ire", 
			"-pp", 
			"-allow-phantom-refs",
			"-W"
		};
			
		Options.v().set_src_prec(Options.src_prec_class);
		Options.v().set_output_format(Options.output_format_jimple);
		
		soot.jimple.toolkits.ide.Main.main(args2);
	}

}
