/*
    this is the main class for the compiler.
    first part is type checking
    files: Typecheck.java, visitor/DepthFirstVisitor.java, visitor/GJDepthFirst.java
*/

// java modules
import java.io.*;
import java.util.*;
// builtin project folders
import visitor2.*;    // <------------- CHANGED THIS FOR OUR VISITORS
import syntaxtree.*;

// our classes
import struct.*;

import toolbox.*;

// main
public class J2V
{
    public static void main(String[] args)  // could use try/catch block instead of exception
        throws ParseException
    {
        MiniJavaParser parser = new MiniJavaParser(System.in);
        Goal goal = parser.Goal();              // THROWS: ParseException

        // Data Members from Visitors
        Map<String,Map<String,Struct>> symbol_table1; // named symbol table for each context - partial
        Map<String,Map<String,Struct>> symbol_table2; // named symbol table for each context - filled in

        /* ---------- START TESTS ---------- */

        // test_struct();

        /* ---------- END TESTS ---------- */

        /* ---------- START VISITS ---------- */

        // CREATES PARTIAL SYMBOL TABLE
        DFStackVisitor df_stack_visitor1 = new DFStackVisitor();
        goal.accept(df_stack_visitor1);

        // HERE GET SYMBOL TABLE #1
        symbol_table1 = df_stack_visitor1.struct_map;
        // System.out.println("SYMBOL TABLE #1");
        // print_map_structs(symbol_table1);

        // FILLS IN THE PARTIAL TABLE
        DFStackVisitor2 df_stack_visitor2 = new DFStackVisitor2(symbol_table1);
        goal.accept(df_stack_visitor2);

        // HERE GET SYMBOL TABLE #2
        symbol_table2 = df_stack_visitor2.struct_map;
        // System.out.println("SYMBOL TABLE #2");
        // print_map_structs(symbol_table2);


        // Vector<Pair> jef = new Vector<Pair>();
        // Vector<Pair> jeebus = new Vector<Pair>();
        // StringBuffer sbuffer = tools.create_vtables(symbol_table2);
        // System.out.println(sbuffer.toString());
        // Vector<Vector<Pair>> all_v = new Vector<>();
        // all_v = tools.create_vtables(symbol_table2);
        // for(Vector<Pair> i: all_v) {
        //     tools.print_vtable(i, );
        // }
        // Struct vtab_starter1 = symbol_table2.get("Global").get("A");
        // jef = tools.vtable_correct(vtab_starter1, symbol_table2);
        // // jef = tools.vtable_create(vtab_starter, symbol_table2, jeebus);
        // // System.out.println("Class A vtable: ");
        // sbuffer = tools.print_vtable(jef, "A");
        // System.out.println(sbuffer.toString());

        // Struct vtab_starter2 = symbol_table2.get("Global").get("B");
        // jef = tools.vtable_correct(vtab_starter2, symbol_table2);
        // // jef = tools.vtable_create(vtab_starter, symbol_table2, jeebus);
        // // System.out.println("Class B vtable: ");
        // sbuffer = tools.print_vtable(jef, "B");
        // System.out.println(sbuffer);

        // Struct vtab_starter3 = symbol_table2.get("Global").get("C");
        // jef = tools.vtable_correct(vtab_starter3, symbol_table2);
        // // jef = tools.vtable_create(vtab_starter, symbol_table2, jeebus);
        // // System.out.println("Class C vtable: ");
        // sbuffer = tools.print_vtable(jef, "C");
        // System.out.println(sbuffer);

        // Struct vtab_starter4 = symbol_table2.get("Global").get("D");
        // jef = tools.vtable_correct(vtab_starter4, symbol_table2);
        // // jef = tools.vtable_create(vtab_starter, symbol_table2, jeebus);
        // // System.out.println("Class D vtable: ");
        // sbuffer = tools.print_vtable(jef, "D");
        // System.out.println(sbuffer);
 



        // HERE EMIT VAPOR IR CODE
        DFVaporVisitor df_vapor_visitor = new DFVaporVisitor(symbol_table2);
        goal.accept(df_vapor_visitor);

        /* ---------- END VISITS ---------- */

        return;
    }

    public static void test_struct()
    {
        Struct numSev = new IntStruct("x", 7);
        // System.out.println(numSev.getType() + " " + numSev.getName() + " " + numSev.getInt());
        Struct boolFal = new BoolStruct("bool", false);
        // System.out.println(boolFal.getType() + " " + boolFal.getName() + " " + boolFal.getBool());
        Struct numList = new ArrStruct("numl", new Vector<Integer>(3));
        // System.out.println(numList.getType() + " " + numList.getName() + " " + numList.getArr());

        Vector<Struct> params = new Vector<Struct>();
        params.add(numSev);
        params.add(boolFal);
        params.add(numList);
        FuncStruct facFunc = new FuncStruct("Fac", "Integer", params);
        // System.out.println(facFunc.getType() + " " + facFunc.getName() + " " + facFunc.getParams());

        Vector<Struct> meths = new Vector<Struct>();
        meths.add(facFunc);
        ClassStruct factorialClass = new ClassStruct("factorial", params, meths);
        // System.out.println(factorialClass.getType() + " " + factorialClass.getName() + " " + factorialClass.getFields() + " " + factorialClass.getMethods());

        // ObjStruct facObject = new ObjStruct("facObject", new ClassStruct("facotrial", params, meths));
        // System.out.println(facObject.getType() + " " + facObject.getName() + " " + facObject.getFields() + " " + facObject.getMethods());
        System.out.println(""); // end test with newline
        toolbox.tools.print(numSev);
        toolbox.tools.print(boolFal);
        toolbox.tools.print(numList);
        toolbox.tools.print(facFunc);
        toolbox.tools.print(factorialClass);
        // toolbox.tools.print(facObject);
        // Vector<Struct> f = helper.fields(factorialClass);
        // for(Struct i: f)
        // {
        //     tools.print(i);
        // }
        Vector<Vector<String>> tester = helper.methodtype(factorialClass, facFunc);
        for(String n: tester.elementAt(0)){
            System.out.println("tester " + n);
        }
        System.out.println(tester);
        System.out.println("");
    }

    public static void print_stack_trace(Stack<String> ss)
    {
        System.out.println(""); // end test with newline
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

        //System.out.println(vs.size());
        for(Map<String,Struct> i: vs)
        {
            for(String k1: i.keySet())
            {
                Struct stc = i.get(k1);
                toolbox.tools.print(stc);
            }
            System.out.println("");
        }

        System.out.println("--------------------\n");
    }

    public static void print_map_structs(Map<String,Map<String,Struct>> ms)
    {
        System.out.println("Print Map of Maps (String,Struct) \n--------------------");

        //System.out.println(ms.size());
        for(String k : ms.keySet())
        {
            Map<String, Struct> m1 = ms.get(k);
            System.out.println("Map: " + k);
            for(String k1 : m1.keySet())
            {
                Struct stc = m1.get(k1);
                System.out.println("Struct: " + k1);
                toolbox.tools.print(stc);
            }
            System.out.println("");
        }

        System.out.println("--------------------\n");
    }
};

