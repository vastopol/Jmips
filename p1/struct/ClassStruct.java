package struct;
import java.util.*;

public class ClassStruct extends Struct {

    public Vector<Struct> fields;
    public Vector<FuncStruct> methods;
    ClassStruct parent;

    public ClassStruct (String n, Vector<Struct> f, Vector<FuncStruct> m)
    {
        name = n;
        type = "class";
        fields = f;
        methods = m;
        parent = null;
    }
    public Vector<Struct> getFields()
    {
        return fields;
    }

    public Vector<FuncStruct> getMethods()
    {
        return methods;
    }

    public void setParent(ClassStruct p)
    {
        parent = p;
    }

    public ClassStruct getParent()
    {
        if (parent == null)
        {
            return null;
        }
        return parent;
    }
}