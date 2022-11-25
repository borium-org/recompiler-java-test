package org.borium.javarecompiler.classfile.instruction;

import org.borium.javarecompiler.classfile.*;

/**
 * Push short.
 */
public class InstructionSIPUSH extends Instruction
{
	/**
	 * The immediate unsigned byte1 and byte2 values are assembled into an
	 * intermediate short, where the value of the short is (byte1 << 8) | byte2. The
	 * intermediate value is then sign-extended to an int value. That value is
	 * pushed onto the operand stack.
	 */
	private int value;

	public InstructionSIPUSH(ByteInputStream in)
	{
		value = in.s2();
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

	public int getValue()
	{
		return value;
	}

	@Override
	public int length()
	{
		return 3;
	}
}
