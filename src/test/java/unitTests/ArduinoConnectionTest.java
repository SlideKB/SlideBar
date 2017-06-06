package unitTests;

import static org.junit.Assert.*;

import java.util.regex.Pattern;

import org.junit.Test;

import com.github.slidekb.back.Arduino;

import jssc.SerialPortList;

public class ArduinoConnectionTest {

	@Test
	public void connectToArduinoTest1(){
		boolean sliderFound = false;
		String[] portNames = SerialPortList.getPortNames();
		for (String port : portNames){
			try {
				Arduino a = new Arduino(port);
				a.initialize();
				if (a.getID() != null){
					sliderFound = true;
					System.out.println(a.getID());
				}
			} catch (Exception e){
				//do nothing
			}
			
		}
		portNames = SerialPortList.getPortNames("/dev/", Pattern.compile("tty.*"));
		for (String port : portNames){
			try {
				Arduino a = new Arduino(port);
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
