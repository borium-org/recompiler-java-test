package org.borium.javarecompiler.classfile.attribute;

import org.borium.javarecompiler.classfile.*;
import org.borium.javarecompiler.classfile.constants.*;

public class ExceptionTable
{
	/**
	 * The values of the two items start_pc and end_pc indicate the ranges in the
	 * code array at which the exception handler is active. The value of start_pc
	 * must be a valid index into the code array of the opcode of an instruction.
	 * The value of end_pc either must be a valid index into the code array of the
	 * opcode of an instruction or must be equal to code_length, the length of the
	 * code array. The value of start_pc must be less than the value of end_pc.
	 */
	public int startPc;

	/**
	 * The values of the two items start_pc and end_pc indicate the ranges in the
	 * code array at which the exception handler is active. The value of start_pc
	 * must be a valid index into the code array of the opcode of an instruction.
	 * The value of end_pc either must be a valid index into the code array of the
	 * opcode of an instruction or must be equal to code_length, the length of the
	 * code array. The value of start_pc must be less than the value of end_pc.
	 */
	public int endPc;

	/**
	 * The value of the handler_pc item indicates the start of the exception
	 * handler. The value of the item must be a valid index into the code array and
	 * must be the index of the opcode of an instruction.
	 */
	public int handlerPc;

	/**
	 * If the value of the catch_type item is nonzero, it must be a valid index into
	 * the constant_pool table. The constant_pool entry at that index must be a
	 * CONSTANT_Class_info structure (4.4.1) representing a class of exceptions that
	 * this exception handler is designated to catch. The exception handler will be
	 * called only if the thrown exception is an instance of the given class or one
	 * of its subclasses.
	 */
	int catchType;

	ConstantClassInfo catchClass;

	public ExceptionTable(ByteInputStream in)
	{
		startPc = in.u2();
		endPc = in.u2();
		handlerPc = in.u2();
		catchType = in.u2();
	}

	public String getExceptionClass()
	{
		return catchClass.getName();
	}

	void addLabels(boolean[] labels)
	{
		labels[startPc] = true;
		labels[endPc] = true;
		labels[handlerPc] = true;
	}
}
