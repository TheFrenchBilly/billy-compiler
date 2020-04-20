package ca.billy.bcel.util.stackmap;

import org.apache.bcel.classfile.StackMap;
import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.Type;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import ca.billy.bcel.utils.Branch;
import ca.billy.bcel.utils.stackmap.StackMapBuilder;

public class StackMapBuilderTest {
    
    private StackMapBuilder stackMapBuilder;
    
    @Before
    public void setup() {
        stackMapBuilder = new StackMapBuilder(new ConstantPoolGen());
    }
    
    @Test
    public void testBuildWithoutInitalLocal() {
        stackMapBuilder.addFrame(createBranch(312));
        stackMapBuilder.addFrame(createBranch(12, Type.BOOLEAN));
        stackMapBuilder.addFrame(createBranch(12, Type.BOOLEAN));
        
        StackMap res = stackMapBuilder.build();
        
        Assert.assertEquals(2, res.getStackMap().length);       
        
        // append frame
        Assert.assertEquals(12, res.getStackMap()[0].getByteCodeOffset()); 
        Assert.assertEquals(1, res.getStackMap()[0].getTypesOfLocals().length);   
        
        // chop frame
        Assert.assertEquals(299, res.getStackMap()[1].getByteCodeOffset()); 
        Assert.assertEquals(0, res.getStackMap()[1].getTypesOfLocals().length);
    }

    @Test
    public void testBuildWithInitalLocal() {
        stackMapBuilder.addFrame(createBranch(78, Type.BOOLEAN));
        stackMapBuilder.addFrame(createBranch(79, Type.BOOLEAN, Type.STRING, Type.STRING));
        stackMapBuilder.addFrame(createBranch(21));
        
        StackMap res = stackMapBuilder.build(Type.FLOAT);
        
        Assert.assertEquals(3, res.getStackMap().length);   
        
        // chop frame
        Assert.assertEquals(21, res.getStackMap()[0].getByteCodeOffset()); 
        Assert.assertEquals(0, res.getStackMap()[0].getTypesOfLocals().length);
        
        // append frame
        Assert.assertEquals(56, res.getStackMap()[1].getByteCodeOffset()); 
        Assert.assertEquals(1, res.getStackMap()[1].getTypesOfLocals().length);   
        
        // append frame
        Assert.assertEquals(0, res.getStackMap()[2].getByteCodeOffset()); 
        Assert.assertEquals(2, res.getStackMap()[2].getTypesOfLocals().length);
    }
    
    private Branch createBranch(int targetPosition, Type... locals) {
        Branch branch = Mockito.mock(Branch.class);
        Mockito.when(branch.getLocals()).thenReturn(locals);
        Mockito.when(branch.getTargetPosition()).thenReturn(targetPosition);
        Mockito.when(branch.getStacks()).thenReturn(new Type[0]);
        return branch;
    }
}
