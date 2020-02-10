package ca.billy;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import org.apache.bcel.classfile.JavaClass;
import org.apache.bcel.generic.ClassGen;

import ca.billy.line.LineProcessor;

/**
 * For now we handle only one file always compiled under the same class name ("MAIN")
 * 
 * @author cedric.bilodeau
 */
public class FileCompiler {

    private List<ClassGen> classGens = new ArrayList<>();

    public void compileFile(String filePath) {
        Log.startTimer();
        LineProcessor lineProcessor = new LineProcessor();

        try (Stream<String> stream = Files.lines(Paths.get(filePath))) {
            stream.forEach(lineProcessor::process);

            classGens.add(lineProcessor.build());
        } catch (IOException e) {
            throw new BillyException("An I/O error occurs opening the file : " + filePath, e);
        }
        Log.printTimer("File " + filePath + " compiled in ");
    }

    public void writeByteCode() {
        JavaClass[] javaClass = new JavaClass[classGens.size()];
        for (int i = 0; i < classGens.size(); ++i) {
            javaClass[i] = classGens.get(i).getJavaClass();
        }

        Verifier.verify(javaClass);

        for (JavaClass jc : javaClass) {
            try {
                jc.dump(jc.getClassName() + ".class");
            } catch (IOException e) {
                throw new BillyException("Unable to write bytecode for class : " + jc.getClassName(), e);
            }
        }

        Log.end();
    }

}
