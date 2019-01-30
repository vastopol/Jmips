package struct;
import java.util.*;

public class ClassStruct extends Struct {

    public Vector<Struct> fields;
    public Vector<Struct> methods;
    ClassStruct parent;

    public ClassStruct (String n, Vector<Struct> f, Vector<Struct> m)
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

    public Vector<Struct> getMethods()
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