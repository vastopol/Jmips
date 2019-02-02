package struct;
import java.util.*;

public class FuncStruct extends Struct {

    public Vector<Struct> literal;
    String return_type;

    public FuncStruct (String n, String t, Vector<Struct> l)
    {
        name = n;
        type = "function";
        return_type = t;
        literal = l;
    }
    public Vector<Struct> getParams()
    {
        return literal;
    }
    
    public String get_returnType()
    {
        return return_type;
    }
}