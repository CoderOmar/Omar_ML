import java.util.List;
import java.util.Map;


public class BM25 {

	/**
	 * @param args
	 * Jun 26, 2012
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

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
