import cs132.util.ProblemException;
import cs132.vapor.parser.VaporParser;
import cs132.vapor.ast.VaporProgram;
import cs132.vapor.ast.VBuiltIn.Op;

import cs132.util.*;
import cs132.vapor.ast.*;
import cs132.vapor.parser.*;

import java.io.*;
import java.util.*;

import Graph.*;

/*
    see build script
    File has to be compiled using class path to include vapor-parser.jar and this folder
    CLASSPATH=$VAPOR_P:/p3
    javac -classpath ${CLASSPATH} V2VM.java
*/

class V2VM
{
    public static void main(String[] args)
        throws IOException, Throwable
    {
        InputStream in = System.in;
        PrintStream err = System.err;
        VaporProgram prog = parseVapor(in,err);  // should be the tree

        // figure out how to traverse the vapor program tree?
        VisitorPrinter v_instr_visitor = new VisitorPrinter();
        VisitorData v_data_visitor = new VisitorData();
        VisitorGraphBuilder v_graph = new VisitorGraphBuilder();

        // graph_tester();

        // info_printer(prog,v_instr_visitor);

        data_grab(prog,v_data_visitor);

        return;
    }
    //----------------------------------------

    public static VaporProgram parseVapor(InputStream in, PrintStream err)
        throws IOException
    {
        VaporProgram tree;
        boolean allowLocals = true;
        boolean allowStack = false;
        String[] registers = null;
        Op[] ops = {
            Op.Add, Op.Sub, Op.MulS, Op.Eq, Op.Lt, Op.LtS,
            Op.PrintIntS, Op.HeapAllocZ, Op.Error,
        };

        try
        {
            tree = VaporParser.run(new InputStreamReader(in), 1, 1,
            java.util.Arrays.asList(ops),
            allowLocals, registers, allowStack);
        }
        catch (ProblemException ex)
        {
            err.println(ex.getMessage());
            return null;
        }

        return tree;
    }
    //----------------------------------------

    public static void go_visit(VInstr.Visitor v,VInstr vi)
        throws Throwable
    {
        String classy = vi.getClass().toString();
        if(classy.equals("class cs132.vapor.ast.VAssign"))
        {
            v.visit((cs132.vapor.ast.VAssign)vi);
        }
        else if(classy.equals("class cs132.vapor.ast.VBranch"))
        {
            v.visit((cs132.vapor.ast.VBranch)vi);
        }
        else if(classy.equals("class cs132.vapor.ast.VBuiltIn"))
        {
            v.visit((cs132.vapor.ast.VBuiltIn)vi);
        }
        else if(classy.equals("class cs132.vapor.ast.VCall"))
        {
            v.visit((cs132.vapor.ast.VCall)vi);
        }
        else if(classy.equals("class cs132.vapor.ast.VGoto"))
        {
            v.visit((cs132.vapor.ast.VGoto)vi);
        }
        else if(classy.equals("class cs132.vapor.ast.VMemRead"))
        {
            v.visit((cs132.vapor.ast.VMemRead)vi);
        }
        else if(classy.equals("class cs132.vapor.ast.VMemWrite"))
        {
            v.visit((cs132.vapor.ast.VMemWrite)vi);
        }
        else if(classy.equals("class cs132.vapor.ast.VReturn"))
        {
            v.visit((cs132.vapor.ast.VReturn)vi);
        }
        else
        {
            System.out.println("error: '" + vi.getClass().toString() + "'");
        }
    }
    //----------------------------------------

    public static void info_printer(VaporProgram  prog, VisitorPrinter vprinter)
        throws Throwable
    {
        VFunction[] fns = prog.functions;       // All the functions in this program
        VDataSegment[] dat = prog.dataSegments; // All the data segments in this program
        String[] reg = prog.registers;          // registers allowed to use
        String tab = "\t";

        // Data Segment
        System.out.println("Data Segments:\n");
        for (VDataSegment d : dat)
        {
            VOperand.Static[] vals = d.values;

            // VDataSegment
            System.out.println("data name: " + d.ident);
            System.out.println("data idx: " + d.index);
            System.out.println("mutable: " + d.mutable);
            for(VOperand v : vals)
            {
                System.out.println("val: " + v);
            }
            System.out.println("");
        }

        // FUNCTION: Vfunction
        System.out.println("Functions:\n");
        for (VFunction f : fns)
        {
            VVarRef.Local[] pms = f.params;   // function parameters
            String[] vs = f.vars;             // function local variables
            VCodeLabel[] lbl = f.labels;      // code labels
            VInstr[] bdy = f.body;            // instructions

            System.out.println("function name: " + f.ident);    // see VTarget
            System.out.println("function idx: " + f.index);
            System.out.println("function params:");
            for (VVarRef.Local p : pms)
            {
                System.out.println(tab + p.ident);
            }
            System.out.println("function local vars:");
            for (String v : vs)
            {
                System.out.println(tab + v);
            }
            System.out.println("function code labels:");
            for (VCodeLabel l : lbl)
            {
                System.out.println(tab + l.ident);
            }
            System.out.println("function instructions: (print visitor)");
            for (VInstr vi : bdy)
            {
                go_visit(vprinter,vi);
            }
            System.out.println("");
        }

        // Resgisters
        System.out.println(reg);
        for (String s : reg)
        {
            System.out.println(s);
        }
        System.out.println("");
    }
    //----------------------------------------

    public static void data_grab(VaporProgram  prog, VisitorData vdatav)
        throws Throwable
    {
        VFunction[] fns = prog.functions;       // All the functions in this program
        VDataSegment[] dat = prog.dataSegments; // All the data segments in this program
        String[] reg = prog.registers;          // registers allowed to use

        HashMap<String,ArrayList<String>> vtabs = new HashMap<String,ArrayList<String>>();

        // DATA SEGMENT
        for (VDataSegment d : dat)
        {
            VOperand.Static[] vals = d.values;

            ArrayList<String> arlst = new ArrayList<String>();

            for(VOperand v : vals) // add to the map
            {
                arlst.add(v.toString());
            }

            if(!arlst.isEmpty())
            {
                vtabs.put(d.ident,arlst);
            }
        }

        // print vtable
        for (Map.Entry<String,ArrayList<String>> entry : vtabs.entrySet())
        {
            System.out.println("const " + entry.getKey());
            ArrayList<String> l = entry.getValue();
            for( String s : l )
            {
                System.out.println("  " + s);
            }
            System.out.println("");
        }

        // FUNCTION: Vfunction
        for (VFunction f : fns)
        {
            String tab = "\t";
            VVarRef.Local[] pms = f.params;   // function parameters
            String[] vs = f.vars;             // function local variables
            VCodeLabel[] lbl = f.labels;      // code labels
            VInstr[] bdy = f.body;            // instructions

            ArrayList<String> diflst = new ArrayList<String>();
            ArrayList<String> loclst = new ArrayList<String>();

            System.out.println(f.ident);
            System.out.println("Params: ");
            for (VVarRef.Local p : pms)
            {
                System.out.println(tab + p.ident);
                diflst.add(p.ident);
            }
            System.out.println("Locals:");
            for (String v : vs)
            {
                System.out.println(tab + v);
                loclst.add(v);
            }
            System.out.println("Labels:");
            for (VCodeLabel l : lbl)
            {
                System.out.println(tab + l.ident);
            }
            System.out.println("Instructions");
            for (VInstr vi : bdy)
            {
                go_visit(vdatav,vi);
            }
            System.out.println("");


            // calculate the in/out/locals

            int in,out,local;
            int argno;
            in = 0; out = 0; local = 0;
            argno = 0;

            argno = diflst.size();
            local = loclst.size();

            System.out.println("func " + f.ident + " [in " + Integer.toString(in)   // function interface
                                + ", out " + Integer.toString(out) + ", local "
                                + Integer.toString(local) + "]");

            if(local >= 1) // save on enter
            {
                for(int i = 0; i < local; i++)
                {
                    System.out.println("  " + "local[" + Integer.toString(i) + "] = $s" + Integer.toString(i));
                }
            }

            if(argno >= 1) // get the parameters
            {
                for(int i = 0; i < argno; i++)
                {
                    System.out.println("  " + "$t" + Integer.toString(i) + " = $a" + Integer.toString(i));
                }
            }

            if(local >= 1) // restore before return
            {
                for(int i = 0; i < local; i++)
                {
                    System.out.println("  " + "$s" + Integer.toString(i) + " = local[" + Integer.toString(i) + "]");
                }
            }

            System.out.println("");
        }

        System.out.println("");
    }

    //----------------------------------------

    public static void graph_tester()
    {
        System.out.println("====================== Testing GRAPH ====================== Testing GRAPH ====================== Testing GRAPH ======================");
        Graph g = new Graph();
        GNode n_1 = g.new_node();
        g.show();
        GNode n_2 = g.new_node();
        g.show();
        g.add_edge(n_1, n_2);
        System.out.println("~~add edge from 1 and 2~~");
        // g.show();

        // if(n_1.goes_to(n_2)) {
        //     System.out.println("supposed to be true 1");
        // }
        // else if(n_1.comes_from(n_2)) {
        //     System.out.println("not supposed to be true 2");
        // }

        // if(n_2.goes_to(n_1)) {
        //     System.out.println("not supposed to be true");
        // }
        // else if(n_2.comes_from(n_1)) {
        //     System.out.println("supposed to be true 2");
        // }

        for(GNode j: g.nodes()) {
            System.out.println("Inside " + j.node_id);
            for(GNode k: j.succ) {
                System.out.println("    Head: " + g.head.node_id + ", " + j.node_id + "'s successor is " + k.node_id);
            }
        }

        GNode n_3 = g.new_node();
        g.add_edge(n_2, n_3);
        System.out.println("~~add edge from 2 to 3~~");

        for(GNode j: g.nodes()) {
            System.out.println("Inside " + j.node_id);
            for(GNode k: j.succ) {
                System.out.println("    Head: " + g.head.node_id + ", " + j.node_id + "'s successor is " + k.node_id);
            }
        }
        // g.show();

        g.rm_edge(n_1, n_2);
        System.out.println("~~rm edge between 1 and 2~~");
        for(GNode j: g.nodes()) {
            System.out.println("Inside " + j.node_id);
            for(GNode k: j.succ) {
                System.out.println("    Head: " + g.head.node_id + ", " + j.node_id + "'s successor is " + k.node_id);
            }
        }
        // g.show();

        System.out.println("~~add edge to 1 and 3~~");
        g.add_edge(n_1, n_3);
        for(GNode j: g.nodes()) {
            System.out.println("Inside " + j.node_id);
            for(GNode k: j.succ) {
                System.out.println("    Head: " + g.head.node_id + ", " + j.node_id + "'s successor is " + k.node_id);
            }
        }
        g.show();

        g.add_edge(n_2, n_1);
        System.out.println("~~add edge to 2 and 1~~");
        for(GNode j: g.nodes()) {
            System.out.println("Inside " + j.node_id);
            for(GNode k: j.succ) {
                System.out.println("    Head: " + g.head.node_id + ", " + j.node_id + "'s successor is " + k.node_id);
            }
        }
        g.show();

        GNode n_4 = g.new_node();
        g.add_edge(n_4, n_2);
        System.out.println("~~add edge to 4 and 2~~");
        for(GNode j: g.nodes()) {
            System.out.println("Inside " + j.node_id);
            for(GNode k: j.succ) {
                System.out.println("    Head: " + g.head.node_id + ", " + j.node_id + "'s successor is " + k.node_id);
            }
        }
        g.show();

        g.add_edge(n_1, n_4);
        System.out.println("~~add edge to 1 and 4~~");
        for(GNode j: g.nodes()) {
            System.out.println("Inside " + j.node_id);
            for(GNode k: j.succ) {
                System.out.println("    Head: " + g.head.node_id + ", " + j.node_id + "'s successor is " + k.node_id);
            }
        }
        g.show();

        System.out.println("");
    }

}



