
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;

public class Search {
	public static void main_ld(String args[]) throws IOException{
	//	System.out.println("hello");
		BufferedReader br=new BufferedReader(new InputStreamReader(System.in));
		String input=br.readLine();
		br.close();
		String[] tokens=input.split(" ");
		HashSet<String> h1=new HashSet<String>();
		HashSet<String> h2=new HashSet<String>();
		HashSet<String> and=new HashSet<String>();
		int count=0;
		for(String i:tokens){
			count++;
			char a=i.charAt(0);
			if(a<97 || a>122)
				a='{';
			String val=binarySearch(i+"_t","index_e/"+a+".txt");//lexical index created under index_e
			//System.out.println("val "+val);
			if(!val.equals("no")){
				String[] catids=val.split("#");
				for(String j:catids)
					h1.add(j);	
			}
			if(count==1)
				h2.addAll(h1);
			for(String c:h1){
				if(h2.contains(c))
					and.add(c);
			}
			h1.clear();
			h2.clear();
			h2.addAll(and);
			and.clear();
			//keep 'and'ing here the catids	

		}
		//System.out.println("and "+h2);
		Iterator it=h2.iterator();
		 String value="";
        while(it.hasNext())
        {
           value =(String)it.next();
            if(value.endsWith("_c"))
            	break;
            
        }
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
			//	System.out.println("newline "+newLine);

			      if(newLine.substring(0, newLine.indexOf("@")).equals(categoryid)){
			    	  break;
			      }
			    }
			in.close();
			//System.out.println("myline "+newLine);
			String toks[]=newLine.split("@",2);
			for(String t:toks[1].split("#")){
				if(t.endsWith("_t"))
					System.out.println(t);
			}
		}
		catch(IOException e){
			e.printStackTrace();
		}

		//newLine contains all entities separated by #
		//select top 10(mostly _t's) and print
		

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