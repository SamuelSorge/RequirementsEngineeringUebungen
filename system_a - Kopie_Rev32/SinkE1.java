/******************************************************************************************************************
* File: SinkE1.java
* @author Team Bud Spencer
* Course: Requirements Engineering und Software-Architektur
* Project: Assignment 1
* Copyright: Copyright (c) 2003 Carnegie Mellon University
* Versions:
*	1.0 November 2014 - SinkE1.
*
* Description:
*
* This class serves as sink filter to generate the output file.
*
* Parameters: none
*
* Internal Methods:	None
*
******************************************************************************************************************/
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Calendar;


public class SinkE1 extends SinkFilter {
	boolean firstWrite = true;
	String fileName = "OutputA.dat";
	
	/**
    * The method "processValueSet" is responsible for formatting and
    * and writting the data in the respective file.
    *
    * @param data which will be written in "OutputA.dat" 
    */
	public void processValueSet(DataStruct data) {
		
		// Create the new output file for "OutputA".
        // If the file exists, the file will be deleted
        // and a new File will be created.
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

			// Get the timestamp in the format yyyy MM dd::hh:mm:ss (e.g. 2014 01 14::07:04:11).
			// The timestamp is written in the first column.
			Calendar TimeStamp = Calendar.getInstance();
			SimpleDateFormat TimeStampFormat = new SimpleDateFormat("yyyy MM dd::hh:mm:ss");
			TimeStamp.setTimeInMillis(data.Time.longValue());
			printString += TimeStampFormat.format(TimeStamp.getTime()) + "\t";

			// If the value of the temperature (in Celsius) isn't empty then,
			// formats the temperature and add it to the output string.
			if(data.Temp != null){
				DecimalFormatSymbols tempDecimalFormatSymbols = new DecimalFormatSymbols();
				tempDecimalFormatSymbols.setDecimalSeparator('.');
				DecimalFormat TempdecimalFormat = new DecimalFormat("+#,000.000;-#", tempDecimalFormatSymbols);
				printString += TempdecimalFormat.format(Double.longBitsToDouble(data.Temp.longValue()));
			}
			printString += "\t";
			
			// If the value of the altitude (in meters) isn't empty then,
			// formats the altitude and add it to the output string.
			if(data.Altitude != null){
				DecimalFormatSymbols attitudeDecimalFormatSymbols = new DecimalFormatSymbols();
				attitudeDecimalFormatSymbols.setDecimalSeparator('.');
				DecimalFormat attitudeDecimalFormat = new DecimalFormat("000000.00000", attitudeDecimalFormatSymbols);
				printString += attitudeDecimalFormat.format(Double.longBitsToDouble(data.Altitude.longValue()));
			}
			printString  += "\n";
		
		    // Writes the whole string in the "OutputB.dat".
			// First column     --> Date and time information
			// Second column    --> The temperature in Celsius
			// Third column     --> The altitude in meters
		try {
		    PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(fileName, true)));
		    out.println(printString);
		    out.close();
		} catch (IOException e) {
		    //exception handling left as an exercise for the reader
		}
		
		
	}
	
}
