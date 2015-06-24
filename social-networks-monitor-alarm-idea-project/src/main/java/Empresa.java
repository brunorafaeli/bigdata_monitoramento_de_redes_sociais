import java.io.*;
import java.util.*;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 * Created by berne on 23/06/15.
 */

public class Empresa implements Serializable {
    private String name;
    private Set<String> terms;
    private List<NamedDictionary> categories;
    private Map<String, List<String>> wordmap;

    public Empresa(String filepath) throws IOException, ParseException {
        this.terms = new HashSet<String>();
        this.categories = new ArrayList<NamedDictionary>();
        this.wordmap = new HashMap<String, List<String>>();

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

        //get categories
        JSONArray jsoncategories = (JSONArray) jsonObject.get("categories");
        Iterator<JSONObject> cat_iter = jsoncategories.iterator();
        while (cat_iter.hasNext()) {
            String jsonString = cat_iter.next().toString();
            NamedDictionary newcategory = new NamedDictionary(jsonString);
            this.categories.add(newcategory);
        }
        Iterator<NamedDictionary> dict_iter = this.categories.iterator();
        while(dict_iter.hasNext()) {
            NamedDictionary currNameDict = dict_iter.next();
            String[] wordArray = currNameDict.getTermsArray();
            for (int i = 0; i < wordArray.length; ++i) {
                String currWord = wordArray[i];
                ArrayList<String> list = new ArrayList<String>();
                if (this.wordmap.containsKey(currWord)) {
                    list = (ArrayList<String>) this.wordmap.get(currWord);
                    list.add(currNameDict.getCat_name());
                    this.wordmap.put(currWord, list);
                }
                else {
                    list.add(currNameDict.getCat_name());
                    this.wordmap.put(currWord, list);
                }
            }
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

    public List<NamedDictionary> getCategories() {
        return categories;
    }

    public Map<String, List<String>> getWordmap() {
        return wordmap;
    }

    public String[] wordCategory(String word) {
        if (wordmap.containsKey(word)) {
            return wordmap.get(word).toArray(new String[wordmap.get(word).size()]);
        }
        return new String[0];
    }
}
