package Graph;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;
import java.util.Vector;
import cs132.util.ProblemException;
import cs132.vapor.parser.VaporParser;
import cs132.vapor.ast.VaporProgram;
import cs132.vapor.ast.VBuiltIn.Op;

import cs132.util.*;
import cs132.vapor.ast.*;
import cs132.vapor.parser.*;

import java.io.*;

public class Pair {
    public String key;
    public Interval value;

    public Pair(String k, Interval v) {
        key = k;
        value = v;
    }

}