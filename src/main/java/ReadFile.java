import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
 
public class ReadFile {
 
	public static void main(String[] args) {
 
		BufferedReader br = null;
 
		try {
 
			String sCurrentLine;
			String[] arr;
 
			br = new BufferedReader(new FileReader("/home/quyennt/workspace/TQA/one/PERF_PLF_ENT_INTRANET_WIKI_READ_PAGE/W33-20130815/AggregateReport.csv"));
 
			while ((sCurrentLine = br.readLine()) != null) {				
				System.out.println(sCurrentLine);
				
				arr = sCurrentLine.split(",");
				for(int i = 0; i < arr.length; i++){
					System.out.println("Item " + i + ":" + arr[i]);
				}
				System.out.println("_____________________________________________");
			}
 
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (br != null)br.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
 
	}
}