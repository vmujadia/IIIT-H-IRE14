

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;

public class Secondary {

	static void createSecondaryIndex(String indexfile,String secondaryindexfile){
		try
		{ 
			BufferedWriter bw = new BufferedWriter(new FileWriter(new File(secondaryindexfile)));
			FileWriter fw = new FileWriter("temp_sec.txt", false); 
			File file=new File("temp_sec.txt");
			BufferedReader br = new BufferedReader(new FileReader(new File(indexfile))); 
			int count=1;
			String line="";
			long offset=0;
			String header="";
			line= br.readLine();
			header=line.split("@")[0];
			offset=0;
			fw.write(line+"\n");
			bw.write(header.substring(0,header.length()-2)+" "+offset+"\n");
			offset=offset+line.length()+1;
			bw.flush();
			String templine="";
			int maincount=0;
			long prevlength=0;
			while ((line = br.readLine()) != null) {
				
				count++;
				maincount++;
				//System.out.println("line "+line);
				//System.out.println("length "+line.length());
				if(count==20)
				{
					//System.out.println("line "+line+" length "+line.length());
					header=line.split("@")[0];
					bw.write(header.substring(0,header.length()-2)+" "+file.length()+"\n");
					bw.flush();
					count=0;

				}
				prevlength=file.length();
				fw.write(line+"\n");
				fw.flush();
				offset=offset+line.length()+1;
				//System.out.println("offset "+file.length());
				templine=line;
				
			}
			header=templine.split("@")[0];
			bw.write(header.substring(0,header.length()-2)+" "+prevlength+"\n");
			bw.close(); 
			fw.close();
			br.close();
		}	
		catch(Exception e)
		{ 
			e.printStackTrace(); 
		} 
	}

	
	 static ArrayList<Long> binarySearch(String word,String filename)
		{
			String line="";
			String nextLine="";
			try
			{
				RandomAccessFile fw1 = new RandomAccessFile(filename, "r"); 
				long start=0;
				long end=fw1.length();
				long middle;
				long mp;
				long mn;
				String linen;
				String linep;

				line=fw1.readLine();
				if(line.substring(0,line.indexOf(' ')).compareTo(word)==0)
				{
					nextLine=fw1.readLine();
				}
				else
				{
					while(start<end)
					{
						middle=(start+end)/2;
						fw1.seek(middle);
						byte c;
						while(true)
						{
							if(fw1.getFilePointer()-2<0)
							{
								break;
							}
							fw1.seek(fw1.getFilePointer()-2);
							c=fw1.readByte();
							if(c=='\n')
								break;
						}
						mp=fw1.getFilePointer();
						linep=fw1.readLine();
						mn=fw1.getFilePointer();
						linen=fw1.readLine();
						nextLine=fw1.readLine();
						if(Long.parseLong(linep.substring(0,linep.indexOf(' ')))==Long.parseLong(word))
						{
							line=linep;
							nextLine=linen;
							break;
						}
						if(Long.parseLong(linen.substring(0,linen.indexOf(' ')))==Long.parseLong(word))
						{
							line=linen;
							break;
						}
						
						if((Long.parseLong(linen.substring(0,linen.indexOf(' ')))>Long.parseLong(word))&&(Long.parseLong(linep.substring(0,linep.indexOf(' ')))<Long.parseLong(word)))
						{
							line=linep;
							nextLine=linen;
							break;
						}	
						
						if(Long.parseLong(linep.substring(0,linep.indexOf(' ')))>Long.parseLong(word))
						{
							
							end=mp;

						}
						
						if(Long.parseLong(linen.substring(0,linen.indexOf(' ')))<Long.parseLong(word))
						{
							start=mn;
						}
					}
				}
				fw1.close();
			}

			catch(IOException ie)
			{
				ie.printStackTrace();
			}
			//return Long.parseLong(line1.split(" ")[1]);
			ArrayList<Long> al=new ArrayList<Long>();
			al.add(Long.parseLong(line.split(" ")[1]));
			try{
			al.add(Long.parseLong(nextLine.split(" ")[1]));
			}
			catch(NullPointerException ne){
				al.add(Long.parseLong(line.split(" ")[1]));
			}
			return al;
		}

	 static void createAlphaIndex(String filename){
		 BufferedWriter[] bwArr=new BufferedWriter[27];
		 
		 try{
		BufferedReader br = new BufferedReader(new FileReader(new File(filename))); 
		 for(int i=0;i<27;i++){
			 bwArr[i]=new BufferedWriter(new FileWriter("index_e/"+(char)(i+97)+".txt",true));
		 }
		 String line="";
		 while ((line = br.readLine()) != null) {
			char first=line.charAt(0);
			try{
				bwArr[first-97].write(line+"\n");
			}
			catch(Exception ex){
				bwArr[26].write(line+"\n");
			}
				 
		 }
		 br.close();
		 for(int i=0;i<27;i++){
			 bwArr[i].close();
		 }
		 }
		 catch(Exception e){
			 e.printStackTrace();
		 }
	 }
	 
	 static void createAlphaIndex_l(String filename){
		 BufferedWriter[] bwArr=new BufferedWriter[27];
		 
		 try{
		BufferedReader br = new BufferedReader(new FileReader(new File(filename))); 
		 for(int i=0;i<27;i++){
			 bwArr[i]=new BufferedWriter(new FileWriter("index_el/"+(char)(i+97)+".txt",true));
		 }
		 String line="";
		 while ((line = br.readLine()) != null) {
			char first=line.charAt(0);
			try{
				bwArr[first-97].write(line+"\n");
			}
			catch(Exception ex){
				bwArr[26].write(line+"\n");
			}
				 
		 }
		 br.close();
		 for(int i=0;i<27;i++){
			 bwArr[i].close();
		 }
		 }
		 catch(Exception e){
			 e.printStackTrace();
		 }
	 }
	 
	public void Secondary_main(){
		createSecondaryIndex("output_c.txt","secondary_c.txt");
		createSecondaryIndex("output_cl.txt","secondary_cl.txt");
		createAlphaIndex("output_e.txt");
		createAlphaIndex_l("output_el.txt");
		System.out.println("done");
	}
}
