package ca.billy;

import java.util.ArrayList;
import java.util.List;

/**
 * The main class for the billy compiler
 * 
 * @author cedric.bilodeau
 *
 */
public class BillyCompiler {
    
    private static final String VERSION = "0.0.1";

    /**
     * The main method for the compiler.
     * 
     * options <br>
     * -h : help <br>
     * -v : verbose <br>
     * -V : verbose and verify <br>
     * 
     * @param args The options and file args
     * @throws BillyException
     */
    public static void main(String[] args) throws BillyException {
        List<String> files = parseArgs(args);
        
        if (!files.isEmpty()) {
            Log.log("Version " + VERSION);
            FileCompiler fileCompiler = new FileCompiler();
            files.forEach(fileCompiler::compileFile);
            fileCompiler.writeByteCode();
        } else {
            printHelp();
        }
    }

    private static List<String> parseArgs(String[] args) {
        List<String> files = new ArrayList<>();
        int i;
        for (i = 0; i < args.length; ++i) {
            if (args[i].equals(Const.VERBOSE)) {
                Log.enable();
            } else if (args[i].equals(Const.VERIFY)) {
                Verifier.enable();
            } else if (args[i].equals(Const.HELP)){
                files.clear();
                break;
            } else {
                files.add(args[i]);
            }
        }
                       
        return files;
    }

    private static void printHelp() {
        Log.enable();
        Log.log("Version " + VERSION);
        Log.log("Usage : java -jar BillyCompiler.jar billyFile [billyFile] [options]");
        Log.log("options:");
        Log.log("\t-h : help");
        Log.log("\t-v : verbose");
        Log.log("\t-V : verbose and verify the generated bytecode with JustIce by Enver Haase, (C) 2001-2002.");   
    }
}
