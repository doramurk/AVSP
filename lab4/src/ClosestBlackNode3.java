import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class ClosestBlackNode3 {
    private static int n;
    private static int e;
    private static HashMap<Integer, Integer> cvorovi = new HashMap<>();
    private static HashMap<Integer, ArrayList<Integer>> bridovi = new HashMap<>();
    public static void main(String[] args) throws IOException {
        Scanner sc = new Scanner(System.in);
        String line;
        int i = 0;
        while (sc.hasNext()) {
            line = sc.nextLine();
            if (i == 0) {
                n = Integer.parseInt(line.split(" ")[0]);
                e = Integer.parseInt(line.split(" ")[1]);
            }
            if (i > 0 && i <= n) {
                cvorovi.put(i - 1, Integer.parseInt(line));
            }
            if (i >= n + 1) {
                int c1 = Integer.parseInt(line.split(" ")[0]);
                int c2 = Integer.parseInt(line.split(" ")[1]);
                if (bridovi.containsKey(c1)) {
                    bridovi.get(c1).add(c2);
                } else {
                    bridovi.put(c1, new ArrayList<>());
                    bridovi.get(c1).add(c2);
                }
                if (bridovi.containsKey(c2)) {
                    bridovi.get(c2).add(c1);
                } else {
                    bridovi.put(c2, new ArrayList<>());
                    bridovi.get(c2).add(c1);
                }
            }
            i++;
        }

        FileWriter fileWriter = new FileWriter("izlaz");
        ArrayList<Integer> rezultat = new ArrayList<>();
        for (Integer key : cvorovi.keySet()) {
            if (cvorovi.get(key).equals(1)) {
                fileWriter.write(key.toString() + " " + Integer.valueOf(0).toString() + "\n");
                System.out.println(key.toString() + " " + Integer.valueOf(0).toString());
            } else {
                rezultat = nadiNajbliziCrni(key);
                if (rezultat.size() != 0) {
                    fileWriter.write(rezultat.get(0) + " " + rezultat.get(1) + "\n");
                    System.out.println(rezultat.get(0) + " " + rezultat.get(1));
                } else {
                    fileWriter.write(Integer.valueOf(-1).toString() + " " + Integer.valueOf(-1).toString() + "\n");
                    System.out.println(Integer.valueOf(-1).toString() + " " + Integer.valueOf(-1).toString());
                }
            }
        }
        fileWriter.close();
    }

    private static ArrayList<Integer> nadiNajbliziCrni(Integer cvor) {
        ArrayList<ArrayList<Integer>> rez = new ArrayList<>();
        boolean[] visited = new boolean[n];

        LinkedList<ArrayList<Integer>> queue = new LinkedList<>();

        visited[cvor]=true;
        ArrayList<Integer> list = new ArrayList<>();
        list.add(0, cvor);
        list.add(1, 0);
        queue.add(list);

        int i = 10;
        int min_cvor = 0;
        while (queue.size() != 0)
        {
            ArrayList<Integer> cvor1 = queue.poll();
            if (cvorovi.get(cvor1.get(0)).equals(1) && cvor1.get(1) <= i && cvor1.get(1) <= 10) {
                i = cvor1.get(1);
                rez.add(cvor1);
            }
            if (cvor1.get(1) > i || cvor1.get(1) > 10) {
                break;
            }

            for (int n : bridovi.get(cvor1.get(0))) {
                if (!visited[n]) {
                    visited[n] = true;
                    ArrayList<Integer> list1 = new ArrayList<>();
                    list1.add(0, n);
                    list1.add(1, cvor1.get(1) + 1);
                    queue.add(list1);
                }
            }
        }
        int min = n;
        int udaljenost = 0;
        boolean ok = false;
        for (ArrayList<Integer> r : rez) {
            if (r.get(0) < min) {
                ok = true;
                min = r.get(0);
                udaljenost = r.get(1);
            }
        }
        ArrayList<Integer> list2 = new ArrayList<>();
        if (ok) {
            list2.add(min);
            list2.add(udaljenost);
        }
        return list2;
    }
}
