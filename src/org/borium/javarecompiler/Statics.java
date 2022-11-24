package org.borium.javarecompiler;

import org.borium.javarecompiler.classfile.*;

/**
 * Helper class with various static methods to reduce clutter.
 */
public class Statics
{
	public static String addPointerIfNeeded(String type)
	{
		switch (type)
		{
		case "byte":
		case "char":
		case "double":
		case "float":
		case "int":
		case "INT64":
		case "short":
		case "bool":
		case "void":
			return type;
		}
		return "Pointer<" + type + ">";
	}

	public static String addPointersIfNeeded(String methodType)
	{
		String signature = methodType;
		// First opening '('
		Assert(signature.startsWith("("), "Method type: Method() expected");
		String result = "(";
		signature = signature.substring(1);
		// Parameters, if any?
		while (!signature.startsWith(")"))
		{
			int pos = signature.indexOf(' ');
			Assert(pos > 0, "Method type: Space between type and name not found");
			String type = signature.substring(0, pos);
			result += addPointerIfNeeded(type) + " ";
			signature = signature.substring(pos + 1);
			while (signature.charAt(0) != ',' && signature.charAt(0) != ')')
			{
				result += signature.substring(0, 1);
				signature = signature.substring(1);
			}
			if (signature.startsWith(", "))
			{
				result += ", ";
				signature = signature.substring(2);
			}
		}
		result += ')';
		signature = signature.substring(1);
		// Return type, if any?
		if (signature.length() > 0)
		{
			// Yup, got something
			result += addPointerIfNeeded(signature);
		}
		return result;
	}

	public static void Assert(boolean condition, String errorMessage)
	{
		if (!condition)
		{
			throw new RuntimeException(errorMessage);
		}
	}

	public static void Check(IndentedOutputStream stream, boolean condition, String errorMessage)
	{
		if (!condition)
		{
			stream.iprintln("// " + errorMessage);
		}
	}

	public static String commaSeparatedList(String[] values)
	{
		String result = "";
		String separator = "";
		for (String param : values)
		{
			result += separator + param;
			separator = ", ";
		}
		return result;
	}

	public static String dotToNamespace(String dots)
	{
		return String.join("::", dots.split("[.]"));
	}

	public static String escapeCharacters(String string)
	{
		StringBuilder sb = new StringBuilder();
//		char[] chars = string.toCharArray();
//		for (char ch : chars)
		for (int i = 0; i < string.length(); i++)
		{
			char ch = string.charAt(i);
			switch (ch)
			{
			case '\n':
				sb.append("\\n");
				break;
			case '\r':
				sb.append("\\r");
				break;
			case '\t':
				sb.append("\\t");
				break;
			case '\'':
				sb.append("\\\'");
				break;
			case '\"':
				sb.append("\\\"");
				break;
			case '\\':
				sb.append("\\\\");
				break;
//			case '\n':sb.append("\\n");break;
			default:
				sb.append(ch);
				break;
			}
		}
		return sb.toString();
	}

	/**
	 * Calculate number of parameters to the method that is part of this name and
	 * type info. 'This' is not assumed to be present.
	 *
	 * @param javaDescriptor Java method descriptor.
	 * @return Parameter count, excluding optional 'this'.
	 */
	public static int getParameterCount(String javaDescriptor)
	{
		if (!javaDescriptor.startsWith("("))
		{
			throw new RuntimeException("Get parameter count for non-method");
		}
		int[] data = { 1, 0 };
		while (javaDescriptor.charAt(data[0]) != ')')
		{
			parseSingleType(javaDescriptor, data);
		}
		return data[1];
	}

	/**
	 * Generate upper-cased hex string. String will have leading zeros if necessary.
	 *
	 * @param value  Value to fill the string with.
	 * @param length Length of output string.
	 * @return hex string of value, with specified length.
	 */
	public static String hexString(int value, int length)
	{
		String result = "00000000" + Integer.toHexString(value);
		result = result.substring(result.length() - length);
		return result.toUpperCase();
	}

	public static boolean isTemplate(String type)
	{
		int index = type.indexOf('<');
		Assert(index != 0, "Template: Starts with '<'");
		return index > 0;
	}

	/**
	 * Convert simple non-template non-pointer Java class name to C++ class name by
	 * replacing all '.' with '::'.
	 *
	 * @param javaClassName Java class name.
	 * @return C++ class name.
	 */
	public static String javaToCppClass(String javaClassName)
	{
		String[] split = javaClassName.split("[.]");
		return String.join("::", split);
	}

//	/**
//	 * Parse Java method return type.
//	 *
//	 * @param javaMethodSignature Method signature.
//	 * @return Return type or null for void.
//	 */
//	public static String parseJavaReturnType(String javaMethodSignature)
//	{
//		int pos = javaMethodSignature.indexOf(')');
//		Assert(pos >= 1, "Method with no return type");
//		String type = javaMethodSignature.substring(pos + 1);
//		int dimensions = 0;
//		while (type.charAt(0) == '[')
//		{
//			dimensions++;
//			type = type.substring(1);
//		}
//		String returnType = "";
//		boolean rawType = true;
//		switch (type.charAt(0))
//		{
//		case 'B':
//			returnType = "byte";
//			break;
//		case 'C':
//			returnType = "char";
//			break;
//		case 'D':
//			returnType = "double";
//			break;
//		case 'F':
//			returnType = "float";
//			break;
//		case 'I':
//			returnType = "int";
//			break;
//		case 'J':
//			returnType = "INT64";
//			break;
//		case 'S':
//			returnType = "short";
//			break;
//		case 'Z':
//			returnType = "bool";
//			break;
//		case 'L':
//			rawType = false;
//			returnType = new JavaTypeConverter(type, false).getCppType();
//			break;
//		case 'T':
//			rawType = false;
//			returnType = type.substring(1, type.length() - 1);
//			break;
//		case 'V':
//			returnType = "void";
//			break;
//		default:
//			Assert(false, "Unhandled type " + type);
//		}
//		Assert(dimensions <= 1, "Too many dimensions: " + dimensions);
//		if (dimensions > 0)
//		{
//			returnType = (rawType ? "JavaRawArray" : "JavaArray") + "<" + returnType + ">";
//			dimensions--;
//		}
//		return returnType;
//	}

	public static String removeJavaArray(String javaArray)
	{
		if (javaArray.startsWith("JavaArray<"))
		{
			return javaArray.substring(10, javaArray.length() - 1);
		}
		if (javaArray.startsWith("JavaRawArray<"))
		{
			return javaArray.substring(13, javaArray.length() - 1);
		}
		Assert(false, "JavaArray<> or JavaRawArray<> expected");
		return null;
	}

	public static String removePointerWrapper(String wrappedObject)
	{
		Assert(wrappedObject.startsWith("Pointer<"), "Pointer<> expected");
		return wrappedObject.substring(8, wrappedObject.length() - 1);
	}

	private static void parseClass(String descriptor, int[] data)
	{
		while (descriptor.charAt(data[0]) != ';' && descriptor.charAt(data[0]) != '<')
		{
			data[0]++;
		}
		if (descriptor.charAt(data[0]) == '<')
		{
			throw new RuntimeException("Templates not supported");
//			data[0]++;
//			while (descriptor.charAt(data[0]) != '>')
//			{
//				int count = data[1];
//				parseSingleType(data);
//				data[1] = count;
//			}
//			data[0]++;
		}
		if (descriptor.charAt(data[0]) == ';')
		{
			data[0]++;
		}
	}

	private static void parseSingleType(String descriptor, int[] data)
	{
		while (descriptor.charAt(data[0]) == '[')
		{
			data[0]++;
		}
		switch (descriptor.charAt(data[0]))
		{
		case 'B':
		case 'C':
		case 'D':
		case 'F':
		case 'I':
		case 'J':
		case 'S':
		case 'Z':
			data[0]++;
			data[1]++;
			break;
		case 'L':
			data[0]++;
			data[1]++;
			parseClass(descriptor, data);
			break;
		case 'V':
			data[0]++;
			break;
		}
	}
}
