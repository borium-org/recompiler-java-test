package org.borium.javarecompiler.classfile.instruction;

import org.borium.javarecompiler.classfile.*;
import org.borium.javarecompiler.classfile.constants.*;

abstract class InstructionWithFieldIndex extends Instruction
{
	/**
	 * The unsigned indexbyte1 and indexbyte2 are used to construct an index into
	 * the run-time constant pool of the current class (2.6), where the value of the
	 * index is (indexbyte1 << 8) | indexbyte2.
	 */
	private int index;

	private ConstantFieldrefInfo fieldref;

	private ConstantNameAndTypeInfo nameType;

	private String fieldName;

	private String fieldType;

	public InstructionWithFieldIndex(ByteInputStream in, ConstantPool cp)
	{
		index = in.u2();
		fieldref = (ConstantFieldrefInfo) cp.get(index);
		nameType = (ConstantNameAndTypeInfo) cp.get(fieldref.nameAndTypeIndex);
		fieldName = nameType.getName();
		fieldType = nameType.getDescriptor();
	}

	@Override
	public void detailedDump(IndentedOutputStream stream)
	{
		String className = getClass().getSimpleName().substring(11).toLowerCase();
		stream.iprintln(className + " " + fieldName + " " + fieldType);
	}

	public String getClassName()
	{
		return "L" + fieldref.getClassName() + ";";
	}

	public String getFieldName()
	{
		return fieldName;
	}

	public String getFieldType()
	{
		return fieldType;
	}

	@Override
	public int length()
	{
		return 3;
	}
}
