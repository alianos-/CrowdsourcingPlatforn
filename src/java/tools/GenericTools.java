/*
 * To change this template, choose GenericTools | Templates
 * and open the template in the editor.
 */
package tools;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

/**
 *
 * @author alianos 
 */
public class GenericTools {

    /**
     * It merges the two given object in a new one. If the same variables overlap, 
     * the ones of b object are used. The original items are unaffected.
     * @param a
     * @param b
     * @return 
     */
    public static JsonObject merge( JsonObject a, JsonObject b ) {
        JsonObject merged = new JsonObject();
        for( Map.Entry<String, JsonElement> entry : a.entrySet() ) {
            merged.add( entry.getKey(), entry.getValue() );
        }
        for( Map.Entry<String, JsonElement> entry : b.entrySet() ) {
            merged.add( entry.getKey(), entry.getValue() );
        }
        return merged;
    }

    public static Pair<Double, Integer> getMaxWithIndex( double[] numbers ) {
        double max = Double.NEGATIVE_INFINITY;
        int index = -1;
        for( int i = 0; i < numbers.length; i++ ) {
            double d = numbers[i];
            if( d > max ) {
                index = i;
                max = d;
            }
        }
        return new Pair( max, index );
    }

    public static List removeAllInstances( Object of, List from ) {
        while( from.remove( of ) );

        return from;
    }

    public static boolean containsOnly( List list, Object o ) {
        for( Object object : list ) {
            if( object != o ) {
                return false;
            }
        }
        return true;
    }

    public static double getMax( double[] numbers ) {
        double max = 0;
        for( double d : numbers ) {
            if( d > max ) {
                max = d;
            }
        }
        return max;
    }

    public static Map<Integer, List<Double>> kMeans( List<Double> data, int numOfClusters ) {
        if( data.size() < numOfClusters ) {
            throw new IllegalArgumentException( "Cannot split that few data to that many clusters" );
        }
        Set discrete = new TreeSet( data );
        if( discrete.size() < numOfClusters ) {
            throw new IllegalArgumentException( "There are less discrete values than the clusters asked" );
        }

        double[] means = new double[numOfClusters];
        for( int i = 0; i < numOfClusters; i++ ) {
            means[i] = data.get( (i * data.size() / numOfClusters) );
        }
        for( int i = 0; i < numOfClusters; i++ ) {
            List cleared = Arrays.asList( means );
            cleared.remove( means[i] );
            if( cleared.contains( means[i] ) ) {
                System.out.println( "double mean @ " + means[i] );
                means[i]++;
                i = 0;
            }
        }
        return kMeans( data, means );

    }

    public static Map<Integer, List<Double>> kMeans( List<Double> data, double[] means ) {
        int numOfClusters = means.length;

        Map<Integer, List<Double>> clusters = new HashMap<Integer, List<Double>>();
        for( int i = 0; i < numOfClusters; i++ ) {
            clusters.put( i, new ArrayList<Double>() );
        }

        for( Double number : data ) {
            double shorterDistance = 100000000000000000000000000000000000000000000000000.0;
            int assignedMean = 0;
            for( int i = 0; i < numOfClusters; i++ ) {
                double mean = means[i];
                double thisDistance = Math.abs( mean - number );
                if( thisDistance < shorterDistance ) {
                    shorterDistance = thisDistance;
                    assignedMean = i;
                }
            }
            List<Double> thisCluster = clusters.get( assignedMean );
            thisCluster.add( number );
            clusters.put( assignedMean, thisCluster );
        }
        //calculate new means
        double[] newMeans = new double[numOfClusters];
        for( int i = 0; i < numOfClusters; i++ ) {
            double total = 0;
            for( Double number : clusters.get( i ) ) {
                total += number;
            }
            newMeans[i] = total / clusters.get( i ).size();
        }
        if( meansDifference( means, newMeans ) ) {
            return clusters;
        }
        else {
            return kMeans( data, newMeans );
        }

    }

    private static boolean meansDifference( double[] means, double[] newMeans ) {
        for( int i = 0; i < means.length; i++ ) {
            if( Math.abs( means[i] - newMeans[i] ) > 1 ) {
                return false;
            }
        }
        return true;
    }

    /**
     * Accepts to equal sized arrays, and returns an array that contains the average of the 2 for each
     * position
     *
     * @param array1
     * @param array2
     * @return
     */
    public static double[] getAvg( double[] array1, double[] array2 ) {
        if( array1.length != array2.length ) {
            throw new IllegalArgumentException( "Provided arrays are of different size" );
        }
        double[] result = new double[array1.length];
        for( int i = 0; i < array1.length; i++ ) {
            result[i] = (array1[i] + array2[i]) / 2;
        }

        return result;
    }

    public static double getAvg( double[] numbers ) {
        double avg = 0d;
        for( double d : numbers ) {
            avg += d;
        }
        avg = avg / numbers.length;

        return avg;
    }

    public static double getAverageLength( List<String> values ) {
        double average = 0d;

        if( values.size() > 0 ) {
            for( String value : values ) {
                average += value.length();
            }
            average = average / values.size();
        }

        return average;
    }

    /**
     * Returns a pair with the number of iterations and the percentage of training data.
     *
     * @return
     */
//    public static Pair<Integer, Integer> whoAmI() {
//        String threadName = Thread.currentThread().getName();
//        Pattern p = Pattern.compile( "\\[(.*?)\\]" );
//        Matcher m = p.matcher( threadName );
//
//        int i = 0;
//        Pair<Integer, Integer> pair = new Pair<Integer, Integer>( -1, -1 );
//        while( m.find() ) {
//            if( i == 0 ) {
//                pair.setValueA( Integer.parseInt( m.group( 1 ) ) );
//            }
//            else if(i == 1){
//                pair.setValueB( Integer.parseInt( m.group( 1 ) ) );
//            }
//            i++;
//        }
//        
//        return pair;
//    }
    public static String getTime() {
        Calendar cal = Calendar.getInstance();
        cal.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat( "HH:mm:ss" );
        return sdf.format( cal.getTime() );
    }

    public static String getUsedMemory() {
        Runtime runtime = Runtime.getRuntime();
        return ((runtime.totalMemory() - runtime.freeMemory()) / (1024 * 1024)) + "";
    }
}
