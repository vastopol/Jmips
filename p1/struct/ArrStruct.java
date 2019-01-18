package struct;
import java.util.*;

public class ArrStruct extends Struct {

    public Vector<Integer> literal;

    public ArrStruct (String n, Vector<Integer> l)
    {
        name = n;
        type = "Integer";
        literal = l;
    }
    public Vector<Integer> getArr()
    {
        return literal;
    }

    public void setArr(Vector<Integer> l)
    {
        literal = l;
    }
}