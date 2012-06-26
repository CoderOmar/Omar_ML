import java.io.File;
import java.io.IOException;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.MMapDirectory;


public class MI {

	
	public static void main(String[] args){
		String word1="电脑";
		String label1="文化";
		String word2="体育";
		
		MI mi=new MI();
		
		MIModel mm=mi.search(word1, label1);
		
		System.out.println(mm.toString());
		
		double mi_v=mm.getMi_value();
		
		System.out.println("MI:"+mi_v);
		
		
		
	}
	
	MIModel search(String queryWord,String wordLabel){
		String indexPath="/home/coder/data/Sougou_index";
		MIModel mm=new MIModel();
		try {
			MMapDirectory dir=new MMapDirectory(new File(indexPath));
			IndexReader ir=IndexReader.open(dir, true);
			IndexSearcher searcher=new IndexSearcher(ir);
			int N=ir.maxDoc();
			TopDocs docs=searcher.search(new TermQuery(new Term("content",queryWord)), N+1);
			int totalC=docs.totalHits;
			ScoreDoc[] sdoc=docs.scoreDocs;
			int A_has_t_is_c=0;
			int B_has_t_not_c=0;
			for(ScoreDoc sd:sdoc){
				int docNum=sd.doc;
				Document document=ir.document(docNum);
				String label=document.get("la");
//				System.out.println("label:"+label);
				if(label.equals(wordLabel)){
					A_has_t_is_c++;
				}else{
					B_has_t_not_c++;
				}
			}
			
			
			TopDocs docs2=searcher.search(new TermQuery(new Term("la",wordLabel)), N+1);
			int totalC2=docs2.totalHits;
			
			int C=totalC2-A_has_t_is_c;
			
			mm.setA(A_has_t_is_c);
			mm.setB(B_has_t_not_c);
			mm.setC(C);
			mm.setN(N);
			
			searcher.close();
			ir.close();
			dir.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			
		}
		return mm;
	}
	
	public static  double mi_function(int A_has_t_is_c,int B_has_t_not_c,int N,int C_nothas_t_is_c){
		double mi=0.0;
		
		mi=Math.log((A_has_t_is_c*N)/((A_has_t_is_c+C_nothas_t_is_c)*(A_has_t_is_c+B_has_t_not_c+0.0)));
		
		return mi;
		
		
	}
	
}
class MIModel{
	private int A;
	private int B;
	private int C;
	private int N;
	
	private double mi_value;
	
	public String toString(){
		return  "A:"+A+"\n"+
				"B:"+B+"\n"+
				"C:"+C+"\n"+
				"N:"+N+"\n";
	}
	
	public double getMi_value() {
		
		mi_value=MI.mi_function(A, B, N, C);
		
		return mi_value;
	}
	public void setMi_value(double mi_value) {
		this.mi_value = mi_value;
	}
	public int getA() {
		return A;
	}
	public void setA(int a) {
		A = a;
	}
	public int getB() {
		return B;
	}
	public void setB(int b) {
		B = b;
	}
	public int getC() {
		return C;
	}
	public void setC(int c) {
		C = c;
	}
	public int getN() {
		return N;
	}
	public void setN(int n) {
		N = n;
	}
	
	
}
