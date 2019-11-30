package src.dominio;

/**
 * Esta clase es el clasico Pair con su respectiva Clave y Valor
 * */
public class Pair<T, T1> {
    T Key;
    T1 Value;

    public Pair(T key, T1 value) {
        Key = key;
        Value = value;
    }

    /**
     * Funcion que retorna la Clave del pair
     * */

    public T getKey() {
        return Key;
    }
    /**
     * Funcion que retorna el Valor del pair
     * */
    public T1 getValue() {
        return Value;
    }
}
