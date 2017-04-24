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

import static back.ActiveProcess.Kernel32.OpenProcess;
import static back.ActiveProcess.Kernel32.PROCESS_QUERY_INFORMATION;
import static back.ActiveProcess.Kernel32.PROCESS_VM_READ;
import static back.ActiveProcess.Psapi.GetModuleBaseNameW;
import static back.ActiveProcess.User32DLL.GetForegroundWindow;
import static back.ActiveProcess.User32DLL.GetWindowTextW;
import static back.ActiveProcess.User32DLL.GetWindowThreadProcessId;

import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.WinDef.HWND;
import com.sun.jna.ptr.PointerByReference;

public class ActiveProcess {

	private static final int MAX_TITLE_LENGTH = 1024;

	/**
	 * returns active process Window name.
	 * @return
	 */
	public static String getWindowTitle() {
		char[] buffer = new char[MAX_TITLE_LENGTH * 2];
		GetWindowTextW(GetForegroundWindow(), buffer, MAX_TITLE_LENGTH);
		return Native.toString(buffer);
	}

	/**
	 * return active process EXE
	 * @return
	 */
	public static String getProcess() {
		char[] buffer = new char[MAX_TITLE_LENGTH * 2];
		PointerByReference pointer = new PointerByReference();
		GetWindowThreadProcessId(GetForegroundWindow(), pointer);
		Pointer process = OpenProcess(PROCESS_QUERY_INFORMATION | PROCESS_VM_READ, false, pointer.getValue());
		GetModuleBaseNameW(process, null, buffer, MAX_TITLE_LENGTH);
		return Native.toString(buffer);
	}

	static class Psapi {
		static {
			Native.register("psapi");
		}

		public static native int GetModuleBaseNameW(Pointer hProcess, Pointer hmodule, char[] lpBaseName, int size);
	}

	static class Kernel32 {
		static {
			Native.register("kernel32");
		}
		public static int PROCESS_QUERY_INFORMATION = 0x0400;
		public static int PROCESS_VM_READ = 0x0010;

		public static native int GetLastError();

		public static native Pointer OpenProcess(int dwDesiredAccess, boolean bInheritHandle, Pointer pointer);
	}

	static class User32DLL {
		static {
			Native.register("user32");
		}

		public static native int GetWindowThreadProcessId(HWND hWnd, PointerByReference pref);

		public static native HWND GetForegroundWindow();

		public static native int GetWindowTextW(HWND hWnd, char[] lpString, int nMaxCount);
	}
}