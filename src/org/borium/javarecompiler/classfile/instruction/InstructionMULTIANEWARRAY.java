package org.borium.javarecompiler.classfile.instruction;

import org.borium.javarecompiler.classfile.*;

/**
 * Create new multidimensional array.
 */
public class InstructionMULTIANEWARRAY extends Instruction
{
	/**
	 * The unsigned indexbyte1 and indexbyte2 are used to construct an index into
	 * the run-time constant pool of the current class (2.6), where the value of the
	 * index is (indexbyte1 << 8) | indexbyte2. The runtime constant pool entry at
	 * the index must be a symbolic reference to a class, array, or interface type.
	 */
	private int index;

	/**
	 * The dimensions operand is an unsigned byte that must be greater than or equal
	 * to 1. It represents the number of dimensions of the array to be created.
	 */
	@SuppressWarnings("unused")
	private int dimensions;

	public InstructionMULTIANEWARRAY(ByteInputStream in)
	{
		index = in.u2();
		dimensions = in.u1();
	}

	@Override
	public void detailedDump(IndentedOutputStream stream)
	{
		String className = getClass().getSimpleName().substring(11).toLowerCase();
//		Constant classRef = cp.get(index);
		stream.iprintln(className + " " + index);
		throw new RuntimeException(className + ": Dump not implemented");
	}

	@Override
	public int getStackDepthChange()
	{
		String name = getClass().getSimpleName().substring(11);
		throw new RuntimeException(name + " stack depth change not implemented");
	}

	@Override
	public int length()
	{
		return 4;
	}
}
