import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Field.Index;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.Field.TermVector;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.MMapDirectory;
import org.apache.lucene.util.Version;
import org.wltea.analyzer.lucene.IKAnalyzer;



public class IndexFileSource {
	
	public static void main(String[] args){
		IndexFileSource rf=new IndexFileSource();
		rf.index();
	}
	
	String indexPath="/home/coder/data/Sougou_index";
	void index(){
		
		IndexWriter iw=null;
		IKAnalyzer analyzer=new IKAnalyzer();
		try {
			MMapDirectory dir=new MMapDirectory(new File(indexPath));
			
			IndexWriterConfig conf=new IndexWriterConfig(Version.LUCENE_35,analyzer);
			
			iw=new IndexWriter(dir,conf);
			
			List<Document> list= allRead();
			int count=0;
			for(Document doc:list){
				Field uniqueField=new Field("id",count+"",Store.YES,Index.NOT_ANALYZED);
				doc.add(uniqueField);
				String label=doc.get("la");
				System.out.println("la:"+label);
				iw.addDocument(doc);
				count++;
			}
			iw.commit();
			iw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			if(iw!=null){
				try {
					iw.close();
				} catch (CorruptIndexException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
	}
	
	
	private List<Document> allRead(){
		List<Document> list=new ArrayList<Document>();
		List<Document> tempList=null;
		
		for(int i=0;i<folder.length;i++){
			String folderPath="/home/coder/data/Sougou/";
			String name=folder[i];
			folderPath=folderPath+name;
			String label=res[i];
			tempList=folderRead(folderPath,name,label);
			list.addAll(tempList);
		}
		return list;
	}
	 

	/**
	 * @param args
	 */
	private List<Document> folderRead(String folderPath,String folderName,String label) {
		List<Document> list=new ArrayList<Document>();

		File folder=new File(folderPath);
		
		System.out.println(folder.getAbsolutePath()+" -- "+label);
		
		File[] files=folder.listFiles();
		for(File file:files){
			FileReader fr=null;
			BufferedReader br=null;
			try {
				Doucment_Sogou ds=new Doucment_Sogou();
				fr=new FileReader(file);
				br=new BufferedReader(fr);
				
				String line=br.readLine();
				StringBuilder content=new StringBuilder();
				while(line!=null){
					content.append(line);
					line=br.readLine();
				}
				
				ds.setContent(content.toString());
				ds.setLabel(label);
//				System.out.println("label:"+label);
				
//				System.out.println(ds);
				
				Document doc=new Document();
				Field contentField=new Field("content",ds.getContent(),Store.YES,Index.ANALYZED,TermVector.WITH_POSITIONS_OFFSETS);
				Field pathField=new Field("path",folderName+"-"+file.getName(),Store.YES,Index.NOT_ANALYZED);
				Field labelField=new Field("la",label,Store.YES,Index.NOT_ANALYZED);
				doc.add(contentField);
				doc.add(labelField);
				doc.add(pathField);
//				String la=doc.get("la");
//				System.out.println("la:"+la);
				list.add(doc);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}finally{
				if(br!=null){
					try {
						br.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				if(fr!=null){
					try {
						fr.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
		return list;
	}

	String folder[]={
			"C000008",
			"C000010",
			"C000013",
			"C000014",
			"C000016",
			"C000020",
			"C000022",
			"C000023",
			"C000024"};
	String res[]={"汽车","财经","IT","健康","体育","旅游","教育","招聘","文化","军事"};
	
	
	
	
}
