package toolbox;
import java.util.*;
import java.lang.*;

import struct.*;
public class tools {

    public static void print(Struct s)
    {
        String swcase = s.getType();

        switch(swcase)
        {
            case "int[]":
                System.out.println(s.getType() + " " + s.getName() + " " + s.getArr());
                break;
        
            case "boolean":
                System.out.println(s.getType() + " " + s.getName() + " " + s.getBool());
                break;

            case "class":
                System.out.println(s.getType() + " " + s.getName());
                // System.out.println(s.getName() + " fields: ");
                for(Struct i: s.getFields()) {
                    System.out.println(s.getName() + " fields: ");
                    tools.print(i);
                    System.out.println("");
                }
                // System.out.println(s.getName() + " methods: ");
                for(Struct i: s.getMethods()) {
                    System.out.println(s.getName() + " methods: ");
                    tools.print(i);
                    System.out.println("");
                }
                break;
            
            case "function":
                System.out.println(s.get_returnType() + " " + s.getType() + " " + s.getName());
                // System.out.println(s.getName() + " parameters: ");
                for(Struct i: s.getParams()) {
                    System.out.println(s.getName() + " parameters: ");
                    tools.print(i);
                    System.out.println("");
                }
                break;
            case "int":
                System.out.println(s.getType() + " " + s.getName() + " " + s.getInt());
                break;
            case "String[]":
                System.out.println("String[]" + " " + s.getName());
                break;
            case "object":
                System.out.println(s.getType() + " " + s.getName() + " " + s.get_className());
                break;
        }
    }

    public static Vector<Struct> methods(Struct c, Map<String,Map<String,Struct>> m , Vector<Struct> f) {
        if(c.getType() != "class") {
            System.out.println("XXXXXXXXXXXXXXXXXXXX");
        }
        for(Struct i: c.getMethods()) {
            f.add(i);
        }
        if(c.getParent() != "") {
            Struct cp = m.get("Global").get(c.getParent());
            // for(Struct i: cp.getFields()) {
            //     f.add(i);
            // }
            if(loop_exist(c, m)){
                return f;
            }
            return methods(cp, m, f);
            // for(Struct i: parent_methods) {
            //     f.add(i);
            // }

        }
        return f;
    }

    public static boolean loop_exist(Struct c, Map<String, Map<String, Struct>> m) {
        Vector<String> seen = new Vector<String>();
        boolean retval = false;
        while(c.getParent() != "")
        {
            if(seen.contains(c.getName())) {
                retval = true;
                break;
            }
            else {
                seen.add(c.getName());
                c = m.get("Global").get(c.getParent());
            }
        }

        return retval;

    }

    // public static Struct final_parent(Struct p, Map<String, Map<String, Struct>> m) {

    // }
    public static Vector<Pair> vtable_correct(Struct c, Map<String,Map<String,Struct>> m) {
        Vector<Pair> eff = new Vector<Pair>();
        Vector<Pair> che = new Vector<Pair>();
        che = vtable_create(c, m, eff);
        return che;
    }

    public static Vector<Pair> vtable_create(Struct c, Map<String,Map<String,Struct>> m ,Vector<Pair> f) {
        if(c.getParent() != "") {
            Struct cp = m.get("Global").get(c.getParent());
            if(loop_exist(c, m)){
                return f;
            }
            
            Vector<Pair> inter = new Vector<Pair>();
             f = vtable_create(cp, m, f);
        }
        boolean exists = false;
        for(Struct i: c.getMethods()) {
            for(int j = 0; j < f.size(); j++) {
                if(i.getName() == f.elementAt(j).getValue().getName()) {
                    Pair yeehaw = new Pair(c.getName(), i);
                    f.set(j, yeehaw);
                    exists = true;
                }
            }

            if(!exists) {
                Pair yeethaw = new Pair(c.getName(), i);
                f.add(yeethaw);
                exists = false;
            }
        }
        return f;
    }

    public static StringBuffer print_vtable(Vector<Pair> table, String cname) {
        StringBuffer string_buf = new StringBuffer();
        string_buf.append("const vmt_" + cname + "\n");
        for(Pair i: table) {
            string_buf.append("  :" + i.getKey() + "." + i.getValue().getName() + "\n");
        }
        string_buf.append("\n");
        return string_buf;
    }

    public static StringBuffer create_vtables(Map<String,Map<String,Struct>> m) {
        Vector<Vector<Pair>> all_vtables = new Vector<>();
        StringBuffer sbuffer = new StringBuffer();
        for(String i: m.get("Global").keySet()) {
            boolean main_method = false;
            Struct temp = m.get("Global").get(i);
            for(Struct j: temp.getMethods()) {
                if(j.getName() == "main" || j.getName() == "Main") {
                    main_method = true;
                }
            }
            if(!main_method){
                Vector<Pair> vtemp = vtable_correct(temp, m);
                all_vtables.add(vtemp);
                StringBuffer btemp = print_vtable(vtemp, temp.getName());
                sbuffer.append(btemp.toString());
            }
        }
        return sbuffer;
    }

    public static Vector<Vector<Pair>> create_vtables_no(Map<String,Map<String,Struct>> m) {
        Vector<Vector<Pair>> all_vtables = new Vector<>();
        for(String i: m.get("Global").keySet()) {
            boolean main_method = false;
            Struct temp = m.get("Global").get(i);
            for(Struct j: temp.getMethods()) {
                if(j.getName() == "main" || j.getName() == "Main") {
                    main_method = true;
                }
            }
            if(!main_method){
                Vector<Pair> vtemp = vtable_correct(temp, m);
                all_vtables.add(vtemp);
                }
            }
            return all_vtables;
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

    public static Stack<String> print_var_stack(Stack<String> st) {
        Stack<String> nst = new Stack<>();
        Stack<String> nst2 = new Stack<>();

        while(!st.empty()) {
            String top = st.pop();
            System.out.println(top);
            nst.push(top);
        }

        while(!nst.empty()) {
            String top2 = nst.pop();
            nst2.push(top2);
        }

        return nst2;
    }
}