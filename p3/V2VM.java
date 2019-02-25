import cs132.util.ProblemException;
import cs132.vapor.parser.VaporParser;
import cs132.vapor.ast.VaporProgram;
import cs132.vapor.ast.VBuiltIn.Op;

import java.io.*;   // InputStreamReader, IOException, PrintStream

/*
    see build script
    File has to be compiled using class path to include vapor-parser.jar
    CLASSPATH=$VAPOR_P
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

        // figure out how to traverse the tree?
        System.out.println(prog);

        return;
    }

    public static VaporProgram parseVapor(InputStream in, PrintStream err)
        throws IOException
    {
        boolean allowLocals = true;
        String[] registers = null;
        boolean allowStack = false;
        VaporProgram tree;
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

}

