package org.borium.javarecompiler.classfile.instruction;

/**
 * Push double.
 */
public class InstructionDCONST extends Instruction
{
	private double value;

	public InstructionDCONST(double value)
	{
		this.value = value;
	}

	@Override
	public int getStackDepthChange()
	{
		return 1;
	}

	public String getValue()
	{
		return Double.toString(value);
	}
}
