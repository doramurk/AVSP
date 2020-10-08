import java.util.*;

public class CF_prvi {
    private static int N;
    private static int M;
    private static int Q;
    private static ArrayList<ArrayList<Double>> utilityII = new ArrayList<>();
    private static ArrayList<ArrayList<Double>> utilityIINorm = new ArrayList<>();
    private static ArrayList<ArrayList<Double>> utilityUU = new ArrayList<>();
    private static ArrayList<ArrayList<Double>> utilityUUNorm = new ArrayList<>();
    private static HashMap<Integer, HashMap<Integer, Double>> slicnostiItema = new HashMap<>();
    private static ArrayList<String> upiti = new ArrayList<>();

    public static void main(String[] args) {
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
                //utilityIINorm.add(redak);
            } else if (i == N + 1) {
                Q = Integer.parseInt(line);
            } else {
                upiti.add(line);
            }
            i++;
        }
        scanner.close();

        utilityIINorm = Normalize(utilityII);
        //utilityUU = transpose(utilityII);
        //utilityUUNorm = Normalize(utilityUU);

        for (String upit : upiti) {
            String[] upit_split = upit.split(" ");
            int item = Integer.parseInt(upit_split[0]);
            int user = Integer.parseInt(upit_split[1]);
            int type = Integer.parseInt(upit_split[2]);
            int k = Integer.parseInt(upit_split[3]);
            double ocjena;
            if (type == 0) {
                ocjena = itemItem(item, user, k);
            } else {
                ocjena = userUser(item, user, k);
            }
            System.out.println(ocjena);
        }


        //za k najslicnijii izracunati predikciju
    }

    private static double userUser(int item, int user, int k) {
        return 0.;
    }

    private static double itemItem(int item, int user, int k) {
        //izracunati slicnost itema
        double sum1 = 0.;
        double sum2 = 0.;
        double sum3 = 0.;
        HashMap<Integer, Double> slicnosti = new HashMap<>();
        if (slicnostiItema.containsKey(item)) {
            slicnosti = slicnostiItema.get(item);
        } else {
            for (int i = 0; i < utilityIINorm.size(); i++) {
                for (int j = 0; j < utilityIINorm.get(i).size(); j++) {
                    sum1 += utilityIINorm.get(item).get(j) * utilityIINorm.get(i).get(j);
                    sum2 += Math.pow(utilityIINorm.get(item).get(j), 2);
                    sum3 += Math.pow(utilityIINorm.get(i).get(j), 2);
                }
                slicnosti.put(i, sum1 / Math.sqrt(sum2 * sum3));
                sum1 = 0.;
                sum2 = 0.;
                sum3 = 0.;
            }
            slicnostiItema.put(item, slicnosti);
        }
        //pronaci k najslicnijih i po njima izracunat
        sum1 = 0.;
        sum2 = 0.;
        for (int i = 0; i < k+1; i++) {
            Integer key = Collections.max(slicnosti.entrySet(), Map.Entry.comparingByValue()).getKey();
            double sim = slicnosti.get(key);
            if (sim > 0.) {
                if (sim != 1.0) {
                    if (utilityII.get(key).get(user) != 0.) {
                        sum1 += utilityII.get(key).get(user) * sim;
                        sum2 += sim;
                    }
                }
            } else {
                break;
            }
            slicnosti.remove(key);
        }
        return sum1 / sum2;
    }

    private static ArrayList<ArrayList<Double>> Normalize(ArrayList<ArrayList<Double>> utilityII) {
        ArrayList<ArrayList<Double>> utilityIINorm = new ArrayList<>();
        int i;
        for (i = 0; i < utilityII.size(); i++) {
            utilityIINorm.add(i, new ArrayList<>());
            Double mean = average(utilityII.get(i));
            for (int j = 0; j < utilityII.get(i).size(); j++) {
                if (utilityII.get(i).get(j) != 0.) {
                    double trenutno = utilityII.get(i).get(j);
                    utilityIINorm.get(i).add(j, (trenutno - mean));
                } else {
                    utilityIINorm.get(i).add(j, (0.));
                }
            }
        }
        return utilityIINorm;
    }

    /*private static ArrayList<ArrayList<Double>> transpose (ArrayList<ArrayList<Double>> matrixIn){
        ArrayList<ArrayList<Double>> matrixOut = new ArrayList<>();
        //for each row in matrix
        for (int r = 0; r < matrixIn.size(); r++){
            ArrayList<Double> innerIn = matrixIn.get(r);

            //for each item in that row
            for (int c = 0; c < innerIn.size(); c++){

                //add it to the outgoing matrix

                //get matrixOut current value
                ArrayList<Double> matrixOutRow = matrixOut.get(c);
                //add new one
                matrixOutRow.add(innerIn.get(c));
                //reset to matrixOut
                matrixOut.set(c,matrixOutRow);
            }
        }
        return matrixOut;
    }*/

    private static Double average(ArrayList<Double> list) {
        double mean = 0.;
        for (double ocjena : list) {
            mean += ocjena;
        }
        mean /= list.stream().filter(v -> v != 0.).count();
        return mean;
    }
}
