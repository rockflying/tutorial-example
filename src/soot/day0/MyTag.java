package soot.day0;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import soot.tagkit.AttributeValueException;
import soot.tagkit.Tag;


public class MyTag implements Tag 
{
	int value;
	
	public MyTag(int value)
	{
		this.value = value;
	}
	
	@Override
	public String getName() 
	{
		return "lu.uni.servel";
	}

	@Override
	public byte[] getValue() throws AttributeValueException 
	{
		ByteArrayOutputStream baos = new ByteArrayOutputStream(4);
		DataOutputStream dos = new DataOutputStream(baos);
		
		try {
			dos.writeInt(value);
			dos.flush();
		} catch(IOException e) {
			System.err.println(e);
			throw new RuntimeException(e);
		}
		
		return baos.toByteArray();
	}

}
