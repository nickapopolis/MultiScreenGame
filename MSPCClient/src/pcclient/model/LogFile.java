package pcclient.model;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class LogFile 
{
	static String fileName = "PCClientLog.txt";
	 
	 //Stream To File
	static FileWriter STF; 
	
	 
	 public static void init(String logFileName)
	 {
		 fileName = logFileName;
		 //create filewriter
		 try 
		 {
			 STF = new FileWriter(fileName, true);
		 } 
		 catch (IOException e)
		 {
			 // TODO Auto-generated catch block
			 e.printStackTrace();
		 }
	 }
	 /**
	  * Writes a string to a file
	  * @param string
	  */
	 public static void write(String string)
	 {
		 
		 //try to write string to file
		try 
		{
			STF.append(getTimeString() +": "+ string +"\n");
			STF.flush();
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	 }
	 
	 static String getTimeString()
	 {
		 SimpleDateFormat dt = new SimpleDateFormat ("E yyyy.MM.dd 'at' hh:mm:ss");
		 //Time, Date, Message
		 Date TDM = new Date(); 
		 return dt.format(TDM);
	 }
	 
	 static void close()
	 {
		 try {
			STF.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	 }
}