package struct;
import java.util.*;

public class ClassStruct extends Struct {

    public Vector<Struct> fields;
    public Vector<FuncStruct> methods;

    public ClassStruct (String n, Vector<Struct> f, Vector<FuncStruct> m)
    {
        name = n;
        type = "class";
        fields = f;
        methods = m;
    }
    public Vector<Struct> getFields()
    {
        return fields;
    }

    public Vector<FuncStruct> getMethods()
    {
        return methods;
    }
}