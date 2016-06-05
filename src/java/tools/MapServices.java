/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tools;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author alianos
 */
public class MapServices<KT, VT> {

    public static void friendlyPrint( Map mp ) {
        Iterator it = mp.entrySet().iterator();
        while( it.hasNext() ) {
            Map.Entry pairs = (Map.Entry) it.next();
            System.out.println( pairs.getKey() + " = " + pairs.getValue() );
        }
    }

    public static synchronized void friendlyPrintToFile( Map mp, String filename ) {
        Iterator it = mp.entrySet().iterator();
        while( it.hasNext() ) {
            Map.Entry pairs = (Map.Entry) it.next();
            Log.log( filename, pairs.getKey() + " = " + pairs.getValue() );
        }
    }

    /**
     * Returns an array with the maps that represent found/notFound/shouldBe
     *
     * @param oMap1 Needs to be LinkedHashMap so we can cut the first TOP components to compare.
     * @param oMap2
     * @param top The top X element to compare
     * @return [0] - same attributes<br>[1]different attributes in map1<br>[2]different attributes in map2
     */
    public static Map[] compareMaps( LinkedHashMap<String, Double> oMap1, LinkedHashMap<String, Double> oMap2, int top ) {
        Map<String, Double> map1 = trimMap( oMap1, top );
        Map<String, Double> map2 = trimMap( oMap2, top );
        Map<String, Double> found = new LinkedHashMap<String, Double>();
        Map<String, Double> notFound = new LinkedHashMap<String, Double>();
        Map<String, Double> shouldBe = new LinkedHashMap<String, Double>();
        Map[] result = new Map[3];
        for( String key1 : map1.keySet() ) {
            if( map2.containsKey( key1 ) ) {
                found.put( key1, map1.get( key1 ) );
            }
            else {
                notFound.put( key1, map1.get( key1 ) );
            }
        }
        for( String key2 : map2.keySet() ) {
            if( !found.containsKey( key2 ) ) {
                shouldBe.put( key2, map2.get( key2 ) );
            }
        }
        result[0] = found;
        result[1] = notFound;
        result[2] = shouldBe;
        return result;
    }

    /**
     * Checks whether the given map contains any of the given keys.
     *
     * @param map
     * @param keys
     * @return
     */
    public static <KT, VT> boolean containsAnyKey( Map<KT, VT> map, List<KT> keys ) {
        if( keys == null || map == null ) {
            return false;
        }
        for( KT key : keys ) {
            if( map.containsKey( key ) ) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns the first Top elements of the map.
     *
     * @param map1
     * @param top
     * @return
     */
    public static LinkedHashMap<String, Double> trimMap( LinkedHashMap<String, Double> map1, int top ) {
        LinkedHashMap<String, Double> trimmedMap = new LinkedHashMap<String, Double>();
        int compared = 0;
        for( Map.Entry<String, Double> entry : map1.entrySet() ) {
            if( compared == top ) {
                break;
            }
            compared++;
            trimmedMap.put( entry.getKey(), entry.getValue() );
        }
        if( compared < top ) {
            System.out.println( " there was a map that did not have as many elements as needed" );
        }
        return trimmedMap;
    }

    public static <K, V> LinkedHashMap<K, V> addToHead( Map<K, V> existingMap, K key, V val ) {
        LinkedHashMap<K, V> newMap = new LinkedHashMap<K, V>();
        newMap.put( key, val );
        for( Map.Entry<K, V> entry : existingMap.entrySet() ) {
            newMap.put( entry.getKey(), entry.getValue() );
        }
        return newMap;
    }
    Map<KT, VT> map;

    public MapServices( Map<KT, VT> map ) {
        this.map = map;
    }

    public List<KT> getKeysForValue( VT value ) {
        List<KT> keys = new ArrayList<KT>();
        for( Map.Entry<KT, VT> entry : map.entrySet() ) {
            if( value.equals( entry.getValue() ) ) {
                keys.add( entry.getKey() );
            }
        }
        return keys;
    }

    public Map<KT, VT> removeBasedOnValues( VT value ) {

        Iterator<Map.Entry<KT, VT>> iter = map.entrySet().iterator();
        while( iter.hasNext() ) {
            Map.Entry<KT, VT> entry = iter.next();
            if( value.equals( entry.getValue() ) ) {
                iter.remove();
            }
        }

        return map;
    }

    public Integer countNumOfValues( VT value ) {
        return getKeysForValue( value ).size();
    }

    /**
     * Sirts the given map based on its values, and returns it in a new LinkedHashMap.
     * The old map in unaffected.
     * @param <N>
     * @param <O>
     * @param map
     * @return 
     */
    public static <N extends Number, O> LinkedHashMap<O, Double> valueSort( Map<O, N> map ) {
        LinkedHashMap<O, Double> sortedMap = new LinkedHashMap<O, Double>();
        for( int i = 0; i < map.size(); i++ ) {
            double max = Double.NEGATIVE_INFINITY;
            O key = null;
            for( Map.Entry<O, N> entry : map.entrySet() ) {
                double doubleValue = entry.getValue().doubleValue();
                if( doubleValue > max && !sortedMap.containsKey( entry.getKey() ) ) {
                    max = doubleValue;
                    key = entry.getKey();
                }
            }
            //safety check
            if( key == null ) {
                throw new NullPointerException( "I could not identify the next maximum. I had one job..." );
            }
            sortedMap.put( key, max );
        }

        return sortedMap;
    }
}
