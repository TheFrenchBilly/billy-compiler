package ca.billy;

import org.apache.bcel.Repository;
import org.apache.bcel.classfile.JavaClass;

public class Verifier {

    private static boolean enabled;

    public static void enable() {
        enabled = true;
        Log.enable();
    }

    public static void verify(JavaClass... javaClass) {
        if (enabled) {
            for (JavaClass jc : javaClass) {
                Repository.addClass(jc);
                Log.log("-------------");
                org.apache.bcel.verifier.Verifier.main(new String[] { jc.getClassName() });
                Log.log("-------------");
            }
        }
    }

}
