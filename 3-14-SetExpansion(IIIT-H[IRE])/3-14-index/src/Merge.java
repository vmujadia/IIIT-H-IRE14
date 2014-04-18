

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.HashSet;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;

public class Merge {

	
private enum state { FIRST, REST, CLOSED };
	
	
	public static void mergeIndicesEntity(int start, int end,int outputnumber) {
		System.out.println("Reached here merge entity");
		int no = end-start+1;
		TreeMap<String, TreeSet<String>> index = new TreeMap<String, TreeSet<String>>();
		BufferedReader[] indexReader = new BufferedReader[no];
		BufferedWriter indexWriter = null;
		String indexLine = ""; 
		state[] indexMarker = new state[no];
		index.clear();
		String[] word = new String[no], posting = new String[no],docList;
		TreeSet<String> sortedDoc = new TreeSet<String>();
		try {
			int n = 0;
			
			for(int i = 0; i < no; i++) {
				indexReader[i] = new BufferedReader(new FileReader("index1/temp_e"+(i+1)));
				indexLine = indexReader[i].readLine();
				indexMarker[i] = state.REST;
				n = indexLine.indexOf('@');
				word[i] = indexLine.substring(0,n);
				posting[i] = indexLine.substring(n+1);
				if(!index.containsKey(word[i])) {
					sortedDoc = new TreeSet<String>();
					docList = posting[i].split("#");
					for(String doc : docList)
						sortedDoc.add(doc);
					index.put(word[i], sortedDoc);
				}
				else {
					sortedDoc = index.get(word[i]);
					docList = posting[i].split("#");
					for(String doc : docList)
						sortedDoc.add(doc);
					index.put(word[i],sortedDoc);
				}
				
				
		}
		
		String firstKey = "";
		indexWriter = new BufferedWriter(new FileWriter("output_e.txt"));
		boolean newRecord = true;
		while(!index.isEmpty()) {
			
			firstKey = index.firstKey();
			indexWriter.write(firstKey+"@");
			newRecord = true;
			for(String doc : index.get(firstKey)) {
				String catid=binarySearch(doc,"cat_index.txt");
				if(!newRecord)
					indexWriter.write("#"+catid);
				else {
					indexWriter.write(catid);
					newRecord = false;
				}
			}
			indexWriter.write("\n");
			
			/*if(noOfWords >= ENTRIES_PER_FILE) {
				indexWriter.flush();
				indexWriter.close();
				primaryIndex.write(firstKey + "\n");
				indexWriter = new BufferedWriter(new FileWriter(indexPath+"/finalIndex"+(++writes)));
				noOfWords = 0;
			}*/
			index.remove(firstKey);
			
			for(int i = 0; i < no; i++) {
				if(indexMarker[i] != state.CLOSED) {
				if(word[i].equals(firstKey)) {
					indexMarker[i] = state.FIRST;
					indexLine = indexReader[i].readLine();
					if(indexLine == null) {
						indexMarker[i] = state.CLOSED;
						indexReader[i].close();
						continue;
					}
					n = indexLine.indexOf('@');
					word[i] = indexLine.substring(0,n);
					posting[i] = indexLine.substring(n+1);
					if(!index.containsKey(word[i])) {
						sortedDoc = new TreeSet<String>();
						docList = posting[i].split("#");
						for(String doc : docList)
							sortedDoc.add(doc);
						index.put(word[i], sortedDoc);
					}
					else {
						sortedDoc = index.get(word[i]);
						docList = posting[i].split("#");
						for(String doc : docList)
							sortedDoc.add(doc);
						index.put(word[i], sortedDoc);
					}
					
				}
				else {
					indexMarker[i] = state.REST;
				}
				}
			}
		}
		/*if(noOfWords%ENTRIES_PER_FILE != 0) {
			primaryIndex.write(firstKey);
		}
			*/
		indexWriter.flush();
		indexWriter.close();
		
	//	primaryIndex.close();
		indexWriter.close();
		
		for(int i = 1; i <= no; i++) {
			indexReader[i-1].close();
			//new File(indexPath+"/temp"+i).delete();
		}
		} catch (FileNotFoundException e) {
			System.err.println("Error while merging indices: "+e.getLocalizedMessage());
		} catch (IOException e) {
			System.err.println("Error while merging indices: "+e.getLocalizedMessage());
		}
		
	}
	public static void mergeIndicesEntityList(int start, int end,int outputnumber) {
		System.out.println("Reached here merge entity");
		int no = end-start+1;
		TreeMap<String, TreeSet<String>> index = new TreeMap<String, TreeSet<String>>();
		BufferedReader[] indexReader = new BufferedReader[no];
		BufferedWriter indexWriter = null;
		String indexLine = ""; 
		state[] indexMarker = new state[no];
		index.clear();
		String[] word = new String[no], posting = new String[no],docList;
		TreeSet<String> sortedDoc = new TreeSet<String>();
		try {
			int n = 0;
			
			for(int i = 0; i < no; i++) {
				indexReader[i] = new BufferedReader(new FileReader("index4/temp_el"+(i+start)));
				indexLine = indexReader[i].readLine();
				indexMarker[i] = state.REST;
				n = indexLine.indexOf('@');
				word[i] = indexLine.substring(0,n);
				posting[i] = indexLine.substring(n+1);
				if(!index.containsKey(word[i])) {
					sortedDoc = new TreeSet<String>();
					docList = posting[i].split("#");
					for(String doc : docList)
						sortedDoc.add(doc);
					index.put(word[i], sortedDoc);
				}
				else {
					sortedDoc = index.get(word[i]);
					docList = posting[i].split("#");
					for(String doc : docList)
						sortedDoc.add(doc);
					index.put(word[i],sortedDoc);
				}
				
				
		}
		
		String firstKey = "";
		indexWriter = new BufferedWriter(new FileWriter("output_el.txt"));
		boolean newRecord = true;
		while(!index.isEmpty()) {
			
			firstKey = index.firstKey();
			indexWriter.write(firstKey+"@");
			newRecord = true;
			for(String doc : index.get(firstKey)) {
				String catid=binarySearch(doc,"cat_index_l.txt");
				if(!newRecord)
					indexWriter.write("#"+catid);
				else {
					indexWriter.write(catid);
					newRecord = false;
				}
			}
			indexWriter.write("\n");
			
			/*if(noOfWords >= ENTRIES_PER_FILE) {
				indexWriter.flush();
				indexWriter.close();
				primaryIndex.write(firstKey + "\n");
				indexWriter = new BufferedWriter(new FileWriter(indexPath+"/finalIndex"+(++writes)));
				noOfWords = 0;
			}*/
			index.remove(firstKey);
			
			for(int i = 0; i < no; i++) {
				if(indexMarker[i] != state.CLOSED) {
				if(word[i].equals(firstKey)) {
					indexMarker[i] = state.FIRST;
					indexLine = indexReader[i].readLine();
					if(indexLine == null) {
						indexMarker[i] = state.CLOSED;
						indexReader[i].close();
						continue;
					}
					n = indexLine.indexOf('@');
					word[i] = indexLine.substring(0,n);
					posting[i] = indexLine.substring(n+1);
					if(!index.containsKey(word[i])) {
						sortedDoc = new TreeSet<String>();
						docList = posting[i].split("#");
						for(String doc : docList)
							sortedDoc.add(doc);
						index.put(word[i], sortedDoc);
					}
					else {
						sortedDoc = index.get(word[i]);
						docList = posting[i].split("#");
						for(String doc : docList)
							sortedDoc.add(doc);
						index.put(word[i], sortedDoc);
					}
					
				}
				else {
					indexMarker[i] = state.REST;
				}
				}
			}
		}
		/*if(noOfWords%ENTRIES_PER_FILE != 0) {
			primaryIndex.write(firstKey);
		}
			*/
		indexWriter.flush();
		indexWriter.close();
		
	//	primaryIndex.close();
		indexWriter.close();
		
		for(int i = 1; i <= no; i++) {
			indexReader[i-1].close();
			//new File(indexPath+"/temp"+i).delete();
		}
		} catch (FileNotFoundException e) {
			System.err.println("Error while merging indices: "+e.getLocalizedMessage());
		} catch (IOException e) {
			System.err.println("Error while merging indices: "+e.getLocalizedMessage());
		}
		
	}
	
	
	
	public static void mergeIndicesCat(int start, int end,int outputnumber) {
		System.out.println("Reached here merge cat");
		int no = end-start+1;
		TreeMap<String, String> index = new TreeMap<String, String>();
		BufferedReader[] indexReader = new BufferedReader[no];
		BufferedWriter indexWriter = null;
		
		String indexLine = ""; 
		state[] indexMarker = new state[no];
		index.clear();
		String[] word = new String[no], posting = new String[no],docList;
		TreeSet<String> sortedDoc = new TreeSet<String>();
		int count=1;
		try {
			int n = 0;
			BufferedWriter cat_index = new BufferedWriter(new FileWriter(new File("cat_index.txt")));
			for(int i = 0; i < no; i++) {
				indexReader[i] = new BufferedReader(new FileReader("index2/temp_c"+(i+1)));
				indexLine = indexReader[i].readLine();
				indexMarker[i] = state.REST;
				n = indexLine.indexOf('@');
				word[i] = indexLine.substring(0,n);
				posting[i] = indexLine.substring(n+1);
				if(!index.containsKey(word[i])) {
					/*sortedDoc = new TreeSet<String>();
					/docList = posting[i].split("#");
					for(String doc : docList)
						sortedDoc.add(doc);*/
					index.put(word[i], posting[i]);
				}
				else {
					/*sortedDoc = index.get(word[i]);
					docList = posting[i].split("#");
					for(String doc : docList)
						sortedDoc.add(doc);*/
					index.put(word[i],index.get(word[i])+"#"+posting[i]);
				}
				
				
		}
		
		String firstKey = "";
		indexWriter = new BufferedWriter(new FileWriter("output_c.txt"));
		//boolean newRecord = true;
		while(!index.isEmpty()) {
			firstKey = index.firstKey();
			if(firstKey.length()>2)
			{
			String vval=count+firstKey.substring(firstKey.length()-2,firstKey.length() );
			cat_index.write(firstKey+"@"+vval+"\n");
			cat_index.flush();
			indexWriter.write(vval+"@"+index.get(firstKey)+"\n");
			count++;
			}
			
			
			
			/*newRecord = true;
			for(String doc : index.get(firstKey)) {
				String catid=binarySearch(doc,"cat_index.txt");
				if(!newRecord)
					indexWriter.write("#"+catid);
				else {
					indexWriter.write(catid);
					newRecord = false;
				}
			}*/
			
			
			/*if(noOfWords >= ENTRIES_PER_FILE) {
				indexWriter.flush();
				indexWriter.close();
				primaryIndex.write(firstKey + "\n");
				indexWriter = new BufferedWriter(new FileWriter(indexPath+"/finalIndex"+(++writes)));
				noOfWords = 0;
			}*/
			index.remove(firstKey);
			
			for(int i = 0; i < no; i++) {
				if(indexMarker[i] != state.CLOSED) {
				if(word[i].equals(firstKey)) {
					indexMarker[i] = state.FIRST;
					indexLine = indexReader[i].readLine();
					if(indexLine == null) {
						indexMarker[i] = state.CLOSED;
						indexReader[i].close();
						continue;
					}
					n = indexLine.indexOf('@');
					word[i] = indexLine.substring(0,n);
					posting[i] = indexLine.substring(n+1);
					if(!index.containsKey(word[i])) {
						index.put(word[i], posting[i]);
					}
					else {
						index.put(word[i],index.get(word[i])+"#"+posting[i]);
					}
					
				}
				else {
					indexMarker[i] = state.REST;
				}
				}
			}
		}
		/*if(noOfWords%ENTRIES_PER_FILE != 0) {
			primaryIndex.write(firstKey);
		}
			*/
		indexWriter.flush();
		indexWriter.close();
		cat_index.close();
	//	primaryIndex.close();
		indexWriter.close();
		
		for(int i = 1; i <= no; i++) {
			indexReader[i-1].close();
			//new File(indexPath+"/temp"+i).delete();
		}
		} catch (FileNotFoundException e) {
			System.err.println("Error while merging indices: "+e.getLocalizedMessage());
		} catch (IOException e) {
			System.err.println("Error while merging indices: "+e.getLocalizedMessage());
		}
		
	}
	
	public static void mergeIndicesCatList(int start, int end,int outputnumber) {
		System.out.println("Reached here merge cat");
		int no = end-start+1;
		TreeMap<String, String> index = new TreeMap<String, String>();
		BufferedReader[] indexReader = new BufferedReader[no];
		BufferedWriter indexWriter = null;
		
		String indexLine = ""; 
		state[] indexMarker = new state[no];
		index.clear();
		String[] word = new String[no], posting = new String[no],docList;
		TreeSet<String> sortedDoc = new TreeSet<String>();
		int count=1;
		try {
			int n = 0;
			BufferedWriter cat_index = new BufferedWriter(new FileWriter(new File("cat_index_l.txt")));
			for(int i = 0; i < no; i++) {
				indexReader[i] = new BufferedReader(new FileReader("index3/temp_cl"+(i+start)));
				indexLine = indexReader[i].readLine();
				indexMarker[i] = state.REST;
				n = indexLine.indexOf('@');
				word[i] = indexLine.substring(0,n);
				posting[i] = indexLine.substring(n+1);
				if(!index.containsKey(word[i])) {
					/*sortedDoc = new TreeSet<String>();
					/docList = posting[i].split("#");
					for(String doc : docList)
						sortedDoc.add(doc);*/
					index.put(word[i], posting[i]);
				}
				else {
					/*sortedDoc = index.get(word[i]);
					docList = posting[i].split("#");
					for(String doc : docList)
						sortedDoc.add(doc);*/
					index.put(word[i],index.get(word[i])+"#"+posting[i]);
				}
				
				
		}
		
		String firstKey = "";
		indexWriter = new BufferedWriter(new FileWriter("output_cl.txt"));
		//boolean newRecord = true;
		while(!index.isEmpty()) {
			firstKey = index.firstKey();
			if(firstKey.length()>2)
			{
			String vval=count+firstKey.substring(firstKey.length()-2,firstKey.length() );
			cat_index.write(firstKey+"@"+vval+"\n");
			cat_index.flush();
			indexWriter.write(vval+"@"+index.get(firstKey)+"\n");
			count++;
			}
			
			
			
			/*newRecord = true;
			for(String doc : index.get(firstKey)) {
				String catid=binarySearch(doc,"cat_index.txt");
				if(!newRecord)
					indexWriter.write("#"+catid);
				else {
					indexWriter.write(catid);
					newRecord = false;
				}
			}*/
			
			
			/*if(noOfWords >= ENTRIES_PER_FILE) {
				indexWriter.flush();
				indexWriter.close();
				primaryIndex.write(firstKey + "\n");
				indexWriter = new BufferedWriter(new FileWriter(indexPath+"/finalIndex"+(++writes)));
				noOfWords = 0;
			}*/
			index.remove(firstKey);
			
			for(int i = 0; i < no; i++) {
				if(indexMarker[i] != state.CLOSED) {
				if(word[i].equals(firstKey)) {
					indexMarker[i] = state.FIRST;
					indexLine = indexReader[i].readLine();
					if(indexLine == null) {
						indexMarker[i] = state.CLOSED;
						indexReader[i].close();
						continue;
					}
					n = indexLine.indexOf('@');
					word[i] = indexLine.substring(0,n);
					posting[i] = indexLine.substring(n+1);
					if(!index.containsKey(word[i])) {
						index.put(word[i], posting[i]);
					}
					else {
						index.put(word[i],index.get(word[i])+"#"+posting[i]);
					}
					
				}
				else {
					indexMarker[i] = state.REST;
				}
				}
			}
		}
		/*if(noOfWords%ENTRIES_PER_FILE != 0) {
			primaryIndex.write(firstKey);
		}
			*/
		indexWriter.flush();
		indexWriter.close();
		cat_index.close();
	//	primaryIndex.close();
		indexWriter.close();
		
		for(int i = 1; i <= no; i++) {
			indexReader[i-1].close();
			//new File(indexPath+"/temp"+i).delete();
		}
		} catch (FileNotFoundException e) {
			System.err.println("Error while merging indices: "+e.getLocalizedMessage());
		} catch (IOException e) {
			System.err.println("Error while merging indices: "+e.getLocalizedMessage());
		}
		
	}
	
	/*
	void mergeEntityFilesList(int start, int end,int outputnumber)
	{

		System.out.println("Reached here");
		int tempcount1_l=end-start+1;
		System.out.println("startfile: temp_c"+start);
		System.out.println("lastfile: temp_c"+end);
		System.out.println("Number of files: "+tempcount1_l);


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
					e.printStackTrace();
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

	
	void mergeCatFilesList(int start, int end,int outputnumber)
	{
		System.out.println("Reached here");
		int tempcount2_l=end-start+1;
		System.out.println("startfile: temp_c"+start);
		System.out.println("lastfile: temp_c"+end);
		System.out.println("Number of files: "+tempcount2_l);

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
					e.printStackTrace();
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

	
	
	
	
	void mergeEntityFiles(int start,int end,int out)
	{
		int tempcount1=end-start+1;
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
			BufferedWriter bwnew = new BufferedWriter(new FileWriter(new File("output_e1.txt"))); 

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
					if(s.startsWith("aavishkar_t") || s.startsWith("sachin_t")){
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
						if(s.startsWith("aavishkar_t") || s.startsWith("sachin_t")){
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
				if(s.startsWith("aavishkar_t")|| s.startsWith("sachin_t")){
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
				if(s.startsWith("aavishkar_t") || s.startsWith("sachin_t")){
					System.out.println("5 news "+news);
				}
				bwnew.write(news+"\n");//write to output.txt
				bwnew.flush();
				if(s.startsWith("aavishkar_t")|| s.startsWith("sachin_t")){
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



	void mergeCatFiles(int start, int end,int outputnumber)
	{
		System.out.println("Reached here");
		int tempcount2=end-start+1;
		System.out.println("startfile: temp_c"+start);
		System.out.println("lastfile: temp_c"+end);
		System.out.println("Number of files: "+tempcount2);
		
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

	*/
	
	static String binarySearch(String word,String filename){
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
	
	



	public static void main_old(String[] args) {
		// TODO Auto-generated method stub
		int start=1;
		int end=1020;
		int outputnumber=1;
		int startcat=1;
		int endcat=51;
		int outputnumbercat=1;
		Merge m=new Merge();
		//m.mergeCatFiles(startcat, endcat, outputnumbercat);
		//m.mergeEntityFiles(start,end,outputnumber);
		m.mergeIndicesCatList(1, 51, 1);//3
		m.mergeIndicesEntityList(1, 648, 1);//4
		//m.mergeIndicesEntity(start, end, outputnumber);
		System.out.println("Done successfully!!!!!!");
		

	}
}