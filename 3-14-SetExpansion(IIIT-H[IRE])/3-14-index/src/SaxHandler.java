

import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import edu.stanford.nlp.tagger.maxent.MaxentTagger;

/**
 *
 * @author vandan
 */




class SaxHandler extends DefaultHandler {

    
    boolean id_check=false;
    boolean end_check=false;
    public static int id = 0;
    
    String current_tag="";
    String title="";
    String lastChange_id="";
    StringBuffer text= new StringBuffer();
    HashMap<String,Integer> Swords = new HashMap<String,Integer>();
    StringBuffer Infobox= new StringBuffer();
    StringBuffer Textbox= new StringBuffer();
    StringBuffer Category= new StringBuffer();
    StringBuffer Taxobox= new StringBuffer();
    StringBuffer Title= new StringBuffer();
    HashSet Catagory_Catagory = new HashSet();
    HashSet Catagory_lexical = new HashSet();
	HashSet List_Catagory = new HashSet();
	HashSet List_lexical = new HashSet();
	HashSet List_g_Catagory = new HashSet();
	HashSet List_g_lexical = new HashSet();
    MaxentTagger tagger;
    
    //constructor
    public SaxHandler() {
       
        tagger = new MaxentTagger("stanford-postagger-2014-01-04/models/english-left3words-distsim.tagger");
        Swords.put("a", 0);
        Swords.put("about", 0);
        Swords.put("after", 0);
        Swords.put("align", 0);
        Swords.put("all", 0);
        Swords.put("also", 0);
        Swords.put("amp", 0);
        Swords.put("an", 0);
        Swords.put("and", 0);
        Swords.put("are", 0);
        Swords.put("as", 0);
        Swords.put("at", 0);
        Swords.put("b", 0);
        Swords.put("be", 0);
        Swords.put("been", 0);
        Swords.put("between", 0);
        Swords.put("bgcolor", 0);
        Swords.put("both", 0);
        Swords.put("br", 0);
        Swords.put("but", 0);
        Swords.put("by", 0);
        Swords.put("c", 0);
        Swords.put("category", 0);
        Swords.put("center", 0);
        Swords.put("com", 0);
        Swords.put("d", 0);
        Swords.put("do", 0);
        Swords.put("e", 0);
        Swords.put("f", 0);
        Swords.put("flagicon", 0);
        Swords.put("for", 0);
        Swords.put("from", 0);
        Swords.put("g", 0);
        Swords.put("gt", 0);
        Swords.put("h", 0);
        Swords.put("has", 0);
        Swords.put("have", 0);
        Swords.put("he", 0);
        Swords.put("her", 0);
        Swords.put("his", 0);
        Swords.put("html", 0);
        Swords.put("http", 0);
        Swords.put("i", 0);
        Swords.put("id", 0);
        Swords.put("if", 0);
        Swords.put("image", 0);
        Swords.put("img", 0);
        Swords.put("in", 0);
        Swords.put("is", 0);
        Swords.put("isbn", 0);
        Swords.put("it", 0);
        Swords.put("its", 0);
        Swords.put("j", 0);
        Swords.put("jpg", 0);
        Swords.put("k", 0);
        Swords.put("l", 0);
        Swords.put("like", 0);
        Swords.put("it", 0);
        Swords.put("m", 0);
        Swords.put("more", 0);
        Swords.put("most", 0);
        Swords.put("n", 0);
        Swords.put("nbsp", 0);
        Swords.put("new", 0);
        Swords.put("no", 0);
        Swords.put("not", 0);
        Swords.put("o", 0);
        Swords.put("of", 0);
        Swords.put("on", 0);
        Swords.put("one", 0);
        Swords.put("or", 0);
        Swords.put("org", 0);
        Swords.put("p", 0);
        Swords.put("q", 0);
        Swords.put("quot", 0);
        Swords.put("r", 0);
        Swords.put("rd", 0);
        Swords.put("ref", 0);
        Swords.put("rowspan", 0);
        Swords.put("s", 0);
        Swords.put("said", 0);
        Swords.put("say", 0);
        Swords.put("she", 0);
        Swords.put("so", 0);
        Swords.put("t", 0);
        Swords.put("td", 0);
        Swords.put("th", 0);
        Swords.put("that", 0);
        Swords.put("the", 0);
        Swords.put("their", 0);
        Swords.put("they", 0);
        Swords.put("this", 0);
        Swords.put("to", 0);
        Swords.put("u", 0);
        Swords.put("url", 0);
        Swords.put("us", 0);
        Swords.put("v", 0);
        Swords.put("vandan", 0);
        Swords.put("w", 0);
        Swords.put("was", 0);
        Swords.put("we", 0);
        Swords.put("were", 0);
        Swords.put("what", 0);
        Swords.put("when", 0);
        Swords.put("which", 0);
        Swords.put("who", 0);
        Swords.put("will", 0);
        Swords.put("with", 0);
        Swords.put("would", 0);
        Swords.put("www", 0);
        Swords.put("x", 0);
        Swords.put("y", 0);
        Swords.put("you", 0);
        Swords.put("z", 0);
        Swords.put("km", 0);
        Swords.put("sq", 0);
        Swords.put("mi", 0);
    }
    
    

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        String value = new String(ch, start, length);        
        if(!end_check && current_tag.equals("title")){
            title=value;
            id_check=true;
        }
        else if(!end_check && id_check && current_tag.equals("id"))
        {   
            lastChange_id=value;         
            id=id+1;
            id_check=false;
        }
        else if(!end_check && current_tag.equals("text"))
        {
            
            text.append(value);
        }
    }
    

    //Method for xml end Element
    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        end_check=true; 
        if ("page".equals(qName)) {
            this.textProcessing(text.toString().trim(), title);
            Index i=new Index();
            text.delete(0, text.length());
            title="";
            i.processMap(Catagory_lexical, Catagory_Catagory);
            i.processListMap(List_g_lexical,List_g_Catagory);
            Catagory_Catagory.clear();
            Catagory_lexical.clear();
            //List_g_Catagory.clear();
            //List_g_lexical.clear();
            
        }
    }
    
    
    
    
    

    //Method for start element
    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        end_check=false;
        current_tag=qName;
    }
    //Method for end document
    @Override
    public void endDocument() throws SAXException {
        
    }
    //Method for start document
    @Override
    public void startDocument() {
    }
    
    //Method for text process
    public void textProcessing(String text, String title) {
    	title=this.convertWord(title);
    	//if(title.contains("list of")||title.contains("list of"))     
    		//System.out.println("Title       ----->  "+title);
        text=this.convertWord(text);
        pos=0;
        boolean TaxoBox=false;
        boolean sys=false;
        boolean taxo_take_flag=false;
		boolean wikitable=false;
		boolean ListFlag=false;
		boolean intro_flag=false;
    	int count1 = 0;
    	int count=0;
    	int sys_count=0;
    	int intro_count=0;
    	int c_list=0;
    	String app_p="";
    	String list_cat="";
    	Index index=new Index();
    	
    	Catagory_lexical.add(new String(title.replaceAll("[^A-Za-z ]", " ").replaceAll("( )+", " ").trim()+"_t"));
    	//Taxo1_Catagory.add(new String(title.trim()+" synonyms"));
    	List_g_Catagory.add(new String(title.replaceAll("[^A-Za-z ]", " ").replaceAll("( )+", " ").trim()+"_l"));
    	
    	
        while ((eend = text.indexOf('\n', pos)) >= 0) 
        {        	
                	app=text.substring(pos, eend).trim();
                	app=app.trim();
                	
                	
            //code to extract infobox
                	
                	if(app.matches("\\{\\{infobox.*"))
                    {
                		if(app.matches(".*\\|.*"))
                		{
                			app=app.replaceAll("\\|", "@#");
                			app=app.split("@#")[0];
                			
                		}
                		else if(app.length()>60)
                		{
                			app=app.split(" ")[1];
                		}
                		
                		app=app.replace("{{infobox", "");
                		app=app.replaceAll("[^a-zA-Z0-9//|//(//), ]", "");
                		if(app.length()>2)
                			Catagory_Catagory.add(new String(app.replaceAll("\n", " ").replaceAll("( )+", " ").trim()+"_i"));
                		////System.out.println(app);
 
                    }
                	
            //end code to extract infobox 	

                	
                	
                	
                	
           //code to extract geobox
                	if(app.matches(".*\\{\\{geobox.*"))
                    {
                		Catagory_Catagory.add(new String("river"+"_t"));
                		////System.out.println("river");
                    }
          //end code to extract geobox 	
                	
                	
                	
                	
         //code to extract taxobox
                	if(app.matches(".*\\{\\{taxobox.*")||TaxoBox)
                    {
                		TaxoBox=true;
                    	String findStr = "{{";
                    	int lastIndex = 0;

                    	while (lastIndex != -1) {

                    		lastIndex = app.indexOf(findStr, lastIndex);

                    		if (lastIndex != -1) {
                    			count++;
                    			lastIndex += findStr.length();
                    		}
                    	}
                    	findStr = "}}";
                    	lastIndex = 0;

                    	while (lastIndex != -1) {

                    		lastIndex = app.indexOf(findStr, lastIndex);

                    		if (lastIndex != -1) {
                    			count1++;
                    			lastIndex += findStr.length();
                    		}
                    	}
                    	if(count==count1)
                    		TaxoBox=false;
                    	String fi=app;
                    	app=app.replaceAll("[^a-zA-Z=\\| ]", "");
                    	String temp=convertWord(app);
                  		if(app.matches(".*phylum.*"))
                  		{
                 			taxo_take_flag=true;
                 			sys=false;
                 		}
                  		else if(app.matches(".*classis.*"))
                  		{
                 			taxo_take_flag=true;
                 			sys=false;
                 		}
                  		else if(app.matches(".*phylum.*"))
                  		{
                 			taxo_take_flag=true;
                 			sys=false;
                 		}
                  		else if(app.matches(".*familia.*"))
                  		{
                 			taxo_take_flag=true;
                 			sys=false;
                 		}
                  		else if(app.matches(".*genus.*"))
                  		{
                 			taxo_take_flag=true;
                 			sys=false;
                 		}
                  		else if(app.matches(".*species.*"))
                  		{
                 			taxo_take_flag=true;
                 			sys=false;
                 		}
                  		else if(app.matches(".*binomial.*"))
                  		{
                 			taxo_take_flag=true;
                 			sys=false;
                 		}
                  		else if(app.matches(".*regnum.*"))
                  		{
                 			taxo_take_flag=true;
                 			sys=false;
                 		}
                  		else if(app.matches(".*binomial_authority.*"))
                  		{
                 			taxo_take_flag=true;
                 			sys=false;
                 		}
                  		else if(app.matches(".*unranked_divisio.*"))
                  		{
                 			taxo_take_flag=true;
                 			sys=false;
                 		}
                  		else if(app.matches(".*unranked_classis.*"))
                  		{
                 			taxo_take_flag=true;
                 			sys=false;
                 		}
                  		else if(app.matches(".*unranked_ordo.*"))
                  		{
                 			taxo_take_flag=true;
                 			sys=false;
                 		}	
                  		else if(app.matches(".*binomial_authority.*"))
                  		{
                 			taxo_take_flag=true;
                 			sys=false;
                 		}
                  		else if(app.matches(".*divisio.*"))
                  		{
                 			taxo_take_flag=true;
                 			sys=false;
                 		}
                  		else if(app.matches(".*synonyms.*")||sys)
                  		{
                  			sys=true;
                  			if(app.contains("=")&&sys_count!=0)
                  				sys=false;
                  			sys_count++;
                  			if(sys)
                  			{
                  				//if(fi.length()>2)
                  				//Catagory_lexical.add(new String(fi.trim()+"_i"));
                  				////System.out.println(fi);(This is lexical item categories must be page title)
                  			}
                  					
                  		}
                  		if(taxo_take_flag)
                  		{
                  			if(temp.contains("="))
                  			{
                  				String temp1[]=temp.split("=");
                  				if(temp1.length>1)
                  				if(temp1[1].matches(".*\\|.*"))
                  				{
                  					String temp2[]=temp1[1].trim().split("\\|");
                  					for(int j=0;j<temp2.length;j++)
                  					{
                  						if(temp2[j].length()>2)
                  						Catagory_Catagory.add(new String(temp2[j].replaceAll("[^a-zA-Z ]", " ").replaceAll("\n", " ").replaceAll("( )+", " ").trim()+"_t"));
                  						////System.out.println(temp2[j].trim());
                  					}
                  				}
                  				else
                  				{
                  					if(temp1[1].length()>2)
                  					Catagory_Catagory.add(new String(temp1[1].replaceAll("[^a-zA-Z ]", " ").replaceAll("\n", " ").replaceAll("( )+", " ").trim()+"_t"));
                  					////System.out.println(temp1[1].trim());
                  				}
                  			}
                  		}
                    }
                	if(!TaxoBox)
                	{
                									//call adding function here 
                	}
          //end code to extract taxobox
                	
                	
                	
          //StaRt Extract Introduction
              	  
                  	  if(app_p.trim().matches("}}.*")&&app.matches(".*'''.*'''.*")&&intro_count==0)
                  	  {
                  		  intro_count++;
                  		  intro_flag=true;
                  	  }
                      if(app.matches("==.*=="))
                      {
                    	  intro_flag=false;
                      }
                       if(intro_flag)
                       {
                    	   String tagged = tagger.tagString(convertWord(app.trim().replaceAll("[^a-zA-Z ]", " ").replaceAll("( )+", " ")));
                       	   if(tagged.contains(" "))
                       	   {
	                    	   String temp_1[]= tagged.split(" ");
	                       	   for(int i=0;i<temp_1.length;i++)
	                       	   {
	                       		   if(temp_1[i].contains("_"))
	                       		   {
	                       			   String temp2[]=temp_1[i].split("_");
	                       			   if(temp2[1].equals("NNS") || temp2[1].equals("NNP") || temp2[1].equals("NNPS"))
	                       			   {
	                       				   if(temp2[0].length()>2)
	                       				   {
	                       					   Catagory_lexical.add(new String(temp2[0].replaceAll("\n", " ").replaceAll("( )+", " ").trim()+"_i"));
	                       					   //System.out.println(temp2[0].trim()+"_i");
	                       				   }
	                       				   ////lexical_items.add(new String(temp2[0]));
	                       			   }
	                       		   }	
	          			  		
	                       	   }
                       	   }
                       }
              	  		
        // EnD Extract Introduction
                       
                	
          	
        // start Extract List of 
                       
                       
                       
                  if(title.contains("list of")||title.contains("lists of"))     
                  {
                	  	   String val=app.replaceAll("&ndash;", "").trim();
            			   val=convertWord(val).trim();
            			   if(val.matches("==.*==.*"))
            			   {
            				   if(val.contains("notes")||val.contains("bibliography")||val.contains("external links")||val.contains("see also")||val.contains("sources")||val.contains("references"))
            				   {
            					   ListFlag=false;
            				   }
            				   else
            				   {
            					   
            					   ////System.out.println("LIST TITLE    "+val+"\n"); 
            					   ListFlag=true;
            					   
            					   
            					   
            					   c_list++;
               				    
            					   if(c_list!=1)
            					   {
            						   
            						   //in.processMap(cat_list, cat);
            						   index.processListMap(List_lexical, List_Catagory);
            						   List_Catagory.clear();
            						   List_lexical.clear();
            						   
            					   }
            					   
            					  
            					   
            					   if(title.trim().length()>3)
            						   List_Catagory.add(title.trim()+" "+val.replaceAll("[^a-zA-Z ]", " ").replaceAll("\n", " ").replaceAll("( )+", " ").trim()+"_l");
            					   
            					   
            					   list_cat=val;
            					   
            					   
            				   }
            			   }
            			   else
            			   {
            				   if(ListFlag)
            				   {
            					   if(!val.contains("list"))
            					   if(val.contains("*"))
            					   {
            						   val=val.replaceAll("[^a-zA-Z*\\|'-� \\[\\]]", " ").replaceAll("( )+", " ");
            						   if(val.length()>2)
            						   {
                						   //if(val.length()>65)
                						   //{
                							   //System.out.println(val+"     "+val.length());  
                						   //}
                						   if(val.matches(".*\\'\\'\\'.*"))
                						   {
                							   String pattern = ".*'''(.+)'''.*";
                							   String updated = val.replaceAll(pattern, "$1"); 
                							   ////System.out.println("a9    "+updated.replaceAll("[^a-zA-Z ]", " "));
                							   
                							   if(updated.trim().length()>2)
                							   {
                							   List_lexical.add(new String(updated.trim()+"_l"));
                							   List_g_lexical.add(new String(updated.trim()+"_l"));
                							   }
                						   }
                						   /*
                						   else if(val.length()>150)
                						   {
                							   //System.out.println(val+"     "+val.length()); 
                							   if(val.matches(".*\\[.*"))
                							   {
                							   char v2[]=val.toCharArray();
                							   boolean s1=false;
                							   String up_f="";
                							   for(char v1:v2)
                							   {
                								   if(v1=='[')
                								   {
                									   s1=true;
                								   }
                								   if(s1)
                								   	up_f+=v1;
                								   if(v1==']')
                								   {
                									   s1=false;
                									   break;
                								   }
                							   }
                							   val=up_f.replaceAll("\\[", "").replaceAll("\\]", "");
                							   ////System.out.println(val);
                							  }
                						   }
                						   */
                						   else
                						   {
                							   val=val.replace("*", "").trim();
                							   char v2[]=val.toCharArray();
                							   boolean s1=false;
                							   String up_f="";
                							   int count_v1=0;
                							   for(char v1:v2)
                							   {
                								   if(v1=='[')
                								   {
                									   count_v1++;
	            									   if(count_v1==2)
	            										   s1=true;
                								   }
                								   if(s1)
                									   up_f+=v1;
                								   if(v1==']')
                								   {
                									   s1=false;
                									   break;
                								   }
                							   }
                							   val=up_f.replaceAll("\\[", "").replaceAll("\\]", "");
                							   if(val.matches(".*�.*"))
                							   {
                								   String val1=val;
                								   if(val1.matches(".*�.*"))
                								   {
                									   	val1=val1.split("�")[0];
                								   }
                								   else if(val1.matches(".*-.*"))
                								   {
                									   	val1=val1.split("-")[0];
                								   }
                								   if(val1.matches(".*\\|.*"))
                    							   {
                    								   String t1[]=val1.split("\\|");
                    								   for(String t:t1)
                    								   {
                    									   ////System.out.println("a8    "+t);
                    									   if(t.trim().length()>2)
                    									   {
                    										   List_lexical.add(new String(t.trim()+"_l"));
                    										   List_g_lexical.add(new String(t.trim()+"_l"));
                    									   }
                    								   }
                    							   }
                    							   else
                    							   {
                    								   if(val1.trim().length()>2)
                    								   {
                    								   List_lexical.add(new String(val1.trim()+"_l"));
                    								   List_g_lexical.add(new String(val1.trim()+"_l"));
                    								   }
                    								   ////System.out.println("a7    "+val1);
                    							   }
                							   }
                							   else
                							   {
                								   if(val.matches(".*\\|.*"))
                    							   {
                    								   String t1[]=val.split("\\|");
                    								   for(String t:t1)
                    								   {
                    									   if(t.trim().length()>2)
                    									   {
                    										   List_lexical.add(new String(t.trim()+"_l"));
                    										   List_g_lexical.add(new String(t.trim()+"_l"));
                    									   }
                    									   //List_lexical.add(new String(t.trim()));
                    									   ////System.out.println("a6    "+t);
                    								   }
                    							   }
                    							   else
                    							   {
                    								   if(val.trim().length()>2)
                    								   {
                    								   List_lexical.add(new String(val.trim()+"_l"));
                    								   List_g_lexical.add(new String(val.trim()+"_l"));
                    								   }
                    								   //List_lexical.add(new String(val.trim()));
                    								   ////System.out.println("a5    "+val);
                    							   }  
                							   }
                						   }
            						   }
            					   }
            					   else
            					   {
            						   if(val.contains("wikitable"))
            						   {
            							   wikitable=true;
            						   }
            						   if(wikitable)
            						   {
            							   val=val.replaceAll("</br>", "").replaceAll("jpg", "").replaceAll("80px", "").replaceAll("file", "");
            							   if(val.matches(".*\\[.*")&&(!val.contains("image")))
            							   {
	            							   val=val.trim();
	            							   char v2[]=val.toCharArray();
	            							   boolean s1=false;
	            							   int count_v1=0;
	            							   String up_f="";
	            							   for(char v1:v2)
	            							   {
	            								   if(v1=='[')
	            								   {
	            									   count_v1++;
	            									   if(count_v1==2)
	            										   s1=true;
	            								   }
	            								   if(s1)
	            									   up_f+=v1;
	            								   if(v1==']')
	            								   {
	            									   s1=false;
	            									   break;
	            								   }
	            							   }
	            							   val=up_f.replaceAll("\\[", "").replaceAll("\\]", "").replaceAll("[^a-zA-Z0-9' ]", " ").replaceAll("( )+", " ");
	            							   if(val.matches(".*�.*"))
	            							   {
	            								   String val1=val;
	            								   if(val1.matches(".*�.*"))
	            								   {
	            									   	val1=val1.split("�")[0];
	            								   }
	            								   else if(val1.matches(".*-.*"))
	            								   {
	            									   	val1=val1.split("-")[0];
	            								   }
	            								   if(val1.matches(".*\\|.*"))
	                							   {
	                								   String t1[]=val1.split("\\|");
	                								   for(String t:t1)
	                								   {
	                									   if(t.trim().length()>2)
	                    								   {
	                    								   List_lexical.add(new String(t.trim()+"_l"));
	                    								   List_g_lexical.add(new String(t.trim()+"_l"));
	                    								   }
	                									   //List_lexical.add(new String(t.trim()));
	                									   ////System.out.println("a4    "+t);
	                								   }
	                							   }
	                							   else
	                							   {
	                								   if(val1.trim().length()>2)
                    								   {
                    								   List_lexical.add(new String(val1.trim()+"_l"));
                    								   List_g_lexical.add(new String(val1.trim()+"_l"));
                    								   }
	                								   //List_lexical.add(new String(val1.trim()));
	                								   ////System.out.println("a3    "+val1);
	                							   }
	            							   }
	            							   else
	            							   {
	            								   if(val.matches(".*\\|.*"))
	                							   {
	                								   String t1[]=val.split("\\|");
	                								   
	                								   for(String t:t1)
	                								   {
	                									   if(t.trim().length()>2)
	                    								   {
	                    								   List_lexical.add(new String(t.trim()+"_l"));
	                    								   List_g_lexical.add(new String(t.trim()+"_l"));
	                    								   }
	                									   //List_lexical.add(new String(t.trim()));
	                									   ////System.out.println("a2    "+t);
	                								   }
	                							   }
	                							   else
	                							   {
	                								   if(val.trim().length()>2)
                    								   {
                    								   List_lexical.add(new String(val.trim()+"_l"));
                    								   List_g_lexical.add(new String(val.trim()+"_l"));
                    								   }
	                								   //List_lexical.add(new String(val.trim()));
	                								   ////System.out.println("a1    "+val);
	                							   }  
	                							   
	            							   }
            							   }
            						   }
            						   
            						   if(val.contains("|}"))
            						   {
            							   wikitable=false;
            						   }
            						   
            					   }   
            				   }
            			   }
                       
                  }    
                       
             //EnD Extract List of
                	
                	
                	
                	
                	
                	
                	
                	
                	
                	
                	
                	
                	
                	
                	
                	
                	
                	
                	
                	
                	
                	
                	
                	
                	
                	
                	
                	
                	
                	
                	
                	
                	
               //code to exctract category
                    	if(app.matches("\\[\\[category:.*\\]\\].*"))
                  		if(app.matches(".*category.*"))
                        {
                    		String f1=app.replace("category:", "").replaceAll("[^a-z//-A-Z0-9//|//(//), ]", "");
                    		if(f1.matches(".*\\|.*"))
                    		{
                    			String f2[]=f1.split("\\|");
                    			for(String f3:f2)
                    			{
                    				f3=f3.trim();
                    				if(f3.length()>0)
                    				{
                    					if(f3.trim().length()>2)
                    						Catagory_Catagory.add(new String(f3.replaceAll("\n", " ").replaceAll("( )+", " ").trim()+"_c"));
                    					//System.out.println(f3);
                    				}
                    			}
                    		}
                    		else
                    		{
                    			if(f1.trim().length()>2)
                    				Catagory_Catagory.add(new String(f1.replaceAll("\n", " ").replaceAll("( )+", " ").trim()+"_c"));
                    			//System.out.println(f1);
                    		}
                        }
               //end code to exctract category
                    	
                    
                    	
                    
                    	
                    	
                    	
                    	
                    	
                    	
                    	
                    	
         if(app.length()>0) 	
        	 app_p=app;       	
         pos = eend + 1;
        }
        /*
        index.processListMap(List_lexical, List_Catagory);
        List_Catagory.clear();
		List_lexical.clear();*/

    }

     
    
    
   
    
String app;int pos = 0, eend;
private static final String[] REPLACEMENT = new String[Character.MAX_VALUE+1];
static {
    for(int i=Character.MIN_VALUE;i<=Character.MAX_VALUE;i++)
        REPLACEMENT[i] = Character.toString(Character.toLowerCase((char) i));
}





private static final String[] REPLACEMENT1 = new String[Character.MAX_VALUE+1];
static {
    for(int i=Character.MIN_VALUE;i<=Character.MAX_VALUE;i++)
        REPLACEMENT1[i] = Character.toString(Character.toLowerCase((char) i));
        REPLACEMENT1[' '] =  "";
}






static int doc_word_count=0;
public String convertWord(String word) {
    StringBuilder sb = new StringBuilder(word.length());
    for(int i=0;i<word.length();i++)
        sb.append(REPLACEMENT[word.charAt(i)]);
    return sb.toString();
} 
public String convertWord1(String word) {
    StringBuilder sb = new StringBuilder(word.length());
    for(int i=0;i<word.length();i++)
        sb.append(REPLACEMENT1[word.charAt(i)]);
    return sb.toString();
} 
   
}
