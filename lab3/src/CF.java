import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.*;

public class CF {
    private static int N;
    private static int M;
    private static int Q;
    private static ArrayList<ArrayList<Double>> utilityII = new ArrayList<>();
    private static ArrayList<Double> utilityIINorm = new ArrayList<>();
    private static ArrayList<Double> utilityUUNorm = new ArrayList<>();
    private static HashMap<Integer, HashMap<Integer, Double>> slicnostiItema = new HashMap<>();
    private static HashMap<Integer, HashMap<Integer, Double>> slicnostiUsera = new HashMap<>();
    private static ArrayList<String> upiti = new ArrayList<>();

    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        int i = 0;
        String line;
        while (scanner.hasNext()) {
            line = scanner.nextLine();
            if (i == 0) {
                N = Integer.parseInt(line.split(" ")[0]);
                M = Integer.parseInt(line.split(" ")[1]);
            } else if (i <= N) {
                ArrayList<Double> redak = new ArrayList<>();
                for (String ocjena : line.split(" ")) {
                    if (ocjena.equals("X")) {
                        redak.add(0.);
                    } else {
                        redak.add(Double.parseDouble(ocjena));
                    }
                }
                utilityII.add(redak);
            } else if (i == N + 1) {
                Q = Integer.parseInt(line);
            } else {
                upiti.add(line);
            }
            i++;
        }
        scanner.close();

        utilityIINorm = Normalize(utilityII, 0);
        utilityUUNorm = Normalize(utilityII, 1);

        //FileWriter fileWriter = new FileWriter("./izlaz");
        for (String upit : upiti) {
            String[] upit_split = upit.split(" ");
            int item = Integer.parseInt(upit_split[0]) - 1;
            int user = Integer.parseInt(upit_split[1]) - 1;
            int type = Integer.parseInt(upit_split[2]);
            int k = Integer.parseInt(upit_split[3]);
            double ocjena;
            if (type == 0) {
                ocjena = itemItem(item, user, k);
            } else {
                ocjena = userUser(item, user, k);
            }
            DecimalFormat df = new DecimalFormat("#.000");
            BigDecimal bd = new BigDecimal(ocjena);
            BigDecimal res = bd.setScale(3, RoundingMode.HALF_UP);
            //fileWriter.write(df.format(res) + "\n");
            System.out.println(df.format(res));
        }
        //fileWriter.close();
    }

    private static double userUser(int item, int user, int k) {
        //izracunati slicnost usera
        double sum1 = 0.;
        double sum2 = 0.;
        double sum3 = 0.;
        HashMap<Integer, Double> slicnosti = new HashMap<>();
        /*if (slicnostiUsera.containsKey(user)) {
            slicnosti = slicnostiUsera.get(user);
        } else {*/
        for (int i = 0; i < utilityII.get(1).size(); i++) {
            for (int j = 0; j < utilityII.size(); j++) {
                if (utilityII.get(j).get(user) != 0. && utilityII.get(j).get(i) != 0.) {
                    sum1 += (utilityII.get(j).get(user) - utilityUUNorm.get(user)) * (utilityII.get(j).get(i) - utilityUUNorm.get(i));
                }
                if (utilityII.get(j).get(i) != 0.) {
                    sum3 += Math.pow((utilityII.get(j).get(i) - utilityUUNorm.get(i)), 2);
                }
                if (utilityII.get(j).get(user) != 0.) {
                    sum2 += Math.pow((utilityII.get(j).get(user) - utilityUUNorm.get(user)), 2);
                }
            }
            slicnosti.put(i, sum1 / Math.sqrt(sum2 * sum3));
            sum1 = 0.;
            sum2 = 0.;
            sum3 = 0.;
        }
        //slicnostiUsera.put(user, slicnosti);
        //pronaci k najslicnijih i po njima izracunat
        sum1 = 0.;
        sum2 = 0.;
        //for (int i = 0; i < k+1; i++) {
        int i = 0;
        while (i < k) {
            Integer key = Collections.max(slicnosti.entrySet(), Map.Entry.comparingByValue()).getKey();
            double sim = slicnosti.get(key);
            if (sim > 0.) {
                if (key != user) {
                    if (utilityII.get(item).get(key) != 0.) {
                        sum1 += utilityII.get(item).get(key) * sim;
                        sum2 += sim;
                        i++;
                    }
                }
            } else {
                break;
            }
            slicnosti.remove(key);
        }
        if (sum2 != 0.) {
            return sum1 / sum2;
        } else return 0.;
    }

    private static double itemItem(int item, int user, int k) {
        //izracunati slicnost itema
        double sum1 = 0.;
        double sum2 = 0.;
        double sum3 = 0.;
        HashMap<Integer, Double> slicnosti = new HashMap<>();
        //if (slicnostiItema.containsKey(item)) {
        //    slicnosti = slicnostiItema.get(item);
        //} else {
        for (int i = 0; i < utilityII.size(); i++) {
            for (int j = 0; j < utilityII.get(i).size(); j++) {
                if (utilityII.get(item).get(j) != 0. && utilityII.get(i).get(j) != 0.) {
                    sum1 += (utilityII.get(item).get(j) - utilityIINorm.get(item)) * (utilityII.get(i).get(j) - utilityIINorm.get(i));
                }
                 if(utilityII.get(i).get(j) != 0.) {
                     sum3 += Math.pow((utilityII.get(i).get(j) - utilityIINorm.get(i)), 2);
                 }
                 if(utilityII.get(item).get(j) != 0.) {
                     sum2 += Math.pow((utilityII.get(item).get(j) - utilityIINorm.get(item)), 2);
                 }
            }
            slicnosti.put(i, sum1 / Math.sqrt(sum2 * sum3));
            sum1 = 0.;
            sum2 = 0.;
            sum3 = 0.;
        }
        //    slicnostiItema.put(item, slicnosti);
        //pronaci k najslicnijih i po njima izracunat
        sum1 = 0.;
        sum2 = 0.;
        int i = 0;
        while (i < k){
            Integer key = Collections.max(slicnosti.entrySet(), Map.Entry.comparingByValue()).getKey();
            double sim = slicnosti.get(key);
            if (sim > 0.) {
                if (key != item) {
                    if (utilityII.get(key).get(user) != 0.) {
                        sum1 += utilityII.get(key).get(user) * sim;
                        sum2 += sim;
                        i++;
                    }
                }
            } else {
                break;
            }
            slicnosti.remove(key);
        }
        if (sum2 != 0.) {
            return sum1 / sum2;
        } else return 0.;
    }

    private static ArrayList<Double> Normalize(ArrayList<ArrayList<Double>> utilityII, int type) {
        ArrayList<Double> utilityNorm = new ArrayList<>();
        if (type == 0) {
            for (int i = 0; i < utilityII.size(); i++) {
                utilityNorm.add(i, average(utilityII.get(i)));
            }
        } else {
            for (int i = 0; i < utilityII.get(1).size(); i++) {
                ArrayList<Double> stupac = new ArrayList<>();
                for (int j = 0; j < utilityII.size(); j++) {
                    stupac.add(utilityII.get(j).get(i));
                }
                utilityNorm.add(i, average(stupac));
            }
        }
        return utilityNorm;
    }

    private static Double average(ArrayList<Double> list) {
        double mean = 0.;
        for (double ocjena : list) {
            mean += ocjena;
        }
        mean /= list.stream().filter(v -> v != 0.).count();
        return mean;
    }
}
