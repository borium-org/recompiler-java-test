package org.borium.javarecompiler.classfile.instruction;

import static org.borium.javarecompiler.Statics.*;

import org.borium.javarecompiler.classfile.*;
import org.borium.javarecompiler.classfile.constants.*;

/**
 * Push item from run-time constant pool.
 */
public class InstructionLDC extends Instruction
{
	private int index;

	private Constant c;

	private String value;

	public InstructionLDC(ByteInputStream in, ConstantPool cp)
	{
		index = in.u1();
		c = cp.get(index);
		if (c instanceof ConstantStringInfo)
		{
			ConstantStringInfo stringValue = (ConstantStringInfo) c;
			value = stringValue.getValue(cp);
		}
		else if (c instanceof ConstantClassInfo)
		{
			ConstantClassInfo classValue = (ConstantClassInfo) c;
			value = cp.getString(classValue.nameIndex);
		}
	}

	@Override
	public void detailedDump(IndentedOutputStream stream)
	{
		String className = getClass().getSimpleName().substring(11).toLowerCase();
		if (c instanceof ConstantStringInfo)
		{
			ConstantStringInfo stringValue = (ConstantStringInfo) c;
			stream.iprintln(className + " \"" + escapeCharacters(value) + "\"");
		}
		else if (c instanceof ConstantInteger)
		{
			ConstantInteger intValue = (ConstantInteger) c;
			stream.iprintln(className + " " + intValue.getValue());
		}
//		else if (c instanceof ConstantClassInfo)
//		{
//			ConstantClassInfo classValue = (ConstantClassInfo) c;
//			stream.iprintln("???" + className + " " + value);
//		}
		else
		{
			stream.iprintln(className + " " + index);
			throw new RuntimeException(className + ": Dump not implemented");
		}
	}

	public Constant getConstant()
	{
		return c;
	}

	@Override
	public int getStackDepthChange()
	{
		return 1;
	}

	@Override
	public int length()
	{
		return 2;
	}
}
