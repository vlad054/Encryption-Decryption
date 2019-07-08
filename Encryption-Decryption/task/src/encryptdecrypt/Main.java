package encryptdecrypt;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {


        String mode = pars("-mode",args);
        int key = Integer.parseInt(pars("-key",args));

        String data = pars("-data",args);
        String pathIn = pars("-in",args);
        String pathOut = pars("-out",args);
        String alg = pars("-alg",args);

        if(pathIn.equals("") && data.equals("")){
            Scanner scn = new Scanner(System.in);
            data = scn.nextLine();
        }
        else if(!pathIn.equals("")){
            File fIn = new File(pathIn);
            try(Scanner scn = new Scanner(fIn)){
                data = scn.nextLine();
            }
            catch(FileNotFoundException e){
                System.out.println(e.getMessage());
            }
        }


        String res="";

        encriptor enc = new encriptor();

        switch (alg){
            case "shift": {
                enc.SetEnctMeth(new encrdecrShift());
                if (mode.equals("enc")) {
                    res = enc.encipt(data,key);
                } else if (mode.equals("dec")) {
                    res = enc.decipt(data, key);
                } else {
                    System.out.println("Mistake");
                }
                break;
            }
            case "unicode":{
                enc.SetEnctMeth(new encrdecrUni());
                if (mode.equals("enc")) {
                    res = enc.encipt(data, key);
                } else if (mode.equals("dec")) {
                    res = enc.decipt(data, key);
                } else {
                    System.out.println("Mistake");
                }
                break;
            }
            default: System.out.println("Mistake");
        }


        if(pathOut.equals("")){
            System.out.println(res);
        }
        else{
            try{
                FileWriter fw = new FileWriter(pathOut);
                fw.write(res);
                fw.close();
            }
            catch (Exception e){
                System.out.println(e.getMessage());
            }
        }
    }
    public static String pars(String key, String... str){
        for (int i=0;i<str.length;i++){
            if (str[i].equals(key)) {
                return str[i+1];
            }
        }
        return "";
    }
}



interface encrdecrMeth {

    String encript(String str, int key);
    String decript(String str, int key);

}

class encrdecrUni implements encrdecrMeth{

    @Override
    public String encript(String str, int shift) {
        String res="";
        for(int i=0;i<str.length();i++){
            res = res + (char)((int)str.charAt(i)+shift);
        }
        return res;
    }
    public String decript(String str, int shift) {
        String res="";

        for(int i=0;i<str.length();i++){
            res = res + (char)((int)str.charAt(i)-shift);
        }
        return res;
    }

}

class  encrdecrShift implements encrdecrMeth{

    @Override
    public String encript(String str, int shift) {

        String res="";

        String alf = "abcdefghijklmnopqrstuvwxyz";

        String bet = "1234567890";

        int pos=0;
        int newPos = 0;

        for(int i=0;i<str.length();i++){

            if(Character.isLetter(str.charAt(i))){
                pos = alf.indexOf(str.charAt(i));
                newPos = pos+shift;
                res = res + alf.charAt(newPos%26);
            }
            else{
                res=  res+ str.charAt(i);
            }
        }
        return res;

    }
    public String decript(String str,  int shift) {
        String res="";

        String alf = "abcdefghijklmnopqrstuvwxyz";

        String bet = "1234567890";

        int pos=0;
        int newPos = 0;

        for(int i=0;i<str.length();i++){
            System.out.println(str.charAt(i));

            if(Character.isLetter(str.charAt(i))){
                pos = alf.indexOf(str.charAt(i));
                newPos = pos-shift;
                //System.out.println(pos+ " "+ newPos);
                //System.out.println((newPos>=0)?newPos:alf.length()+newPos%26);
                res = res + alf.charAt((newPos>=0)?newPos:alf.length()+newPos%26);
                // /System.out.println(res);
            }
            else{
                res=  res + str.charAt(i);
            }
        }
        return res;
    }
}

class encriptor {

    encrdecrMeth curEncrMethod;

    public void SetEnctMeth(encrdecrMeth en){
        this.curEncrMethod =  en;
    }

    public String encipt(String str, int key){
        return this.curEncrMethod.encript(str, key);
    }

    public String decipt(String str, int key){
        return this.curEncrMethod.decript(str, key);
    }

}