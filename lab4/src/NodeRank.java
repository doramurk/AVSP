import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class NodeRank {

    private static int n;
    private static double beta;
    private static int q;
    private static ArrayList<String> upiti = new ArrayList<>();
    private static HashMap<Integer, List<Double>> r = new HashMap<>();
    private static HashMap<Integer, List<Integer>> pokazuje = new HashMap<>();
    private static HashMap<Integer, List<Integer>> cvorovi = new HashMap<>();

    public static void main(String[] args) throws IOException {
        Scanner sc = new Scanner(System.in);
        String line;
        int i = 0;
        while (sc.hasNext()) {
            line = sc.nextLine();
            if (i == 0) {
                n = Integer.parseInt(line.split(" ")[0]);
                beta = Double.parseDouble(line.split(" ")[1]);
                for (int j = 0; j < n; j++) {
                    cvorovi.put(j, new ArrayList<>());
                }
            }
            if (i > 0 && i <= n) {
                List<Integer> susjedi = new ArrayList<>();
                for (String str : line.split(" ")) {
                    susjedi.add(Integer.parseInt(str));
                }
                pokazuje.put(i - 1, susjedi);
                for (int susjed : susjedi) {
                    cvorovi.get(susjed).add(i - 1);
                }
            }
            if (i == n + 1) {
                q = Integer.parseInt(line);
            }
            if (i > n + 1) {
                upiti.add(line);
            }
            i++;
        }

        ArrayList<Double> r0 = new ArrayList<>();
        for (int j = 0; j < n; j++) {
            r0.add(1. / n);
        }
        r.put(0, r0);
        for (int j = 1; j < 101; j++) {
            ArrayList<Double> rj = new ArrayList<>();
            for (int k = 0; k < n; k++) {
                double suma = 0.;
                for (int u : cvorovi.get(k)) {
                    suma += (beta * r.get(j-1).get(u)) / pokazuje.get(u).size();
                }
                suma += ((1 - beta) / n);
                rj.add(k, suma);
            }
            r.put(j, rj);
        }

        //FileWriter fileWriter = new FileWriter("izlaz");
        for (String upit : upiti) {
            int ni = Integer.parseInt(upit.split(" ")[0]);
            int ti = Integer.parseInt(upit.split(" ")[1]);

            System.out.println(String.format("%.10f", r.get(ti).get(ni)));
            //fileWriter.write(String.format("%.10f", r.get(ti).get(ni)));
            //fileWriter.write("\n");
        }
        //fileWriter.close();
    }
}
