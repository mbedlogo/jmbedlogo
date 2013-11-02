import java.io.PrintWriter;
import java.util.Hashtable;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.FileWriter;
import java.io.Console;

public class StdioJL {

  static LContext lc;
  static String logname="log.txt";

  static String[] primclasses =
    {"SystemPrims", "MathPrims", "ControlPrims",
     "DefiningPrims", "WordListPrims", "FilePrims",
     "SerialPrims", "TCPPrims", "TickerPrims"
     };

  public static void main(String[] args){
    lc = new LContext();
    Logo.setupPrims(primclasses, lc);
    System.out.println("Welcome to Logo!");
    logString("Welcome to Logo!");
    (new LogoCommandRunner("load \"startup startup", lc)).run();
//    Console in = System.console();
    BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
    while(true) {
      try{runCommand(in.readLine());}
      catch(Exception e){System.out.println(e);}
    }
  }


  static void runCommand(String l){
    if(l==null) return;
    logString(l);
    if(l.equals("*stop*")) {lc.timeToStop=true; StdioJL.println("stopped.");}
    else if(lc.thread != null) StdioJL.println("busy");
    else if(l.charAt(0)=='.') (new Thread (new LogoCommandRunner(l.substring(1), lc))).start();
    else (new Thread (new LogoCommandRunner("handle-line \"|"+l+"|", lc))).start();
  }

  static void println(String s){
    String ls = "  "+Logo.prs(s);
    System.out.println(ls);
    logString(ls);
  }

  static void logString(String str){
    if(logname==null) return;
    try {
      PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(logname, true)));
      writer.println(str);
      writer.close();
    } catch (Exception e) {System.out.println("log error: "+str);}
  }
}


class LContext {
  Hashtable<String,Symbol> oblist = new Hashtable<String,Symbol>();
  Hashtable<Object,Hashtable<Object,Object>> props = new Hashtable<Object,Hashtable<Object,Object>>();
  MapList iline;
  Symbol cfun, ufun;
  Object ufunresult, juststop = new Object();
  boolean mustOutput, timeToStop;
  int priority = 0;
  Object[] locals;
  String errormessage;
  Thread thread;
  String filename="startup";
  boolean loadFromResource = false;
}
