package org.borium.javarecompiler.classfile.attribute;

import org.borium.javarecompiler.classfile.*;
import org.borium.javarecompiler.classfile.constants.*;

public class AttributeLocalVariableTypeTable extends ClassAttribute
{
	private LocalVariableType[] localVariableTypes;

	public AttributeLocalVariableTypeTable(ClassAttribute attribute, ConstantPool cp)
	{
		super(attribute);
		decode(cp);
	}

	public LocalVariableType[] getLocalVariableTypes()
	{
		return localVariableTypes;
	}

	@Override
	protected void detailedDump(IndentedOutputStream stream)
	{
		stream.iprintln("Local Variable Types: " + localVariableTypes.length);
		stream.indent(1);
		for (int i = 0; i < localVariableTypes.length; i++)
		{
			stream.iprint(i + ": ");
			localVariableTypes[i].dump(stream);
			stream.println();
		}
		stream.indent(-1);
	}

	private void decode(ConstantPool cp)
	{
		ByteInputStream in = new ByteInputStream(info);
		int numberOfEntries = in.u2();
		localVariableTypes = new LocalVariableType[numberOfEntries];
		for (int i = 0; i < numberOfEntries; i++)
		{
			localVariableTypes[i] = new LocalVariableType(in, cp);
		}
		in.close();
	}
}
