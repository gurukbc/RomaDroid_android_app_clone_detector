package RomaDroid;

import java.io.IOException;
import java.lang.Thread.State;
import java.util.Timer;
import java.util.TimerTask;

public class Get_Xml extends Get_FileList {
	
	String [] paths = super.get_FileList("D:\\Kbc_NRF_ApiVector\\androZoo\\ben\\");
	//String [] compare = super.get_FileList("D:\\Kbc_OSS_Tool\\apk_all_original_decompile");
	String file_path = "D:\\Kbc_NRF_ApiVector\\androZoo\\ben\\";
	int count = super.count;
	boolean race_condition[] = new boolean[count + 1];

	public void get_Xml() throws InterruptedException {
		
		final Object lock = new Object();
		int i = 0;
		
		for (int j = 0; j < count+1; j++) {
			race_condition[j] = false;
		}
		
		synchronized(this) {
			do {
				
				int exit;
				
				/*
				String [] tmp = new String[paths.length];
				int ro = 0;
				int tmpI = 0;
				
				for(int k = 0; k < paths.length; k++) {
					for(int j = 0; j < compare.length; j++) {
						if(paths[k].contains(compare[j])) {
							ro = 1;
						}
					}
					if(ro == 0) {
						tmp[tmpI] = paths[k];
						tmpI++;
					}
					ro = 0;
				}
				*/
				
				try {
					String cmd = "cmd /c java -jar D:\\Kbc_OSS_Tool\\apktool_2.3.3.jar d " +
							this.file_path +paths[i] + " -o D:\\Kbc_NRF_ApiVector\\androZoo\\ben_decompile\\" + paths[i];	
					System.out.println(cmd);
					Process p = Runtime.getRuntime().exec(cmd);
					exit = p.waitFor();
					race_condition[i] = true;
					i++;				
				
						/*
					else {
						i++;
					} */
				}catch(Exception e) {
					System.out.println("error");
					continue;
				}
				
				
			} while(end_Check());
		}
		
	}
	
	public int race_Check() {
		int result = 0;
		for(int i = 0; i < count; i++) {
			if(race_condition[i]) {continue;}
			else {
				result = i;
				break;
			}
		}
		return result;
	}
	
	public boolean end_Check() {
		for(int i = 0; i < count; i++) {
			if(race_condition[i]) {continue;}
			else { return true;}
		}
		return false;
	}
	
	public static void main(String[] args) throws InterruptedException {
		Get_Xml excute = new Get_Xml();
		excute.get_Xml();
	}

}
