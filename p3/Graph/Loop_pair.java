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

public class Loop_pair {
    public int loop_start_line;
    public int loop_end_line;
    public String loop_name;

    public Loop_pair(String lname, int loop_start) {
        loop_name = lname;
        loop_start_line = loop_start;
    }

}