import java.util.Scanner;

public class S_DES {
    public static String key1;
    public static String key2;
    public static long time;
    public static int[] P10 = new int[] { 3, 5, 2, 7, 4, 10, 1, 9, 8, 6 };
    public static int[] P8 = new int[] { 6, 3, 7, 4, 8, 5, 10, 9 };
    public static int[] IP = new int[] { 2, 6, 3, 1, 4, 8, 5, 7 };
    public static int[] IP_1 = new int[] { 4, 1, 3, 5, 7, 2, 8, 6 };
    public static int[] EPBox = new int[] { 4, 1, 2, 3, 2, 3, 4, 1 };
    public static String[][] SBox1 = new String[][] {
            { "01", "00", "11", "10" }, { "11", "10", "01", "00" },
            { "00", "10", "01", "11" }, { "11", "01", "00", "10" } };
    public static String[][] SBox2 = new String[][] {
            { "00", "01", "10", "11" }, { "10", "11", "01", "00" },
            { "11", "00", "01", "10" }, { "10", "01", "00", "11" } };
    public static int[] SPBox = new int[] { 2, 4, 3, 1 };

    public static String Leftshift(String str, int n) { //移位
        char[] ch_in = str.toCharArray();
        char[] ch_out = new char[5];
        for (int i = 0; i < ch_in.length; i++) {
            int a = ((i - n) % ch_in.length);
            if (a < 0) {
                if (n == 1) {
                    ch_out[ch_in.length - 1] = ch_in[i];
                }
                if (n == 2) {
                    if (i == 0) {
                        ch_out[ch_in.length - 2] = ch_in[i];
                    } else {
                        ch_out[ch_in.length - 1] = ch_in[i];
                    }

                }
            } else {
                ch_out[a] = ch_in[i];
            }
        }
        return new String(ch_out);
    }

    public static String Permute(String str, int[] P) { //置换
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < P.length; i++) {
            sb.append(str.charAt((P[i]) - 1));
        }
        return new String(sb);
    }

    public static void getkey(String key) { //获得key1和key2
        String mainkey = key;
        mainkey = Permute(mainkey, P10);
        String Leftshift11 = mainkey.substring(0, 5);
        Leftshift11 = Leftshift(Leftshift11, 1);//移位后
        String Leftshift12 = mainkey.substring(5, 10);
        Leftshift12 = Leftshift(Leftshift12, 1);//移位后
        key1 = Leftshift11 + Leftshift12;
        key1 = Permute(key1, P8);

        String Leftshift21 = Leftshift(Leftshift11, 2);
        String Leftshift22 = Leftshift(Leftshift12, 2);
        key2 = Leftshift21 + Leftshift22;
        key2 = Permute(key2, P8);

    }

    public static String xor(String str, String key) { //异或
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < str.length(); i++) {
            if (str.charAt(i) == key.charAt(i)) {
                sb.append("0");
            } else {
                sb.append("1");
            }
        }
        return new String(sb);
    }

    public static String SearchBox(String str, int n) { //SBox的查找
        StringBuilder sb = new StringBuilder();
        sb.append(str.charAt(0));
        sb.append(str.charAt(3));
        String ret = new String(sb);
        StringBuilder sb1 = new StringBuilder();
        sb1.append(str.charAt(1));
        sb1.append(str.charAt(2));
        String ret1 = new String(sb1);
        String retu = new String();
        if (n == 1) {
            retu = SBox1[Integer.parseInt(ret, 2)][Integer.parseInt(ret1, 2)];
        } else {
            retu = SBox2[Integer.parseInt(ret, 2)][Integer.parseInt(ret1, 2)];
        }
        return retu;
    }

    public static String round(String str, int n){ //轮函数
        String R = Permute(str, EPBox);
        if(n==1){
            R=xor(R,key1);
            String R1 = R.substring(0, 4);
            String R2 = R.substring(4, 8);
            R1 = SearchBox(R1, 1);
            R2 = SearchBox(R2, 2);
            R = R1+R2;
        }
        if(n==2){
            R=xor(R,key2);
            String R1 = R.substring(0, 4);
            String R2 = R.substring(4, 8);
            R1 = SearchBox(R1, 1);
            R2 = SearchBox(R2, 2);
            R = R1+R2;
        }
        return Permute(R, SPBox);
    }

    public static String encipher(String input) { //加密

        String plaintext = input;
        plaintext = Permute(plaintext, IP);
        String L0 = plaintext.substring(0, 4);
        String R0 = plaintext.substring(4, 8);
        String L1 = R0;
        String R1 = xor(round(R0,1), L0);
        String L2 = xor(round(R1,2), L1);
        String R2 = R1;
        String ciphertext = L2 + R2;
        ciphertext = Permute(ciphertext, IP_1);
        return ciphertext;
    }

    public static String decode(String input) { //解密
        String plaintext = input;
        plaintext = Permute(plaintext, IP);
        String L0 = plaintext.substring(0, 4);
        String R0 = plaintext.substring(4, 8);
        String L1 = R0;
        String R1 = xor(round(R0,2), L0);
        String L2 = xor(round(R1,1), L1);
        String R2 = R1;
        String ciphertext = L2 + R2;
        ciphertext = Permute(ciphertext, IP_1);
        return ciphertext;
    }

    public static StringBuilder ACSII_encipher(String input) { //ASCII字符加密
        String plaintext = input;
        StringBuilder ciphertext = new StringBuilder();
        for(int i = 0; i < plaintext.length(); i++) {
            char ch_in = plaintext.charAt(i);
            int ascii_in = (int) ch_in;
            String text = String.format("%08d", Integer.parseInt(Integer.toBinaryString(ascii_in)));
            text = Permute(text, IP);
            String L0 = text.substring(0, 4);
            String R0 = text.substring(4, 8);
            String L1 = R0;
            String R1 = xor(round(R0, 1), L0);
            String L2 = xor(round(R1, 2), L1);
            String R2 = R1;
            String text_out = L2 + R2;
            text_out = Permute(text_out, IP_1);
            int ascii_out = Integer.parseInt(text_out, 2);
            ciphertext.append((char)ascii_out);
        }

       return ciphertext;
    }

    public static StringBuilder ACSII_decode(String input) { //ASCII字符解密
        String plaintext = input;
        StringBuilder ciphertext = new StringBuilder();
        for(int i = 0; i < plaintext.length(); i++) {
            char ch_in = plaintext.charAt(i);
            int ascii_in = (int) ch_in;
            String text = String.format("%08d", Integer.parseInt(Integer.toBinaryString(ascii_in)));
            text = Permute(text, IP);
            String L0 = text.substring(0, 4);
            String R0 = text.substring(4, 8);
            String L1 = R0;
            String R1 = xor(round(R0, 2), L0);
            String L2 = xor(round(R1, 1), L1);
            String R2 = R1;
            String text_out = L2 + R2;
            text_out = Permute(text_out, IP_1);
            int ascii_out = Integer.parseInt(text_out, 2);
            ciphertext.append((char)ascii_out);
        }

        return ciphertext;
    }

    public static StringBuilder attack(String en,String de){//暴力破解


        String plaintext = en;
        String ciphertext = de;
        StringBuilder sb=new StringBuilder();

        int found = 0;
        long stime = System.currentTimeMillis();
        for(int i = 0; i < 1024; i++){
            String key_p = String.format("%010d", Integer.parseInt(Integer.toBinaryString(i)));
            String key0 = Permute(key_p, P10);
            String Leftshift11 = key0.substring(0, 5);
            Leftshift11 = Leftshift(Leftshift11, 1);//移位后
            String Leftshift12 = key0.substring(5, 10);
            Leftshift12 = Leftshift(Leftshift12, 1);//移位后
            key1 = Leftshift11 + Leftshift12;
            key1 = Permute(key1, P8);
            String Leftshift21 = Leftshift(Leftshift11, 2);
            String Leftshift22 = Leftshift(Leftshift12, 2);
            key2 = Leftshift21 + Leftshift22;
            key2 = Permute(key2, P8);

            String plaintext0 = Permute(plaintext, IP);
            String L0 = plaintext0.substring(0, 4);
            String R0 = plaintext0.substring(4, 8);
            String L1 = R0;
            String R1 = xor(round(R0,2), L0);
            String L2 = xor(round(R1,1), L1);
            String R2 = R1;
            String ciphertext0 = L2 + R2;
            ciphertext0 = Permute(ciphertext0, IP_1);
            if(ciphertext0.equals(ciphertext)){
                sb.append(key_p);
                sb.append("\n");
                //System.out.println("key="+key_p);
                found+=1;
                if(found == 1){
                    long etime = System.currentTimeMillis();
                    time=etime-stime;
                }
            }
        }
        return sb;
        //System.out.println("共找到"+found+"个key");
    }

}
