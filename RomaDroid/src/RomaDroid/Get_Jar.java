package RomaDroid;

import java.io.IOException;

public class Get_Jar extends Get_FileList {
	
	String file_path = "D:\\Kbc_OSS_Tool\\apk_fdroid\\";
	String[] paths = super.get_FileList(file_path);
	
	
	public void get_Jar() throws InterruptedException {
		
		final Object lock = new Object();
		int i = 0;	
		
		try {
			synchronized(this) {
				do {
					System.out.println(paths[i]);
					String output = paths[i].substring(0,paths[i].length()-3);
					output += "jar";
					String cmd = "cmd /c D:\\Kbc_OSS_Tool\\dex2jar-2.0\\dex2jar-2.0\\d2j-dex2jar.bat -f -o " +
							"D:\\Kbc_OSS_Tool\\apk_fdroid_Jar\\" + output + " D:\\Kbc_OSS_Tool\\apk_fdroid\\" + paths[i];				
					Process p = Runtime.getRuntime().exec(cmd);
					int exit = p.waitFor();
					i++;
				} while(paths[i] != null);
			}
		}catch(IOException e) {
			e.printStackTrace();
		}
		
	}

	public static void main(String[] args) throws InterruptedException {
		// TODO Auto-generated method stub
		Get_Jar exe = new Get_Jar();
		exe.get_Jar();
	}

}
