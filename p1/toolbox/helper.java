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

    // public static String methodtype(ClassStruct c, FuncStruct f) {

    //     return ;
    // }
    
    
}