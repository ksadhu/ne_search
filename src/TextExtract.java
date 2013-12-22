import net.htmlparser.jericho.*;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.*;
import java.net.*;

public class TextExtract {
	public static void main(String[] args) throws Exception {
		
		MicrosoftTagTypes.register();
		PHPTagTypes.register();
		PHPTagTypes.PHP_SHORT.deregister(); // remove PHP short tags for this example otherwise they override processing instructions
		MasonTagTypes.register();
		NEtagger.GateIntialize();
		String inp="/media/LENOVO/Dataset/newdata/cornell/project/";
		String out="/media/LENOVO/Dataset/newdata/cornell/project_text";
		File f=new File(inp);
		File[] li=f.listFiles();
		for(int p=0;p<li.length;p++)
		{
			System.out.println(li[p].getAbsolutePath());
			Source source=new Source(new URL("file:"+li[p].getAbsolutePath()));
			source.fullSequentialParse();
			String text=source.getTextExtractor().setIncludeAttributes(true).toString();
			text=NEtagger.tagString(text);
			text=text.replaceAll("&lt;![A-Z][A-Z][0-9][0-9]*&gt;", "");
			Pattern pt= Pattern.compile("<Person>[a-zA-Z \\.]+</Person>");
			Pattern cid=Pattern.compile(" [A-Z][A-Z][ ]*[0-9]+[/0-9]* ");
			Pattern email=Pattern.compile("[A-Z][A-Z][0-9]+[/A-Z]+ |[a-zA-Z0-9]+@[a-zA-Z0-9]+\\.[a-zA-Z0-9]+\\.[a-zA-Z0-9]+");
			Matcher m=pt.matcher(text);
			Matcher m1=cid.matcher(text);
			Matcher m2=email.matcher(text);
			int cnt=0,i=0,j=0;
			String name="";
			while(m.find()){
				if(cnt==1)
					break;
				name=m.group();
				cnt++;
			}
			String course="";
			while(m1.find()){
				i++;
				course=m1.group();
			}
			if(i>0){
				text=text.replaceAll(course, "<Course>"+course+"</Course>");
			}
			while(m2.find()){
				text=text.replaceAll(m2.group(), "<Email>"+m2.group()+"</Email>");	
			}
			System.out.println(name);
			text=text.replaceAll("I ", name);
			text=text.replaceAll("[mM]y ", name);
			
			
			PrintWriter pr = null;
			pr = new PrintWriter(out+"/"+li[p].getName());
			pr.println(text);
			pr.flush(); pr.close();
		}
		/*String lis[]=text.split("[a-z0-9]\\. ");
		System.out.println("No of lines : "+lis.length);
		for(String s: lis)
			System.out.println("String : "+s.trim());*/
  }

	private static String getTitle(Source source) {
		Element titleElement=source.getFirstElement(HTMLElementName.TITLE);
		if (titleElement==null) return null;
		// TITLE element never contains other tags so just decode it collapsing whitespace:
		return CharacterReference.decodeCollapseWhiteSpace(titleElement.getContent());
	}

	private static String getMetaValue(Source source, String key) {
		for (int pos=0; pos<source.length();) {
			StartTag startTag=source.getNextStartTag(pos,"name",key,false);
			if (startTag==null) return null;
			if (startTag.getName()==HTMLElementName.META)
				return startTag.getAttributeValue("content"); // Attribute values are automatically decoded
			pos=startTag.getEnd();
		}
		return null;
	}
}