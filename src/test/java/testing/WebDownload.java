package testing;

import java.io.IOException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.github.slidekb.util.Update;


public class WebDownload {

	public static void main(String[] args) throws IOException {
		Document doc = Jsoup.connect("https://slidekb.com/pages/software").get();
        String text = doc.body().text();

//        System.out.print(text);
        Update test = new Update();
        System.out.println(test.isAndGetUpdate("1.1.0"));
	}

}
