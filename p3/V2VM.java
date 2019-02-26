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
        throws IOException
    {
        InputStream in = System.in;
        PrintStream err = System.err;
        VaporProgram prog = parseVapor(in,err);  // should be the tree

        // figure out how to traverse the vapor program tree?
        VisitorPrinter v_instr_visitor = new VisitorPrinter();

        info_printer(prog);

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

    public static void info_printer(VaporProgram  prog)
    {
        VFunction[] fns = prog.functions;       // All the functions in this program
        VDataSegment[] dat = prog.dataSegments; // All the data segments in this program
        String[] reg = prog.registers;          // registers allowed to use

        // PROGRAM: VaporProgram
        // System.out.println(prog);

        // FUNCTION: Vfunction
        // System.out.println(fns);

        for (VFunction f : fns)
        {
            VVarRef.Local[] pms = f.params; // function parameters
            String[] vs = f.vars;             // function local variables

            System.out.println("function name: " + f);
            System.out.println("function num: " + f.index);
            System.out.println("function params:");
            for (VVarRef.Local p : pms)
            {
                System.out.println(p.toString());
            }
            System.out.println("function local vars:");
            for (String v : vs)
            {
                System.out.println(v);
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

}

