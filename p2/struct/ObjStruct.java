package struct;
import java.util.*;

public class ObjStruct extends Struct {

    public String literal;

    public ObjStruct (String n, String l)
    {
        name = n;
        type = "object";
        literal = l;
    }
    public String get_className() {
        return literal;
    }

    // public Vector<Struct> getFields()
    // {
    //     return literal;
    // }

    // public Vector<Struct> getMethods()
    // {
    //     return literal.getMethods();
    // }


}