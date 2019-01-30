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
                System.out.println(s.getType() + " " + s.getName() + " " + s.getFields());
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
        }
    }
}