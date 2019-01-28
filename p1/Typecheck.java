/*
    this is the main class for the compiler.
    first part is type checking
    files: Typecheck.java, visitor/DepthFirstVisitor.java, visitor/GJDepthFirst.java
*/

// java modules
import java.io.*;
import java.util.*;

// builtin project folders
import visitor.*;
import syntaxtree.*;

// our classes
import struct.*;

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
            Map<String,Map<String,Struct>> sym_table; // named symbol table for each context

            /* ---------- START TESTS ---------- */

            // test_struct();

            // DFPrintVisitor df_print_visitor = new DFPrintVisitor();
            // goal.accept(df_print_visitor);

            // DFStackTestVisitor df_stack_test_visitor = new DFStackTestVisitor();
            // goal.accept(df_stack_test_visitor);
            // print_stack_trace(df_stack_test_visitor.context_stack);
            // print_vec_maps(df_stack_test_visitor.map_vec);
            // print_map_maps(df_stack_test_visitor.map_map);

            /* ---------- END TESTS ---------- */


            /* ---------- START VISITS ---------- */

            /*DFStackVisitor df_stack_test_visitor = new DFStackVisitor();
            goal.accept(df_stack_test_visitor);

            print_vec_structs(df_stack_visitor.struct_vec);*/

            //print_map_structs(df_stack_visitor.map_map);

            // HERE GET SYMBOL TABLE ONCE KNOWN CORRECT
            // sym_table = df_stack_visitor.struct_map;

            /*
            // HERE DO TYPECHECK
            GJTypeCheckVisitor<> type_checker = new GJTypeCheckVisitor<>(context);
            goal.accept(type_checker);
            pass_check = GJTypeCheckVisitor.check_me;
            */

            /* ---------- END VISITS ---------- */

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

    public static void test_struct()
    {
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

        System.out.println(""); // end test with newline
    }

    public static void print_stack_trace(Stack<String> ss)
    {
        System.out.println("Print Stack Trace\n--------------------");
        for( String s : ss )
        {
            System.out.println(s);
        }
        System.out.println("--------------------\n");
    }

    public static void print_vec_maps(Vector<Map<String,String>> vm)
    {
        System.out.println("Print Vector of Maps (String,String) \n--------------------");
        for( Map<String,String> entry : vm )
        {
            entry.forEach( (k,v) -> System.out.println("( "+ k + " : " + v + " )") );
            System.out.println("");
        }
        System.out.println("--------------------\n");
    }

    public static void print_map_maps(Map<String,Map<String,String>> mm)
    {
        System.out.println("Print Map of Maps (String,String) \n--------------------");
        for( Map.Entry<String,Map<String,String>> entry : mm.entrySet() )
        {
            String key = entry.getKey();
            Map<String,String> val = entry.getValue();
            System.out.println("Map: " + key);
            val.forEach( (k,v) -> System.out.println("( "+ k + " : " + v + " )") );
            System.out.println("");
        }
        System.out.println("--------------------\n");

    }

    public static void print_vec_structs(Vector<Map<String,Struct>> vs)
    {
        System.out.println("Print Vector of Maps (String,Struct) \n--------------------");
        for( Map<String,Struct> entry : vs )
        {
            entry.forEach( (k,v) -> System.out.println("( "+ k + " : " + v + " )") );
            System.out.println("");
        }
        System.out.println("--------------------\n");
    }

    public static void print_map_structs(Map<String,Map<String,Struct>> ms)
    {
        System.out.println("Print Map of Maps (String,Struct) \n--------------------");
        for( Map.Entry<String,Map<String,Struct>> entry : ms.entrySet() )
        {
            String key = entry.getKey();
            Map<String,Struct> val = entry.getValue();
            System.out.println("Map: " + key);
            val.forEach( (k,v) -> System.out.println("( " + k + " : " + v + " )") );
        }
        System.out.println("--------------------\n");

    }
};

