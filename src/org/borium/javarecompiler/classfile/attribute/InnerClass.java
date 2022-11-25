package org.borium.javarecompiler.classfile.attribute;

import org.borium.javarecompiler.classfile.*;
import org.borium.javarecompiler.classfile.constants.*;

class InnerClass
{
	private int innerClassInfoIndex;

	private int outerClassInfoIndex;

	private int innerClassNameIndex;

	private int innerClassAccessFlags;

	private ConstantClassInfo outer;

	private ConstantClassInfo inner;

	private String outerName;

	private String innerName;

	private String innerClassName;

	public InnerClass(ByteInputStream in, ConstantPool cp)
	{
		innerClassInfoIndex = in.u2();
		outerClassInfoIndex = in.u2();
		innerClassNameIndex = in.u2();
		innerClassAccessFlags = in.u2();
		outer = (ConstantClassInfo) cp.get(outerClassInfoIndex);
		outerName = cp.getString(outer.nameIndex);
		inner = (ConstantClassInfo) cp.get(innerClassInfoIndex);
		innerName = cp.getString(inner.nameIndex);
		innerClassName = cp.getString(innerClassNameIndex);
	}

	public void detailedDump(IndentedOutputStream stream)
	{
		stream.indent(1);
		stream.iprintln("Outer: " + outerName);
		stream.iprintln("Inner: " + innerName);
		stream.iprintln("Name: " + innerClassName);

		stream.iprint("Access: ");
		int flags = innerClassAccessFlags;
		stream.printHex(flags, 4);
		flags = ClassFile.printAccessFlag(stream, flags, 0x4000, " Enum");
		flags = ClassFile.printAccessFlag(stream, flags, 0x2000, " Annotation");
		flags = ClassFile.printAccessFlag(stream, flags, 0x1000, " Synthetic");
		flags = ClassFile.printAccessFlag(stream, flags, 0x0400, " Abstract");
		flags = ClassFile.printAccessFlag(stream, flags, 0x0200, " Interface");
		flags = ClassFile.printAccessFlag(stream, flags, 0x0010, " Final");
		flags = ClassFile.printAccessFlag(stream, flags, 0x0008, " Static");
		flags = ClassFile.printAccessFlag(stream, flags, 0x0004, " Protected");
		flags = ClassFile.printAccessFlag(stream, flags, 0x0002, " Private");
		flags = ClassFile.printAccessFlag(stream, flags, 0x0001, " Public");
		if (flags != 0)
		{
			stream.print(" Invalid ");
			stream.printHex(flags, 4);
		}
		stream.println();

		stream.indent(-1);
	}
}
