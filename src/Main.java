import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Main {
    public static ArrayList<Hit> list;

    public static void main(String[] args) throws IOException {
        list = new ArrayList<>();

        ArrayList<DataModel> testModels = new ArrayList<>();

        BufferedReader br = new BufferedReader(new FileReader("src/big_file.txt"));
        StringBuilder sb = new StringBuilder();

        String line = br.readLine();
        ArrayList<Double> close = new ArrayList<>();
        int index = 0 ;
        for (int i = 0; line != null; i++) {
            index++;
            sb.append(line).append("\n");
            if (index==1000){
            close.add(Double.valueOf(line));
            index = 0;}
            line = br.readLine();
        }

        String fileAsString = sb.toString();


//        int [] open = {50,60,80,40,30,110,250,360,120,120,350,420,230,740,120,560,450,560,780,890,8990};
        Double[] closes = new Double[close.size()];
        for (int j = 0 ; j<close.size();j++){
            closes[j]= close.get(j);
        }
//        int [] high = {50,60,80,40,30,110,250,360,120,120,350,420,230,740,120,560,450,560,780,890,8990};
//        int [] low = {50,60,80,40,30,110,250,360,120,120,350,420,230,740,120,560,450,560,780,890,8990};


        DataModel dataModel;
        for (int i = 0; i < closes.length; i++) {
            dataModel = new DataModel();

            dataModel.close = closes[i];
//            dataModel.open=open[i];
//            dataModel.high=high[i];
//            dataModel.low=low[i];
            testModels.add(dataModel);
        }

        list = createHits(testModels);


    }

    public static ArrayList<Hit> createHits(ArrayList<DataModel> dataModels) {
        ArrayList<Hit> hits = new ArrayList<Hit>();


//        ArrayList<Integer> range = new ArrayList<>();
//        while (size>=EACH_RANGE){
//            range.add(EACH_RANGE);
//            size-=EACH_RANGE;
//        }
//        range.add((int) size);
//
//        for (int i = 0 ; i<range.size();i++){
//
//                int first = i*EACH_RANGE;
//                int last = ((range.get(i)==30)?i*EACH_RANGE+range.get(i):(i+1)*EACH_RANGE);
//
//                hits = findHits(first,last,range.get(i),dataModels);
//
//
//        }
//
//
//
        hits = findHits(dataModels);


        return hits;
    }

    private static ArrayList<Hit> findHits(ArrayList<DataModel> dataModels) {
        ArrayList<Hit> hits = new ArrayList<Hit>();
        Hit firstHit = new Hit();
        firstHit = hitInit(dataModels.get(0));
        firstHit.valueNumber = 0;
        firstHit.valuePrice = 0;
        if (dataModels.get(1) != null) {
            if (dataModels.get(1).close > firstHit.getClose()) {
                firstHit.pivotType = 0;
            } else {
                firstHit.pivotType = 1;
            }
        }

        hits.add(firstHit);

        double lastHitIndex = 0;
        for (int i = 1; i < dataModels.size() - 1; i++) {
            Hit hit = new Hit();
            Hit lastHit = hits.get(hits.size() - 1);
            findPivotSupport(i, dataModels);
            if (findPivotResistance(i, dataModels)) {
                hit = hitInit(dataModels.get(i));
                hit.pivotType = 0;
                hit.valuePrice = Math.abs(lastHit.close - hit.close);
                hit.valueNumber = i - lastHitIndex;
                lastHitIndex = i;

            } else if (findPivotSupport(i, dataModels)) {
                hit = hitInit(dataModels.get(i));
                hit.pivotType = 1;
                hit.valuePrice = Math.abs(lastHit.close - hit.close);
                hit.valueNumber = i - lastHitIndex;
                lastHitIndex = i;
            }
            if (hit.close != 0) {
                hits.add(hit);
            }
            System.out.println("last hit is" + lastHitIndex + "\n");
        }

        Hit hit;
        for (int i = 0; i < hits.size(); i++) {
            hit = new Hit();
            hit = hits.get(i);
            System.out.print("\n" + "hit close is : " + hit.close + "\n" +
                    "hit valueNumber is : " + hit.valueNumber + "\n" +
                    "hit price difference is : " + hit.valuePrice + "\n"
                    );
        }


        return hits;
    }

    private static boolean findPivotSupport(int index, ArrayList<DataModel> dataModels) {
        boolean pivotType = false;


        if (dataModels.get(index).close < dataModels.get(index + 1).close && dataModels.get(index).close < dataModels.get(index - 1).close) {
            pivotType = true;
        }
        return pivotType;
    }

    private static boolean findPivotResistance(int index, ArrayList<DataModel> dataModels) {
        boolean pivotType = false;
        if (dataModels.get(index).close > dataModels.get(index + 1).close && dataModels.get(index).close > dataModels.get(index - 1).close) {
            pivotType = true;
        }
        return pivotType;
    }

    private static Hit hitInit(DataModel dataModel) {
        Hit hit = new Hit();
        hit.close = dataModel.close;
        hit.open = dataModel.open;
        hit.high = dataModel.high;
        hit.low = dataModel.low;
        return hit;
    }

    private static ArrayList<Chart> makeCart(ArrayList<Hit> hits) {
        ArrayList<Chart> charts = new ArrayList<Chart>();
        double[] prices = new double[hits.size()];


        prices[0] = hits.get(0).close;
//        charts.add(prices[0],);
        Chart chart;
        for (int i = 1; i < hits.size(); i++) {
            chart = new Chart();

        }


        return charts;
    }


    private int valueFinder(ArrayList<Hit> hits) {
        int value = 0;


        for (int index = 0; index < hits.size(); index++) {

            if (hits.get(index).pivotType == 0) {
                if (hits.get(index).close < hits.get(index - 2).close) {
                    valuePriceFinder(index, index, hits);
                    index -= 2;
                    bestMinFinder(hits, index);
                } else {
                    valuePriceFinder(index, index, hits);
                }
            } else {
                if (hits.get(index).close > hits.get(index - 2).close) {
                    valuePriceFinder(index, index, hits);
                    index -= 2;
                    bestMaxFinder(hits, index);
                } else {
                    valuePriceFinder(index, index, hits);
                }
            }
        }

//        do{
//           while (hits.get(index).pivotType==0&&hits.get(index).valuePrice>hits.get(i).valuePrice){
//               priceDis = Math.abs(hits.get(index).valuePrice-hits.get(i).valuePrice);
//               countDis = Math.abs(hits.get(index).valueNumber-hits.get(i).valueNumber);
//               if (i>2){
//                   i -=2;
//               }else {
//                   i=0;
//               }
//           }if (hits.get(index).pivotType==0&&hits.get(index).valuePrice>hits.get(i).valuePrice){
//
//           }
//               if (i>2){
//                   i -=2;
//               }else {
//                   i--;
//               }
//
//
//        }while (hits.get(index).pivotType!=hits.get(i).pivotType);{
//            priceDis = Math.abs(hits.get(index).valuePrice-hits.get(i).valuePrice);
//            countDis = Math.abs(hits.get(index).valueNumber-hits.get(i).valueNumber);
//        }

        return value;
    }

    private void bestMinFinder(ArrayList<Hit> hits, int index) {
        if (hits.get(index).close < hits.get(index - 2).close) {
            valuePriceFinder(index, index, hits);
            index -= 2;
            bestMinFinder(hits, index);
        } else if (Double.parseDouble(String.valueOf(Math.abs(hits.get(index + 1).close - hits.get(index).close))) <
                (double) (38 / 100) * Double.parseDouble(String.valueOf(Math.abs(hits.get(index + 2).close - hits.get(index - 1).close)))) {
            valuePriceFinder(index, index, hits);
            index -= 2;
            bestMinFinder(hits, index);
        }

    }

    private void bestMaxFinder(ArrayList<Hit> hits, int index) {
        if (hits.get(index).close > hits.get(index - 2).close) {
            valuePriceFinder(index, index, hits);
            index -= 2;
            bestMinFinder(hits, index);
        } else if (Double.parseDouble(String.valueOf(Math.abs(hits.get(index + 1).close - hits.get(index).close))) >
                (double) (38 / 100) * Double.parseDouble(String.valueOf(Math.abs(hits.get(index + 2).close - hits.get(index - 1).close)))) {
            valuePriceFinder(index, index, hits);
            index -= 2;
            bestMinFinder(hits, index);
        }

    }

    void valuePriceFinder(int pointerIndex, int i, ArrayList<Hit> hits) {
        //TODO
        hits.get(pointerIndex).valuePrice = Math.abs(hits.get(i).close - hits.get(i - 1).close);
    }
}
