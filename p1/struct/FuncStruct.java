package struct;
import java.util.*;

public class FuncStruct extends Struct {

    public Vector<Struct> literal;

    public FuncStruct (String n, String t, Vector<Struct> l)
    {
        name = n;
        type = t;
        literal = l;
    }
    public Vector<Struct> getParams()
    {
        return literal;
    }
}