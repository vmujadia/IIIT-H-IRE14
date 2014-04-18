
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;

public class Search {
	public static void main(String args[]) throws IOException{
		System.out.println("Enter Query");

		BufferedReader br=new BufferedReader(new InputStreamReader(System.in));
		String input=br.readLine();
		br.close();
		String[] tokens=input.split(" ");
		LinkedHashMap<String,Integer> h1=new LinkedHashMap<String,Integer>();
		HashMap<String,Integer> h2=new HashMap<String,Integer>();
		HashMap<String,Integer> and=new HashMap<String,Integer>();
		HashMap<String,Integer> map = new HashMap<String,Integer>();
		ValueComparator bvc =  new ValueComparator(h2);
		TreeMap<String,Integer> sorted_map = new TreeMap<String,Integer>(bvc);
		ArrayList<String> queries=new ArrayList<String>();
		queries.addAll(Arrays.asList(tokens));
		LinkedHashSet<String> hh=new LinkedHashSet<String>();
		int count=0;
		for(String i:tokens){
			int weight=100;
			count++;
			char a=i.charAt(0);
			if(a<97 || a>122)
				a='{';
			String val=binarySearch(i+"_l","output_ranked/"+a+".txt");//lexical index created under index_e
			//System.out.println("val "+val);
			if(!val.equals("no")){
				String[] catids=val.split("#");
				for(String j:catids){
					weight--;
					h1.put(j,weight);
				}
			}
			if(count==1){
				for(String c:h1.keySet()){
					h2.put(c, 0);
				}
			}

			for(String c:h1.keySet()){
				if(h2.containsKey(c))
					and.put(c,h1.get(c)+h2.get(c));
			}
			h1.clear();
			h2.clear();
			h2.putAll(and);
			and.clear();
			//keep 'and'ing here the catids

		}
		sorted_map.putAll(h2);
		for(String key :sorted_map.keySet()) {
			hh.add(key);
		}
		//System.out.println("and "+sorted_map);
		//sorted_map.clear();
		if(sorted_map.size()>0){
			TreeSet<String> hhtemp=new TreeSet<String>(new LengthComparator());
			String value="";
			int ccount=0;
			LinkedHashMap<String,String> catmap=new LinkedHashMap<String,String>();
			for(String category:hh){
				ccount++;
				String catname=binarySearchFromCat(category.substring(0,category.length()-2),"cat_index_l.txt");
				System.out.println("category "+category+" : "+catname);
				hhtemp.add(catname);
				catmap.put(catname, category);
				if(ccount==5)
					break;
			}
			Iterator<String> it=hhtemp.iterator();

			while(it.hasNext())
			{
				int flag=0;
				value =(String)it.next();
				String val1=value;
				value=catmap.get(value);
				if(value.endsWith("_l") && !val1.startsWith("list of hindus")&&!val1.startsWith("list of mottos") && !val1.startsWith("list of file systems")){

					//h1 contains final catids
					//pick one and store it in categoryid
					String categoryid=value;
					System.out.println("cat "+categoryid);
					ArrayList<Long> offset=binarySearchCategory(categoryid.substring(0,categoryid.length()-2),"secondary_cl.txt");
					String line="";
					System.out.println("offset "+offset);
					try{
						RandomAccessFile file=null;
						FileChannel fc=null;
						try {
							file = new RandomAccessFile("output_cl.txt", "r");
							fc = file.getChannel();
						} catch (Exception e) {
							e.printStackTrace();
						}
						fc.position(offset.get(0));
						long differ;
						if(offset.get(1)!=offset.get(0))
							differ=offset.get(1)-offset.get(0);
						else
							differ=file.length()-offset.get(0);
						System.out.println(differ);
						MappedByteBuffer mp = fc.map(MapMode.READ_ONLY, offset.get(0), differ);
						byte[] buffer = new byte[(int)differ];
						mp.get(buffer);
						fc.close();
						file.close();
						BufferedReader in = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(buffer)));
						String newLine;
						for (newLine = in.readLine(); newLine != null; newLine = in.readLine()) {
							//System.out.println(categoryid+"    newline "+newLine);

							if(newLine.substring(0, newLine.indexOf("@")).equals(categoryid)){
								break;
							}
						}
						in.close();
						TreeSet<String> entities=new TreeSet<String>(new LengthComparator());
						//System.out.println("myline "+newLine);
						if(newLine==null)
							continue;
						String toks[]=newLine.split("@",2);
						for(String t:toks[1].split("#")){
							//if(t.endsWith("_l"))
							if(t.length()>5 && !queries.contains(t.substring(0, t.length()-2)) && !t.endsWith("px_l"))
								entities.add(t.substring(0,t.length()-2));
						}
						if(entities.size()>=15){
							Iterator<String> iter=entities.iterator();
							String ent="";
							int counter=0;
							while(iter.hasNext())
							{
								counter++;
								ent=(String)iter.next();
								//System.out.println(entities.size());
								if(counter>=(entities.size()/2-5) && counter<(entities.size()/2+5))
									System.out.println(ent);
								if(counter==(entities.size()/2+5)){
									flag=1;
									break;
								}
							}

						}
						else
							continue;
					}
					catch(IOException e){
						e.printStackTrace();
					}
				}
				if(flag==1)
					break;
			}
		}
		else{
			count=0;
			h1.clear();
			h2.clear();
			and.clear();
			sorted_map.clear();
			hh.clear();
			for(String i:tokens){
				int weight=100;
				count++;
				char a=i.charAt(0);
				if(a<97 || a>122)
					a='{';
				String val=binarySearch(i+"_t","index_e/"+a+".txt");//lexical index created under index_e
				//System.out.println("val "+val);
				if(!val.equals("no")){
					String[] catids=val.split("#");
					for(String j:catids){
						weight--;
						h1.put(j,weight);
					}
				}
				if(count==1){
					for(String c:h1.keySet()){
						h2.put(c, 0);
					}
				}

				for(String c:h1.keySet()){
					if(h2.containsKey(c))
						and.put(c,h1.get(c)+h2.get(c));
				}
				h1.clear();
				h2.clear();
				h2.putAll(and);
				and.clear();
				//keep 'and'ing here the catids

			}
			sorted_map.putAll(h2);
			for(String key :sorted_map.keySet()) {
				hh.add(key);
			}
			TreeSet<String> hhtemp=new TreeSet<String>(new LengthComparator());
			String value="";
			int ccount=0;
			LinkedHashMap<String,String> catmap=new LinkedHashMap<String,String>();
			for(String category:hh){
				ccount++;
				String catname=binarySearchFromCat(category.substring(0,category.length()-2),"cat_index.txt");
				System.out.println("category "+category+" : "+catname);
				hhtemp.add(catname);
				catmap.put(catname, category);
				//if(ccount==5)
					//break;
			}
			Iterator<String> it=hhtemp.iterator();

			while(it.hasNext())
			{
				int flag=0;
				
				value =(String)it.next();
				String val1=value;
				value=catmap.get(value);
				if(value.endsWith("_c") && !value.equals("920172_c") && !val1.endsWith("deaths_c") && !val1.endsWith("births_c")){

					//h1 contains final catids
					//pick one and store it in categoryid
					String categoryid=value;
					System.out.println("cat "+categoryid);
					ArrayList<Long> offset=binarySearchCategory(categoryid.substring(0,categoryid.length()-2),"secondary_c.txt");
					String line="";
					//System.out.println("offset "+offset);
					try{
						RandomAccessFile file=null;
						FileChannel fc=null;
						try {
							file = new RandomAccessFile("output_c.txt", "r");
							fc = file.getChannel();
						} catch (Exception e) {
							e.printStackTrace();
						}
						fc.position(offset.get(0));
						long differ;
						if(offset.get(1)!=offset.get(0))
							differ=offset.get(1)-offset.get(0);
						else
							differ=file.length()-offset.get(0);
						MappedByteBuffer mp = fc.map(MapMode.READ_ONLY, offset.get(0), differ);
						byte[] buffer = new byte[(int)differ];
						mp.get(buffer);
						fc.close();
						file.close();
						BufferedReader in = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(buffer)));
						String newLine;
						for (newLine = in.readLine(); newLine != null; newLine = in.readLine()) {
							//System.out.println("newline "+newLine);

							if(newLine.substring(0, newLine.indexOf("@")).equals(categoryid)){
								break;
							}
						}
						in.close();
						TreeSet<String> entities=new TreeSet<String>(new LengthComparator());
						//System.out.println("myline "+newLine);
						if(newLine==null)
							continue;
						String toks[]=newLine.split("@",2);
						for(String t:toks[1].split("#")){
							//if(t.endsWith("_l"))
							if(t.endsWith("_t") && t.length()>5 && !queries.contains(t.substring(0, t.length()-2)))
								entities.add(t.substring(0,t.length()-2));
						}
						//System.out.println("e "+entities);
						if(entities.size()>=15){
							Iterator<String> iter=entities.iterator();
							String ent="";
							int counter=0;
							while(iter.hasNext())
							{
								counter++;
								ent=(String)iter.next();
								//System.out.println(entities.size());
								if(counter>=(entities.size()/2-5) && counter<(entities.size()/2+5))
									System.out.println(ent);
								if(counter==(entities.size()/2+5)){
									flag=1;
									break;
								}
							}

						}
						else
							continue;
					}
					catch(IOException e){
						e.printStackTrace();
					}
				}
				if(flag==1)
					break;
			}
		}


	}


	static String binarySearchFromCat(String word,String filename)
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
			if(line.substring(line.indexOf('@')+1,line.length()-2).compareTo(word)==0)
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
					//System.out.println("sdfgd "+linep.substring(linep.indexOf('@')+1,linep.length()-2));
					if(Long.parseLong(linep.substring(linep.indexOf('@')+1,linep.length()-2))==Long.parseLong(word))
					{
						line=linep;
						nextLine=linen;
						break;
					}
					if(Long.parseLong(linen.substring(linen.indexOf('@')+1,linen.length()-2))==Long.parseLong(word))
					{
						line=linen;
						break;
					}

					if((Long.parseLong(linen.substring(linen.indexOf('@')+1,linen.length()-2))>Long.parseLong(word))&&(Long.parseLong(linep.substring(linep.indexOf('@')+1,linep.length()-2))<Long.parseLong(word)))
					{
						line=linep;
						nextLine=linen;
						break;
					}	

					if(Long.parseLong(linep.substring(linep.indexOf('@')+1,linep.length()-2))>Long.parseLong(word))
					{

						end=mp;

					}

					if(Long.parseLong(linen.substring(linen.indexOf('@')+1,linen.length()-2))<Long.parseLong(word))
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
		return line.split("@")[0];
	}



	static ArrayList<Long> binarySearchCategory(String word,String filename)
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
					//System.out.println(word+"          "+linen);
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

}
class MyComp implements Comparator<String>{

	public int compare(String n1, String n2) {
		if(Long.parseLong(n1) > Long.parseLong(n2)){
			return 1;
		} else {
			return -1;
		}
	}
}

class LengthComparator implements Comparator<String>{

	public int compare(String n1, String n2) {

		if(n1.length() >= n2.length()){
			return 1;
		} else {
			return -1;
		}
	}
}

class ValueComparator implements Comparator<String> {

	Map<String, Integer> base;
	public ValueComparator(Map<String, Integer> base) {
		this.base = base;
	}  
	public int compare(String a, String b) {
		if (base.get(a) >= base.get(b)) {
			return -1;
		} else {
			return 1;
		} 
	}
}