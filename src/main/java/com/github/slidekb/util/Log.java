package com.github.slidekb.util;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class Log {
	public static void logMessage(String message) {
		PrintWriter out;
		try {
			out = new PrintWriter(new FileWriter("output.txt", true), true);
			out.write(message + "\r\n");
			out.close();
		} catch (IOException e) {
			System.out.println("Logger->void()-> could not write to file");
		}
	}
}