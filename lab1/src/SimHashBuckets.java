import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

public class SimHashBuckets {
    private static Integer n = -1;
    private static Integer q = -1;
    private static ArrayList<String> sazeci = new ArrayList<>();
    private static ArrayList<String> upiti = new ArrayList<>();

    public static void main(String[] args) throws IOException, NoSuchAlgorithmException {
        Scanner scanner = new Scanner(System.in);
        int i = 0;
        String line;
        while(scanner.hasNext()) {
            line = scanner.nextLine();
            if(n == -1) {
                n = Integer.parseInt(line);
            } else if(i <= n) {
                sazeci.add(simhash(line.replace("\n", "")));
            } else if(q == -1) {
                q = Integer.parseInt(line);
            } else {
                upiti.add(line);
            }
            i++;
        }
        HashMap<Integer, HashSet<Integer>> kandidati = LSH(n, sazeci, 8);
        obradi_upite(kandidati);
    }

    private static HashMap<Integer, HashSet<Integer>> LSH(Integer n, ArrayList<String> sazeci, int br_pojasa) {
        HashMap<Integer, HashSet<Integer>> kandidati = new HashMap<>();
        for(int i = 0; i < n; i++) {
            HashSet<Integer> novi = new HashSet<>();
            kandidati.put(i, novi);
        }
        String hash;
        int val;
        for(int pojas = 1; pojas < br_pojasa + 1; pojas++) {
            HashMap<Integer, HashSet<Integer>> pretinci = new HashMap<>();
            for(int id = 0; id < n; id++) {
                hash = sazeci.get(id);
                val = hash2int(pojas, hash);
                HashSet<Integer> tekstovi_u_pretincu;
                if (pretinci.containsKey(val)) {
                    tekstovi_u_pretincu = new HashSet<>(pretinci.get(val));
                    for (Integer text_id : tekstovi_u_pretincu) {
                        kandidati.get(id).add(text_id);
                        kandidati.get(id).add(id);
                        kandidati.get(text_id).add(id);
                        kandidati.get(text_id).add(text_id);
                    }
                } else {
                    tekstovi_u_pretincu = new HashSet<>();
                }
                tekstovi_u_pretincu.add(id);
                pretinci.put(val, tekstovi_u_pretincu);
            }
        }
        return kandidati;
    }

    private static int hash2int(int pojas, String hash) {
        String bitovi_pojasa = hash.substring(127 - pojas * 16 + 1, 127 - (pojas - 1) * 16 + 1);
        return Integer.parseInt(bitovi_pojasa, 2);
    }

    private static void obradi_upite(HashMap<Integer, HashSet<Integer>> kandidati) throws IOException {
        int i = 0;
        int id = 0;
        int k = 0;
        String[] upit;
        //FileWriter izlaz = new FileWriter("./izlaz");
        for (i = 0; i < upiti.size(); i++) {
            upit = upiti.get(i).split(" ");
            id = Integer.parseInt(upit[0]);
            k = Integer.parseInt(upit[1]);
            int brojac = 0;
            int hammingova;
            for (Integer rbr : kandidati.get(id)) {
                hammingova = hammingova_udaljenost(sazeci.get(rbr), sazeci.get(id));
                if (k >= hammingova && hammingova >= 0) {
                    brojac++;
                }
            }
            //izlaz.write(((Integer)(brojac - 1)).toString() + "\n");
            System.out.println(brojac-1);
        }
        //izlaz.close();
        //System.out.println(brojac-1);
    }

    private static int hammingova_udaljenost(String s, String s1) {
            int i = 0, count = 0;
            while (i < s.length())
            {
                if (s.charAt(i) != s1.charAt(i))
                    count++;
                i++;
            }
            return count;
    }

    private static String simhash(String text) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        ArrayList<String> jedinke;
        jedinke = generiraj_jedinke(text);
        ArrayList<Integer> sh = new ArrayList<>();
        for(int i = 0; i < 128; i++) {
            sh.add(i, 0);
        }
        //int[] sh = new int[128];
        String hash_bin;
        for(String jedinka: jedinke) {
            hash_bin = calculate_hash(jedinka);
            for (int i = 0; i < hash_bin.length(); i++){
                char c = hash_bin.charAt(i);
                if(c == '1') {
                    sh.set(i, sh.get(i) + 1);
                } else {
                    sh.set(i, sh.get(i) - 1);
                }
            }
        }
        for(int i = 0; i < 128; i++) {
            if(sh.get(i) >= 0) {
                sh.set(i, 1);
            } else {
                sh.set(i, 0);
            }
        }
        String rez = "";
        for (Integer s : sh)
        {
            rez += s.toString();
        }
        return rez;
    }

    private static String calculate_hash(String s) throws UnsupportedEncodingException, NoSuchAlgorithmException {

        /*MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] thedigest = md.digest(s.getBytes());
        byte[] digest = md.digest();
        String myHash = DatatypeConverter
                .printHexBinary(digest);
        int myHash = Integer.parseInt(myHash,16);
        return Integer.toBinaryString(myHash);*/
        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] messageDigest = md.digest(s.getBytes());
        BigInteger no = new BigInteger(1, messageDigest);
        String hashtext = no.toString(2);
        while (hashtext.length() < 128) {
            hashtext = "0" + hashtext;
        }
        //int myHash = Integer.parseInt(hashtext,16);
        return hashtext;
        //return "111111";
    }

    private static ArrayList<String> generiraj_jedinke(String text) {
        String[] text_strings = text.split(" ");

        return new ArrayList<>(Arrays.asList(text_strings));
    }

}
