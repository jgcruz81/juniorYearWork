import java.awt.EventQueue;
import javax.swing.JFileChooser.*;
import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;

import java.awt.BorderLayout;
import java.awt.Component;

import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.*;
import java.net.*;
import java.io.*;
import java.util.*;

public class wtf {

	public static void main(String[] args) throws IOException {
		File f  = new File("/Users/juan/eclipse-workspace/Hello/Juan Cruz/poop.docx");
		FileInputStream fis = null;
		long size = 0;
		String path = f.getPath();
		try {
			fis = new FileInputStream(path);
			size = fis.available();
		}catch(Exception Ex) {
			System.out.println("File does not exist");
		}
		
		byte[] fileContent = Files.readAllBytes(f.toPath());
		
		File f1  = new File("/Users/juan/Downloads/poop.docx");
		FileInputStream fiz = null;
		long size1 = 0;
		String path1 = f1.getPath();
		try {
			fis = new FileInputStream(path1);
			size = fis.available();
		}catch(Exception Ex) {
			System.out.println("File does not exist");
		}
		
		byte[] fileContent1 = Files.readAllBytes(f1.toPath());
		int z  = 0;
		for(int x = 0; x < 6083; x++) {
			if(fileContent[x] != fileContent1[x]) {
				System.out.println(x);z++;
				
			}
		}
		System.out.println(z);
	}

}
