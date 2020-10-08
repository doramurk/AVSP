import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class ClosestBlackNode2 {
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

        //FileWriter fileWriter = new FileWriter("izlaz");
        ArrayList<Integer> rezultat = new ArrayList<>();
        for (Map.Entry<Integer, Integer> integerIntegerEntry : cvorovi.entrySet()) {
            if (((Map.Entry) integerIntegerEntry).getValue() == Integer.valueOf(1)) {
                //fileWriter.write(((Map.Entry) integerIntegerEntry).getKey().toString() + " " + Integer.valueOf(0).toString() + "\n");
                System.out.println(((Map.Entry) integerIntegerEntry).getKey().toString() + " " + Integer.valueOf(0).toString());
            } else {
                rezultat = nadiNajbliziCrni((Integer) ((Map.Entry) integerIntegerEntry).getKey());
                if (rezultat.size() != 0) {
                    //fileWriter.write(rezultat.get(0) + " " + rezultat.get(1) + "\n");
                    System.out.println(rezultat.get(0) + " " + rezultat.get(1));
                } else {
                    //fileWriter.write(Integer.valueOf(-1).toString() + " " + Integer.valueOf(-1).toString() + "\n");
                    System.out.println(Integer.valueOf(-1).toString() + " " + Integer.valueOf(-1).toString());
                }
            }
        }
        //fileWriter.close();
    }

    private static ArrayList<Integer> nadiNajbliziCrni(Integer cvor) {
        ArrayList<Integer> rez = new ArrayList<>();
        boolean[] visited = new boolean[n];

        LinkedList<Integer> queue = new LinkedList<>();

        visited[cvor]=true;
        queue.add(cvor);

        int i = 0;
        while (queue.size() != 0)
        {
            i++;
            Integer cvor1 = queue.poll();
            if (cvorovi.get(cvor1).equals(1)) {
                rez.add(cvor1);
                rez.add(i);
                break;
            }

            for (int n : bridovi.get(cvor1)) {
                if (!visited[n]) {
                    visited[n] = true;
                    queue.add(n);
                }
            }
        }
        return rez;
    }
}
