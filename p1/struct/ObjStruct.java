package struct;
import java.util.*;

public class ObjStruct extends Struct {

    public Struct literal;

    public ObjStruct (String n, Struct l)
    {
        name = n;
        type = l.getName();
        literal = l;
    }

    public Vector<Struct> getFields()
    {
        return literal.getFields();
    }

    public Vector<FuncStruct> getMethods()
    {
        return literal.getMethods();
    }


}