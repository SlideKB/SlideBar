/**
 Copyright 2017 John Kester (Jack Kester)

 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
 */

package testing;

import jssc.*;

import java.io.IOException;

/**
 * Created by JackSB on 3/27/2017.
 */
public class jsscTest {
    public static void main(String[] args){
        SerialPort serialPort = new SerialPort("COM16");
        try {
            serialPort.openPort();//Open serial port
            serialPort.setParams(9600, 8, 1, 0);//Set params.
            System.out.println(serialPort.readString(10));
            serialPort.writeBytes("612]".getBytes());
            Thread.sleep(1000);
            serialPort.writeBytes("112]".getBytes());
            serialPort.closePort();//Close serial port
        }
        catch (SerialPortException ex) {
            System.out.println(ex);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

}
