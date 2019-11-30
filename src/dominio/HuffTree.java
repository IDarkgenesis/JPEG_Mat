package src.dominio;

import java.util.*;

/** La utilidad de esta clase es para representar un Arbol binario*/
class Node{
    int freq;
    short data;
    boolean has_info;

    Node left;
    Node right;
}

/** La utilidad de esta clase es para que se puedan comparar dos nodods del arbol en una Priority queue*/
class HuffComp implements Comparator<Node> {
    public int compare(Node x, Node y){
        return x.freq-y.freq;
    }
}



public class HuffTree {
    /**
     * Esta función se utiliza para crear un Arbol de Huffman y retorna un HashMap para su posterior utilización
     *
     * @param  freq_elem Un TreeMap conteniendo los valores con sus respectivas frequencias
     * @return Un HashMap representando cada valor con su codigo huffman
     * */
    public HashMap<Short,String> createHuffTree(TreeMap<Short,Integer> freq_elem) {

        int tsize= freq_elem.size();
        PriorityQueue<Node> queue= new PriorityQueue<>(tsize, new HuffComp());

        for(Map.Entry<Short,Integer> entry : freq_elem.entrySet()) {
            Short key = entry.getKey();
            Integer value = entry.getValue();

            Node root= new Node();
            root.data= key;
            root.freq= value;
            root.has_info= true;
            root.right= null;
            root.left= null;

            queue.add(root);
        }

        Node root= new Node();

        while(queue.size() > 1){
            Node l= queue.peek();
            queue.poll();

            Node r= queue.peek();
            queue.poll();

            Node p= new Node();

            p.freq= l.freq+r.freq;
            p.has_info=false;
            p.right= r;
            p.left= l;

            root= p;

            queue.add(p);
        }
        HashMap<Short,String> hash= new HashMap<>();
        genHash(root, "", hash);
        return hash;
    }

    /**
     * Esta función se utiliza para generar el HashMap con los valores y su respectivo codigo
     *
     * @param  root Un nodo que representa un elemento del Arbol de Huffman
     * @param s String que se utiliza para contruir el codigo de una clave en particular
     * @param hash Hash que se va construyendo mientras se avanza en el arbol
     * */
    public static void genHash(Node root, String s, HashMap<Short, String> hash)
    {

        if (root.left == null && root.right == null && root.has_info) {
            hash.put(root.data,s);
            return;
        }

        genHash(root.left, s+"0", hash);
        genHash(root.right, s+ "1", hash);
    }
}