import java.util.*;

public class PCY {
    private static Integer N;
    private static Double s;
    private static Integer b;
    private static int prag;
    private static ArrayList<ArrayList<Integer>> kosare = new ArrayList<>();
    private static HashMap<Integer, Integer> brPredmeta = new HashMap<>();

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int i = 0;
        String line;
        while (scanner.hasNext()) {
            line = scanner.nextLine();
            if (i == 0) {
                N = Integer.parseInt(line);
            } else if (i == 1) {
                s = Double.parseDouble(line);
            } else if (i == 2) {
                b = Integer.parseInt(line);
            } else {
                String[] line_elements = line.split(" ");
                ArrayList<Integer> list = new ArrayList<>();
                for (String el : line_elements) {
                    list.add(Integer.parseInt(el));
                }
                kosare.add(list);
            }
            i++;
        }
        prag = (int) (s * N);

        for (List<Integer> kosara : kosare) {
            for (Integer predmet : kosara) {
                if (brPredmeta.containsKey(predmet)) {
                    brPredmeta.put(predmet, brPredmeta.get(predmet) + 1);
                } else {
                    brPredmeta.put(predmet, 1);
                }
            }
        }
        long m = brPredmeta.values().stream().filter(v -> v >= prag).count();
        //System.out.println((m*(m-1))/2);
        HashMap<Integer, Integer> pretinci = new HashMap<>();
        for (List<Integer> kosara : kosare) {
            for (i = 0; i < kosara.size(); i++) {
                int predmet1 = kosara.get(i);
                for (int j = i + 1; j < kosara.size(); j++) {
                    int predmet2 = kosara.get(j);
                    if (predmet1 != predmet2 && brPredmeta.get(predmet1) >= prag && brPredmeta.get(predmet2) >= prag) {
                        int k = ((predmet1 * brPredmeta.size()) + predmet2) % b;
                        if (pretinci.containsKey(k)) {
                            pretinci.put(k, pretinci.get(k) + 1);
                        } else {
                            pretinci.put(k, 1);
                        }
                    }
                }
            }
        }
        long P = pretinci.values().stream().filter(v -> v >= prag).count();
        //System.out.println(P);
        HashMap<List<Integer>, Integer> parovi = new HashMap<>();
        for (List<Integer> kosara : kosare) {
            for (i = 0; i < kosara.size(); i++) {
                int predmet1 = kosara.get(i);
                for (int j = i + 1; j < kosara.size(); j++) {
                    int predmet2 = kosara.get(j);
                    if (predmet1 != predmet2 && brPredmeta.get(predmet1) >= prag && brPredmeta.get(predmet2) >= prag) {
                        int k = ((predmet1 * brPredmeta.size()) + predmet2) % b;
                        if (pretinci.containsKey(k)) {
                            if (pretinci.get(k) >= prag) {
                                List<Integer> par = new ArrayList<>();
                                par.add(predmet1);
                                par.add(predmet2);
                                if (parovi.containsKey(par)) {
                                    parovi.put(par, parovi.get(par) + 1);
                                } else {
                                    parovi.put(par, 1);
                                }
                            }
                        }
                    }
                }
            }
        }
        HashMap<List<Integer>, Integer> paroviSorted = sortByValue(parovi);
        System.out.println(((Long)((m*(m-1))/2)).toString());
        System.out.println(((Long)(P)).toString());
        for (Integer broj : paroviSorted.values()) {
            if(broj >= s*N) {
                System.out.println(broj.toString());
            } else {
                break;
            }
        }
    }

    private static HashMap<List<Integer>, Integer> sortByValue(HashMap<List<Integer>, Integer> parovi) {
        List<Map.Entry<List<Integer>, Integer> > lista = new LinkedList<>(parovi.entrySet());
        lista.sort(new Comparator<Map.Entry<List<Integer>, Integer>>() {
            public int compare(Map.Entry<List<Integer>, Integer> o1,
                               Map.Entry<List<Integer>, Integer> o2) {
                return (o2.getValue()).compareTo(o1.getValue());
            }
        });
        HashMap<List<Integer>, Integer> res = new LinkedHashMap<>();
        for (Map.Entry<List<Integer>, Integer> el : lista) {
            res.put(el.getKey(), el.getValue());
        }
        return res;
    }
}
