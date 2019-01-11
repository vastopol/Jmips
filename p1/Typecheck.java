/*
    this is the main class for the compiler.
    first part is type checking
    files: Typecheck.java, visitor/DepthFirstVisitor.java, visitor/GJDepthFirst.java
*/

// java modules
import java.io.*;

// project folders
import visitor.*;
import syntaxtree.*;

// main
public class Typecheck
{
    public static void main(String[] args)  // could use try/catch block instead of exception
        throws FileNotFoundException, ParseException
    {
        if(args.length == 0) // exit if no input
        {
            System.out.println("Error: Need input file.");
            System.exit(1);
        }

        for(String f : args) // process input files
        {
            // Parser components
            File file = new File(f);
            Reader reader = new FileReader(file); // THROWS: FileNotFoundException
            MiniJavaParser parser = new MiniJavaParser(reader);

            // Default Visitors
            GJDepthFirst gj_visitor = new GJDepthFirst();
            DepthFirstVisitor df_visitor = new DepthFirstVisitor();

            // Custom Visitors
            DFPrintVisitor df_print_visitor = new DFPrintVisitor();

            // type check with visitors
            boolean pass_check = false;
            Goal goal = parser.Goal(); // THROWS: ParseException
            goal.accept(df_print_visitor);

            if(pass_check)
            {
                System.out.println("Program type checked successfully.");
            }
            else
            {
                System.out.println("Type error.");
                //System.out.println("File: " + f);
                System.exit(1);
            }
        }

        return;
    }
};

