package org.borium.javarecompiler.classfile.instruction;

import org.borium.javarecompiler.classfile.*;
import org.borium.javarecompiler.classfile.constants.*;

abstract class InstructionWithTypeIndex extends Instruction
{
	/**
	 * The unsigned indexbyte1 and indexbyte2 are used to construct an index into
	 * the run-time constant pool of the current class (2.6), where the value of the
	 * index is (indexbyte1 << 8) | indexbyte2. The run-time constant pool entry at
	 * the index must be a symbolic reference to a class, array, or interface type.
	 */
	private int index;

	private ConstantClassInfo classInfo;

	protected String className;

	public InstructionWithTypeIndex(ByteInputStream in, ConstantPool cp)
	{
		index = in.u2();
		classInfo = (ConstantClassInfo) cp.get(index);
		className = cp.getString(classInfo.nameIndex).replace('/', '.');
	}

	@Override
	public void detailedDump(IndentedOutputStream stream)
	{
		String instructionClassName = getClass().getSimpleName().substring(11).toLowerCase();
		stream.iprintln(instructionClassName + " " + className);
	}

	public String getClassName()
	{
		return className;
	}

	@Override
	public int length()
	{
		return 3;
	}
}
