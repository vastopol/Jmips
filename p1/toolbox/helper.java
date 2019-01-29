package toolbox;
import java.sql.Struct;
import java.util.*;

import struct.ClassStruct;
import struct.FuncStruct;

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
        f.addAll(c.getFields());
        return f;
    }
    
}