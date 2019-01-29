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
                System.out.println("Fields: ");
                for(Struct i: s.getFields()) {
                    tools.print(i);
                }
                System.out.println("Methods: ");
                for(FuncStruct i: s.getMethods()) {
                    tools.print(i);
                }
                break;
            
            case "function":
                System.out.println(s.get_returnType() + " " + s.getType() + " " + s.getName());
                System.out.println("Parameters: ");
                for(Struct i: s.getParams()) {
                    tools.print(i);
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
                System.out.println("Fields: ");
                for(Struct i: s.getFields()) {
                    tools.print(i);
                }
                System.out.println("Methods: ");
                for(FuncStruct i: s.getMethods()) {
                    tools.print(i);
                }
                break;
        }
    }
}