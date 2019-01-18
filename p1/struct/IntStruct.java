package struct;
import java.util.*;

public class IntStruct extends Struct {

    public int literal;

    public IntStruct (String n, int l)
    {
        name = n;
        type = "Integer";
        literal = l;
    }
    public int getInt()
    {
        return literal;
    }

    public void setInt(int l)
    {
        literal = l;
    }
}