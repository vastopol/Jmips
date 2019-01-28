package struct;
import java.util.*;

public class BoolStruct extends Struct {

    public Boolean literal;

    public BoolStruct (String n, Boolean l)
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

    public void setBool(Boolean l)
    {
        literal = l;
    }
}