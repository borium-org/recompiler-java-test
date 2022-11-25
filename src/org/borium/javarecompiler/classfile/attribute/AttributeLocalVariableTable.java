package org.borium.javarecompiler.classfile.attribute;

import org.borium.javarecompiler.classfile.*;
import org.borium.javarecompiler.classfile.constants.*;

public class AttributeLocalVariableTable extends ClassAttribute
{
	/** All the variables in this table. */
	private VariableTableEntry[] variableTable;

	public AttributeLocalVariableTable(ClassAttribute attribute, ConstantPool cp)
	{
		super(attribute);
		decode(cp);
	}

	public VariableTableEntry[] getVariableTable()
	{
		return variableTable;
	}

	@Override
	protected void detailedDump(IndentedOutputStream stream)
	{
		stream.iprintln("Local Variables: " + variableTable.length);
		stream.indent(1);
		for (int i = 0; i < variableTable.length; i++)
		{
			stream.iprint(i + ": ");
			variableTable[i].dump(stream);
			stream.println();
		}
		stream.indent(-1);
	}

	private void decode(ConstantPool cp)
	{
		ByteInputStream in = new ByteInputStream(info);
		int variableTableLength = in.u2();
		variableTable = new VariableTableEntry[variableTableLength];
		for (int i = 0; i < variableTableLength; i++)
		{
			variableTable[i] = new VariableTableEntry(in, cp);
		}
		in.close();
	}
}
