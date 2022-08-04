
/**
 * name: Eric Osterman
 * assignment: PP3
 */


import java.util.ArrayList;

/**
 * MapEntry class made for data structures in project
 * built using UML given in PP3
 */
public class MapEntry<K, V>{
    private K key;
    private V value;

    // constructor to intialize key to K and value to V
    public MapEntry(K key, V value){
        this.key = key;
        this.value = value;
    }

    // returns value of key
    public K getKey(){
        return key;
    }

    // returns the value of the may entry
    public V getValue(){
        return value;
    }

    // sets the value of key to k
    public void setKey(K k){
        this.key = k;
    }

    // sets the value of value to v
    public void setValue(V v){
        this.value = v;
    }

    // returns a formatted string that contains the values of key and value as a pair
    @Override
    public String toString(){
        return String.format("(%s, %s)", key, value);
    } 

} //end of class