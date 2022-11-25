package org.borium.javarecompiler.classfile.instruction;

import static org.borium.javarecompiler.Statics.*;

import org.borium.javarecompiler.classfile.*;
import org.borium.javarecompiler.classfile.constants.*;

/**
 * Check whether object is of given type.
 */
public class InstructionCHECKCAST extends InstructionWithTypeIndex
{
	public InstructionCHECKCAST(ByteInputStream in, ConstantPool cp)
	{
		super(in, cp);
		if (className.startsWith("["))
		{
			convertArray();
		}
	}

	@Override
	public int getStackDepthChange()
	{
		return 0;
	}

	/**
	 * Convert array from '[L...;' format into '...[][][]' format.
	 */
	private void convertArray()
	{
		String className = this.className;
		int indexCount = 0;
		while (className.charAt(0) == '[')
		{
			className = className.substring(1);
			indexCount++;
		}
		Assert(className.charAt(0) == 'L', "CHECKCAST: Array of objects expected");
		className = className.substring(1, className.length() - 1);
		while (indexCount-- > 0)
		{
			className += "[]";
		}
		this.className = className;
	}
}
