/*
*	Developer : Byoung Chul Kim in CSOS, Dankook University
*	Email : gurukbc2@gmail.com
*	Phone : 010-7713-6911
*	Target : Detection OSS Similarity
*/
package RomaDroid;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import info.debatty.java.stringsimilarity.LongestCommonSubsequence;
import info.debatty.java.stringsimilarity.*;

public class ToolTest extends Get_BKFeature{

	
	public static int topAll;
	public static int minCom = 100;
	public static int maxCom = 0;
	public static int selectCount = 0;
	
	public static void createCSV(String[] data, String title, String filepath, float fp) {
		try {
			BufferedWriter fw = new BufferedWriter(new FileWriter(filepath + "/" + title + ".csv", true));
			
			for(String tmp : data) {
				if(tmp != null) {
				fw.write(tmp + ",");
				fw.newLine();
				
				}
			}
			
			fw.flush();
			fw.close();
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	

	public static void main(String[] args) throws IOException {
		
		Get_FileList tmp = new Get_FileList();
		String target;
		int length = 0;
		String ori_str[] = null;
		String obf_str[] = null;

		//String ori_path = "D:\\Kbc_OSS_Tool\\apk_all_nsri_original_decompile\\";
		//String obf_path = "D:\\Kbc_OSS_Tool\\apk_all_nsri_obfuscation_decompile\\";
		
		String ori_path = args[0] + "\\";
		String obf_path = args[1] + "\\";
		String path = args[2];
		String fileName = args[3];
		
		String[] obf_list = tmp.get_FileList(obf_path);
		String[] tmp_ori = tmp.get_FileList(ori_path);
		String[] ori_list = tmp.get_FileList(ori_path);
		String[] data = null;		
		String obf_xml[] = null;
		String ori_xml[] = null;
		
		float feature_size = 0;
		float all_size = 0;
				
		Get_BKFeature test = new Get_BKFeature();
		info.debatty.java.stringsimilarity.MetricLCS lcs = 
	    new info.debatty.java.stringsimilarity.MetricLCS();
		JaroWinkler jw = new JaroWinkler();
		NGram ngram = new NGram(2);
		
		data = new String[ori_list.length * 500];		
		ori_str = new String[ori_list.length + 1];
		obf_str = new String[obf_list.length + 1];
		ori_xml = new String[ori_list.length + 1];
		obf_xml = new String[obf_list.length + 1];		
		
		
		int count = 0;
		int k = 0;
		int optionIndex = -1;
		int check = 0;
		String[] src = new String[tmp_ori.length+1];
		src[0] = tmp_ori[0];

		for(int i = 0; i < obf_list.length; i++) {
			obf_xml[i] = obf_path + obf_list[i] + "\\AndroidManifest.xml";
			//obf_xml[i] = obf_path + obf_list[i];
			obf_str[i] = test.file_open(obf_xml[i]);
		}
		
		
		
		for(int i = 0; i < ori_list.length; i++) {	
			File oTmp = new File(ori_path + ori_list[i]);
			long tmpSize = (long)oTmp.length();
			all_size += tmpSize;
			ori_xml[i] = ori_path + ori_list[i] + "\\AndroidManifest.xml";
			//ori_xml[i] = ori_path + ori_list[i];
			ori_str[i] = test.file_open(ori_xml[i]);
		}
		
		
		System.out.println("--------Intent Filter Component Analysis--------");
		System.out.println("ALL CASE COMPONENT = " + topAll/2);
		System.out.println("MAX COMPONENT COUNT = " + maxCom);
		System.out.println("MIN COMPONENT COUNT = " + minCom);
		System.out.println("MEAN COMPONENT COUNT = " + ((topAll/2)/ori_list.length));
		System.out.println("SELECT COMPONENT COUNT = " + selectCount/2);
				
		int set_size = obf_list.length;
		int all_count = 0;
		int success_all = 0;
		float tp = 0;
		float fp = 0;
		float tn = 0;
		float fn = 0;
		float ac = 0;
		float pc = 0;
		float rc = 0;
		float sum_ac = 0;
		float sum_pc = 0;
		float sum_rc = 0;
		int cop_count = 0;
		
		
		for(int i = 0; i < obf_list.length; i++) {
			if(obf_str[i] != null) {
				success_all++;
			}
		}
		
		System.out.println("===== RomaDroid LCS result ====");
		for(int i = 0; i < ori_list.length; i++) {
			cop_count = 0;
			for(int j = 0; j < obf_list.length; j++) {
							
				//Option IF start
				if(obf_str[j] != null && ori_str[i] != null) {
				float result = (float)((100-lcs.distance(ori_str[i], obf_str[j]) * 100));
			
				float thres = 90;
				String[] parse = obf_list[j].split("-");
				
			
				all_count++;
				
				if((ori_list[i].contains(parse[0])) && result >= thres) {
					tp += 1.0;
				}
				
				else if ((ori_list[i].contains(parse[0])) && result < thres) {
					fn += 1.0;			
				}

				else if(!ori_list[i].contains(parse[0]) && result >= thres) {

					cop_count += 1;
					String cur = Integer.toString(cop_count);
					String ori = ori_list[i];
					String cop = obf_list[j];
					data[(int) fp] = cur + "," + ori + "," + cop;
					
					fp += 1.0;
					
				}
				
				else if(!ori_list[i].contains(parse[0]) && result < thres) {						
					tn += 1.0;
				}
				result = (float) 0.0;
				
				} //Option END
								
			}
			
		}

		ac = (float)((tp + tn) / (tp + tn + fp + fn));
		pc = (float)(tp  / (tp + fp));
		rc = (float)(tp / (tp + fn));
		float fpr = (float)(fp / (fp + tn));
		float tpr = (float)(tp / (fn + tp));
		
		float npc = (float)((tp + tn) / (tp + tn + fp));
		

		System.out.println("all compare case : " + all_count);
		System.out.println("Data Set size(original) : " + set_size);
		System.out.println("Pass Set size(original) : " + success_all);
		System.out.println("true positive case : " + tp);
		System.out.println("false positive case : " + fp);
		System.out.println("Accuracy(정확도) : " + ac);
		System.out.println("Precision(정밀도) : " + pc);
		System.out.println("Recall(재현율) : " + rc);
		System.out.println("True negative rate(정밀도 대체) : " + npc);
		System.out.println("F1 score : " + ((2 * pc * rc)/(pc + rc)));
		System.out.println("tpr : " + tpr);
		System.out.println("fpr : " + fpr*100);
		
		
		all_size = all_size / 1000;
		System.out.println("File all size : " + all_size);
		System.out.println("File mean : " + all_size/(float)ori_list.length);
		System.out.println("Feature mean : " + feature_size/(float)ori_list.length);
		
		createCSV(data, fileName, path, fp);
	    
				
	}



}
