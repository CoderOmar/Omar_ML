import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.index.TermEnum;
import org.apache.lucene.store.MMapDirectory;


public class SourceFileReader {

	
	public static void main(String[] args){
		
		String indexPath="/home/coder/data/Sougou_index";
		int totalNum=0;
		int totalLength=0;
		try {
			MMapDirectory dir=new MMapDirectory(new File(indexPath));
			IndexReader ir=IndexReader.open(dir, true);
			totalNum=ir.maxDoc();
			System.out.println("total:"+totalNum);
			
			TermEnum terms=ir.terms(new Term("content"));
			
			Map<String,Integer> zidian=new HashMap<String,Integer>(); 
			
			int termCount=0;
			while(terms.next()){
				String ci=terms.term().text();
				
				String reg="\\d";
				Pattern p = Pattern.compile(reg);
				Matcher m=p.matcher(ci);
				boolean flag=m.find();
				if(!flag){
					System.out.println(ci);
					termCount++;
					zidian.put(ci, termCount);
//					System.out.println("get it");
				}else{
//					System.out.println("not get it");
				}
			}
			System.out.println("termCount:"+termCount);
			
//			ObjectOutputStream out = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(new File("zidian.txt")))); 
//			out.writeObject(zidian);
//			out.flush();
//			out.close(); 
			
//			int i=100;
//			for(int i =0 ; i <totalNum;i++){
			
//				Document doc=ir.document(i);
//				String content=doc.get("content");
//				totalLength+=content.length();
//				System.out.println("content:"+content);
//				String label=doc.get("la");
//				System.out.println("label:"+label);
//				String id=doc.get("id");
//				System.out.println("id:"+id);
//				String path=doc.get("path");
//				System.out.println("path:"+path);
				
//				TermFreqVector vector=ir.getTermFreqVector(i, "content");
//				String terms[]=vector.getTerms();
//				int[] freqs=vector.getTermFrequencies();
				
//				System.out.println("terms.length:"+terms.length+"  ---  freqs.length:"+freqs.length);
				
				
//				for(int num=0;num<terms.length;num++){
//					String term = terms[num];
//					int freq=freqs[num];
//					System.out.print("term:"+term+" -- "+freq+" | ");
//				}
//				System.out.println();
			
//			}
			
			ir.close();
			dir.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			
		}
		
		double avalLength=totalLength/(totalNum+0.0);
		
		System.out.println("TotalLength:"+totalLength);
		System.out.println("totalNum:"+totalNum);
		System.out.println("avalLength:"+avalLength);
		
		
	}
	
}
