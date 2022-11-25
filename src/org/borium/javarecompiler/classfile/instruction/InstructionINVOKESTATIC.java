package org.borium.javarecompiler.classfile.instruction;

import org.borium.javarecompiler.classfile.*;
import org.borium.javarecompiler.classfile.constants.*;

public class InstructionINVOKESTATIC extends Instruction
{
	/**
	 * The unsigned indexbyte1 and indexbyte2 are used to construct an index into
	 * the run-time constant pool of the current class (2.6), where the value of the
	 * index is (indexbyte1 << 8) | indexbyte2. The run-time constant pool entry at
	 * the index must be a symbolic reference to a method or an interface method
	 * (5.1), which gives the name and descriptor (4.3.3) of the method or interface
	 * method as well as a symbolic reference to the class or interface in which the
	 * method or interface method is to be found.
	 */
	private int index;
	private ConstantMethodrefInfo methodref;

	private ConstantClassInfo classInfo;

	private ConstantNameAndTypeInfo nameType;

	private String methodClassName;

	private String methodName;

	public InstructionINVOKESTATIC(ByteInputStream in, ConstantPool cp)
	{
		index = in.u2();
		methodref = (ConstantMethodrefInfo) cp.get(index);
		classInfo = (ConstantClassInfo) cp.get(methodref.classIndex);
		nameType = (ConstantNameAndTypeInfo) cp.get(methodref.nameAndTypeIndex);
		methodClassName = cp.getString(classInfo.nameIndex).replace('/', '.');
		methodName = nameType.getName();
	}

	@Override
	public void detailedDump(IndentedOutputStream stream)
	{
		String className = getClass().getSimpleName().substring(11).toLowerCase();
		stream.iprintln(className + " " + methodClassName + "." + methodName);
	}

	public String getMethodClassName()
	{
		return methodClassName;
	}

	public String getmethodDescriptor()
	{
		return nameType.getDescriptor();
	}

	public String getMethodName()
	{
		return methodName;
	}

	@Override
	public int getStackDepthChange()
	{
		int stackDepthChange = 0;
		// stackDepthChange--; // INVOKESTATIC does not have 'this' pointer
		stackDepthChange -= nameType.getParameterCount(); // parameters, if any, are used and removed
		stackDepthChange += nameType.getReturnTypeCount(); // void (0) or anything else (1)
		return stackDepthChange;
	}

	@Override
	public int length()
	{
		return 3;
	}
}
