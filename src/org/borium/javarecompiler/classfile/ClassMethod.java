package org.borium.javarecompiler.classfile;

import static org.borium.javarecompiler.Statics.*;
import static org.borium.javarecompiler.classfile.ClassFile.*;

import java.util.*;

import org.borium.javarecompiler.*;
import org.borium.javarecompiler.classfile.attribute.*;
import org.borium.javarecompiler.classfile.constants.*;
import org.borium.javarecompiler.classfile.instruction.*;

/**
 * Each method, including each instance initialization method (2.9.1) and the
 * class or interface initialization method (2.9.2), is described by a
 * method_info structure. No two methods in one class file may have the same
 * name and descriptor (4.3.3).
 * <p>
 * The structure has the following format:
 *
 * <pre>
	method_info
	{
		u2 access_flags;
		u2 name_index;
		u2 descriptor_index;
		u2 attributes_count;
		attribute_info attributes[attributes_count];
	}
 * </pre>
 */
public class ClassMethod
{
	private static final int AccessSynthetic = 0x1000;
	private static final int AccessStrict = 0x0800;
	private static final int AccessAbstract = 0x0400;
	private static final int AccessNative = 0x0100;
	private static final int AccessVarArgs = 0x0080;
	private static final int AccessBridge = 0x0040;
	private static final int AccessSynchronized = 0x0020;
	private static final int AccessFinal = 0x0010;
	private static final int AccessStatic = 0x0008;
	private static final int AccessProtected = 0x0004;
	private static final int AccessPrivate = 0x0002;
	private static final int AccessPublic = 0x0001;

	/**
	 * The value of the access_flags item is a mask of flags used to denote access
	 * permission to and properties of this method. The interpretation of each flag,
	 * when set, is specified in Table 4.6-A. <table border>
	 * <tr>
	 * <th>Flag Name</th>
	 * <th>Value</th>
	 * <th>Interpretation</th>
	 * </tr>
	 * <tr>
	 * <td>ACC_PUBLIC</td>
	 * <td>0x0001</td>
	 * <td>Declared public; may be accessed from outside its package.</td>
	 * </tr>
	 * <tr>
	 * <td>ACC_PRIVATE</td>
	 * <td>0x0002</td>
	 * <td>Declared private; accessible only within the defining class and other
	 * classes belonging to the same nest (5.4.4).</td>
	 * </tr>
	 * <tr>
	 * <td>ACC_PROTECTED</td>
	 * <td>0x0004</td>
	 * <td>Declared protected; may be accessed within subclasses.</td>
	 * </tr>
	 * <tr>
	 * <td>ACC_STATIC</td>
	 * <td>0x0008</td>
	 * <td>Declared static.</td>
	 * </tr>
	 * <tr>
	 * <td>ACC_FINAL</td>
	 * <td>0x0010</td>
	 * <td>Declared final; must not be overridden (5.4.5).</td>
	 * </tr>
	 * <tr>
	 * <td>ACC_SYNCHRONIZED</td>
	 * <td>0x0020</td>
	 * <td>Declared synchronized; invocation is wrapped by a monitor use.</td>
	 * </tr>
	 * <tr>
	 * <td>ACC_BRIDGE</td>
	 * <td>0x0040</td>
	 * <td>A bridge method, generated by the compiler.</td>
	 * </tr>
	 * <tr>
	 * <td>ACC_VARARGS</td>
	 * <td>0x0080</td>
	 * <td>Declared with variable number of arguments.</td>
	 * </tr>
	 * <tr>
	 * <td>ACC_NATIVE</td>
	 * <td>0x0100</td>
	 * <td>Declared native; implemented in a language other than the Java
	 * programming language.</td>
	 * </tr>
	 * <tr>
	 * <td>ACC_ABSTRACT</td>
	 * <td>0x0400</td>
	 * <td>Declared abstract; no implementation is provided.</td>
	 * </tr>
	 * <tr>
	 * <td>ACC_STRICT</td>
	 * <td>0x0800</td>
	 * <td>In a class file whose major version number is at least 46 and at most 60:
	 * Declared strictfp.</td>
	 * </tr>
	 * <tr>
	 * <td>ACC_SYNTHETIC</td>
	 * <td>0x1000</td>
	 * <td>Declared synthetic; not present in the source code.</td>
	 * </tr>
	 * </table>
	 * The value 0x0800 is interpreted as the ACC_STRICT flag only in a class file
	 * whose major version number is at least 46 and at most 60. For methods in such
	 * a class file, the rules below determine whether the ACC_STRICT flag may be
	 * set in combination with other flags. (Setting the ACC_STRICT flag constrained
	 * a method's floating-point instructions in Java SE 1.2 through 16 (2.8).) For
	 * methods in a class file whose major version number is less than 46 or greater
	 * than 60, the value 0x0800 is not interpreted as the ACC_STRICT flag, but
	 * rather is unassigned; it is not meaningful to "set the ACC_STRICT flag" in
	 * such a class file.
	 * <p>
	 * Methods of classes may have any of the flags in Table 4.6-A set. However,
	 * each method of a class may have at most one of its ACC_PUBLIC, ACC_PRIVATE,
	 * and ACC_PROTECTED flags set (JLS 8.4.3).
	 * <p>
	 * Methods of interfaces may have any of the flags in Table 4.6-A set except
	 * ACC_PROTECTED, ACC_FINAL, ACC_SYNCHRONIZED, and ACC_NATIVE (JLS 9.4). In a
	 * class file whose version number is less than 52.0, each method of an
	 * interface must have its ACC_PUBLIC and ACC_ABSTRACT flags set; in a class
	 * file whose version number is 52.0 or above, each method of an interface must
	 * have exactly one of its ACC_PUBLIC and ACC_PRIVATE flags set.
	 * <p>
	 * If a method of a class or interface has its ACC_ABSTRACT flag set, it must
	 * not have any of its ACC_PRIVATE, ACC_STATIC, ACC_FINAL, ACC_SYNCHRONIZED, or
	 * ACC_NATIVE flags set, nor (in a class file whose major version number is at
	 * least 46 and at most 60) have its ACC_STRICT flag set.
	 * <p>
	 * An instance initialization method (2.9.1) may have at most one of its
	 * ACC_PUBLIC, ACC_PRIVATE, and ACC_PROTECTED flags set, and may also have its
	 * ACC_VARARGS and ACC_SYNTHETIC flags set, and may also (in a class file whose
	 * major version number is at least 46 and at most 60) have its ACC_STRICT flag
	 * set, but must not have any of the other flags in Table 4.6-A set.
	 * <p>
	 * In a class file whose version number is 51.0 or above, a method whose name is
	 * &lt;clinit&gt; must have its ACC_STATIC flag set.
	 * <p>
	 * A class or interface initialization method (2.9.2) is called implicitly by
	 * the Java Virtual Machine. The value of its access_flags item is ignored
	 * except for the setting of the ACC_STATIC flag and (in a class file whose
	 * major version number is at least 46 and at most 60) the ACC_STRICT flag, and
	 * the method is exempt from the preceding rules about legal combinations of
	 * flags.
	 * <p>
	 * The ACC_BRIDGE flag is used to indicate a bridge method generated by a
	 * compiler for the Java programming language.
	 * <p>
	 * The ACC_VARARGS flag indicates that this method takes a variable number of
	 * arguments at the source code level. A method declared to take a variable
	 * number of arguments must be compiled with the ACC_VARARGS flag set to 1. All
	 * other methods must be compiled with the ACC_VARARGS flag set to 0.
	 * <p>
	 * The ACC_SYNTHETIC flag indicates that this method was generated by a compiler
	 * and does not appear in source code, unless it is one of the methods named in
	 * 4.7.8.
	 * <p>
	 * All bits of the access_flags item not assigned in Table 4.6-A are reserved
	 * for future use. (This includes the bit corresponding to 0x0800 in a class
	 * file whose major version number is less than 46 or greater than 60.) They
	 * should be set to zero in generated class files and should be ignored by Java
	 * Virtual Machine implementations.
	 */
	private int accessFlags;

	/**
	 * The value of the name_index item must be a valid index into the constant_pool
	 * table. The constant_pool entry at that index must be a CONSTANT_Utf8_info
	 * structure (4.4.7) representing either a valid unqualified name denoting a
	 * method (4.2.2), or (if this method is in a class rather than an interface)
	 * the special method name &lt;init&gt;, or the special method name
	 * &lt;clinit&gt;.
	 */
	private int nameIndex;

	/**
	 * The value of the descriptor_index item must be a valid index into the
	 * constant_pool table. The constant_pool entry at that index must be a
	 * CONSTANT_Utf8_info structure representing a valid method descriptor (4.3.3).
	 * Furthermore:
	 * <ul>
	 * <li>If this method is in a class rather than an interface, and the name of
	 * the method is &lt;init&gt;, then the descriptor must denote a void
	 * method.</li>
	 * <li>If the name of the method is &lt;clinit&gt;, then the descriptor must
	 * denote a void method, and, in a class file whose version number is 51.0 or
	 * above, a method that takes no arguments.</li>
	 * </ul>
	 */
	private int descriptorIndex;

	private HashMap<String, ClassAttribute> attributes = new HashMap<>();

	private ArrayList<ClassAttribute> attributeList = new ArrayList<>();

	/** This method name. */
	private String name;

	/** This method descriptor. */
	private String descriptor;

	public void addReferencedClasses(ReferencedClasses referencedClasses)
	{
		referencedClasses.add(getType());
	}

	public void dump(IndentedOutputStream stream, ConstantPool cp)
	{
		stream.println("Method: " + cp.getString(nameIndex) + " " + cp.getString(descriptorIndex));
		stream.indent(1);

		stream.iprint("Access Flags: ");
		stream.printHex(accessFlags, 4);
		int flags = accessFlags;
		flags = printAccessFlag(stream, flags, AccessSynthetic, " Synthetic");
		flags = printAccessFlag(stream, flags, AccessStrict, " Strict");
		flags = printAccessFlag(stream, flags, AccessAbstract, " Abstract");
		flags = printAccessFlag(stream, flags, AccessNative, " Native");
		flags = printAccessFlag(stream, flags, AccessVarArgs, " VarArgs");
		flags = printAccessFlag(stream, flags, AccessBridge, " Bridge");
		flags = printAccessFlag(stream, flags, AccessSynchronized, " Synchronized");
		flags = printAccessFlag(stream, flags, AccessFinal, " Final");
		flags = printAccessFlag(stream, flags, AccessStatic, " Static");
		flags = printAccessFlag(stream, flags, AccessProtected, " Protected");
		flags = printAccessFlag(stream, flags, AccessPrivate, " Private");
		flags = printAccessFlag(stream, flags, AccessPublic, " Public");
		if (flags != 0)
		{
			stream.print(" Invalid ");
			stream.printHex(flags, 4);
		}
		stream.println();

		stream.iprintln("Attributes: " + attributes.size());
		stream.indent(1);
		for (int i = 0; i < attributes.size(); i++)
		{
			stream.iprint(i + ": ");
			ClassAttribute attribute = attributeList.get(i);
			attribute.dump(stream);
		}
		stream.indent(-2);
	}

	public AttributeCode getCode()
	{
		AttributeCode code = (AttributeCode) attributes.get("Code");
		return code;
	}

	public String getDescriptor()
	{
		return descriptor;
	}

	public ExceptionTable[] getExceptionTable()
	{
		AttributeCode code = (AttributeCode) attributes.get("Code");
		return code.getExceptionTable();
	}

	public Instruction[] getInstructions()
	{
		AttributeCode code = (AttributeCode) attributes.get("Code");
		return code != null ? code.getInstructions() : null;
	}

	public int getLocalsCount()
	{
		AttributeCode code = (AttributeCode) attributes.get("Code");
		return code.getLocalsCount();
	}

	public VariableTableEntry[] getLocalVariableTable()
	{
		AttributeCode code = (AttributeCode) attributes.get("Code");
		return code.getLocalVariableTable();
	}

	public String getName()
	{
		return name;
	}

	public int getParameterCount()
	{
		return Statics.getParameterCount(descriptor) + (isStatic() ? 0 : 1);
	}

	public String getType()
	{
		if (attributes.containsKey("Signature"))
		{
			AttributeSignature sig = (AttributeSignature) attributes.get("Signature");
			return sig.getSignature();
		}
		return descriptor;
	}

	public boolean isAbstract()
	{
		return (accessFlags & AccessAbstract) != 0;
	}

	public boolean isPresentInSource()
	{
		if ((accessFlags & (AccessSynthetic | AccessBridge)) != 0)
		{
			Assert((accessFlags & (AccessSynthetic | AccessBridge)) == (AccessSynthetic | AccessBridge),
					"Both Synthetic and Bridge expected");
			return false;
		}
		return true;
	}

	public boolean isStatic()
	{
		return (accessFlags & AccessStatic) != 0;
	}

	public void read(ByteInputStream in, ConstantPool cp)
	{
		accessFlags = in.u2();
		nameIndex = in.u2();
		name = cp.getString(nameIndex);
		descriptorIndex = in.u2();
		descriptor = cp.getString(descriptorIndex);
		int attributeCount = in.u2();
		for (int i = 0; i < attributeCount; i++)
		{
			ClassAttribute attribute = ClassAttribute.readAttribute(in, cp);
			attributes.put(attribute.getName(), attribute);
			attributeList.add(attribute);
		}
		// TODO validation
	}
}