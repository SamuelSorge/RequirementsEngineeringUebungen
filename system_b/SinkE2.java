import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Calendar;


public class SinkE2 extends SinkFilter{
	boolean firstWriteOutputB = true;
	boolean firstWriteWildPoints = true;
	String fileName = "OutputB.dat";
	String wildPointFileName = "WildPoints.dat";
	
	public void processValueSet(DataStruct data) {
		
		if(firstWriteOutputB){
			File f = new File(fileName);
			try {
				if(f.exists())f.delete();
				f.createNewFile();
				firstWriteOutputB = false;
			} catch (IOException e) {
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
			DecimalFormat TempdecimalFormat = new DecimalFormat("000.000", tempDecimalFormatSymbols);
			printString += TempdecimalFormat.format(Double.longBitsToDouble(data.Temp.longValue()));
		}
		printString += "\t";
		
		
		if(data.Altitude != null){
			DecimalFormatSymbols attitudeDecimalFormatSymbols = new DecimalFormatSymbols();
			attitudeDecimalFormatSymbols.setDecimalSeparator('.');
			DecimalFormat attitudeDecimalFormat = new DecimalFormat("000000.00000", attitudeDecimalFormatSymbols);
			printString += attitudeDecimalFormat.format(Double.longBitsToDouble(data.Altitude.longValue()));
		}
		printString  += "\t";
		
		if(data.Pressure != null){
			DecimalFormatSymbols pressureDecimalFormatSymbols = new DecimalFormatSymbols();
			pressureDecimalFormatSymbols.setDecimalSeparator(':');
			DecimalFormat pressureDecimalFormat = new DecimalFormat("00.00000", pressureDecimalFormatSymbols);
			printString += pressureDecimalFormat.format(Double.longBitsToDouble(data.Pressure.longValue()));
			if(data.WildPressure != null){
				printString += "*";
			}
		}
		printString  += "\n";
		
		try {
		    PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(fileName, true)));
		    out.println(printString);
		    out.close();
		} catch (IOException e) {
			System.out.print("Error while create output file.");
		}
		
		
		if(data.WildPressure != null){
			if(firstWriteWildPoints){
				File f = new File(wildPointFileName);
				try {
					if(f.exists())f.delete();
					f.createNewFile();
					firstWriteWildPoints = false;
				} catch (IOException e) {
					System.out.print("Error while create output file.");
				}
			}
			
			String wildString = "";
			
			Calendar WildTimeStamp = Calendar.getInstance();
			SimpleDateFormat WildTimeStampFormat = new SimpleDateFormat("yyyy MM dd::hh:mm:ss");
			WildTimeStamp.setTimeInMillis(data.Time.longValue());
			wildString += WildTimeStampFormat.format(WildTimeStamp.getTime()) + "\t";
			
			
			
				DecimalFormatSymbols pressureDecimalFormatSymbols = new DecimalFormatSymbols();
				pressureDecimalFormatSymbols.setDecimalSeparator(':');
				DecimalFormat pressureDecimalFormat = new DecimalFormat("00.00000", pressureDecimalFormatSymbols);
				printString += pressureDecimalFormat.format(Double.longBitsToDouble(data.WildPressure.longValue()));
				
				wildString  += "\n";
				
				try {
				    PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(wildPointFileName, true)));
				    out.println(wildString);
				    out.close();
				} catch (IOException e) {
					System.out.print("Error while create output file.");
				}
		}
		
		
	}
	
}