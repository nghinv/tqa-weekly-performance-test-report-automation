import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class ReadFile {

	public static void main(String[] args) {

		BufferedReader br = null;

		try {
			String path = "/home/quyennt/workspace/TQA/one/PERF_PLF_ENT_INTRANET_WIKI_READ_PAGE";

			String files;
			File folder = new File(path);
			File[] listOfFiles = folder.listFiles();

			for (int i = 0; i < listOfFiles.length; i++) {

//				if (listOfFiles[i].isFile()) {
					files = listOfFiles[i].getName();
					System.out.println(files);
//				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (br != null)
					br.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}

	}
}