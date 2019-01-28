package struct;
import java.util.*;

public class StringStruct extends Struct {

    public String literal;

    public StringStruct (String n, String l)
    {
        name = n;
        type = "String[]";
        literal = l;
    }
    public String getString()
    {
        return literal;
    }

    public void setString(String l)
    {
        literal = l;
    }
}