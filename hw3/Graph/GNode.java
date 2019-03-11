package Graph;

import java.util.Map;
import java.util.Vector;
import cs132.util.ProblemException;
import cs132.vapor.parser.VaporParser;
import cs132.vapor.ast.VaporProgram;
import cs132.vapor.ast.VBuiltIn.Op;

import cs132.util.*;
import cs132.vapor.ast.*;
import cs132.vapor.parser.*;

import java.io.*;

public class GNode {
    // Vector<Edge> out_edge;
    // Vector<Edge> in_edge;
    Map<String, Boolean> live_in;
    Map<String, Boolean> live_out;
    public int node_id;
    public Vector<GNode> pred;
    public Vector<GNode> succ;
    Vector<GNode> adj;
    public int out_degree;
    public int in_degree;
    public SourcePos line;

    public GNode(Graph g) {
        node_id = g.node_count;
        pred = new Vector<>();
        succ = new Vector<>();
        adj = new Vector<>();
        out_degree = 0;
        in_degree = 0;
        line = null;
        // System.out.println(node_id + " GNode created");
    }
        
    public GNode(Graph g, SourcePos s) {
        node_id = g.node_count;
        pred = new Vector<>();
        succ = new Vector<>();
        adj = new Vector<>();
        out_degree = 0;
        in_degree = 0;
        line = s;
        // System.out.println(node_id + " GNode created");
    }

    public boolean goes_to(GNode n) {
        boolean goes = false;

        for(GNode i: succ) {
            if(i.node_id == n.node_id) {
                goes = true;
            }
        }

        return goes;
    }

    public boolean comes_from(GNode n) {
        boolean goes = false;

        for(GNode i: pred) {
            if(i.node_id == n.node_id) {
                goes = true;
            }
        }

        return goes;
    }

    public boolean adj_to(GNode n) {
        boolean goes = false;

        for(GNode i: adj) {
            if(i.node_id == n.node_id) {
                goes = true;
            }
        }

        return goes;
    }

    
}