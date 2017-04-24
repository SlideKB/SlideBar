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

package back;

import java.io.File;
import java.io.FileInputStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import processes.Scroller;
import processes.TypeWriter;

public class ProcessManager {
	
	private ArrayList<Process> proci = new ArrayList<Process>();
	
	public ProcessManager(){
		
	}
	
	
	/**
	 * calls listFilesForFolder("\\src\\plugins").
	 * adds internal process into the list and instances each.
	 * @return true if successful.
	 */
	protected boolean loadProcesses(){
		File jarDir = new File(ClassLoader.getSystemClassLoader().getResource(".").getPath());
		List<String> classNames = new ArrayList<String>();
		ZipInputStream zip = null;
		try {
			zip = new ZipInputStream(new FileInputStream(jarDir.getPath() + "/SlideBar.jar"));

			System.out.println("could not fine jar file");
			for (ZipEntry entry = zip.getNextEntry(); entry != null; entry = zip.getNextEntry()) {
                if (entry.getName().startsWith("plugins/")){
					if (!entry.isDirectory() && entry.getName().endsWith(".class")) {
						String className = entry.getName().replace('/', '.');
						classNames.add(className.substring(0, className.length() - ".class".length()));
					}
				} else {

				}

            }
		} catch (Exception e) {
			System.out.println("could not find jar file");
		}
		System.out.println(classNames);
		jarDir = new File(ClassLoader.getSystemClassLoader().getResource(".").getPath() + "\\plugins");
		File[] plugins = jarDir.listFiles();
		for (String theClassName : classNames){
			try {
				URL loadPath = jarDir.toURI().toURL();
				URL[] classUrl = new URL[]{loadPath};
				ClassLoader cl = new URLClassLoader(classUrl);
				if (!theClassName.contains("$")){
					Class loadedClass = cl.loadClass(theClassName);
					Process process = (Process)loadedClass.newInstance();
					proci.add(process);
				}
			} catch (Exception e){
				e.printStackTrace();
			}
		}
		if (plugins != null){
			for (File file : plugins) {
				if (file.isFile()) {
					try {
						URL loadPath = jarDir.toURI().toURL();
						URL[] classUrl = new URL[]{loadPath};
						ClassLoader cl = new URLClassLoader(classUrl);
						String ClassName = file.getName().substring(0, file.getName().length()-6);
						if (!ClassName.contains("$")){
							Class loadedClass = cl.loadClass("plugins." + ClassName);
							Process process = (Process)loadedClass.newInstance();
							proci.add(process);
						}
					} catch (Exception e){
						e.printStackTrace();
					}
				}
			}
		}
	    Process typeWriter = new TypeWriter();
	    Process scroller = new Scroller();
	    proci.add(typeWriter);
	    proci.add(scroller);
	    for (Process p : proci){
	    	System.out.println("Process: " + p.getLabelName());
	    	System.out.println(Arrays.toString(p.getProcessNames()));
			System.out.println("");
	    }
	    return true;
	}

	
	public ArrayList<Process> getProci(){
		return proci;
	}
	
	protected void removeProci(boolean RemoveAll){
		if (RemoveAll){
			proci.removeAll(proci);
		}
	}
}
