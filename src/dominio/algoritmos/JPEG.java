package src.dominio.algoritmos;

import src.dominio.HuffTree;
import src.dominio.Pair;
import src.persistencia.CompressedFile;
import src.persistencia.UncompressedFile;

import java.util.*;

/**
 * Esta clase representa el algoritmo de compresión y descompresión JPEG.
 * Se encarga de comprimir y descomprimir imagenes PPM, haciendo uso de la transformada discreta de coseno y Huffman para una mayor compressión.
 *
 * @author Bartomeu Perelló
 */

public class JPEG extends Algorithm
{
    /**
     * Comprime el archivo ppm que es representado por el fichero uncompressed.
     * Inicialmente se obtiene todos los datos de la imagen y separa la parte del "Header" de los valores de los píxeles.
     *
     * @param uncompressed Archivo a comprimir.
     * @return Array de bytes con la información necesaria para poder descomprimir la imagen introducida.
     *
     * @see src.persistencia.UncompressedFile
     */
    @Override
    public byte[] compress(UncompressedFile uncompressed)
    {
        byte[] data= uncompressed.readAll();

        modQuant(Q);

        byte[] version= new byte[3];
        version[0]=data[0];
        version[1]=data[1];
        version[2]=data[2];

        int it=3;
        ArrayList<Byte> wid= new ArrayList<>();
        ArrayList<Byte> hei= new ArrayList<>();

        //OBTENIR SIZE FOTO
        boolean comment=false,first=false;
        for (; it < data.length; it++) {
            if(!comment){
                if((char)data[it] == '#'){
                    comment= true;
                }
                else if (data[it] == ' ') first= true;

                else if(! first){
                    wid.add(data[it]);
                }
                else if(first){
                    if(data[it] == 10) break;
                    hei.add(data[it]);
                }
            }
            else{
                if(data[it] == 10){
                    comment= false;
                }
            }
        }
        //OBTENIR VALOR MAXIM BYTE(UNICA UTILITAT PER SABER ON ACABA EL HEADER)
        ArrayList<Byte> max_val= new ArrayList<>();
        comment=false;

        for (++it; it < data.length; it++) {
            if(!comment){
                if((char)data[it] == '#'){
                    comment= true;
                }
                else if(data[it] == 10) break;
                else max_val.add(data[it]);
            }
            else{
                if(data[it] == 10){
                    comment= false;
                }
            }
        }
        it++;
        //OBTENIR TAMANY IMATGE
        short w= im_size(wid);
        short h= im_size(hei);

        short[][] Y= new short[h][w];
        short[][] U= new short[h][w];
        short[][] V= new short[h][w];

        //TRANSFORMACIO DE RGB A YCbC|YUV
        short R,G,B;

        for (int i = 0; i < h; i++) {
            for (int j = 0; j < w; j++) {
                R= (short) (data[it++] & 0x00FF);
                G= (short) (data[it++] & 0x00FF);
                B= (short) (data[it++] & 0x00FF);

                Y[i][j]= (short)(0 + ( 0.299 * R ) + ( 0.587 * G ) + ( 0.114 * B ) );
                U[i][j]= (short)(128 - ( 0.168736 * R ) - (0.331264 * G ) + (0.5 * B ));
                V[i][j]= (short)(128 + ( 0.5 * R ) - ( 0.418688 * G ) - ( 0.081312 * B ));
            }
        }

        //RESTAR 128 PARA PODER APLICAR DCT(FUNCIÓ AMB COSINUS, VALORS ENTRE -128,127)
        for(int i=0; i < h; i++){
            for (int j = 0; j < w; j++) {
                Y[i][j]-=128;
                U[i][j]-=128;
                V[i][j]-=128;
            }
        }

        short[][] YD= new short[h][w];
        short[][] UD= new short[h][w];
        short[][] VD= new short[h][w];

        //DCT TRANSFORMATION
        double alphaU,alphaV;

        int iiterations= h/8, jiterations= w/8;

        for (int i = 0; i < iiterations; i++) {
            for (int j = 0; j < jiterations; j++) {

                for (int u = 0; u < 8; u++) {
                    for (int v = 0; v < 8; v++) {

                        if(u == 0)alphaU= 1.0/Math.sqrt(2);
                        else alphaU= 1;
                        if(v == 0)alphaV= 1.0/Math.sqrt(2);
                        else alphaV= 1;

                        short YAUX=0;
                        short UAUX=0;
                        short VAUX=0;

                        for (int x = 0; x < 8; x++) {
                            for (int y = 0; y < 8; y++) {
                                double calc= ( Math.cos( ((2*x+1)*Math.PI*u)/16 ) * Math.cos( ((2*y+1)*Math.PI*v)/16 ) );

                                YAUX += (short)( ( Y[(8*i)+x][(8*j)+y] * calc ) );
                                UAUX += (short)( ( U[(8*i)+x][(8*j)+y] * calc ) );
                                VAUX += (short)( ( V[(8*i)+x][(8*j)+y] * calc ) );
                            }
                        }

                        double calc= (1.0/4) * alphaU * alphaV;
                        YD[(8*i)+u][(8*j)+v]= (short)(YAUX * calc);
                        UD[(8*i)+u][(8*j)+v]= (short)(UAUX * calc);
                        VD[(8*i)+u][(8*j)+v]= (short)(VAUX * calc);

                    }
                }


            }
        }

        //Cuantification
        for (int i = 0; i < iiterations; i++) {
            for (int j = 0; j < jiterations; j++) {

                for (int u = 0; u < 8; u++) {
                    for (int v = 0; v < 8; v++) {
                        YD[(8*i)+u][(8*j)+v]/= cuant_mat_lum[(u*8)+v];
                        UD[(8*i)+u][(8*j)+v]/= cuant_mat_lum[(u*8)+v];
                        VD[(8*i)+u][(8*j)+v]/= cuant_mat_lum[(u*8)+v];
                    }
                }

            }
        }

        ArrayList<Short> resY= new ArrayList<>();
        ArrayList<Short> resU= new ArrayList<>();
        ArrayList<Short> resV= new ArrayList<>();



        //SIMPLIFICAR ARRAYS
        simplify_res(resY,YD);

        simplify_res(resU,UD);

        simplify_res(resV,VD);

        //AMAGATZEMAR TOTS ELS VALORS EN UN ARRAY PER APLICAR HUFFMAN
        ArrayList<Short> ALL= new ArrayList<>();

        ALL.addAll(resY);
        ALL.addAll(resU);
        ALL.addAll(resV);

        //OBTENIR ARBRE DE HUFFMAN
        TreeMap<Short,Integer> frq_elem= new TreeMap<>();
        int allSize= ALL.size();

        for (int i = 0; i < allSize; i++) {
            short act= ALL.get(i);
            Integer aux;
            aux= frq_elem.get(act);
            if(aux != null) frq_elem.put(act,aux+1);
            else frq_elem.put(act,1);
        }

        HuffTree ht= new HuffTree();
        HashMap<Short,String> hash= ht.createHuffTree(frq_elem);

        //APLICAR HUFFMAN
        ArrayList<Byte> RES= new ArrayList<>();
        int bit=7;
        byte aux=0;


        for (int i = 0; i < allSize; i++) {
            Short key= ALL.get(i);
            String value= hash.get(key);
            int vsize= value.length();

            for (int j = 0; j < vsize; j++) {
                char c= value.charAt(j);

                if(bit == -1) {
                    RES.add(aux);
                    aux=0;
                    bit=7;
                }

                if(c == '0'){
                    aux &= ~(1 << bit--);
                }

                else{
                    aux |= 1 << bit--;
                }
            }
        }

        //PASSAR HASHMAP A ARRAY PER GUARDARLO A DADES
        ArrayList<Pair<Short,String>> hash2Array= new ArrayList<>();
        int maxSS=0;

        for(Map.Entry<Short,String> entry : hash.entrySet()) {
            Short key = entry.getKey();
            String value = entry.getValue();

            if(value.length() > maxSS)maxSS= value.length();

            Pair<Short,String> p= new Pair<>(key,value);

            hash2Array.add(p);
        }

        byte[] rbyte= new byte[2 + hash.size()*2 + hash.size()*maxSS*2 + hash.size() + 2 + 2 + RES.size()];
        int rbS= rbyte.length, i, hs= hash.size();

        //SIZE HASH
        rbyte[1]= (byte)(hs & 0xFF);
        hs= hs >>> 8;
        rbyte[0]= (byte)(hs & 0xFF);
        i=2;
        hs= hash2Array.size();
        //GUARDAR HASH

        for (int j = 0; j < hs; j++) {
            Pair<Short,String>p= hash2Array.get(j);
            short k=p.getKey();
            byte PA,PB;
            PB=(byte)(k & 0xFF);
            PA=(byte)((k >>> 8) & 0xFF);
            rbyte[i++]= PA;
            rbyte[i++]= PB;

            String val=p.getValue();
            rbyte[i++]=(byte)((val.length() >>> 8) & 0xFF);
            rbyte[i++]=(byte)(val.length() & 0xFF);
            for (int l = 0; l < val.length(); l++) rbyte[i++]= (byte)val.charAt(l);
        }
        //GUARDAR AMPLADA I LLARGADA
        rbyte[i++]= (byte)((w >>> 8) & 0xFF);
        rbyte[i++]= (byte)(w & 0xFF);
        rbyte[i++]= (byte)((h >>> 8) & 0xFF);
        rbyte[i++]= (byte)(h & 0xFF);

        //GUARDAR CODI HUFF
        for (int j = 0; j < RES.size(); j++) rbyte[i++]= RES.get(j);

        return rbyte;
    }

    /**
     * Descomprime el fichero comprimido anteriormente para recuperar la imagen original.
     * Inicialmente se obtiene todos los datos del archivo comprimido y mediante Huffman se recuperan los datos comprimidos necesarios para recuperar la imagen original.
     *
     * @param compressedBytes Archivo a descomprimir.
     * @return Array de bytes con la estructura necesaria para ser reprentado como un archivo PPM.
     *
     * @see src.persistencia.UncompressedFile
     */
    @Override
    public byte[] decompress(CompressedFile compressedBytes)
    {
        byte[] data= compressedBytes.readAll();

        modQuant(Q);

        //RESTAURAR HASHMAP
        int it=0;
        int hs=( ( (data[it++] & 0xFF) << 8) | (data[it++] & 0xFF) );

        HashMap<String,Short> hash= new HashMap<>();

        for (int i = 0; i < hs; i++) {
            Short key= (short)( ( (data[it++] & 0xFF) << 8) | (data[it++] & 0xFF) );

            short slength=(short)( ( (data[it++] & 0xFF) << 8) | (data[it++] & 0xFF) );
            byte[]aux=new byte[slength];

            for (int j = 0; j < slength; j++) aux[j]=data[it++];
            String value= new String(aux);

            hash.put(value,key);
        }
        //RECONSTRUIR INPUT
        char ver='6';
        short w,h;

        w= (short)( ( (data[it++] & 0xFF) << 8) | (data[it++] & 0xFF) );
        h= (short)( ( (data[it++] & 0xFF) << 8) | (data[it++] & 0xFF) );

        int imageSize=w*h;

        short[] Y=new short[imageSize];
        short[] U=new short[imageSize];
        short[] V=new short[imageSize];

        //RESTAURAR VALORS
        int pos=0;
        boolean keyFound=false,first=true,zeros=false;
        String key="";
        int bit=7;
        byte actByte=0;
        byte[] val = new byte[1];
        Short elem=0;

        int iiterations= h/8, jiterations=w/8;

        //CREAR MATRIUS
        short[][] YD= new short[h][w];
        short[][] UD= new short[h][w];
        short[][] VD= new short[h][w];

        //RESTAURAR Y
        for (int i = 0; i < iiterations; i++) {
            for (int j = 0; j < jiterations; j++) {

                for (int u = 0; u < 8; u++) {
                    for (int v = 0; v < 8; v++) {

                        while(! keyFound && !zeros) {
                            if(bit < 0 || first){
                                first=false;
                                bit=7;
                                actByte = data[it++];
                            }

                            val[0]= (byte) ((actByte >>> bit--) & 0x01);
                            val[0]= (byte)asciiDigits[val[0]];
                            String aux= new String(val);
                            key+=aux;

                            elem=(hash.get(key));
                            if(elem != null){
                                if(elem == 3000) zeros=true;
                                keyFound=true;
                            }
                        }
                        key="";
                        keyFound=false;

                        if(zeros)YD[(8*i)+u][(8*j)+v]=0;
                        else YD[(8*i)+u][(8*j)+v]=elem;
                    }
                }
                zeros=false;
            }
        }

        //RESTAURAR U
        for (int i = 0; i < iiterations; i++) {
            for (int j = 0; j < jiterations; j++) {

                for (int u = 0; u < 8; u++) {
                    for (int v = 0; v < 8; v++) {

                        while(! keyFound && !zeros) {
                            if(bit < 0 || first){
                                first=false;
                                bit=7;
                                actByte = data[it++];
                            }

                            val[0]= (byte) ((actByte >>> bit--) & 0x01);
                            val[0]= (byte)asciiDigits[val[0]];
                            String aux= new String(val);
                            key+=aux;

                            elem=hash.get(key);
                            if(elem != null){
                                if(elem == 3000) zeros=true;
                                keyFound=true;
                            }
                        }
                        key="";
                        keyFound=false;

                        if(zeros)UD[(8*i)+u][(8*j)+v]=0;
                        else UD[(8*i)+u][(8*j)+v]=elem;
                    }
                }
                zeros=false;
            }
        }

        //RESTAURAR V
        for (int i = 0; i < iiterations; i++) {
            for (int j = 0; j < jiterations; j++) {

                for (int u = 0; u < 8; u++) {
                    for (int v = 0; v < 8; v++) {

                        while(! keyFound && !zeros) {
                            if(bit < 0 || first){
                                first=false;
                                bit=7;
                                actByte = data[it++];
                            }

                            val[0]= (byte) ((actByte >>> bit--) & 0x01);
                            val[0]= (byte)asciiDigits[val[0]];
                            String aux= new String(val);
                            key+=aux;

                            elem=hash.get(key);
                            if(elem != null){
                                if(elem == 3000) zeros=true;
                                keyFound=true;
                            }
                        }
                        key="";
                        keyFound=false;

                        if(zeros)VD[(8*i)+u][(8*j)+v]=0;
                        else VD[(8*i)+u][(8*j)+v]=elem;
                    }
                }
                zeros=false;
            }
        }

        //Decuantification
        for (int i = 0; i < iiterations; i++) {
            for (int j = 0; j < jiterations; j++) {

                for (int u = 0; u < 8; u++) {
                    for (int v = 0; v < 8; v++) {
                        YD[(8*i)+u][(8*j)+v]*= cuant_mat_lum[(u*8)+v];
                        UD[(8*i)+u][(8*j)+v]*= cuant_mat_lum[(u*8)+v];
                        VD[(8*i)+u][(8*j)+v]*= cuant_mat_lum[(u*8)+v];
                    }
                }

            }
        }

        short[][] YN= new short[h][w];
        short[][] UN= new short[h][w];
        short[][] VN= new short[h][w];


        //INVERSE DCT
        double alphaU,alphaV;

        for (int i = 0; i < iiterations; i++) {
            for (int j = 0; j < jiterations; j++) {

                for (int x = 0; x < 8; x++) {
                    for (int y = 0; y < 8; y++) {

                        short YAUX=0;
                        short UAUX=0;
                        short VAUX=0;

                        for (int u = 0; u < 8; u++) {
                            for (int v = 0; v < 8; v++) {

                                if(u == 0)alphaU= 1.0/Math.sqrt(2);
                                else alphaU= 1;
                                if(v == 0)alphaV= 1.0/Math.sqrt(2);
                                else alphaV= 1;

                                double calc= ( Math.cos( ((2*x+1)*Math.PI*u)/16 ) * Math.cos( ((2*y+1)*Math.PI*v)/16 ) );
                                calc= calc * alphaV*alphaU;

                                YAUX += (short)( ( YD[(8*i)+u][(8*j)+v] * calc ) );
                                UAUX += (short)( ( UD[(8*i)+u][(8*j)+v] * calc ) );
                                VAUX += (short)( ( VD[(8*i)+u][(8*j)+v] * calc ) );
                            }
                        }

                        double calc= (1.0/4);
                        YN[(8*i)+x][(8*j)+y]= (short)(YAUX * calc);
                        UN[(8*i)+x][(8*j)+y]= (short)(UAUX * calc);
                        VN[(8*i)+x][(8*j)+y]= (short)(VAUX * calc);

                    }
                }


            }
        }

        //SUMAR
        for(int i=0; i < h; i++){
            for (int j = 0; j < w; j++) {
                YN[i][j]+=128;
                UN[i][j]+=128;
                VN[i][j]+=128;
            }
        }

        short[] R= new short[imageSize];
        short[] G= new short[imageSize];
        short[] B= new short[imageSize];

        //TRANSFORMAR DE YUV-> RGB
        it=0;
        for (int i = 0; i < h; i++) {
            for (int j = 0; j < w; j++) {
                R[it]= (short) (YN[i][j] + ( 1.402 * ( VN[i][j] - 128 ) ) );
                G[it]= (short) (YN[i][j] - ( 0.344136 * ( UN[i][j] - 128 ) ) - ( 0.714136 * ( VN[i][j] - 128 ) ) );
                B[it]= (short) (YN[i][j] + ( 1.772 * (UN[i][j]- 128 ) ) );
                ++it;
            }
        }

        char[] he= short2ascii(h);
        char[] wi= short2ascii(w);
        byte[] RES= new byte[imageSize * 3 + 3 + wi.length + 1 +he.length + 1 + 3 +1];

        //INSERIR HEADER
        RES[0]='P';
        RES[1]=(byte)ver;
        RES[2]=10;
        pos=3;

        for (int i = 0; i < wi.length; i++) RES[pos++]=(byte)wi[i];

        RES[pos++]=' ';

        for (int i = 0; i < he.length; i++) RES[pos++]=(byte)he[i];

        RES[pos++]= 10;

        RES[pos++]='2';
        RES[pos++]='5';
        RES[pos++]='5';
        RES[pos++]=10;

        for (int i = 0; i < imageSize; i++) {
            RES[pos++]=(byte)(R[i] & 0xFF);
            RES[pos++]=(byte)(G[i] & 0xFF);
            RES[pos++]=(byte)(B[i] & 0xFF);
        }
        return RES;
    }

    private int Q=50;

    /** Matriz utilizada para reducir las bajas frequiencias en el canal de luminosidad Y*/
    private static double[] cuant_mat_lum= {
            16,11,10,16,24,40,51,61,
            12,12,14,19,26,58,60,55,
            14,13,16,24,40,57,69,56,
            14,17,22,29,51,87,80,62,
            18,22,37,56,68,109,103,77,
            24,35,55,64,81,104,113,92,
            49,64,78,87,103,121,120,101,
            72,92,95,98,112,100,103,99
    };


    /** Array para poder generar un cambio de un digito a su equivalente en carácter*/
    private static char[] asciiDigits={'0','1','2','3','4','5','6','7','8','9'};

    /** Funcion que pasa un numero a su equivalente en ASCII.
     *
     * @param i Valor a transformar.
     * @return Retorna un array de carácteres representando el valor introducido.
     * */
    private char[] short2ascii(short i){

        char[] res;
        short aux= i;
        ArrayList<Character> ra= new ArrayList<>();
        while (aux > 0){
            ra.add(asciiDigits[aux%10]);
            aux/=10;
        }
        res= new char[ra.size()];
        int k=0;
        for (int j = ra.size()-1; j >= 0; j--) res[k++]= ra.get(j);
        return res;
    }

    /** Funcion que retorna el valor representado por un conjunto de Bytes
     *
     * @param in ArrayList que contiene los bytes que representan un número concreto.
     * @return Retorna un short que es la representación de los bytes del input.
     * */
    private short im_size(ArrayList<Byte> in){

        short aux=0;

        for (int i = 0; i < in.size(); i++) {
            aux=(short) ((aux) +(in.get(i)-(byte)48));
            if(i < in.size()-1)aux *=10;
        }
        return aux;
    }

    /** Funcion que reduce el tamaño del intput introducido mediante marcas.
     * @param res ArrayList que al final de la función contriene los valores de data comprimidos.
     * @param data Array de shorts que necesita ser comprimido.
     * */
    private void simplify_res(ArrayList<Short> res, short[][] data) {
        ArrayList<Short> aux= new ArrayList<>();
        boolean endseq=false;
        int iiterations= data.length/8, jiterations= data[0].length/8;

        for (int i = 0; i < iiterations; i++) {
            for (int j = 0; j < jiterations; j++) {

                for (int u = 0; u < 8; u++) {
                    for (int v = 0; v < 8; v++) {

                        if (endseq) {
                            if (data[(8 * i) + u][(8 * j) + v] != 0) {
                                endseq = false;
                                res.addAll(aux);
                                aux.clear();
                                res.add(data[(8 * i) + u][(8 * j) + v]);
                            } else {
                                aux.add(data[(8 * i) + u][(8 * j) + v]);
                            }
                        }
                        else{
                            if(data[(8 * i) + u][(8 * j) + v] == 0){
                                endseq=true;
                                aux.add(data[(8 * i) + u][(8 * j) + v]);
                            }
                            else{
                                res.add(data[(8 * i) + u][(8 * j) + v]);
                            }
                        }

                    }
                }
                aux.clear();
                if(endseq){
                    res.add((short)3000);
                    endseq=false;
                }
            }
        }

        /*
        for (int i = 0; i < imageSize; i++) {

            if(i%64 == 0 && i != 0){
                aux.clear();
                if(endseq){
                    res.add((short)3000);
                    endseq=false;
                }
            }

            if(endseq) {
                if(data[i] != 0) {
                    endseq=false;
                    res.addAll(aux);
                    aux.clear();
                    res.add(data[i]);
                }
                else {
                    aux.add(data[i]);
                }
            }

            else{
                if(data[i] == 0){
                    endseq=true;
                    aux.add(data[i]);
                }
                else{
                    res.add(data[i]);
                }
            }
        }
        if(endseq){
            res.add((short)3000);
        }
        */
    }

    /** Funcion que modifica la matriz de cuantificación
     * @param Q Valor que indica el indice de calidad a comprimir
     * */
    private void modQuant(int Q){
        int S;
        if(Q < 50) S= 5000/Q;
        else S= 200 - 2*Q;

        for (int i = 0; i < 64; i++) {
            cuant_mat_lum[i]= Math.floor( ( S * cuant_mat_lum[i] + 50) / 100 );
            if(cuant_mat_lum[i] == 0) cuant_mat_lum[i]=1;
        }
    }

    /**
     * Retorna el nombre de este algoritmo
     *
     * @return nombre del algoritmo
     */
    public String getName()
    {
        return "JPEG";
    }

}