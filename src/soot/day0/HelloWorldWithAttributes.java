package soot.day0;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.lang.reflect.Modifier;
import java.util.Arrays;

import soot.ArrayType;
import soot.Local;
import soot.PatchingChain;
import soot.RefType;
import soot.Scene;
import soot.SootClass;
import soot.SootMethod;
import soot.Type;
import soot.Unit;
import soot.VoidType;
import soot.jimple.IdentityStmt;
import soot.jimple.InvokeStmt;
import soot.jimple.JasminClass;
import soot.jimple.Jimple;
import soot.jimple.JimpleBody;
import soot.jimple.ParameterRef;
import soot.jimple.StringConstant;
import soot.tagkit.GenericAttribute;
import soot.util.JasminOutputStream;

public class HelloWorldWithAttributes {

	
	public static void main(String[] args) throws IOException {
		Scene.v().loadClassAndSupport("java.lang.Object");
		Scene.v().loadClassAndSupport("java.lang.System");

		Scene.v().loadNecessaryClasses();
		
		SootClass sClass = new SootClass("HelloWorld", Modifier.PUBLIC);
		sClass.setSuperclass(Scene.v().getSootClass("java.lang.Object"));
		Scene.v().addClass(sClass);
		
		
		SootMethod sMethod = new SootMethod("main", 
				Arrays.asList(new Type[] {ArrayType.v(RefType.v("java.lang.String"), 1)}), 
				VoidType.v(), Modifier.PUBLIC | Modifier.STATIC);
		sClass.addMethod(sMethod);
		
		JimpleBody body = Jimple.v().newBody(sMethod);
		sMethod.setActiveBody(body);
		PatchingChain<Unit> units = body.getUnits();
		
		Local arg = Jimple.v().newLocal("l0", ArrayType.v(RefType.v("java.lang.String"), 1));
		body.getLocals().add(arg);
		
		Local tmpRef = Jimple.v().newLocal("tmpRef", RefType.v("java.io.PrintStream"));
        body.getLocals().add(tmpRef);
		
		ParameterRef paraRef = Jimple.v().newParameterRef(ArrayType.v(RefType.v("java.lang.String"), 1), 0);
		IdentityStmt idStmt = Jimple.v().newIdentityStmt(arg, paraRef);
		
		//tags
		idStmt.addTag(new MyTag(1));
		units.add(idStmt);
		
		units.add(Jimple.v().newAssignStmt(tmpRef, Jimple.v().newStaticFieldRef(
				Scene.v().getField("<java.lang.System: java.io.PrintStream out>").makeRef())));
		
		SootMethod toCall = Scene.v().getMethod("<java.io.PrintStream: void println(java.lang.String)>");
		
		InvokeStmt ivStmt = Jimple.v().newInvokeStmt(Jimple.v().newVirtualInvokeExpr(tmpRef, toCall.makeRef(), StringConstant.v("Hello world!")));
		ivStmt.addTag(new MyTag(2));
		units.add(ivStmt);

		units.add(Jimple.v().newReturnVoidStmt());
		
		
		
		
		GenericAttribute classAttr = new GenericAttribute("lu.uni.servel", "foo".getBytes());
		sClass.addTag(classAttr);
		
		GenericAttribute mAttr = new GenericAttribute("lu.uni.servel.method", "mmm".getBytes());
		sMethod.addTag(mAttr);
		
		
		String fileName = "HelloWorld.class"; //SourceLocator.v().getFileNameFor(sClass, Options.output_format_class);
		OutputStream streamOut = new JasminOutputStream(new FileOutputStream(fileName));
		PrintWriter writerOut = new PrintWriter(new OutputStreamWriter(streamOut));
		
		JasminClass jasminClass = new JasminClass(sClass);
		jasminClass.print(writerOut);
		writerOut.flush();
		streamOut.close();
	}

}
