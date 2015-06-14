import java.io.*;
import java.util.HashSet;
import java.util.Set;
import java.util.Iterator;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 * Created by Pedro on 14/06/2015.
 */
public class NamedDictionary implements Serializable {
        private String name;
        private Set<String> terms;

    public NamedDictionary(String filepath) throws IOException, ParseException {
        this.terms = new HashSet<String>();

        JSONParser parser = new JSONParser();
        Object obj = parser.parse(new FileReader(filepath));
        JSONObject jsonObject = (JSONObject) obj;
        this.name = (String) jsonObject.get("name");

        // loop array
        JSONArray jsonterms = (JSONArray) jsonObject.get("terms");
        Iterator<String> iterator = jsonterms.iterator();
        while (iterator.hasNext()) {
            this.terms.add(iterator.next());
        }
    }

    public String getName() {
        return name;
    }

    public Set<String> getTerms() {
        return terms;
    }
    public String[] getTermsArray() {
        return terms.toArray(new String[terms.size()]);
    }
}
