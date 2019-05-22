package RomaDroid;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.BufferedReader;


public class Get_BKFeature extends Get_FileList {
	
	public static int option;
	String target;
	int space_count = 0;
	int line_count = 0;
	
	Get_BKFeature() {
		target = "";
	}
	
	public boolean isIntent(String line) {
		Pattern p = Pattern.compile("<intent-filter>");
		Matcher m = p.matcher(line);
		if(m.find()){
			return true;
		}
		return false;
	}
	
	public boolean isExported(String line) {
		Pattern p2 = Pattern.compile("exported=\"true\"");
		Matcher m2 = p2.matcher(line);
		
		if(m2.find()) {
			return true;
		}
		return false;
	}
	
	public int intentIndex(String[] line, int index) {
		if(index - 1 <= 0) { return 0;}
		Pattern p1 = Pattern.compile("name=");
		/*
		Matcher m1;
		if (index > 0) {
			m1 = p1.matcher(line[index-1]);
		}
		else {
			m1 = p1.matcher(line[index]);
			return index;
		}*/
		Matcher m1 = p1.matcher(line[index - 1]);
		if(m1.find() && !line[index-1].contains("meta-data") && !line[index-1].contains("android.intent") &&
			((line[index-1].contains("activity") || line[index-1].contains("service") || line[index-1].contains("receiver")))) {
			
			return index;
		}
		return intentIndex(line, index-1);
	}
	
	public boolean isOverlap(ArrayList<String> list, String tar, int index) {
		for(int i = 0; i < list.size(); i++) {
			if(list.get(i) != null) {
				if((list.get(i)).equals(tar)) {
					return true;
				}
			}
		}
		return false;
	}
	
	public boolean isCustom(String line) {
		Pattern p1 = Pattern.compile("exported=\"false\"");
		Matcher m1 = p1.matcher(line);
		
		if(m1.find()) {
			return true;
		}
		return false;
	}
	
	public String name_Feature(String line) {
		//System.out.println(line);
		
		String result = "";
		Pattern p = Pattern.compile("android:name=\".+[A-Za-z]\"");
		Pattern p2 = Pattern.compile("\".+[A-Za-z]\"");
		Pattern p3 = Pattern.compile("[A-Z]");
		//Pattern p4 = Pattern.compile("[a-z]");
		Matcher m = p.matcher(line);
		
		if(m.find()) {
			result = m.group(0);
		}
		
		Matcher m2 = p2.matcher(result);
		
		if(m2.find()) {
			result = m2.group(0);
		}
		
		if (result.contains(".android.")) {
			String [] ret = result.split("\\.");
			ret[ret.length - 1] = "\"" + ret[ret.length - 1];
			return ret[ret.length - 1];
		}
		
		String[] ret = result.split("\\b+android");
		String[] filter;
		String[] filter2;
		
		
		//System.out.println("=========");
		//System.out.println(result);
		
		/*
		filter2 = result.split("\\.");
		Matcher m4 = p4.matcher(filter2[0]);
		*/
		//System.out.println(ret.length);
		if(result.contains("android.") && ret.length > 1) {
			if(result.contains("android.permission")) {
				result = ret[0];
			}
			else {
				result = ret[1];
				filter = result.split("\\.");
				for(int i = 0; i < filter.length; i++) {
					Matcher m3 = p3.matcher(filter[i]);
					if(m3.find()) {
						result = filter[i];
						result = "\"" + result;
					}
				}
			}
		}
		
		else {
			result = ret[0];
		}
		
		
		
		filter2 = result.split("\\.");		
		
		if(filter2.length > 1) {
			for(int i = 0; i < filter2.length; i++) {
				Matcher m3 = p3.matcher(filter2[i]);
				if(m3.find()) {
					result = filter2[i];
					result = "\"" + result;
				}
			}
		}		

		String[] last = result.split("\\.");		
		result = last[last.length-1];
		return result;
	}
	
	public String file_open(String xml_name) throws IOException {
		
		ArrayList<String> Intent_Feature = new ArrayList<>();
		String line = "";
		String main_Name = "";
		String [] all_line;
		space_count = 0;
		line_count = 0;
		int tree_count = 0;
		int line_count = 0;
		int i = 0;
		target = "";
		int option = 0;
		
		try {		
			File xml_single = new File(xml_name);
			File tmp_line = new File(xml_name);
			File real_line = new File(xml_name);
			FileReader xmlReader = new FileReader(xml_single);
			FileReader tmpReader = new FileReader(tmp_line);
			FileReader realReader = new FileReader(real_line);
			BufferedReader tmpBuf = new BufferedReader(tmpReader);
			BufferedReader realBuf = new BufferedReader(realReader);
			int singleCh = 0;
			
			
			//line count read
			while((line = tmpBuf.readLine()) != null) {
				line_count++;
				Intent_Feature.add("P");
				Intent_Feature.add("P");
			}
			
			line = "";
			all_line = new String[line_count+1];
			
			//real line save
			while((line = realBuf.readLine()) != null) {
				all_line[i] = line;
				if(isIntent(all_line[i]) && i > 1) {
					
					int tmpIndex = intentIndex(all_line, i);
					if (tmpIndex == 0) {return null;}
					String feature = name_Feature(all_line[tmpIndex-1]);
		
					if(!isOverlap(Intent_Feature,feature,tmpIndex) && !isCustom(all_line[tmpIndex-1])) {
						Intent_Feature.add(tmpIndex-1, feature);
					}
				}
				else if(isExported(all_line[i]) && i > 1) {
					int tmpIndex = i;
					String feature = name_Feature(all_line[i]);
					if(!isOverlap(Intent_Feature,feature,tmpIndex)) {
						Intent_Feature.add(tmpIndex, feature);
					}	
				}
				else {
				
				}
				i++;
			}			
			i = 0;
					
			//single read
			while((singleCh = xmlReader.read()) != -1) {
				//System.out.print((char)singleCh);
			
				// space
				if((int)singleCh == 32) {
					//System.out.print((int)singleCh);
					space_count++;
				}
				
				// line change
				if((int)singleCh == 10) {
					//System.out.print((int)singleCh);
					String oneTree = "";
					line_count++;
					target += space_count;
					//oneTree += space_count + "BN";
					space_count = 0;
					//target += getSHA256(oneTree);
					target += "B";
					target += "N";
					
					
					tree_count += 3;
					i++;
					
					// && !Intent_Feature.get(i).contains("MainActivity")
					if(Intent_Feature.get(i) != "P" && !Intent_Feature.get(i).contains("MainActivity")) {
						String component = target.substring(target.length()-3, target.length());
						component += Intent_Feature.get(i);
						
					//	System.out.println(component);
						
						component = component.trim();
						component = getSHA256(component);
						target += component;
						option ++;
					}
				}							
			}
						
		}catch(FileNotFoundException e) {	
			
		}
		
		//target = getSHA256(target);
		
		ToolTest.topAll += option;
		if(ToolTest.maxCom < option) {
			ToolTest.maxCom = option;
		}
		if(ToolTest.minCom > option) {
			ToolTest.minCom = option;
		}
		
		if(option <= 1) {
			ToolTest.selectCount++;
		}
		
		
		//System.out.println(option);
		//System.out.println("============" + option);
		
		
		if(option < 0) {
			target = getSHA256(target);
			return null;
		}


		return target;
		
	}
	
}
