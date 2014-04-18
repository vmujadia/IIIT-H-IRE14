

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.util.HashSet;
import java.util.Map;
import java.util.TreeMap;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.helpers.DefaultHandler;

class Index
{

	public static int tempcount1=0;
	public static int tempcount2=0;
	public static int tempcount1_l=0;
	public static int tempcount2_l=0;
	public static TreeMap<String,String> map_entity=new TreeMap<String,String>();
	public static TreeMap<String,String> map_cat=new TreeMap<String,String>();

	public static TreeMap<String,String> map_listof_e=new TreeMap<String,String>();
	public static TreeMap<String,String> map_listof_c=new TreeMap<String,String>();
	void processMap(HashSet<String> entity,HashSet<String> categ){

		for(String i:entity){
			
			for(String j:categ){
				//entry in forward index entity--> category
				if(i.endsWith("_t")){
					
					String temp=i.substring(0,i.length()-2);
					temp=temp.replaceAll("[^A-Za-z0-9]"," ");
					temp=temp.trim().replaceAll("( )+", " ");
					String tokens[]=temp.split(" ");
					for(String c:tokens){
						if(map_entity.containsKey(c.toLowerCase()+"_t")){
							map_entity.put(c.toLowerCase()+"_t", map_entity.get(c.toLowerCase()+"_t") + "#" +j.toLowerCase()); 
						}
						else
							map_entity.put(c.toLowerCase()+"_t",j.toLowerCase());
					}
				}
				else{
					if(map_entity.containsKey(i.toLowerCase())){
						map_entity.put(i.toLowerCase(), map_entity.get(i.toLowerCase()) + "#" +j.toLowerCase()); 
					}
					else
						map_entity.put(i.toLowerCase(),j.toLowerCase());
				}

				//entry in inverted index category-->entity
				if(map_cat.containsKey(j.toLowerCase())){
					map_cat.put(j.toLowerCase(), map_cat.get(j.toLowerCase()) + "#" +i.toLowerCase()); 
				}
				else
					map_cat.put(j.toLowerCase(),i.toLowerCase());

			}
		}

		
		if(map_entity.size()>=10000){
			//write to file
			try
			{
				tempcount1++;
				FileWriter fw = new FileWriter("index1/temp_e"+tempcount1, false); 
				for (Map.Entry<String, String> entry : map_entity.entrySet()) {
					fw.write(entry.getKey()+"@"+entry.getValue()+"\n");
					
				}
				fw.close();
				map_entity.clear();
			}
			catch(IOException e){
				e.printStackTrace();
			}
		}
		if(map_cat.size()>=10000){
			//write to file
			try
			{
				tempcount2++;
				FileWriter fw = new FileWriter("index2/temp_c"+tempcount2, false); 
				for (Map.Entry<String, String> entry : map_cat.entrySet()) {

					fw.write(entry.getKey()+"@"+entry.getValue()+"\n");
					//System.out.println(entry.getKey()+" "+entry.getValue());
				}
				fw.close();
				map_cat.clear();
			}
			catch(IOException e){
				e.printStackTrace();
			}
		}
	}



	void dumpExtraRecords(){
		try
		{
			if(map_entity.size()>0){
				tempcount1++;
				FileWriter fw = new FileWriter("index1/temp_e"+tempcount1, false); 
				for (Map.Entry<String, String> entry : map_entity.entrySet()) {
					fw.write(entry.getKey()+"@"+entry.getValue()+"\n");
					//System.out.println(entry.getKey()+" "+entry.getValue());
				}
				fw.close();
				map_entity.clear();
			}
		}
		catch(IOException e){
			e.printStackTrace();
		}


		try
		{
			if(map_cat.size()>0){
				tempcount2++;
				FileWriter fw = new FileWriter("index2/temp_c"+tempcount2, false); 
				for (Map.Entry<String, String> entry : map_cat.entrySet()) {

					fw.write(entry.getKey()+"@"+entry.getValue()+"\n");
					//System.out.println(entry.getKey()+" "+entry.getValue());
				}
				fw.close();
				map_cat.clear();
			}
		}
		catch(IOException e){
			e.printStackTrace();
		}
	}


	void mergeEntityFiles()
	{
		
		System.out.println("Reached here");
		Map<String, Integer> map = new TreeMap<String, Integer>(); 
		int i=0,i1=0; 
		String s = null,s1=null; 
		try
		{ 
			BufferedReader[] brArr = new BufferedReader[tempcount1]; 
			for(int j=0; j<tempcount1; j++) 
			{ 
				brArr[j] = new BufferedReader(new FileReader(new File("index1/temp_e" +(j+1)))); 
				map.put(brArr[j].readLine(), j); 
			} 
			BufferedWriter bwnew = new BufferedWriter(new FileWriter(new File("output_e.txt"))); 

			while(!map.isEmpty()) 
			{ 
				s = map.keySet().iterator().next(); 
				i = map.get(s); 
				
				map.remove(s); 
				try{
					s1=map.keySet().iterator().next(); 
					i1=map.get(s1); 
					String[] split_s,split_s1; 
					split_s=s.split("@");

					split_s1=s1.split("@"); 
					if(s.startsWith("yusuf_t") || s.startsWith("sachin_t")){
						System.out.println("1 s "+s+" i "+i);
						System.out.println("2 s1 "+s1+" i1 "+i1);
					}
					if(split_s[0].equals(split_s1[0]))
					{ 

						HashSet<String> hh=new HashSet<String>();
						String toks1[]=split_s[1].split("#");
						for(String xx:toks1)
							hh.add(xx);
						String toks2[]=split_s1[1].split("#");
						for(String xx:toks2)
							hh.add(xx);
						String finalval="";
						for(String xx:hh)
							finalval+=(xx+"#");
						map.put(split_s[0]+"@"+finalval,i); 
						if(s.startsWith("yusuf_t") || s.startsWith("sachin_t")){
							System.out.println("3 finlval "+finalval+" i "+i);
						}
						//map.put(split_s[0]+"@"+split_s[1]+"#"+split_s1[1],i);
						s = brArr[i1].readLine(); 

						map.remove(s1); 
						if(s != null ) 
						{ 
							map.put(s, i1); 
						} 
						continue; 
					} 
				}
				catch(Exception e){
					e.printStackTrace();
				}
				String[] s_temp={};
				String[] s1_temp={};
				try{
				s_temp=s.split("@");
				s1_temp=s_temp[1].split("#");
				String news="";
				news+=s_temp[0]+"@";
				if(s.startsWith("yusuf_t") || s.startsWith("sachin_t")){
					System.out.println("4 news "+news);
				}
				for(int z=0;z<s1_temp.length;z++){

					try{
						String catid=binarySearch(s1_temp[z],"cat_index.txt");
						if(!catid.equals("no"))
							news+=catid+"#";
					}
					catch(Exception e)
					{
						e.printStackTrace();
					}

				}
				if(s.startsWith("yusuf_t") || s.startsWith("sachin_t")){
					System.out.println("5 news "+news);
				}
				bwnew.write(news+"\n");//write to output.txt
				bwnew.flush();
				if(s.startsWith("yusuf_t") || s.startsWith("sachin_t")){
					System.out.println("6 news here");
				}
				}
				catch(Exception e){
					e.printStackTrace();
				}
				
				s = brArr[i].readLine(); 

				if(s != null ) 
				{ 
					map.put(s, i); 
				} 

			} 
			bwnew.close(); 

			for(int j=0; j<brArr.length; j++) 
			{ 
				brArr[j].close(); 

			} 
		} 
		catch(Exception e)
		{ 
			e.printStackTrace(); 
		} 
	}



	void mergeCatFiles()
	{
		System.out.println("Reached here");
		
		Map<String, Integer> map = new TreeMap<String, Integer>(); 
		int i=0,i1=0; 
		String s = null,s1=null; 

		try
		{ 
			BufferedReader[] brArr = new BufferedReader[tempcount2]; 
			for(int j=0; j<tempcount2; j++) 
			{ 
				brArr[j] = new BufferedReader(new FileReader(new File("index2/temp_c" +(j+1)))); 
				map.put(brArr[j].readLine(), j); 
			} 
			BufferedWriter bwnew = new BufferedWriter(new FileWriter(new File("output_c.txt"))); 
			BufferedWriter cat_index = new BufferedWriter(new FileWriter(new File("cat_index.txt")));
			int count=1;
			while(!map.isEmpty()) 
			{ 

				s = map.keySet().iterator().next(); 

				i = map.get(s); 

				map.remove(s); 
				try{
					s1=map.keySet().iterator().next(); 

					i1=map.get(s1); 

					String[] split_s,split_s1; 
					split_s=s.split("@");
					split_s1=s1.split("@"); 

					if(split_s[0].equals(split_s1[0]))
					{ 
						//System.out.println("here in if "+split_s[0]+" "+split_s1[0]);
						map.put(split_s[0]+"@"+split_s[1]+"#"+split_s1[1],i); 
						s = brArr[i1].readLine(); 

						map.remove(s1); 
						if(s != null ) 
						{ 
							map.put(s, i1); 
						} 
						//System.out.println("here in if1 "+s+" "+i1);
						continue; 
					} 

				}
				catch(Exception e){
					//e.printStackTrace();
				}
				if(s.contains("@")){
					String[] s_temp=s.split("@");
					if(s_temp[0].length()>2)
					{
					String vval=count+s_temp[0].substring(s_temp[0].length()-2,s_temp[0].length() );
					cat_index.write(s_temp[0]+"@"+vval+"\n");
					cat_index.flush();
					bwnew.write(vval+"@"+s_temp[1]+"\n");//write to output.txt
					count++;
					bwnew.flush();
					}
				}
				s = brArr[i].readLine(); 
				if(s != null ) 
				{ 
					map.put(s, i); 
				} 

			} 
			bwnew.close(); 
			cat_index.close();
			for(int j=0; j<brArr.length; j++) 
			{ 
				brArr[j].close(); 

			} 
		} 
		catch(Exception e)
		{ 
			System.out.println("s "+s +" i "+i);
			e.printStackTrace(); 
		} 
	}

	
	
	
	void processListMap(HashSet<String> entity,HashSet<String> categ){

		for(String i:entity){
			
			for(String j:categ){
				
				//entry in forward index entity--> category
		
					String temp=i.substring(0,i.length()-2);
					temp=temp.replaceAll("[^A-Za-z]"," ");
					temp=temp.trim().replaceAll("( )+", " ");
					String tokens[]=temp.split(" ");
					for(String c:tokens){
						if(map_listof_e.containsKey(c.toLowerCase()+"_l")){
							map_listof_e.put(c.toLowerCase()+"_l", map_listof_e.get(c.toLowerCase()+"_l") + "#" +j.toLowerCase()); 
						}
						else
							map_listof_e.put(c.toLowerCase()+"_l",j.toLowerCase());
					}
				

				//entry in inverted index category-->entity
				if(map_listof_c.containsKey(j.toLowerCase())){
					map_listof_c.put(j.toLowerCase(), map_listof_c.get(j.toLowerCase()) + "#" +i.toLowerCase()); 
				}
				else
					map_listof_c.put(j.toLowerCase(),i.toLowerCase());

			}
		}

		
		if(map_listof_e.size()>=10000){
			//write to file
			try
			{
				tempcount1_l++;
				FileWriter fw = new FileWriter("index4/temp_el"+tempcount1_l, false); 
				for (Map.Entry<String, String> entry : map_listof_e.entrySet()) {
					fw.write(entry.getKey()+"@"+entry.getValue()+"\n");
					
				}
				fw.close();
				map_listof_e.clear();
			}
			catch(IOException e){
				e.printStackTrace();
			}
		}
		if(map_listof_c.size()>=10000){
			//write to file
			try
			{
				tempcount2_l++;
				FileWriter fw = new FileWriter("index3/temp_cl"+tempcount2_l, false); 
				for (Map.Entry<String, String> entry : map_listof_c.entrySet()) {

					fw.write(entry.getKey()+"@"+entry.getValue()+"\n");
					//System.out.println(entry.getKey()+" "+entry.getValue());
				}
				fw.close();
				map_listof_c.clear();
			}
			catch(IOException e){
				e.printStackTrace();
			}
		}
	}



	void dumpExtraRecordsList(){
		try
		{
			if(map_listof_e.size()>0){
				tempcount1_l++;
				FileWriter fw = new FileWriter("index4/temp_el"+tempcount1_l, false); 
				for (Map.Entry<String, String> entry : map_listof_e.entrySet()) {
					fw.write(entry.getKey()+"@"+entry.getValue()+"\n");
					//System.out.println(entry.getKey()+" "+entry.getValue());
				}
				fw.close();
				map_listof_e.clear();
			}
		}
		catch(IOException e){
			e.printStackTrace();
		}


		try
		{
			if(map_listof_c.size()>0){
				tempcount2_l++;
				FileWriter fw = new FileWriter("index3/temp_cl"+tempcount2_l, false); 
				for (Map.Entry<String, String> entry : map_listof_c.entrySet()) {

					fw.write(entry.getKey()+"@"+entry.getValue()+"\n");
					//System.out.println(entry.getKey()+" "+entry.getValue());
				}
				fw.close();
				map_listof_c.clear();
			}
		}
		catch(IOException e){
			e.printStackTrace();
		}
	}


	void mergeEntityFilesList()
	{

		System.out.println("Reached here");
		Map<String, Integer> map = new TreeMap<String, Integer>(); 
		int i=0,i1=0; 
		String s = null,s1=null; 
		try
		{ 
			BufferedReader[] brArr = new BufferedReader[tempcount1_l]; 
			for(int j=0; j<tempcount1_l; j++) 
			{ 
				brArr[j] = new BufferedReader(new FileReader(new File("index4/temp_el" +(j+1)))); 
				map.put(brArr[j].readLine(), j); 
			} 
			BufferedWriter bwnew = new BufferedWriter(new FileWriter(new File("output_el.txt"))); 

			while(!map.isEmpty()) 
			{ 
				s = map.keySet().iterator().next(); 
				i = map.get(s); 

				map.remove(s); 
				try{
					s1=map.keySet().iterator().next(); 
					i1=map.get(s1); 
					String[] split_s,split_s1; 
					split_s=s.split("@");

					split_s1=s1.split("@"); 

					if(split_s[0].equals(split_s1[0]))
					{ 

						HashSet<String> hh=new HashSet<String>();
						String toks1[]=split_s[1].split("#");
						for(String xx:toks1)
							hh.add(xx);
						String toks2[]=split_s1[1].split("#");
						for(String xx:toks2)
							hh.add(xx);
						String finalval="";
						for(String xx:hh)
							finalval+=(xx+"#");
						map.put(split_s[0]+"@"+finalval,i); 
						//map.put(split_s[0]+"@"+split_s[1]+"#"+split_s1[1],i);
						s = brArr[i1].readLine(); 

						map.remove(s1); 
						if(s != null ) 
						{ 
							map.put(s, i1); 
						} 
						continue; 
					} 
				}
				catch(Exception e){
					//e.printStackTrace();
				}
				String[] s_temp={};
				String[] s1_temp={};
				try{
				s_temp=s.split("@");
				s1_temp=s_temp[1].split("#");
				String news="";
				news+=s_temp[0]+"@";

				for(int z=0;z<s1_temp.length;z++){

					try{
						String catid=binarySearch(s1_temp[z],"cat_index_l.txt");
						if(!catid.equals("no"))
							news+=catid+"#";
					}
					catch(Exception e)
					{
						e.printStackTrace();
					}

				}

				bwnew.write(news+"\n");//write to output.txt
				bwnew.flush();
				}
				catch(Exception e){
					
				}
				
				s = brArr[i].readLine(); 

				if(s != null ) 
				{ 
					map.put(s, i); 
				} 

			} 
			bwnew.close(); 

			for(int j=0; j<brArr.length; j++) 
			{ 
				brArr[j].close(); 

			} 
		} 
		catch(Exception e)
		{ 
			e.printStackTrace(); 
		} 
	}


	void mergeCatFilesList()
	{
		System.out.println("Reached here");
		
		Map<String, Integer> map = new TreeMap<String, Integer>(); 
		int i=0,i1=0; 
		String s = null,s1=null; 

		try
		{ 
			BufferedReader[] brArr = new BufferedReader[tempcount2_l]; 
			for(int j=0; j<tempcount2_l; j++) 
			{ 
				brArr[j] = new BufferedReader(new FileReader(new File("index3/temp_cl" +(j+1)))); 
				map.put(brArr[j].readLine(), j); 
			} 
			BufferedWriter bwnew = new BufferedWriter(new FileWriter(new File("output_cl.txt"))); 
			BufferedWriter cat_index = new BufferedWriter(new FileWriter(new File("cat_index_l.txt")));
			int count=1;
			while(!map.isEmpty()) 
			{ 

				s = map.keySet().iterator().next(); 

				i = map.get(s); 

				map.remove(s); 
				try{
					s1=map.keySet().iterator().next(); 

					i1=map.get(s1); 

					String[] split_s,split_s1; 
					split_s=s.split("@");
					split_s1=s1.split("@"); 

					if(split_s[0].equals(split_s1[0]))
					{ 
						//System.out.println("here in if "+split_s[0]+" "+split_s1[0]);
						map.put(split_s[0]+"@"+split_s[1]+"#"+split_s1[1],i); 
						s = brArr[i1].readLine(); 

						map.remove(s1); 
						if(s != null ) 
						{ 
							map.put(s, i1); 
						} 
						//System.out.println("here in if1 "+s+" "+i1);
						continue; 
					} 

				}
				catch(Exception e){
					//e.printStackTrace();
				}
				if(s.contains("@")){
					String[] s_temp=s.split("@");
					String vval=count+s_temp[0].substring(s_temp[0].length()-2,s_temp[0].length() );
					cat_index.write(s_temp[0]+"@"+vval+"\n");
					cat_index.flush();
					bwnew.write(vval+"@"+s_temp[1]+"\n");//write to output.txt
					count++;
					bwnew.flush();
				}
				s = brArr[i].readLine(); 
				if(s != null ) 
				{ 
					map.put(s, i); 
				} 

			} 
			bwnew.close(); 
			cat_index.close();
			for(int j=0; j<brArr.length; j++) 
			{ 
				brArr[j].close(); 

			} 
		} 
		catch(Exception e)
		{ 
			System.out.println("s "+s +" i "+i);
			e.printStackTrace(); 
		} 
	}


	String binarySearch(String word,String filename){
		//System.out.println("word "+word);
		String line="";
		String nextLine="";
		RandomAccessFile fw1=null;
		try{
			fw1 = new RandomAccessFile(filename, "r"); 
			long start=0;
			long end=fw1.length();
			long middle;
			long mp;
			long mn;
			String linen;
			String linep;

			line=fw1.readLine();
			if(line.substring(0,line.indexOf('@')).compareTo(word)==0)
			{
				nextLine=fw1.readLine();
				//System.out.println(line);
			}
			else
			{
				while(start<end)
				{
					//System.out.println(start+"  -------   "+end);
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
					if(!linen.matches(".*@.*")){
						fw1.close();
						return "no";
						
					}
					if(!linep.matches(".*@.*")){
						fw1.close();
						return "no";}
					int cmpn=linen.substring(0, linen.indexOf('@')).compareTo(word);
					int cmpp=linep.substring(0, linep.indexOf('@')).compareTo(word);
					if(cmpp==0)
					{
						//System.out.println(linep+" 1");
						line=linep;
						nextLine=linen;
						break;
					}
					if(cmpn==0)
					{
						//System.out.println(linen+" 2");
						line=linen;
						break;
					}
					if(cmpn>0&&cmpp<0)
					{
						line=linep;
						nextLine=linen;
						break;
					}	
					if(cmpp>0)
						end=mp;

					if(cmpn<0)
						start=mn;

				}
			}
			fw1.close();
		}

		catch(IOException ie)
		{
			ie.printStackTrace();
			try{
			fw1.close();
			}
			catch(Exception e){
				e.printStackTrace();
			}
			return "no";
		}
		
		return line.split("@")[1];
	}

}
public class Start
{
	public static void main(String[] args) {
		
		 SAXParserFactory factory =SAXParserFactory.newInstance();
	        try{
	            InputStream xmlInput = new FileInputStream("three.xml");
	            SAXParser saxparser = factory.newSAXParser();
	            DefaultHandler handler = new SaxHandler();
	            
	            saxparser.parse(xmlInput, handler);
	            xmlInput.close();
	           
	        }catch(Throwable err){
	            err.printStackTrace();
	            
	        }
		
		
		try {
			
			//set_parsing first_call=new set_parsing("sample.xml");
			//first_call.set_parsing_run();
			
			Index i=new Index();
			i.dumpExtraRecords();
			i.dumpExtraRecordsList();
			
			
			
			
			Merge m=new Merge();
			m.mergeIndicesCat(1, i.tempcount2, 1);//2
			m.mergeIndicesEntity(1, i.tempcount1, 1);//1
			m.mergeIndicesCatList(1, i.tempcount2_l, 1);//3
			m.mergeIndicesEntityList(1, i.tempcount1_l, 1);//4
			
			
			Secondary s=new Secondary();
			s.Secondary_main();
			
			//i.mergeCatFiles();
			//i.mergeEntityFiles();
			//i.mergeCatFilesList();
			//i.mergeEntityFilesList();
			//Merge m=new Merge();
			//m.mergeMain();
			System.out.println("Done..hopefully !! :P");
		} catch (Exception e) {
			e.printStackTrace();
		}


	}


}
