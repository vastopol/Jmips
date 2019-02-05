package struct;
import java.util.*;

public class ClassStruct extends Struct {

    public Vector<Struct> fields;
    public Vector<Struct> methods;
    public String parent;

    public ClassStruct (String n, Vector<Struct> f, Vector<Struct> m)
    {
        name = n;
        type = "class";
        fields = f;
        methods = m;
        parent = "";
    }
    public Vector<Struct> getFields()
    {
        return fields;
    }

    public Vector<Struct> getMethods()
    {
        return methods;
    }

    public void setParent(String p)
    {
        parent = p;
    }

    public String getParent()
    {
        return parent;
    }
}