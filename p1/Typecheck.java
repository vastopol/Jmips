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
import struct.*;
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
            // Parser Components
            File file = new File(f);
            Reader reader = new FileReader(file);    // THROWS: FileNotFoundException
            MiniJavaParser parser = new MiniJavaParser(reader);
            Goal goal = parser.Goal();              // THROWS: ParseException

            // Data Members from Visitors
            boolean pass_check = false;
            /*Vector<Map<THING>> context;*/


            /* ---------- START TEST STRUCT ---------- */    // Krishna
            Struct numSev = new IntStruct("x", 7);
            System.out.println(numSev.getType() + " " + numSev.getName() + " " + numSev.getInt());
            Struct boolFal = new BoolStruct("bool", false);
            System.out.println(boolFal.getType() + " " + boolFal.getName() + " " + boolFal.getBool());
            Struct numList = new ArrStruct("numl", new Vector<Integer>(3));
            System.out.println(numList.getType() + " " + numList.getName() + " " + numList.getArr());

            Vector<Struct> params = new Vector<Struct>();
            params.add(numSev);
            params.add(boolFal);
            params.add(numList);
            FuncStruct facFunc = new FuncStruct("Fac", "Integer", params);
            System.out.println(facFunc.getType() + " " + facFunc.getName() + " " + facFunc.getParams());

            Vector<FuncStruct> meths = new Vector<FuncStruct>();
            meths.add(facFunc);
            ClassStruct factorialClass = new ClassStruct("factorial", params, meths);
            System.out.println(factorialClass.getType() + " " + factorialClass.getName() + " " + factorialClass.getFields() + " " + factorialClass.getMethods());
            /* ---------- END TEST STRUCT ---------- */


            /* ---------- START TEST PRINT && SYMBOL VISITOR ---------- */
            DFSymbolVisitor simbol = new DFSymbolVisitor();
            DFPrintVisitor df_print_visitor = new DFPrintVisitor();
            goal.accept(simbol);
            /* ---------- END TEST PRINT && SYMBOL VISITOR ---------- */


            /* ---------- START TEST STACK VISITOR ---------- */    // Sean
            DFStackVisitor df_stack_visitor = new DFStackVisitor();
            goal.accept(df_stack_visitor);

            Stack<String> vs = df_stack_visitor.context_stack;
            System.out.println("\nPrint Stack\n--------------------");
            for( String v : vs )
            {
                System.out.println(v);
            }
            System.out.println("--------------------\n");
            /* ---------- END TEST STACK VISITOR ---------- */


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

