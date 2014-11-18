import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Calendar;


public class SinkE1 extends SinkFilter{
	boolean firstWrite = true;
	String fileName = "OutputA.dat";
	public void processValueSet(DataStruct data) {
		
		if(firstWrite){
			File f = new File(fileName);
			try {
				if(f.exists())f.delete();
				f.createNewFile();
				firstWrite = false;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				System.out.print("Error while create output file.");
			}
		}
		
			String printString = "";

			Calendar TimeStamp = Calendar.getInstance();
			SimpleDateFormat TimeStampFormat = new SimpleDateFormat("yyyy MM dd::hh:mm:ss");
			TimeStamp.setTimeInMillis(data.Time.longValue());
			printString += TimeStampFormat.format(TimeStamp.getTime()) + "\t";

			if(data.Temp != null){
				DecimalFormatSymbols tempDecimalFormatSymbols = new DecimalFormatSymbols();
				tempDecimalFormatSymbols.setDecimalSeparator('.');
				DecimalFormat TempdecimalFormat = new DecimalFormat("+#,000.000;-#", tempDecimalFormatSymbols);
				printString += TempdecimalFormat.format(Double.longBitsToDouble(data.Temp.longValue()));
			}
			printString += "\t";
			
			
			if(data.Altitude != null){
				DecimalFormatSymbols attitudeDecimalFormatSymbols = new DecimalFormatSymbols();
				attitudeDecimalFormatSymbols.setDecimalSeparator('.');
				DecimalFormat attitudeDecimalFormat = new DecimalFormat("000000.00000", attitudeDecimalFormatSymbols);
				printString += attitudeDecimalFormat.format(Double.longBitsToDouble(data.Altitude.longValue()));
			}
			printString  += "\n";
		
		
		try {
		    PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(fileName, true)));
		    out.println(printString);
		    out.close();
		} catch (IOException e) {
		    //exception handling left as an exercise for the reader
		}
		
		
	}
	
}
