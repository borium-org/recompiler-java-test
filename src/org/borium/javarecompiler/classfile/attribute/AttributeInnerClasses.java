package org.borium.javarecompiler.classfile.attribute;

import org.borium.javarecompiler.classfile.*;
import org.borium.javarecompiler.classfile.constants.*;

public class AttributeInnerClasses extends ClassAttribute
{
	private InnerClass[] classTable;

	public AttributeInnerClasses(ClassAttribute attribute, ConstantPool cp)
	{
		super(attribute);
		decode(cp);
	}

	@Override
	protected void detailedDump(IndentedOutputStream stream)
	{
		stream.iprintln("Inner classes: " + classTable.length);
		stream.indent(1);
		for (int i = 0; i < classTable.length; i++)
		{
			stream.iprintln(i + ":");
			classTable[i].detailedDump(stream);
		}
		stream.indent(-1);
	}

	private void decode(ConstantPool cp)
	{
		ByteInputStream in = new ByteInputStream(info);
		int numberOfClasses = in.u2();
		classTable = new InnerClass[numberOfClasses];
		for (int i = 0; i < numberOfClasses; i++)
		{
			classTable[i] = new InnerClass(in, cp);
		}
		in.close();
	}
}
