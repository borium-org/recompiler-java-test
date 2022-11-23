package org.borium.javarecompiler;

import java.util.*;

//import org.borium.javarecompiler.classfile.*;
//import org.borium.javarecompiler.cplusplus.*;

public class Recomp
{
	public static boolean instructionComments = false;

	public static boolean stackComments = false;

	public static boolean dumpInstructions = false;

	public static boolean dumpStatements = false;

	/** Processed classes using C++ styled fully qualified names. */
//	public static HashMap<String, ClassFile> processedClasses = new HashMap<>();
	/**
	 * Fully qualified class names for all classes that are part of the processed
	 * classes hash map. The base hash map is add-only and is the only instance in
	 * the app so far that is being iterated over. HashMap iteration needs an
	 * iterator over the entry set, and to reduce complexity for now we'll use an
	 * array list of keys of the map and iterate over the list when needed.
	 */
//	private static ArrayList<String> processedClassNames = new ArrayList<>();

	/**
	 * Mapping between simple names and fully qualified names, no duplicates
	 * allowed.
	 */
	public static HashMap<String, String> simpleClassNames = new HashMap<>();

	public static void main(String[] args)
	{
//		IndentedOutputStream.disableLocking();
		if (args.length == 0)
		{
			args = new String[] { //
					"-classpath", "../recompiler-java-test/bin", //
					"-outputpath", "../recompiler-cpp-test", //
					"-mainclass", "org.borium.javarecompiler.Recomp", //
					"-vs", "2005", //
					"-comments", "all", //
			};
		}
		Recomp recompiler = new Recomp();
		for (int argc = 0; argc < args.length; argc += 2)
		{
			switch (args[argc])
			{
			case "-classpath":
				recompiler.addClassPath(args[argc + 1]);
				break;
			case "-outputpath":
				recompiler.setOutputPath(args[argc + 1]);
				break;
			case "-mainclass":
				recompiler.setMainClass(args[argc + 1]);
				break;
			case "-vs":
				recompiler.setVisualStudio(args[argc + 1]);
				break;
			case "-comments":
				recompiler.setCommentLevel(args[argc + 1]);
				break;
			default:
				throw new RuntimeException("Unsupported argument " + args[argc]);
			}
		}
		recompiler.run();
		System.out.println("Done.");
	}

	/** Main class name within source class path. There can be only one. */
	private String mainClass;

	/** List of all class paths to search for the class. */
	private ArrayList<String> classPaths = new ArrayList<>();

	/** Output path for generated files. There can be only one. */
	private String outputPath;

	/** Visual Studio version for generated project. There can be only one. */
	private String visualStudio;

//	private ArrayList<CppClass> generatedClasses = new ArrayList<>();

	public void addClassPath(String classPath)
	{
		classPaths.add(classPath);
	}

	public void run()
	{
		System.out.println("Processing " + mainClass);
//		ClassFile classFile = processClassFile(mainClass);
//		addNewClass(classFile);
//		ArrayList<String> newClassNames = new ArrayList<>();
//		addReferencedClasses(newClassNames, classFile);
//		while (newClassNames.size() > 0)
//		{
//			String newClassName = newClassNames.remove(0);
//			// Skip nested classes for now
//			if (newClassName.indexOf('$') >= 0)
//			{
//				System.out.println("Skipping nested class " + newClassName);
//				continue;
//			}
//			System.out.println("Processing " + newClassName);
//			classFile = processClassFile(newClassName);
//			addNewClass(classFile);
//			addReferencedClasses(newClassNames, classFile);
//		}
//		generateClasses();
//		writeClasses();
	}

	public void setMainClass(String mainClass)
	{
		if (this.mainClass != null)
		{
			throw new RuntimeException(
					"Main class already set to '" + this.mainClass + "', not setting it to '" + mainClass + "'");
		}
		this.mainClass = mainClass;
	}

	public void setOutputPath(String outputPath)
	{
		if (this.outputPath != null)
		{
			throw new RuntimeException(
					"Output path already set to '" + this.outputPath + "', not setting it to '" + outputPath + "'");
		}
		this.outputPath = outputPath;
	}

	public void setVisualStudio(String visualStudio)
	{
		if (this.visualStudio != null)
		{
			throw new RuntimeException("Visual Studio already set to '" + this.visualStudio + "', not setting it to '"
					+ visualStudio + "'");
		}
		this.visualStudio = visualStudio;
	}

//	private void addNewClass(ClassFile classFile)
//	{
//		String className = classFile.getClassName();
//		processedClasses.put(className, classFile);
//		processedClassNames.add(className);
//		String simpleClassName = className.substring(className.lastIndexOf('.') + 1);
//		Assert(!simpleClassNames.containsKey(simpleClassName), "Duplicate simple class name " + simpleClassName);
//		simpleClassNames.put(simpleClassName, className);
//	}

//	private void addReferencedClasses(ArrayList<String> newClassNames, ClassFile classFile)
//	{
//		ReferencedClasses allReferences = classFile.getReferencedClasses();
//
//		for (String reference : allReferences.getClasses())
//		{
//			// Reference is in java/something/Class format
//			if (reference.startsWith("java/"))
//			{
//				continue;
//			}
//			// Processed classes are using a.b.c naming convention
//			String ref = reference.replace('/', '.');
//			if (!processedClasses.containsKey(ref) && !newClassNames.contains(ref))
//			{
//				newClassNames.add(ref);
//			}
//		}
//	}

//	private void generateClasses()
//	{
//		for (String className : processedClassNames)
//		{
//			System.out.println("Generating " + className);
//			CppClass cppClass = new CppClass(processedClasses.get(className));
//			generatedClasses.add(cppClass);
//		}
//	}

//	private ClassFile processClassFile(String classFileName)
//	{
//		if (classFileName.startsWith("java."))
//		{
//			return null;
//		}
//		String classPathFileName = classFileName.replace('.', '/') + ".class";
//		String fileName = null;
//		for (String classPath : classPaths)
//		{
//			File file = new File(classPath + "/" + classPathFileName);
//			if (file.exists() && file.isFile())
//			{
//				fileName = classPath + "/" + classPathFileName;
//				break;
//			}
//		}
//		if (fileName == null)
//		{
//			System.out.println("Error: " + classFileName);
//			throw new RuntimeException("Class " + classFileName + " not found");
//		}
//		ClassFile classFile = new ClassFile();
//		try
//		{
//			classFile.read(fileName);
//		}
//		catch (ClassFormatError | IOException e)
//		{
//			e.printStackTrace();
//		}
//		try
//		{
//			IndentedOutputStream stream = new IndentedOutputStream(
//					fileName.substring(0, fileName.length() - 5) + "txt");
//			classFile.dump(stream);
//			stream.close();
//		}
//		catch (IOException e)
//		{
//			e.printStackTrace();
//		}
//		return classFile;
//	}

	private void setCommentLevel(String commentLevel)
	{
		switch (commentLevel)
		{
		case "all":
			instructionComments = true;
			stackComments = true;
			break;
		case "none":
			instructionComments = false;
			stackComments = false;
			break;
		default:
			throw new RuntimeException("Unsupported comment level " + commentLevel);
		}

	}

//	private void writeClasses()
//	{
//		for (CppClass cppClass : generatedClasses)
//		{
//			System.out.println("Writing " + cppClass.getFullClassName());
//			cppClass.writeClass(outputPath);
//		}
//	}
}
