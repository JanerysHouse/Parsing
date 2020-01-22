import com.google.gson.Gson;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class Parser {
    public static void main(String[] args) throws IOException {
        Document doc = Jsoup.connect("http://onlinesim.ru/price-list").get();
        Elements countryServices = doc.select("div.country-services");

        Map<String, Map<String, BigDecimal>> result = new HashMap<>();

        for (Element countryElement : countryServices) {
            String country = countryElement.select("h2").first().text();
            country = country.replace(":", "");

            result.put(country, new HashMap<>());

            Elements services = countryElement.select("div.service-block");
            for (Element serviceElement : services) {
                String serviceName = serviceElement.ownText();
                String priceString = serviceElement
                        .select(".price-text")
                        .first()
                        .text()
                        .replace("₽", "");
                BigDecimal price = new BigDecimal(priceString);

                result.get(country).put(serviceName, price);
            }
        }
        String jsonStr = new Gson().toJson(result);
        System.out.println(jsonStr);

    }
}
