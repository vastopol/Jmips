package toolbox;
import java.util.*;

import struct.*;

public class Pair {
    String key;
    Struct value;

    public Pair(String k, Struct v) {
        key = k;
        value = v;
    }

    public String getKey() {
        return key;
    }

    public Struct getValue() {
        return value;
    }
}