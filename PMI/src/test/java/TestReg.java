import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class TestReg {
	
	public static void main(String[] args){
		
		String reg="\\d";
		String string="ad_";
		
		Pattern p = Pattern.compile(reg);
		
		Matcher m=p.matcher(string);
		
		boolean flag=m.find();
		
		if(flag){
			System.out.println("get it");
		}else{
			System.out.println("not get it");
		}
	}

}
