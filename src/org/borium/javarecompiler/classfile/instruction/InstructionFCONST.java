package org.borium.javarecompiler.classfile.instruction;

import org.borium.javarecompiler.classfile.*;

/**
 * Push float.
 */
public class InstructionFCONST extends Instruction
{
	private double value;

	public InstructionFCONST(double value)
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
}
