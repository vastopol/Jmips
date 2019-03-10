package cs132.vapor.ast;
import cs132.util.SourcePos;

import java.lang.Throwable; // need for each visit function

import java.io.*;
import java.util.*;

import org.w3c.dom.Node;

import Graph.*;


// SEE THE VInstr.java file


/**
 * Visitor for instruction nodes, where each visitor function takes a parameter and
 * returns a value.
 *
 * @param <P>
 *     The type of the extra parameter taken by each visitor function.
 * @param <R>
 *     The type of the value returned by each visitor function.
 * @param <E>
 *     The exception type that each node is allowed to throw.  If you don't want checked
 *     exceptions here, use {@link java.lang.Throwable}.
 */

public class VisitorGraphBuilder<Throwable> //extends VInstr.Visitor
// public class VisitorPrinter<P,R,E extends java.lang.Throwable> extends VInstr.VisitorPR
{
    public Map<Integer, Vector<String>> def;
    public Map<Integer, Vector<String>> use;
    public Graph flow_graph;
    GNode last_node;

    public VisitorGraphBuilder()
    {
        def = new HashMap();
        use = new HashMap();
        flow_graph = new Graph();
        last_node = null;
        ;
    }

	public String graph_visit(VAssign a, String s)
        throws java.lang.Throwable
    {
        // ;
        GNode n1 = flow_graph.new_node(a);
        System.out.println("        " + a.dest.toString() + " = " + a.source.toString() + " " + n1.line.toString());
        if(last_node != null) {
            boolean worked = flow_graph.add_edge(last_node, n1);
            System.out.println("adding edge from " + last_node.node_id + " to " + n1.node_id);
            if(!worked) {
                System.out.println("shit didnt work g");
            }
        }
        else {
            System.out.println("last node was null");
        }
        last_node = n1;
        Vector<String> tmp_vec = new Vector<>();

        tmp_vec.add(a.dest.toString());
        def.put(n1.node_id, tmp_vec);
    
        
        // System.out.println(a.source.getClass());
        String c = a.source.getClass().toString();
        if(!c.equals("class cs132.vapor.ast.VLitInt") ){
            tmp_vec = new Vector<>();
            tmp_vec.add(a.source.toString());
            use.put(n1.node_id, tmp_vec);
        }

        

        String ret_val = a.dest.toString() + " " + a.source.toString();

        return ret_val;
    }

	public String graph_visit(VCall c, String s) throws
        java.lang.Throwable
    {
        // for(VFunction o: c.addr) {
        //     o.accept(this);
        // }
        GNode n1 = flow_graph.new_node(c);
        if(last_node != null) {
            // System.out.println("adding edge from " + last_node.node_id + " to " + n1.node_id);
            boolean worked = flow_graph.add_edge(last_node, n1);
            if(!worked) {
                System.out.println("shit didnt work g");
            }
        }
        else{
            System.out.println("last node was null");
        }
        last_node = n1;
        
        // System.out.println("        address of function being called:" + c.addr.getClass().toString() + " " + n1.line.toString());
        for(VOperand i: c.args) {
            // System.out.println("        argument being passed to function: " + i);
        }
        // System.out.println("        return value of the function: " + c.dest);


        Vector<String> tmp_vec = new Vector<>();
        tmp_vec.add(c.dest.toString());
        def.put(n1.node_id, tmp_vec);

        tmp_vec = new Vector<>();
        if(!c.addr.getClass().toString().equals("class cs132.vapor.ast.VLabelRef") && !c.addr.getClass().toString().equals("class cs132.vapor.ast.VAddr$Label")) {
            tmp_vec.add(c.addr.toString());
        }

        for(VOperand i: c.args) {
            String c_name = i.getClass().toString();
            // System.out.println(c_name);
            if(!c_name.equals("class cs132.vapor.ast.VLitInt") && !c_name.equals("class cs132.vapor.ast.VLabelRef")) {
                tmp_vec.add(i.toString());
            }
        }
        use.put(n1.node_id, tmp_vec);

        return s;

    }

    public String graph_visit(VBuiltIn c, String s)
        throws java.lang.Throwable
    {
        String ret = s;
        boolean stay = false;

        if(c.dest != null) {
            stay = true;
        }

        for(VOperand i: c.args) {
            String cname = i.getClass().toString();
            if(!cname.equals("class cs132.vapor.ast.VLitInt") && !cname.equals("class cs132.vapor.ast.VLitStr"))
            {
                stay = true;
            }
        }

        if(stay) {
            GNode n1 = flow_graph.new_node(c);
            if(last_node != null) {
                // System.out.println("adding edge from " + last_node.node_id + " to " + n1.node_id);
                boolean worked = flow_graph.add_edge(last_node, n1);
                if(!worked) {
                    System.out.println("shit didnt work g");
                }
            }
            else{
                System.out.println("last node was null");
            }
            last_node = n1;
    
            // System.out.println("        built in: " + c.op.name.toString() + " " + n1.line.toString());
            if(c.args != null) {
                for(VOperand i: c.args) {
                    // System.out.println("            arg:" + i.toString() + " " + i.getClass());
    
                }
    
            }
    
            Vector<String> tmp_vec = new Vector<>();
            if(c.dest != null) {

                tmp_vec.add(c.dest.toString());
                this.def.put(n1.node_id, tmp_vec);
                // System.out.println("            return: " + c.dest.toString());
                String cmp1 = "HeapAllocZ";
                String cmp2 = "HeapAlloc";
                if(c.op.name.toString().equals(cmp1) || c.op.name.toString().equals(cmp2)) {
                    ret = c.dest.toString();
                }
            }
    
            tmp_vec = new Vector<>();
            for(VOperand i: c.args) {
                String c_name = i.getClass().toString();
                if(!c_name.equals("class cs132.vapor.ast.VLitInt") && !c_name.equals("class cs132.vapor.ast.VLitStr")) {
                    tmp_vec.add(i.toString());
                    // System.out.println("``````````````" + i.toString());
                }
            }
            use.put(n1.node_id, tmp_vec);
            ;
        }
        
        return ret;
    }

    public String graph_visit(VMemWrite w, String s)
        throws java.lang.Throwable
    {
        int ind = s.indexOf(" ");
        String dest = "";
        if(ind != -1) {
            dest = s.substring(0, ind);
        }
        else{
            dest = s;
        }

        String w_cast = "";

        if(w.dest instanceof VMemRef.Global) {
            w_cast = ((VMemRef.Global)w.dest).base.toString();

            // System.out.println("            $mem ref is " + w_cast);
        }
        else if(w.dest instanceof VMemRef.Stack) {
            w_cast = ((VMemRef.Stack)w.dest).region.toString();
            // System.out.println("            $array ref is " + w_cast);
        }
        
        if(s != null) {
            // System.out.println("        " + dest + " = " + w.source.toString() + w.source.getClass());
        }
        else{
            // System.out.println("s is null ");
        }
        // VMemRef.Global dest_getter = new VMemRef.Global(dest.source);
        // ;
        GNode n1 = flow_graph.new_node(w);
        if(last_node != null) {
            boolean worked = flow_graph.add_edge(last_node, n1);
            // System.out.println("adding edge from " + last_node.node_id + " to " + n1.node_id);
            if(!worked) {
                System.out.println("shit didnt work g");
            }
        }
        else {
            System.out.println("last node was null");
        }
        last_node = n1;
        Vector<String> tmp_vec = new Vector<>();

        tmp_vec.add(dest);
        use.put(n1.node_id, tmp_vec);
    
        
        // System.out.println(a.source.getClass());
        String c = w.source.getClass().toString();
        String ret = w.source.toString();

        
        if(!c.equals("class cs132.vapor.ast.VLitInt") && !c.equals("class cs132.vapor.ast.VLabelRef") && w_cast != "") {
            // System.out.println("        " + w.dest.toString() + " = " + w.toString() + " " + n1.line.toString());
            tmp_vec = new Vector<>();
            tmp_vec.add(w_cast);
            tmp_vec.add(ret);
            use.put(n1.node_id, tmp_vec);
        }
        else if(c.equals("class cs132.vapor.ast.VLabelRef")) {
            ret = dest;
        }
        ;

        return ret;
    }

    public String graph_visit(VMemRead r, String s)
        throws java.lang.Throwable
    {
        int ind = s.indexOf(" ");
        String source = "";
        if(ind != -1) {
            source = s.substring(0, ind);
        }
        else{
            source = s;
        }
        String r_cast = "";

        if(r.source instanceof VMemRef.Global) {
            r_cast = ((VMemRef.Global)r.source).base.toString();

            // System.out.println("            $mem ref is " + r_cast);
        }
        else if(r.source instanceof VMemRef.Stack) {
            r_cast = ((VMemRef.Stack)r.source).region.toString();
            // System.out.println("            $array ref is " + r_cast);
        }
            
        if(s != null) {
            // System.out.println("        " + r.dest.toString() + " = " + source + " " + r.source.toString() + " " + r.source.getClass().toString());
        }
        else{
            System.out.println("s is null ");
        }
        // ;
        GNode n1 = flow_graph.new_node(r);
        if(last_node != null) {
            boolean worked = flow_graph.add_edge(last_node, n1);
            // System.out.println("adding edge from " + last_node.node_id + " to " + n1.node_id);
            if(!worked) {
                System.out.println("shit didnt work g");
            }
        }
        else {
            System.out.println("last node was null");
        }
        last_node = n1;
        Vector<String> tmp_vec = new Vector<>();

        tmp_vec.add(r.dest.toString());
        def.put(n1.node_id, tmp_vec);
        
        
        // System.out.println(a.source.getClass());
        // String c = r.source.getClass().toString();
        if(!r_cast.equals("class cs132.vapor.ast.VLitInt") && r_cast != "") {
            // System.out.println("        " + r.dest.toString() + " = " + r.toString() + " " + n1.line.toString());
            tmp_vec = new Vector<>();
            tmp_vec.add(r_cast);
            use.put(n1.node_id, tmp_vec);
        }
        ;
        String ret = r.dest.toString();

        return ret;
    }

    public String graph_visit(VBranch b, String s)
        throws java.lang.Throwable
    {
        GNode n1 = flow_graph.new_node(b);
        if(last_node != null) {
            boolean worked = flow_graph.add_edge(last_node, n1);
            // System.out.println("adding edge from " + last_node.node_id + " to " + n1.node_id);
            if(!worked) {
                System.out.println("shit didnt work g");
            }
        }
        else {
            System.out.println("last node was null");
        }
        last_node = n1;

        String c = b.value.getClass().toString();
        if(!c.equals("class cs132.vapor.ast.VLitInt")) {

            if(b.positive) {
                // System.out.println("        if " + b.value.toString() + " " + n1.line.toString());
            }
            else {
                // System.out.println("        if0 " + b.value.toString() + " " + n1.line.toString());
            }
            Vector<String> tmp_vec = new Vector<>();
            tmp_vec.add(b.value.toString());
            use.put(n1.node_id, tmp_vec);
        }


        ;

        return s;
    }

    public String graph_visit(VGoto g, String s)
        throws java.lang.Throwable
    {
        ;

        return s;
    }

    public String graph_visit(VReturn r, String s)
        throws java.lang.Throwable
    {

        if(r !=  null && r.value != null && !r.value.getClass().toString().equals("class cs132.vapor.ast.VLitInt")) {
            GNode n1 = flow_graph.new_node(r);
            if(last_node != null) {
                boolean worked = flow_graph.add_edge(last_node, n1);
                // System.out.println("adding edge from " + last_node.node_id + " to " + n1.node_id);
                if(!worked) {
                    System.out.println("shit didnt work g");
                }
            }
            else {
                System.out.println("last node was null");
            }
            last_node = n1;

            String c = r.value.getClass().toString();
            if(!c.equals("class cs132.vapor.ast.VLitInt") && c != "") {
                // System.out.println("        return " + r.value.toString() + " " + n1.line.toString());
                Vector<String> tmp_vec = new Vector<>();
                tmp_vec.add(r.value.toString());
                use.put(n1.node_id, tmp_vec);
            }
        }
        

        return s;
    }

}