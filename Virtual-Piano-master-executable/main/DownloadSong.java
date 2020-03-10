package main;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/*
 * ����λ��url,����Ϊsearched_song������
 */
public class DownloadSong {
	private String searched_song;
	private String inputType;
	private int count = 0;
	public DownloadSong(String url,String searched_song,String inputType) {
		this.searched_song=searched_song;
		this.inputType = inputType;
	    this.getHtmlElements(url);
	}
    public int getCount() {
    	return this.count;
    }
	// ��ȡ�����ͼƬ������
	public void saveToFile(String destUrl ) {

	    FileOutputStream fos = null;
	    BufferedInputStream bis = null;
	    HttpURLConnection httpUrl = null;
	    URL url = null;
	    int BUFFER_SIZE = 1024;
	    byte[] buf = new byte[BUFFER_SIZE];
	    int size = 0;
	    try {
	        url = new URL(destUrl);
	        httpUrl = (HttpURLConnection) url.openConnection();
	        httpUrl.connect();
	        bis = new BufferedInputStream(httpUrl.getInputStream());
	        String imgName = destUrl.substring(7, destUrl.lastIndexOf("."));
	        System.out.println(imgName);
	        File dir = new File("resources/images/"+searched_song);
	        if (!dir.exists()) {
	            dir.mkdirs();
	        }
	        File file = new File("resources/images/"+searched_song+"/"+ count +inputType);
	        System.out.println(file.getAbsolutePath());

	        fos = new FileOutputStream(file);
	        while ((size = bis.read(buf)) != -1) {
	            fos.write(buf, 0, size);
	        }
	        fos.flush();
	    } catch (IOException e) {
	        System.out.println("IOException");
	    } catch (ClassCastException e) {
	        System.out.println("ClassCastException");
	    } finally {
	        count++;
	        try {
	            fos.close();
	            bis.close();
	            httpUrl.disconnect();
	        } catch (IOException e) {
	        } catch (NullPointerException e) {
	        }
	    }
	}

	    // ����url��Ԫ��
	    private void getHtmlElements(String url) {
	        try {
	            Document doc = Jsoup.connect(url).get();
	            // ��ȡ��׺��Ϊpng��imgԪ��
	            Elements imgs = doc.select("img[src$="+inputType+"]");
	            //Elements imgs = doc.getElementsByTag("img");
	            
	            for (Element element : imgs) {
	            	String temp = element.attr("alt");
	            	if(temp.indexOf(searched_song)!=-1)
	            	{
	            		saveToFile(element.attr("src"));
	            	}
	            }
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	    }


}
