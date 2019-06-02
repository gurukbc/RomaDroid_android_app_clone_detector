# RomaDroid (Target to detect of Android Clone Application)

Developer : Byoung Chul Kim in CSOS, Dankook University  
Contect Mail : gurukbc2@gmail.com  
Paper : 2019 IEEE ACCESS (publish)  
Tool Name : RomaDroid  

# Usage  
  
  Command (Window)  
  <pre><code>
  java -jar RomaDroid.jar target_directory_path1 taget_directory_path2 result_csv_path result_csv_name  
  </code></pre>
  
  You have to input 4 argument  
  1 : Target_directory_path (This directory is set of decompiled APK)  
  2 : Target_directory_path2 (This directory is set of decompiled APK)  
  3 : CSV File Path (You can check result file in this path)  
  4 : CSV File Name 
  
  If App called A has a similarity rate of more than 90% to App called B,  
  RomaDroid catch this case, and write in CSV.
  If App called A has a similarity rate of more than 90% to App called A,  
  RomaDroid dont catch this case.
  
# Precautions 

This tool is minor version of RomaDroid (You can check main version tool in IEEE ACCESS Paper)  
minor version tool is different in core code from main version ; just little  
But,  
minor version RomaDroid has slightly better performance than main version RomaDroid.  
If you want to main version RomaDroid (same Paper version), please contact to mail.  
  
Finally,  
If you write paper or making open-source-software, I hope that you cite our paper in IEEE ACCESS.

# Minor version - Main version  

```java
/* Minor Version */  
for (line in lines of AndroidManifest.xml) {  
  if (line in second Tag of AndroidManifest.xml)   
     {  
       line = 2B (second Tag signature)  
     }  
}  
  
  
/* Main Version */  
for (line in lines of AndroidManifest.xml) {  
  if (line in second Tag of AndroidManifest.xml)   
     {  
       if (line is head of current Tag) {  
         line = 2B  
       }  
       else {  
         continue;  
       }  
     }  
 }  
         
```

If the line is too long to go to the next line,  
Minor version RomaDroid add this line to new signature.

#  Version  


