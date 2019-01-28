package struct;
import java.util.*;

public class ObjStruct extends Struct {

    public ClassStruct literal;
    public String objtype;

    public ObjStruct (String n, ClassStruct l)
    {
        name = n;
        type = "object";
        objtype = l.getName();
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