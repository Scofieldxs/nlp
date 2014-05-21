package ICTCLAS.I3S.AC;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import ICTCLAS.I3S.AC.ICTCLAS50;




public class newSplit {
	ArrayList<String> filenameList=new ArrayList<String>();
	public  static String pathString="D://nlp//resource";
	public static String targetDoc="D://nlp//target//";
	public ICTCLAS50 ictclas50=new ICTCLAS50();;
	
	
	
	public void splitOneDocument(File filename,String targetName) throws IOException{
		String encoding="GBK";
        String finalString=null;
        if(filename.isFile() && filename.exists()){ //判断文件是否存在
            InputStreamReader read;
			
			read = new InputStreamReader(new FileInputStream(filename),encoding);
			//考虑到编码格式
	        BufferedReader bufferedReader = new BufferedReader(read);
	   
	        String lineTxt = null;
	        while((lineTxt = bufferedReader.readLine()) != null){
	       	 finalString+=lineTxt;
	        }
	        read.close();
	        
	        //分词
	        byte[] nativeBytes1 = ictclas50.ICTCLAS_ParagraphProcess(finalString.getBytes(), 0, 1);
            String nativeStr1 = new String(nativeBytes1,0,nativeBytes1.length,"GB2312");
            
            //挑选词
            String nativeStr2[]=nativeStr1.split(" ");
            Set<String> list=new HashSet<String>();
        
            for(String str:nativeStr2){
            	if(str.contains("n")||str.contains("v")||str.contains("a")){            		
            		String tmpString=str.substring(0, str.indexOf('/'));
            		if(!tmpString.matches("[a-zA-Z0-9]*")&&tmpString.length()>1){
            			list.add(tmpString);
            		}
        		
            	}
            }
            
            StringBuffer stringBuffer=new StringBuffer();
            Iterator iterator=list.iterator();
            while(iterator.hasNext()){
            	stringBuffer.append(iterator.next()+" ");
            }
            
            File f = new File(targetName);
            f.createNewFile();
            BufferedWriter output = new BufferedWriter(new FileWriter(f));
            output.write(stringBuffer.toString());
            output.close();
            System.out.println(targetName+"Done.");
       }
	}
	
	public static void main(String[] args) {
		newSplit split=new newSplit();
		try{
			String argu=".";
			if(split.ictclas50.ICTCLAS_Init(argu.getBytes("GB2312"))==false){
				System.out.println("Init Fail");
			}
			else{
				System.out.println("Init succeed.");
			}
		}catch(Exception e){
			
		}
		
		 File file = new File(pathString);   	        
	     File[] array = file.listFiles();   

	     for(int i=0;i<array.length;i++){
	    	 try {
				split.splitOneDocument(array[i], targetDoc+i+".txt");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	     }
	}
}
