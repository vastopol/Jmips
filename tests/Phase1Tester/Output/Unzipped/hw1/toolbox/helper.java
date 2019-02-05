package toolbox;
import java.util.*;

import struct.*;

public class helper{

    public static String classname(ClassStruct c) {
        return c.getName();
    }

    public static String methodname(FuncStruct f) {
        return f.getName();
    }

    public static boolean distinct(Vector<String> l) {
        boolean duplicates = false; 
        for(String i: l) {
            for(String j: l) {
                if(i == j) {
                    duplicates = true;
                }
            }
        }

        return duplicates;
    }

    public static Vector<Struct> fields(Struct c, Map<String,Map<String,Struct>> m) {
        Vector<Struct> f = new Vector<Struct>();
        if(c.getType() != "class") {
            System.out.println("XXXXXXXXXXXXXXXXXXXX");
        }
        for(Struct i: c.getFields()) {
            f.add(i);
        }
        if(c.getParent() != "") {
            Struct cp = m.get("Global").get(c.getParent());
            if(tools.loop_exist(c, m)){
                return f;
            }
            // for(Struct i: cp.getFields()) {
            //     f.add(i);
            // }
            Vector<Struct> parent_fields = fields(cp, m);
            for(Struct i: parent_fields) {
                f.add(i);
            }

        }
        return f;
    }

    public static Vector<Vector<String>> methodtype(Struct c, Struct f) {
        Vector<String> typelist = new Vector<String>();
        Vector<String> retlist = new Vector<String>();
        retlist.add(f.get_returnType());
        for(Struct i: f.getParams()) {
            typelist.add(i.getType());
        }
        Vector<Vector<String>> tuple = new Vector<Vector<String>>();
        tuple.add(typelist);
        tuple.add(retlist);
        return tuple;
    }

    public static boolean noOverloading(Struct id, Struct idp, Struct idM, Map<String, Map<String, Struct>> m) {
        boolean overloading = true;
        // System.out.println("sup1");
        Vector<Struct> foo = new Vector<Struct>();
        Vector<Struct> p = tools.methods(id, m, foo);
        for(Struct i: p) {
            // System.out.println("sup2");
            foo = new Vector<Struct>();
            Vector<Struct> q = tools.methods(idp, m, foo);
            for(Struct j: q) {
                // System.out.println("sup3");
                Vector<Vector<String>> idn = methodtype(id, i);
                Vector<Vector<String>> idpn = methodtype(idp, j);
                // System.out.println("sup4");
                // if(idn.equals(idpn)) {
                //     overloading = true;
                //     // System.out.println("sup5");
                //     break;
                // }
                if(i.getName() == idM.getName()) {
                    if(j.getName() == idM.getName()) {
                        if(!idn.equals(idpn)) {
                            overloading = false;
                            break;
                        }
                    }
                }
            }
        }
        return overloading;
    }

    
    
    
}