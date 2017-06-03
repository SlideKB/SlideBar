package unitTests;

import static org.junit.Assert.*;

import org.junit.Test;

import com.github.slidekb.back.Arduino;

public class ArduinoConnectionTest {

	@Test
	public void connectToArduinoTest1(){
		boolean sliderFound = false;
		for (int i = 1; i < 30; i++){
			try {
				Arduino a = new Arduino("COM"+i);
				a.initialize();
				if (a.getID() != null){
					sliderFound = true;
					System.out.println(a.getID());
				}
			} catch (Exception e){
				//do nothing
			}
			
		}
		assertTrue(sliderFound);
	}
}
