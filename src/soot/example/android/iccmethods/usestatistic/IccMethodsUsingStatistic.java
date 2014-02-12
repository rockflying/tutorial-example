package soot.example.android.iccmethods.usestatistic;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import soot.Body;
import soot.BodyTransformer;
import soot.G;
import soot.PackManager;
import soot.Transform;
import soot.Unit;
import soot.jimple.Stmt;
import soot.options.Options;

public class IccMethodsUsingStatistic extends BodyTransformer {

	private static String app;
	private static Map<String, Integer> signature2count = new HashMap<String, Integer>();
	
	private static BufferedWriter bw = null; 
	
	public static void main(String[] args) throws IOException 
	{
		IccMethodsUsingStatistic icc = new IccMethodsUsingStatistic();
		
		bw = new BufferedWriter(new FileWriter("src/iccUseResult.txt"));	
		
		icc.loadIccMethods("src/IPCMethods.txt");
		
		File file = new File(args[0]);
		
		if (file.isFile())
		{
			app = args[0];
			icc.parseOneApk(app);
		}
		else
		{
			File[] files = file.listFiles();
			for (File f : files)
			{
				app = f.getAbsolutePath();
				icc.parseOneApk(app);
			}
		}
		
		bw.write("\n");
		bw.write("RESULT" + "\n");
		bw.write("__________________________________________________________________" + "\n");
		
		for (Map.Entry<String, Integer> entry : signature2count.entrySet())
		{
			String key = entry.getKey();
			int value = entry.getValue();
			
			bw.write(key + ": " + value + "\n");
		}
		
		
		bw.close();
	}

	public void parseOneApk(String apkPath)
	{
		G.reset();
		String[] args2 = 
		{
			"-android-jars", "/Users/li.li/Project/github/android-platforms",
			"-process-dir", apkPath,
			"-ire", 
			"-pp", 
			"-allow-phantom-refs"
		};

		Options.v().set_src_prec(Options.src_prec_apk);
		Options.v().set_output_format(Options.output_format_none);
		
        PackManager.v().getPack("jtp").add(new Transform("jtp.StatisticBodyTransformer", this));
        
        try
        {
        	soot.Main.main(args2);
        }
        catch (Exception ex)
        {
        	try {
				bw.write("Exception:" + apkPath);
				bw.flush();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
	}
	
	public void loadIccMethods(String path)
	{
		try 
		{
			BufferedReader br = new BufferedReader(new FileReader(path));
			
			String line = "";
			while ((line = br.readLine()) != null) 
			{
			    if (line.isEmpty() || line.startsWith("#"))
			    {
			    	continue;
			    }
			    
			    int pos = line.indexOf('>');
			    String signature = line.substring(0, pos+1);
			    
			    signature2count.put(signature, 0);
			    
			    System.out.println(signature);
			}
			
			br.close();
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}
	
	@Override
	protected void internalTransform(Body b, String phaseName, Map<String, String> options) 
	{
		String pkgName = b.getMethod().getDeclaringClass().getPackageName();
		if (pkgName.startsWith("android.support"))
		{
			return;
		}
		
		for (Iterator<Unit> iter = b.getUnits().snapshotIterator(); iter.hasNext(); )
		{
			Stmt stmt = (Stmt) iter.next();
			
			if (! stmt.containsInvokeExpr())
			{
				continue;
			}
			
			String signature = stmt.getInvokeExpr().getMethod().getSignature();

			if (signature2count.containsKey(signature))
			{
				int value = signature2count.get(signature);
				signature2count.put(signature, value+1);
				System.out.println("-------->" + app + ":" + signature + ":" + (value+1));
				try {
					bw.write(app + ":" + signature + ":" + (value+1) + "\n");
					bw.flush();
				} catch (IOException e) {
					
					e.printStackTrace();
				}
			}
		}
		
	}
}

