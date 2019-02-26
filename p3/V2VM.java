import cs132.util.ProblemException;
import cs132.vapor.parser.VaporParser;
import cs132.vapor.ast.VaporProgram;
import cs132.vapor.ast.VBuiltIn.Op;

import cs132.util.*;
import cs132.vapor.ast.*;
import cs132.vapor.parser.*;

import java.io.*;

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

        info_printer(prog,v_instr_visitor);

        return;
    }

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

    public static void info_printer(VaporProgram  prog, VisitorPrinter vprinter)
        throws Throwable
    {
        VFunction[] fns = prog.functions;       // All the functions in this program
        VDataSegment[] dat = prog.dataSegments; // All the data segments in this program
        String[] reg = prog.registers;          // registers allowed to use
        String tab = "\t";

        // PROGRAM: VaporProgram
        // System.out.println(prog);

        // FUNCTION: Vfunction
        // System.out.println(fns);

        for (VFunction f : fns)
        {
            VVarRef.Local[] pms = f.params;   // function parameters
            String[] vs = f.vars;             // function local variables
            VCodeLabel[] lbl = f.labels;      // code labels
            VInstr[] bdy = f.body;            // instructions

            System.out.println("function name: " + f.ident);    // see VTarget
            System.out.println("function num: " + f.index);
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

        // DATA SEGMENTS: VDataSegment
        System.out.println(dat);

        for (VDataSegment d : dat)
        {
            System.out.println(d);
        }

        System.out.println(reg);
        for (String s : reg)
        {
            System.out.println(s);
        }
    }

    public static void go_visit(VisitorPrinter vprinter,VInstr vi)
        throws Throwable
    {
        String classy = vi.getClass().toString();
        System.out.println("\t" + classy);
        if(classy.equals("class cs132.vapor.ast.VAssign"))
        {
            vprinter.visit((cs132.vapor.ast.VAssign)vi);
        }
        else if(classy.equals("class cs132.vapor.ast.VBranch"))
        {
            vprinter.visit((cs132.vapor.ast.VBranch)vi);
        }
        else if(classy.equals("class cs132.vapor.ast.VBuiltIn"))
        {
            vprinter.visit((cs132.vapor.ast.VBuiltIn)vi);
        }
        else if(classy.equals("class cs132.vapor.ast.VCall"))
        {
            vprinter.visit((cs132.vapor.ast.VCall)vi);
        }
        else if(classy.equals("class cs132.vapor.ast.VGoto"))
        {
            vprinter.visit((cs132.vapor.ast.VGoto)vi);
        }
        else if(classy.equals("class cs132.vapor.ast.VMemRead"))
        {
            vprinter.visit((cs132.vapor.ast.VMemRead)vi);
        }
        else if(classy.equals("class cs132.vapor.ast.VMemWrite"))
        {
            vprinter.visit((cs132.vapor.ast.VMemWrite)vi);
        }
        else if(classy.equals("class cs132.vapor.ast.VReturn"))
        {
            vprinter.visit((cs132.vapor.ast.VReturn)vi);
        }
        else
        {
            System.out.println("error: '" + vi.getClass().toString() + "'");
        }
    }
}

