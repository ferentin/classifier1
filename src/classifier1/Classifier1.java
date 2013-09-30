package classifier1;

/**
 *
 * @author k
 */
import java.lang.String;
import java.net.MalformedURLException;
//import java.net.URI;
//import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
//import org.apache.lucene.analysis.PorterStemmer;
//import java.util.regex.*;
import rita.wordnet.RiWordnet;

public class Classifier1 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws MalformedURLException {


      String prevQuery="telling";
      String   query  = "tell"; 
         
            
        
//        System.out.println(prevQuery);
//        System.out.println(query);
        String type = "new";

        if (prevQuery.equals(query)) {
            type = "same";
        } else {
            if (wordReorder(prevQuery, query)) {
                type = "wordReorder";
            } else {
                if (punctuation(prevQuery, query)) {
                    type = "punctuation";
                } else {
                    if (addWords(prevQuery, query)) {
                        type = "addWords";
                    } else {
                        if (removeWords(prevQuery, query)) {
                            type = "removeWords";
                        } else {
                            if (urlStrip(prevQuery, query)) {
                                type = "urlStrip";
                            } else {
                                if (stemming(prevQuery, query)) {
                                    type = "stemming";
                                } else {
                                    if (formacronym(prevQuery, query)) {
                                        type = "formacronym";
                                    } else {
                                        if (expandacronym(prevQuery, query)) {
                                            type = "expandacronym";
                                        } else {
                                            if (abbreviation(prevQuery, query)) {
                                                type = "abbreviation";
                                            } else {
                                                if (subString(prevQuery, query)) {
                                                    type = "subString";
                                                } else {
                                                    if (superString(prevQuery, query)) {
                                                        type = "superstring";
                                                    } else {
                                                        if (wordsubstitution(prevQuery, query)) {
                                                            type = "wordsubstitution";
                                                        } else {
                                                            if (spelling(prevQuery, query)) {
                                                                type = "spelling";
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        System.out.println(type);

    }

    public static String format(String str1) {
        String st2, st3, st4;
        st2 = str1.replaceAll("[\\p{Punct}&&[^-,.']]", "");  //   punctuation={',-,.}  remove everything else
        st3 = st2.replaceAll("\\s+", " ");   //oxi pollapla kena, mono 1   keno = '_'  
        st4 = st3.replaceAll("\\s+", "_");   //swsto format:opou keno pavla 

        //String[] part = st4.split("_");
        return st4;
    }

    public static boolean wordReorder(String prevQuery, String query) {
         prevQuery = format(prevQuery);
        query = format(query);
        String[] a, b;
        a = prevQuery.split("_");
        Arrays.sort(a);
        b = query.split("_");
        Arrays.sort(b);
        return Arrays.equals(a, b);
    }

    public static boolean removeWords(String prevQuery, String query) {
         prevQuery = format(prevQuery);
        query = format(query);
        String[] a, b;
        a = prevQuery.split("_");
        b = query.split("_");
        if (b.length == a.length) {
            return false;
        } else if (a.length > b.length) {
            for (int i = 0; i < b.length; i++) {
                for (int j = 0; j < a.length; j++) {
                    if (b[i].equals(a[i])) {
                       continue;
                    } else {
                       return false;
                    }
                }
            }
        }
        return false;

    }

    public static boolean addWords(String prevQuery, String query) {
         prevQuery = format(prevQuery);
        query = format(query);
        String[] a, b;
        a = prevQuery.split("_");
        b = query.split("_");
        if (b.length == a.length) {
            return false;
        } else if (a.length < b.length) {
            for (int i = 0; i < a.length; i++) {
               
                    if (a[i].equals(b[i])) {
                        continue;
                    } else {
                        return false;
                    }
                
            }
        }
        return false;

    }

    public static boolean subString(String prevQuery, String query) {
         prevQuery = format(prevQuery);
        query = format(query);
        return prevQuery.contains(query);
    }

    public static boolean superString(String prevQuery, String query) {
         prevQuery = format(prevQuery);
        query = format(query);
        return query.contains(prevQuery);
    }

    public static boolean abbreviation(String prevQuery, String query) {
         prevQuery = format(prevQuery);
        query = format(query);
        String[] a, b;
        a = prevQuery.split("_");
        Arrays.sort(a);
        b = query.split("_");
        Arrays.sort(b);
        if (b.length != a.length) {
            return false;
        } else {
            for (int i = 0; i < a.length; i++) {
                if (a[i].contains(b[i]) || b[i].contains(a[i])) {
                    //System.out.println("swst");
                    continue;
                }
                else
                {
                    return false;
                }
            }
            return true;
        }

    }

    public static boolean stemming(String prevQuery, String query) {
         prevQuery = format(prevQuery);
        query = format(query);
        // xwrizw se lekseis ta 2 queries 
        // sorting 
        // efarmozw stem se kathe leksh tou 1ou query kai an to stemmed einai idio me kapoio query tou 2ou 
        //tote ontws exw stemming se ena zevgos tote return true
        // kai an meta prokuptoun idies lekseis tote exw ontws stemming ara return true.

        String[] a, b;
        a = prevQuery.split("_");
        Arrays.sort(a);
        b = query.split("_");
        Arrays.sort(b);

        for (int i = 0; i < a.length; i++) {
            String temp = helpstemming(a[i]);
            if (temp != null) {
                for (int j = 0; j < b.length; j++) {
                    if (temp.equals(b[j])) {
                        return true;
                    }
                }
            } else {
                continue;
            }
        }

        return false;

    }

    public static String helpstemming(String word) {

        //__________STEMMING SE KATHE LEKSH________ 
        String term = word;
        char[] chars = term.toCharArray();
        Stemmer stemm = new Stemmer();
        for (int i = 0; i < term.length(); i++) {
            stemm.add(chars[i]);
        }
        stemm.stem();
        String stemmed;
        /* and now, to test toString() : */
        stemmed = stemm.toString();
        /* to test getResultBuffer(), getResultLength() : */
        /* u = new String(s.getResultBuffer(), 0, s.getResultLength()); */
        //System.out.println(stemmed);

        //____________________________________________________________________

        if (word.equals(stemmed)) {
            return null;
        } else {
            return stemmed;
        }
    }

    public static boolean formacronym(String prevQuery, String query) {
         prevQuery = format(prevQuery);
        query = format(query);
        String[] a, b;
        a = prevQuery.split("_");
        b = query.split("_");

        if (b.length <= 1 && a.length <= 1) {
            return false;
        }
        String temp = Character.toString(a[0].charAt(0));
        String r;
        for (int i = 1; i <= a.length - 1; i++) {

            //r = (a[0].charAt(0)).toString();

            r = Character.toString(a[i].charAt(0));
            temp = temp.concat(r);
            //System.out.println(temp);
            //temp.add(a[0].charAt(i));
        }
        //System.out.println(a.length);
//       System.out.println(a[0]);
//       System.out.println(a[0].charAt(0));
        //  System.out.println(temp);
//          System.out.println("formatted previous"+prevQuery.replaceAll(".", ""));
        // System.out.println("prev="+prevQuery);
        // System.out.println("query="+query);
        if (query.equals(temp)) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean expandacronym(String prevQuery, String query) {
                return formacronym(query, prevQuery);
    }

    public static boolean urlStrip(String prevQuery, String query) throws MalformedURLException {
        /* antiparadeigma!!! petaei EXCEPTION--> HANDLER */
        /* me  aplo  prevQuery="http://docs.oracle.com/javase/6/docs/api/java/lang/String.html");
         query  = "oracle"; 
         doulevei ok!
         */
        URL url;
        try {
            url = new URL(prevQuery);
            String au = url.getAuthority();
            
            System.out.println(au);
            String[] p = au.split("\\.");
                       if (p[1].equals(query)) {
                return true;
            }
        } catch (MalformedURLException exc) {
            return false;
        }
        return false;
    }

    public static boolean spelling(String prevQuery, String query) {
         prevQuery = format(prevQuery);
        query = format(query);

        String s = prevQuery;
        String t = query;

        if (s == null || t == null) {
            throw new IllegalArgumentException("Strings must not be null");
        }

        int n = s.length(); // length of s
        int m = t.length(); // length of t


        int p[] = new int[n + 1]; //'previous' cost array, horizontally
        int d[] = new int[n + 1]; // cost array, horizontally
        int _d[]; //placeholder to assist in swapping p and d

        // indexes into strings s and t
        int i; // iterates through s
        int j; // iterates through t

        char t_j; // jth character of t

        int cost; // cost

        for (i = 0; i <= n; i++) {
            p[i] = i;
        }

        for (j = 1; j <= m; j++) {
            t_j = t.charAt(j - 1);
            d[0] = j;

            for (i = 1; i <= n; i++) {
                cost = s.charAt(i - 1) == t_j ? 0 : 1;
                // minimum of cell to the left+1, to the top+1, diagonally left and up +cost
                d[i] = Math.min(Math.min(d[i - 1] + 1, p[i] + 1), p[i - 1] + cost);
            }

            // copy current distance counts to 'previous row' distance counts
            _d = p;
            p = d;
            d = _d;
        }

        int r = p[n];

        //System.out.println(r);
        if (r < 3) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean wordsubstitution(String prevQuery, String query) {
         prevQuery = format(prevQuery);
        query = format(query);
        //elegxos 1: olh h frash prevQuery  susxetizetai me thn frash query 
         
        if (wordSubstitutionHelper(prevQuery, query) == true) {
            
            return true;
        }
        // elegxos 2: gia kathe zeugari leksewn -zeugari  idio h susxetizomeno
        String[] a, b;
        a = prevQuery.split("_");
        b = query.split("_");
        if (a.length != b.length) {
            return false;
        }

        for (int i = 0; i < a.length; i++) {
            if (a[i].equals(b[i]) || wordSubstitutionHelper(a[i], b[i])) {
                             //  continue;
                
            }
            else
            {
                continue;
            }
             
            return true;
        }
         return false;
    }

    public static boolean wordSubstitutionHelper(String a, String b) {
        RiWordnet wordnet = new RiWordnet(null);
        if (wordnet.exists(a) == false || wordnet.exists(b) == false) {
            return false;
        }
        String pos = null;
        ArrayList<String> posarray = new ArrayList<String>();
        if (wordnet.isNoun(a)) {
            posarray.add("n");
        }
        if (wordnet.isVerb(a)) {
            posarray.add("v");
        }
        if (wordnet.isAdjective(a)) {
            posarray.add("a");
        }
        if (wordnet.isAdverb(a)) {
            posarray.add("r");
        }
        //System.out.println(posarray.get(0));
        for (int k = 0; k < posarray.size(); k++) {
            pos = (posarray.get(k)).toString();
            int[] ids = wordnet.getSenseIds(a, pos);
            String[] result;
            for (int j = 0; j < ids.length; j++) {
                //SYNONYMS
                result = wordnet.getAllSynonyms(ids[j]);
                if (result == null) {
                    continue;
                }
                for (int i = 0; i < result.length; i++) {
                    if (result[i].equals(b)) {
//                        System.out.println("word a: " + a);
//                        System.out.println("related word: " + result[i]);
//                        System.out.println("word b: " + b);
                        return true;
                    }
                }
            }
            // HYPONYMS
            result = wordnet.getAllHyponyms(a, pos);
            if (result != null) {
                for (int i = 0; i < result.length; i++) {
                    if (result[i].equals(b)) {
//                        System.out.println("word a: " + a);
//                        System.out.println("related word: " + result[i]);
//                        System.out.println("word b: " + b);
                        return true;
                    }
                }
            }

            // HYPERNYMS
            result = wordnet.getAllHypernyms(a, pos);
            if (result != null) {
                for (int i = 0; i < result.length; i++) {
                    if (result[i].equals(b)) {
//                        System.out.println("word a: " + a);
//                        System.out.println("related word: " + result[i]);
//                        System.out.println("word b: " + b);
                        return true;
                    }
                }
            }

            // MERONYMS
            result = wordnet.getAllMeronyms(a, pos);
            if (result != null) {
                for (int i = 0; i < result.length; i++) {
                    if (result[i].equals(b)) {
//                        System.out.println("word a: " + a);
//                        System.out.println("related word: " + result[i]);
//                        System.out.println("word b: " + b);
                        return true;
                    }
                }
            }
            // HOLONYMS
            result = wordnet.getAllSynonyms(a, pos);
            if (result != null) {
                for (int i = 0; i < result.length; i++) {
                    if (result[i].equals(b)) {
                        //System.out.println("word a: " + a);
                        // System.out.println("related word: " + result[i]);
                        //System.out.println("word b: " + b);
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static boolean punctuation(String prevQuery, String query) {
         prevQuery = format(prevQuery);
        query = format(query);
        // SYSXETISH ME TO formating pou kanw sta queries!!!!!
        return false;
    }
}
