import edu.stanford.nlp.ling.Sentence;
import edu.stanford.nlp.ling.TaggedWord;
import edu.stanford.nlp.ling.HasWord;
import edu.stanford.nlp.tagger.maxent.MaxentTagger;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FeaturesExtract {

	public static void main(String[] args) throws Exception {
	    String inp="/media/LENOVO/Dataset/newdata/cornell/student_text";
	    String out="/media/LENOVO/Dataset/newdata/cornell/student_rel";   
	    File f=new File(inp);
		File[] li=f.listFiles();
	    String mdl="/home/kumar/stanford-postagger-2010-05-26/models/left3words-wsj-0-18.tagger";
	    MaxentTagger tagger = new MaxentTagger(mdl);
	    for(int p=0;p<li.length;p++){
	    	BufferedReader br=new BufferedReader(new FileReader(inp+"/"+li[p].getName()));
	    	String st=br.readLine();
	    	st=tagger.tagString(st);
	    	String sents[]=st.split(" \\. ");
	    	System.out.println(st);
	    	Pattern NE=Pattern.compile("<Person>[a-zA-Z. ,/]+</Person>|<Organization>[a-zA-Z0-9\\. ,/]+</Organization>|<Location>[a-zA-Z0-9\\. ,/]+</Location>|<Course>[A-Z /]+[A-Z][A-Z][ ]*[0-9]+[/0-9]*[A-Z /]+</Course>+|<Email>[/A-Z ]+[a-zA-Z0-9]+@[a-zA-Z0-9]+\\.[a-zA-Z0-9]+\\.[a-zA-Z0-9]+[/ A-Z]+</Email>");
	    	PrintWriter pr = null;
	    	pr = new PrintWriter(out+"/"+li[p].getName());
	    	for(String s: sents){
	    		Matcher m1 = NE.matcher(s);
	    		int ind[];
	    		ind= new int[1000];
	    		int cnt=0;
	    		while(m1.find()){
	    			System.out.println(m1.group());
	    			ind[cnt]=m1.start();
	    			ind[cnt+1]=m1.end();
	    			cnt+=2;
	    		}
	    		int fl=0;
	    		while(ind[fl]!=0){
	    			fl++;
	    		}
	    		TreeMap <String, Integer> tMap = new TreeMap<String , Integer>();
	    		for(int i=0;i<fl-1;i=i+2){
	    			for(int j=i+2;j<fl-1;j=j+2)
	    			{
	    				String s1=  s.substring(ind[i], ind[i+1]).replaceAll("/[A-Z:]+ ", " ");
	    				s1=s1.replaceAll("<[a-zA-Z]+>", "");
	    				s1=s1.replaceAll("</[a-zA-Z]+>", "");
	    				String s2=  s.substring(ind[j], ind[j+1]).replaceAll("/[A-Z:]+ ", " ");
	    				s2=s2.replaceAll("<[a-zA-Z]+>", "");
	    				s2=s2.replaceAll("</[a-zA-Z]+>", "");
	    				String temp="";
	    				if(j-i==2)
	    				{
	    					if(s.substring(ind[i+1], ind[j]).length()<50)
	    					{
	    						temp=s1+"	"+s.substring(ind[i+1], ind[j])+"   "+s2;
	    						if(tMap.containsKey(temp))
	    							tMap.put(temp, tMap.get(temp)+1);
	    						else
	    							tMap.put(temp, 1);
	    						//pr.println(s1+"	"+s.substring(ind[i+1], ind[j])+"   "+s2);
	    					}
	    				}
	    				else
	    				{	
	    					temp=s1+"	   "+s2;
	    					if(tMap.containsKey(temp))
    							tMap.put(temp, tMap.get(temp)+1);
    						else
    							tMap.put(temp, 1);
	    					//pr.println(s1+"	   "+s2);
	    				}
	    			}
	    		}
	    		Set st1= tMap.entrySet();
	    		Iterator it1= st1.iterator();
		    	int tr=0;
		    	while (it1.hasNext()){
		    		Map.Entry me = (Map.Entry)it1.next();
		    		pr.println(me.getValue()+" : "+me.getKey());	    						    		
	    		}
		    	tMap.clear();
	    	}
	    	pr.flush(); pr.close();
	    	//}
	    }
	  }
}
/*for(int i=0;i<(fl-3);i+=2){
String ent1=s.substring(ind[i], ind[i+1]);
String rel=s.substring(ind[i+1], ind[i+2]);
String ent2=s.substring(ind[i+2], ind[i+3]);
pr.println("[ "+ent1+" , "+ent2+" ] "+"---------"+rel);
//System.out.println("[ "+ent1+" , "+ent2+" ] "+"---------"+rel);
}*/	
