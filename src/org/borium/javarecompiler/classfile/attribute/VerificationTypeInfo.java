package org.borium.javarecompiler.classfile.attribute;

import org.borium.javarecompiler.classfile.*;

class VerificationTypeInfo
{
	private static final int ITEM_Top = 0;
	private static final int ITEM_Integer = 1;
	private static final int ITEM_Float = 2;
	private static final int ITEM_Double = 3;
	private static final int ITEM_Long = 4;
	private static final int ITEM_Null = 5;
	private static final int ITEM_UninitializedThis = 6;
	private static final int ITEM_Object = 7;
	private static final int ITEM_Uninitialized = 8;

	/** Verification type tag (0...8). */
	private int tag;

	/** Constant pool index for ObjectVariableInfo entry. */
	@SuppressWarnings("unused")
	private int cpoolIndex;

	/**
	 * Code offset for UninitializedVariableInfo where variable is created witgh new
	 * operator.
	 */
	@SuppressWarnings("unused")
	private int offset;

	public VerificationTypeInfo(ByteInputStream in)
	{
		tag = in.u1();
		switch (tag)
		{
		case ITEM_Top:
			break;
		case ITEM_Integer:
			break;
		case ITEM_Float:
			break;
		case ITEM_Double:
			throw new ClassFormatError("Unhandled double");
		case ITEM_Long:
			throw new ClassFormatError("Unhandled long");
		case ITEM_Null:
			break;
		case ITEM_UninitializedThis:
			break;
		case ITEM_Object:
			cpoolIndex = in.u2();
			break;
		case ITEM_Uninitialized:
			offset = in.u2();
			break;
		default:
			throw new ClassFormatError("Unrecognized stack map entry tag " + tag);
		}
	}
}
