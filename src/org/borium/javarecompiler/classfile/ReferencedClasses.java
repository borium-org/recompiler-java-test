package org.borium.javarecompiler.classfile;

import static org.borium.javarecompiler.Statics.*;

import java.util.*;

import org.borium.javarecompiler.classfile.constants.*;

/**
 * Special magic class to store referenced classes. It is not iterable so loops
 * over it must work by iterating over the tree set of referenced classes. Java
 * iterator is templated but 'silent' iterator over tree set is not, and it is
 * easier to get it to work. All classes being added are parsed for template
 * parameters, so each template can potentially add multiple references.
 */
public class ReferencedClasses
{
	private TreeSet<String> referencedClasses = new TreeSet<>();

	/**
	 * Construct a pre-initialized referenced classes object.
	 * <p>
	 * <b>Not doing but keeping the comment.</b>Classes are added in here manually
	 * if they need template parameters but there is no type information for these
	 * classes in compiled class file. The overhead of always adding these classes
	 * is quite low. Classes added:
	 * <ul>
	 * <li>java.util.Iterator<T></li>
	 * </ul>
	 *
	 * @param cp Constant pool from which all class info constants are to be added
	 *           to referenced classes.
	 */
	public ReferencedClasses(ConstantPool cp)
	{
//		add("Ljava/util/Iterator<Ljava/lang/Object;>;");
		cp.addReferencedClasses(this);
	}

	/**
	 * Add a type as a referenced type. Only class types are added, and template
	 * types are simplified or split into multiple classes where each template
	 * argument is added separately.
	 *
	 * @param type Java type or signature where types are not erased.
	 */
	public void add(String type)
	{
		while (type.length() > 0)
		{
			char prefix = type.charAt(0);
			type = type.substring(1);
			switch (prefix)
			{
			case 'B': // byte
			case 'C': // char
			case 'D': // double
			case 'F': // float
			case 'I': // int
			case 'J': // long
			case 'S': // short
			case 'Z': // bool
			case 'V': // void
			case '(': // (), ignore
			case ')': // (), ignore
			case '[': // array, ignore
				break;
			case 'T':
				type = skipClass(type);
				break;
			case 'L':
				type = addClass(type);
				break;
			case '<':
				type = addTemplateBaseClasses(type);
				break;
			default:
				throw new RuntimeException("Bad prefix '" + prefix + "'");
			}
		}
	}

	public TreeSet<String> getClasses()
	{
		return referencedClasses;
	}

	public int getTemplateParameterCount(String fullClassName)
	{
		for (String referencedClass : referencedClasses)
		{
			referencedClass = javaToCppClass(referencedClass.replace('/', '.'));
			int pos = referencedClass.indexOf('<');
			Assert(pos != 0, "'<' at the beginning of class name");
			if (pos >= 0)
			{
				String baseName = referencedClass.substring(0, pos);
				if (baseName.equals(fullClassName) || baseName.endsWith("::" + fullClassName))
				{
					String templateCount = referencedClass.substring(pos + 1, referencedClass.length() - 1);
					return Integer.parseInt(templateCount);
				}
			}
		}
		return 0;
	}

	public void removeClass(String className)
	{
		referencedClasses.remove(className.replace('.', '/'));
	}

	private String addClass(String type)
	{
		String className = "";
		while (type.charAt(0) != ';' && type.charAt(0) != '<')
		{
			className += type.charAt(0);
			type = type.substring(1);
		}
		if (type.charAt(0) == '<')
		{
			type = type.substring(1);
			int parameterCount = 0;
			while (type.charAt(0) == 'L' || type.charAt(0) == 'T')
			{
				// Do not add template parameter classes
				boolean isClass = type.charAt(0) == 'L';
				type = type.substring(1);
				if (isClass)
				{
					type = addClass(type);
				}
				else
				{
					type = skipClass(type);
				}
				parameterCount++;
			}
			className += "<" + parameterCount + ">";
			insert(className);
			Assert(type.charAt(0) == '>', "Template terminator expected");
			type = type.substring(1);
			Assert(type.charAt(0) == ';', "Class terminator expected");
			type = type.substring(1);
		}
		else if (type.charAt(0) == ';')
		{
			type = type.substring(1);
			insert(className);
		}
		return type;
	}

	private String addTemplateBaseClasses(String type)
	{
		while (type.charAt(0) != '>')
		{
			// Skip template type parameter name
			while (type.charAt(0) != ':')
			{
				type = type.substring(1);
			}
			type = type.substring(1);
			Assert(type.charAt(0) == 'L', "Template base class expected");
			type = addClass(type.substring(1));
		}
		return type.substring(1);
	}

	/**
	 * Insert new class, but check if name is duplicate first. Duplicates in this
	 * context are template classes that can be with or without template parameter
	 * counts. Template classes with parameters are inserted into referenced classes
	 * collection when header is generated, due to signatures being available.
	 * Type-erased template classes are inserted into referenced classes collection
	 * at the time when source is generated, and there signatures are not always
	 * available.
	 *
	 * @param className Class name to check and insert.
	 */
	private void insert(String className)
	{
		// Exact match? Say we already have it
		if (referencedClasses.contains(className))
		{
			return;
		}
		// Check if new class is template and old one isn't
		if (className.contains("<"))
		{
			String rawTemplate = className.substring(0, className.indexOf('<'));
			referencedClasses.remove(rawTemplate);
			referencedClasses.add(className);
			return;
		}
		// New class may be a raw version of the template class that we already have
		String template = className + "<";
		for (String clazz : referencedClasses)
		{
			if (clazz.startsWith(template))
			{
				return;
			}
		}
		// Not a match
		referencedClasses.add(className);
	}

	private String skipClass(String type)
	{
		while (type.charAt(0) != ';' && type.charAt(0) != '<')
		{
			type = type.substring(1);
		}
		if (type.charAt(0) == '<')
		{
			type = type.substring(1);
			while (type.charAt(0) == 'L')
			{
				type = type.substring(1);
				type = skipClass(type);
			}
			Assert(type.charAt(0) == '>', "Template terminator expected");
			type = type.substring(1);
			Assert(type.charAt(0) == ';', "Class terminator expected");
			type = type.substring(1);
		}
		else if (type.charAt(0) == ';')
		{
			type = type.substring(1);
		}
		return type;
	}
}
