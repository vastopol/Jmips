/*
    this is the main class for the compiler.
    first part is type checking
    files: Typecheck.java, visitor/DepthFirstVisitor.java, visitor/GJDepthFirst.java
*/

import java.io.*;

public class Typecheck
{
    public static void main(String[] args)  // could use try/catch block instead of exception
        throws FileNotFoundException
    {
        if(args.length == 0)
        {
            System.out.println("Error: Need input file.");
            System.exit(1);
        }
        for(String s : args)
        {
            boolean pass_type_check = false;
            File file = new File(s);
            Reader reader = new FileReader(file);
            MiniJavaParser parser = new MiniJavaParser(reader);

            // here is parse/type check

            if(pass_type_check)
            {
                System.out.println("Program type checked successfully.");
            }
            else
            {
                System.out.println("Type error.");
                System.out.println("File: " + s);
                System.exit(1);
            }
        }

        return;
    }
};

