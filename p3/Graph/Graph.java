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

public class Graph {

    public int node_count;
    public int edges;
    public int vertices;
    public Map<Integer, Vector<GNode>> adj_matrix;
    public Vector<GNode> all_nodes;
    public GNode head;

    public Graph() {
        node_count = 0;
        edges = 0;
        vertices = 0;
        all_nodes = new Vector<>();
        head = null;
        adj_matrix = new HashMap();
        // System.out.println("Setup graph");
    }

    public Vector<GNode> nodes() {
        return all_nodes;
    }

    public GNode new_node(VInstr vin) {
        node_count++;
        // System.out.println(node_count + " is the new node_count in Graph::new_node()"); 

        GNode temp = new GNode(this, vin.sourcePos);
        // System.out.println(temp.node_id + " created in Graph::new_node()"); 

        vertices++;
        // System.out.println(vertices);
        adj_matrix.put(node_count, temp.adj);
        all_nodes.add(temp);
        // System.out.println(all_nodes.size() + " is all_nodes size in new_node()"); 

        if(head == null) {
            head = temp;
        }

        return temp;
    }

    public GNode new_node() {
        node_count++;
        // System.out.println(node_count + " is the new node_count in Graph::new_node()"); 

        GNode temp = new GNode(this);
        // System.out.println(temp.node_id + " created in Graph::new_node()"); 

        vertices++;
        // System.out.println(vertices);
        adj_matrix.put(node_count, temp.adj);
        all_nodes.add(temp);
        // System.out.println(all_nodes.size() + " is all_nodes size in new_node()"); 

        if(head == null) {
            head = temp;
        }

        return temp;
    }

    public boolean is_pred(GNode check, GNode possible) {
        boolean ret = false;
        if(check.node_id == possible.node_id) {
            return true;
        }
        for(GNode i: check.pred) {
            if(i.node_id == possible.node_id) {
                return true;
            }
            else {
                ret = is_pred(i, possible);
            }
        }

        return ret;
    }

    public boolean add_edge(GNode from, GNode to) {
        
        if(is_pred(from, to)) {
            System.out.println("(Node " + to.node_id + ") can go to (Node" + from.node_id + ") and would cause a loop");
            return false;
        }

        for(GNode c: from.succ) {
            if(c.node_id == to.node_id) {
                System.out.println("attempted to add already existing edge");
                return false;
            }
        }
        from.succ.add(to);
        from.adj.add(to);

        from.out_degree++;
        edges++;
        to.in_degree++;

        to.pred.add(from);
        to.adj.add(from);

        return true;
    }

    public boolean rm_edge(GNode from, GNode to) {
        if(from.node_id == to.node_id) {
            return false;
        }

        boolean edge_exists = false;
        for(GNode c: from.succ) {
            if(c.node_id == to.node_id) {
                edge_exists = true;
            }
        }

        if(!edge_exists) {
            System.out.println("attempted to remove non existent edge");
            return false;
        }

        for(int i = 0; i < from.succ.size(); i++) {
            if(from.succ.elementAt(i).node_id == to.node_id) {
                from.succ.remove(i);
            }
        }

        for(int i = 0; i < from.adj.size(); i++) {
            if(from.adj.elementAt(i).node_id == to.node_id) {
                from.adj.remove(i);
                adj_matrix.replace(from.node_id, from.adj);
            }
        }

        from.out_degree--;
        edges--;
        to.in_degree--;

        for(int i = 0; i < to.pred.size(); i++) {
            if(to.pred.elementAt(i).node_id == from.node_id) {
                to.pred.remove(i);
            }
        }

        for(int i = 0; i < to.adj.size(); i++) {
            if(to.adj.elementAt(i).node_id == from.node_id) {
                to.adj.remove(i);
                adj_matrix.replace(from.node_id, from.adj);
            }
        }

        return true;
    }

    public void show() {
        Stack<GNode> visit_stack = new Stack<>();

        GNode tmp = head;

        while(tmp != null) {
            if(tmp.node_id == head.node_id) {
                System.out.println("Head is (Node " + tmp.node_id + ")");
            }
            System.out.println("(Node " + tmp.node_id + ")'s children:");
            for(GNode i: tmp.succ) {
                System.out.println("    (Node " + tmp.node_id + ")'s child Node (" + i.node_id + ")");
                visit_stack.push(i);
            }

            if(visit_stack.empty()) {
                break;
            }
            else {
                tmp = visit_stack.pop();
            }
        }
    }

}