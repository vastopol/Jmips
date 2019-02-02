package struct;
import java.util.*;

/**
 * All void structs must implement this interface
 */

public class Struct {
    String name;
    String type;

    public String getType()
    {
        return type;
    }
    public String getName()
    {
        return name;
    }
    public int getInt(){return 0;}
    public void setInt(int l){}

    public String getString(){return "";}
    public void setInt(String l){}

    public Boolean getBool(){return false;}
    public void setBool(Boolean l){}

    public Vector<Integer> getArr(){return new Vector<>();}
    public void setArr(Vector<Integer> l){}

    public Vector<Struct> getParams(){return new Vector<>();}
    public String get_returnType(){return "";}

    public Vector<Struct> getFields(){return new Vector<>();}
    public Vector<Struct> getMethods(){return new Vector<>();}
    public void setParent(String p){}
    public String getParent(){return "";}
    public String get_className(){return "";}
}
//INT, BOOL, ARRAY, FUNC, CLASS