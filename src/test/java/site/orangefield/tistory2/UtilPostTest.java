package site.orangefield.tistory2;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.Test;

public class UtilPostTest {

    @Test
    public void getContentWithoutImg_테스트() {
        // 1. 가짜 데이터
        String html = "안녕 <img src='#'> 반가워 <img src='#'>";
        Document doc = Jsoup.parse(html);
        // System.out.println("doc1 : " + doc);

        // 2. 실행
        Elements els = doc.select("img");
        for (Element el : els) {
            el.remove();
        }
        // System.out.println("doc2 : " + doc);
        System.out.println(doc.select("body").text());

        // 3. 검증
        Elements elsVerify = doc.select("img");
        assertTrue(elsVerify.size() == 0);
    }
}
