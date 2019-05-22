package RomaDroid;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Get_Component extends Get_FileList {
	
	String [] files = super.get_FileList("A");
	
	public static void main(String[] args) {
	try {  

		BufferedReader br = new BufferedReader(new FileReader("item.txt"));

		String str;
		
		Pattern p = Pattern.compile("(^[0-9]*$)");
		Matcher m = p.matcher("input");

		while ((str = br.readLine()) != null) {


		}
		}catch(Exception e) {
			e.printStackTrace();
		}
	
	}
	
}