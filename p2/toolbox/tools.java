package toolbox;
import java.util.*;
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
}