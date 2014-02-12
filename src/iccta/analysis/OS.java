package iccta.analysis;

import java.io.File;

public class OS {

	public static void delete(String path) 
	{
		File file = new File(path);
		file.delete();
	}

}
