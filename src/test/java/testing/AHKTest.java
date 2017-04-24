package testing;

import com.sun.jna.Library;
import com.sun.jna.Native;
 import com.sun.jna.Pointer;
 import com.sun.jna.WString;

public class AHKTest {

    public interface autoHotKeyDll extends Library {
        public void ahkExec(WString s);
        public void ahkdll(WString s,WString o,WString p);
        public void addFile(WString s, int a);
        public void ahktextdll(WString s,WString o,WString p);
        public Pointer ahkFunction(WString f,WString p1,WString p2,WString p3,WString p4,WString p5,WString p6,WString p7,WString p8,WString p9,WString p10);
    }

    public static void main(String args[]) throws InterruptedException {
        Pointer pointer;
        System.out.println("running in " + System.getProperty("sun.arch.data.model"));

        System.out.println("Loading dll");
        autoHotKeyDll lib = (autoHotKeyDll) Native.loadLibrary("AutoHotkey.dll", autoHotKeyDll.class);


        System.out.println("initialisation");
        lib.ahktextdll(new WString(""),new WString(""),new WString(""));
//        Thread.sleep(100);
//        lib.addFile(new WString(System.getProperty("user.dir") + "\\lib.ahk"), 1);
//        Thread.sleep(1000);

//        System.out.println("function call");
//        System.out.println("return:" + lib.ahkFunction(new WString("function"),new WString(""),new WString(""),new WString(""),new WString(""),new WString(""),new WString(""),new WString(""),new WString(""),new WString(""),new WString("")).getString(0));
        Thread.sleep(1000);

        System.out.println("displaying cbBox");
        lib.ahkExec(new WString("Send {Right}"));
        Thread.sleep(1000);
    }
}