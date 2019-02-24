import cs132.util.ProblemException;
import cs132.vapor.parser.VaporParser;
import cs132.vapor.ast.VaporProgram;
import cs132.vapor.ast.VBuiltIn.Op;

import java.io.InputStreamReader;
import java.io.IOException;
import java.io.PrintStream;

class V2VM
{
    public static void main(String[] args)
    {
        System.out.println("hi");
    }

    public static VaporProgram parseVapor(InputStream in, PrintStream err) throws IOException
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

/*

wtf

does not exist?

import cs132.util.SourcePos;
import cs132.util.ProblemException;
import cs132.vapor.parser.VaporParser;


*/


/*

Your main file should be called V2VM.java, and if P.vapor contains a syntactically correct Vapor program, then

    $ java V2VM < P.vapor > P.vaporm

creates a Vapor-M program P.vaporm with the same behavior as P.vapor

A Vapor-M program p.vaporm can be run using the Vapor interpreter vapor.jar as follows:

    $ java -jar vapor.jar run -mips p.vaporm

*/
