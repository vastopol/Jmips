package struct;
import java.util.*;

public class BoolStruct extends Struct {

    public boolean literal;

    public BoolStruct (String n, boolean l)
    {
        name = n;
        type = "boolean";
        literal = l;
    }

    public boolean getLiteral()
    {
        return literal;
    }

    public void setType(String t)
    {
        type = t;
    }

    public void setBool(boolean l)
    {
        literal = l;
    }
}