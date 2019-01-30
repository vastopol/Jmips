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

    public static Vector<Struct> fields(ClassStruct c) {
        Vector<Struct> f = new Vector<Struct>();
        for(Struct i: c.getFields()) {
            f.add(i);
        }
        if(c.getParent() != null) {
            for(Struct j: c.getParent().getFields()) {
                f.add(j);
            }
        }
        return f;
    }

    public static Vector<Vector<String>> methodtype(ClassStruct c, FuncStruct f) {
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

    public static boolean noOverloading(ClassStruct id, ClassStruct idp, FuncStruct idM) {
        boolean overloading = false;
        for(FuncStruct i: id.getMethods()) {
            for(FuncStruct j: idp.getMethods()) {
                Vector<Vector<String>> idn = methodtype(id, i);
                Vector<Vector<String>> idpn = methodtype(idp, j);
                if(idn.equals(idpn)) {
                    overloading = true;
                    break;
                }
            }
        }
        return overloading;
    }

    
    
    
}