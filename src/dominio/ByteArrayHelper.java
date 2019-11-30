package src.dominio;

/**
 * Esta clase contiene funciones auxiliares para byte arrays.
 * 
 * @author Pol Aguilar
 */
public class ByteArrayHelper 
{
    /**
     * Concadena dos arrays de bytes.
     * 
     * @param a primer array de bytes
     * @param b segundo array de bytes
     * @return concadenación de a y b
     */
    public static byte[] concatenate(byte[] a, byte[] b) {
        byte[] c = new byte[a.length + b.length];
        System.arraycopy(a, 0, c, 0, a.length);
        System.arraycopy(b, 0, c, a.length, b.length);

        return c;
    }

    /**
     * Convierte un entero en los nBytes.
     * 
     * @param n entero a convertir
     * @param nBytes numero de bytes con los que se representara el entero
     * @return array de bytes que se representa n
     */
    public static byte[] intToByteArray(int n, int nBytes)
    {
        byte[] buffer = new byte[nBytes];
        for(int i=0; i<nBytes; i++) buffer[i] = (byte) (n >>> (i * 8));
        return buffer;
    }

    /**
     * Convierte un array de bytes en un entero.
     * 
     * @param buffer array de bytes a convertir
     * @return entero
     * 
     */
    public static int byteArrayToInt(byte[] buffer)
    {
        int n = 0;
        for (int i = 0; i < buffer.length; i++) {
            int a = ((buffer[i] & 0xff)) * (int) Math.pow(2,i * 8);
            n = n + a;
        }
        return n;
    }
}