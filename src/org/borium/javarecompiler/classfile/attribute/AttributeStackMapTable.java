package org.borium.javarecompiler.classfile.attribute;

import org.borium.javarecompiler.classfile.*;
import org.borium.javarecompiler.classfile.constants.*;

public class AttributeStackMapTable extends ClassAttribute
{
	private StackMapFrame[] stackMapEntries;

	public AttributeStackMapTable(ClassAttribute attribute, ConstantPool cp)
	{
		super(attribute);
		decode(cp);
	}

	private void decode(ConstantPool cp)
	{
		ByteInputStream in = new ByteInputStream(info);
		int numberOfEntries = in.u2();
		stackMapEntries = new StackMapFrame[numberOfEntries];
		for (int i = 0; i < numberOfEntries; i++)
		{
			stackMapEntries[i] = new StackMapFrame(in);
		}
		in.close();
	}
}
