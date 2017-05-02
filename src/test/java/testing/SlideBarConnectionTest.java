package testing;

import com.github.slidekb.back.Arduino;
import com.github.slidekb.back.PortManager;

/**
 * Created by JackSec on 4/27/2017.
 */
public class SlideBarConnectionTest {
    public static void main(String[] args) {
        PortManager pm = new PortManager();
        pm.findAndConnect();
        for (Arduino a : pm.getArduinos().values()) {
            a.getID();
            a.getPortName();
            a.writeUntilComplete(512);
            a.writeUntilComplete(100);
            a.writeUntilComplete(1000);
        }
        for (Arduino a : pm.getArduinos().values()) {
            a.writeUntilComplete(512);
        }
        for (Arduino a : pm.getArduinos().values()) {
            a.writeUntilComplete(100);
        }
        for (Arduino a : pm.getArduinos().values()) {
            a.writeUntilComplete(1000);
        }
    }
}
