package org.borium.javarecompiler.classfile.attribute;

import org.borium.javarecompiler.classfile.*;

class StackMapFrame
{
	/** Stack map entry tag (0...255). */
	private int frameType;
	@SuppressWarnings("unused")
	private int offsetDelta;
	private VerificationTypeInfo[] stack;
	private VerificationTypeInfo[] locals;

	public StackMapFrame(ByteInputStream in)
	{
		frameType = in.u1();
		if (frameType >= 0 && frameType <= 63) // SAME (0...63)
		{
			offsetDelta = frameType;
		}
		else if (frameType >= 64 && frameType <= 127)// SAME_LOCALS_1_STACK_ITEM (64...127)
		{
			offsetDelta = frameType - 64;
			stack = new VerificationTypeInfo[1];
			stack[0] = new VerificationTypeInfo(in);
		}
		else if (frameType == 247) // SAME_LOCALS_1_STACK_ITEM_EXTENDED (247)
		{
			offsetDelta = in.u2();
			stack = new VerificationTypeInfo[1];
			stack[0] = new VerificationTypeInfo(in);
		}
		else if (frameType >= 248 && frameType <= 250) // CHOP (248...250)
		{
			offsetDelta = in.u2();
		}
		else if (frameType == 251) // = SAME_FRAME_EXTENDED (251)
		{
			offsetDelta = in.u2();
		}
		else if (frameType >= 252 && frameType <= 254) // APPEND (252...254)
		{
			offsetDelta = in.u2();
			locals = new VerificationTypeInfo[frameType - 251];
			for (int i = 0; i < locals.length; i++)
			{
				locals[i] = new VerificationTypeInfo(in);
			}
		}
		else if (frameType == 255) // FULL_FRAME (255)
		{
			offsetDelta = in.u2();
			int numberOfLocals = in.u2();
			locals = new VerificationTypeInfo[numberOfLocals];
			for (int i = 0; i < numberOfLocals; i++)
			{
				locals[i] = new VerificationTypeInfo(in);
			}
			int numberOfStackItems = in.u2();
			stack = new VerificationTypeInfo[numberOfStackItems];
			for (int i = 0; i < numberOfStackItems; i++)
			{
				stack[i] = new VerificationTypeInfo(in);
			}
		}
		else
		{
			throw new ClassFormatError("Unsupported frame type " + frameType);
		}
	}
}
