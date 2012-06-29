import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map.Entry;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.MMapDirectory;

public class TotalPMI {
	private MMapDirectory dir = null;
	private IndexReader ir = null;
	private IndexSearcher searcher = null;
	private String indexPath="/home/coder/data/Sougou_index";

	private void init() {
		try {
			dir = new MMapDirectory(new File(indexPath));
			ir = IndexReader.open(dir, true);
			searcher = new IndexSearcher(ir);
		} catch (CorruptIndexException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void close() {
		try {
			searcher.close();
			ir.close();
			dir.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	MIModel search(String queryWord, String wordLabel) {

		MIModel mm = new MIModel();
		try {
			int N = ir.maxDoc();
			TopDocs docs = searcher.search(new TermQuery(new Term("content",
					queryWord)), N + 1);
			int totalC = docs.totalHits;
			ScoreDoc[] sdoc = docs.scoreDocs;
			int A_has_t_is_c = 0;
			int B_has_t_not_c = 0;
			for (ScoreDoc sd : sdoc) {
				int docNum = sd.doc;
				Document document = ir.document(docNum);
				String label = document.get("la");
				// System.out.println("label:"+label);
				if (label.equals(wordLabel)) {
					A_has_t_is_c++;
				} else {
					B_has_t_not_c++;
				}
			}

			TopDocs docs2 = searcher.search(new TermQuery(new Term("la",
					wordLabel)), N + 1);
			int totalC2 = docs2.totalHits;

			int C = totalC2 - A_has_t_is_c;

			mm.setA(A_has_t_is_c);
			mm.setB(B_has_t_not_c);
			mm.setC(C);
			mm.setN(N);

		} catch (IOException e) {
			e.printStackTrace();
		} finally {

		}
		return mm;
	}

	public String getIndexPath() {
		return indexPath;
	}

	public void setIndexPath(String indexPath) {
		this.indexPath = indexPath;
	}

	public TotalPMI(String indexPath) {
		super();
		this.indexPath = indexPath;
		init();
	}

	public TotalPMI() {
		super();
		init();
	}

	/**
	 * @param args
	 *            Jun 29, 2012
	 */
	public static void main(String[] args) {
		String indexPath = "/home/coder/data/Sougou_index";
		HashMap<String, Integer> hash=BaseUtils.loadDictFromFile();
		
		TotalPMI tpmi=new TotalPMI();
		String wordLabel="IT";
		for(Entry<String,Integer> entry:hash.entrySet()){
			String term=entry.getKey();
			double pmi=tpmi.search(term, wordLabel).getMi_value();
			if(pmi!=0){
				System.out.println("label:"+wordLabel+" -- term:"+term+" -- pmi:"+pmi);
			}
		}
		tpmi.close();
	}

}
