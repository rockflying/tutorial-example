package soot.def;
import java.util.Iterator;
import java.util.Map;

import soot.Body;
import soot.BodyTransformer;
import soot.PackManager;
import soot.PatchingChain;
import soot.Transform;
import soot.Unit;
import soot.options.Options;


public class GetInitializingInfo extends BodyTransformer
{
	public static void main(String[] args) 
	{
		Options.v().set_src_prec(Options.src_prec_apk);

		Options.v().set_output_format(Options.output_format_dex);
		
        PackManager.v().getPack("jtp").add(new Transform("jtp.myInstrumenter", new GetInitializingInfo()));

        soot.Main.main(args);
	}

	@Override
	protected void internalTransform(Body b, String phaseName, Map<String, String> options) 
	{
		PatchingChain<Unit> units = b.getUnits();
		
		/*if (b.getMethod().toString().contains("<com.example.epicctest.MainActivity: void onCreate(android.os.Bundle)>"))
		{
			System.out.println(b);
		}*/
		
		//important to use snapshotIterator here
		for(Iterator<Unit> iter = units.snapshotIterator(); iter.hasNext();) 
		{
			Unit u = iter.next();
			
			if (u.toString().contains("ToOtherActivityListener"))
			{
				System.out.println(b);
			}
			/*if (u.toString().contains("Activity"))
			{
				System.out.println(u);
			}*/
			
		}
		
	}

	
}
