package t_invariante_test;


import java.util.regex.Matcher;
import java.util.regex.Pattern;



import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;


import monitor.RedDePetri;

public class TinvarianteTester {

	private String logToTest;
	private int[][] tinvariantes; 
	
	public TinvarianteTester() {
		this.tinvariantes=RedDePetri.getInstance().getTInv();
	}
	
	public void setLogToTest(String log){
		this.logToTest = log;
	}

	
    @Test
    public void testSimpleTrue() {
        this.logToTest= "1233";
        assertTrue(test());
        this.logToTest= "0";
        assertFalse(test());
        this.logToTest = "29 Kasdkf 2300 Kdsdf";
        assertTrue(test());
        this.logToTest = "99900234";
        assertTrue(test());
    }

	
	public boolean test() {
		Pattern pattern = Pattern.compile("\\d{3}");
        Matcher matcher = pattern.matcher(logToTest);
        if (matcher.find()){
            return true;
        }
        return false;
		
	}

}
