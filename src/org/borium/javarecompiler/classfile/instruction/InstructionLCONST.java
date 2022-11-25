package org.borium.javarecompiler.classfile.instruction;

import org.borium.javarecompiler.classfile.*;

/**
 * Push long constant.
 */
public class InstructionLCONST extends Instruction
{
	private long value;

	public InstructionLCONST(long value)
	{
		this.value = value;
	}

	@Override
	public void detailedDump(IndentedOutputStream stream)
	{
		String className = getClass().getSimpleName().substring(11).toLowerCase();
		stream.iprintln(className + " " + value);
	}

	@Override
	public int getStackDepthChange()
	{
		return 1;
	}

	public String getValue()
	{
		return value + "LL";
	}
}
