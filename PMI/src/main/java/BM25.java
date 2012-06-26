import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.index.TermFreqVector;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.MMapDirectory;
import org.apache.lucene.util.Version;
import org.wltea.analyzer.IKSegmentation;
import org.wltea.analyzer.Lexeme;
import org.wltea.analyzer.lucene.IKAnalyzer;


public class BM25 {

	/**
	 * @param args
	 * Jun 26, 2012
	 */
	public static void main(String[] args) {
		String query="我爱北京天安门";
		List<String> queryWords=new ArrayList<String>();
		queryWords.add("我");
		queryWords.add("爱");
		queryWords.add("北京");
		queryWords.add("天安门");
//		queryWords.add("天安");
		BM25 bm25=new BM25();
		double bm=bm25.search(query);
		double bm2=bm25.search(queryWords);

		System.out.println("bm1:"+bm);
		System.out.println("bm2:"+bm2);
	}

	
	double search(List<String> queryWords){
		String indexPath="/home/coder/data/Sougou_index";
		double bm25=0.0;
		try {
			
			
			MMapDirectory dir=new MMapDirectory(new File(indexPath));
			IndexReader ir=IndexReader.open(dir, true);
			IndexSearcher searcher=new IndexSearcher(ir);
			
			int N=ir.maxDoc();

			if(queryWords==null||queryWords.size()==0){
				System.out.println("something error on compute BM25 score");
				return bm25;
			}
			
			double k1=2.0;
			double b=0.75;
			double aval=1596.4451144611949;
			for(String queryWord:queryWords){
				
			
				TopDocs docs=searcher.search(new TermQuery(new Term("content",queryWord)), N+1);
				int totalC=docs.totalHits;
				ScoreDoc[] sdocs=docs.scoreDocs;

				double idf=Math.log(N/(totalC+0.0));
				//24256.025994210722
				
				for(ScoreDoc sdoc:sdocs){
					int docId=sdoc.doc;
					Document d=searcher.doc(docId);
					String content=d.get("content");
					
					int tf=0;
					
					TermFreqVector vector=ir.getTermFreqVector(docId, "content");
					String terms[]=vector.getTerms();
					int[] freqs=vector.getTermFrequencies();
					int len_d=content.length();
					
					for(int i = 0 ; i < terms.length; i ++){
						String term = terms[i];
						if(term.equals(queryWord)){
							tf=freqs[i];
						}
					}
					
					if(tf==0){
						continue;
					}
					
					double bm_temp=(idf*tf*(k1+1))/(tf+k1*(1-b+b*(len_d/aval)));
//						System.out.println("word:"+queryWord+" -- docId:"+docId+" -- bm value: "+bm_temp);
					bm25+=bm_temp;
				}
			}
			
			searcher.close();
			ir.close();
			dir.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally{
			
		}
		return bm25;
	}
	double search(String q){
		String indexPath="/home/coder/data/Sougou_index";
		double bm25=0.0;
		try {
			
			
			MMapDirectory dir=new MMapDirectory(new File(indexPath));
			IndexReader ir=IndexReader.open(dir, true);
			IndexSearcher searcher=new IndexSearcher(ir);
			
			int N=ir.maxDoc();

//			if(queryWords==null||queryWords.size()==0){
//				System.out.println("something error on compute BM25 score");
//				return bm25;
//			}
			
			double k1=2.0;
			double b=0.75;
			double aval=1596.4451144611949;
//			for(String queryWord:queryWords){
				
				QueryParser qp=new QueryParser(Version.LUCENE_35,"content",new IKAnalyzer());
				Query query=qp.parse(q);
			
				TopDocs docs=searcher.search(query, N+1);
				int totalC=docs.totalHits;
				ScoreDoc[] sdocs=docs.scoreDocs;

				double idf=Math.log(N/(totalC+0.0));
				//24256.025994210722
				IKSegmentation ik=new IKSegmentation(new StringReader(q));
				Lexeme qwL=ik.next();
				List<String> qwords=new ArrayList<String>();
				while(qwL!=null){
					String word=qwL.getLexemeText();
					System.out.println("word:"+word);
					qwords.add(word);
					qwL=ik.next();
				}
				
				for(ScoreDoc sdoc:sdocs){
					int docId=sdoc.doc;
					Document d=searcher.doc(docId);
					String content=d.get("content");
					
					int tf=0;
					
					TermFreqVector vector=ir.getTermFreqVector(docId, "content");
					String terms[]=vector.getTerms();
					int[] freqs=vector.getTermFrequencies();
					int len_d=content.length();
					
					for(String queryWord:qwords){
						
						for(int i = 0 ; i < terms.length; i ++){
							String term = terms[i];
							if(term.equals(queryWord)){
								tf=freqs[i];
							}
						}
						
						if(tf==0){
							continue;
						}
						
						double bm_temp=(idf*tf*(k1+1))/(tf+k1*(1-b+b*(len_d/aval)));
//						System.out.println("word:"+queryWord+" -- docId:"+docId+" -- bm value: "+bm_temp);
						bm25+=bm_temp;

					}
					
				}
//			}
			
			searcher.close();
			ir.close();
			dir.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			
		}
		return bm25;
	}
	public static double scoreFunction(List<String> queryWords,Map<String,Integer> documentWords){
		double bm25=0.0;
		if(queryWords==null||documentWords==null||queryWords.size()==0||documentWords.size()==0){
			System.out.println("something error on compute BM25 score");
			return bm25;
		}
		int len_d=documentWords.values().size();
		
		double k1=2.0;
		double b=0.75;
		
		for(String q:queryWords){
			
			int tf=documentWords.get(q);
			
			
			
		}
		
		return bm25;
	}

}
