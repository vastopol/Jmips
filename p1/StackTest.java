/*
    this is the main class for the compiler.
    first part is type checking
    files: Typecheck.java, visitor/DepthFirstVisitor.java, visitor/GJDepthFirst.java
*/

// java modules
import java.io.*;
import java.util.*;

// project folders
import visitor.*;
import syntaxtree.*;

// main
public class StackTest
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
            Goal goal = parser.Goal(); // THROWS: ParseException

            // data members from visitor classes
            boolean pass_check = false;
            /*Vector<Map<THING>> context;*/

            //TESTING
            DFStackVisitor df_stack_visitor = new DFStackVisitor();
            goal.accept(df_stack_visitor);

            Stack<String> vs = df_stack_visitor.context_stack;
            System.out.println("\nPrint Stack\n--------------------");
            for( String v : vs )
            {
                System.out.println(v);
            }
            System.out.println("\n--------------------\n");

            //builds symbol table using symbol visitor class
            /*DFSymbolVisitor context_builder = new DFSymbolVisitor();
            goal.accept(context_builder);
            context = contex_builder.Symtab;*/

            //builds type check class with symbol table
            //type checks

           /* GJTypeCheckVisitor<> type_checker = new GJTypeCheckVisitor<>(context);
            goal.accept(type_checker);
            pass_check = GJTypeCheckVisitor.check_me;*/


            if(pass_check)
            {
                System.out.println("Program type checked successfully.\n");
            }
            else
            {
                System.out.println("Type error.\n");
                //System.out.println("File: " + f);
                System.exit(1);
            }
        }

        return;
    }
};

