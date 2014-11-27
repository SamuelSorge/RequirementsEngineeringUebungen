import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/******************************************************************************************************************
*
* The SinkE2 filter extends the basic SinkFilter, which is proivded by the filter framework.
* The SinkE2 filter is responsible for:
*   1. To create the "OutputB.dat" and "WildPoints.dat".
*   2. Format the data
*   3. Write the formatted data in the generated files from step 1.
*       (If the pressure value is correct the data will, the data will be written in the "OutputB.dat" file.
*       In the other case the pressure value is a  wildpoint, it will be written in the "WildPoints.dat".)
*
******************************************************************************************************************/
public class SinkE2 extends SinkFilter {

    private boolean firstWriteOutputB = true;
    private boolean firstWriteWildPoints = true;
    private String outputBFileName = "OutputB.dat";
    private String wildPointoutputBFileName = "WildPoints.dat";


    /**
    * The method "processValueSet" is responsible for formatting and
    * and writting the data in the respective file.
    *
    * @param data which will be written in "OutputB.dat" or
    * or in the "WildPoints.dat".
    */
    public void processValueSet(DataStruct data) {

        // Create the new output file for "OutputB".
        // If the file exists, the file will be deleted
        // and a new File will be created.
        if (firstWriteOutputB) {
            File f = new File(outputBFileName);
            try {
                if (f.exists())f.delete();
                f.createNewFile();
                firstWriteOutputB = false;
                String header = "Time: \t\t\t\t\t\t" + "Temperature (C): \t\t" + "Altitude (m):\t\t\t" + "Pressure (psi)\n" +
                                "________________________________________________________________________________________________";
                writeData(outputBFileName, header);
            } catch (IOException e) {
                System.out.print("Error while create output file.");
            }
        }



        String printString = "";

        // Get the timestamp in the format yyyy:dd::hh:mm:ss (e.g. 2014:14:07:04:11).
        // The timestamp is written in the first column.
        Calendar timeStamp = Calendar.getInstance();
        SimpleDateFormat timeStampFormat = new SimpleDateFormat("yyyy:dd:hh:mm:ss");
        timeStamp.setTimeInMillis(data.Time.longValue());
        printString += timeStampFormat.format(timeStamp.getTime()) + "\t\t\t";

        // If the value of the temperature (in Celsius) isn't empty then,
        // formats the temperature and add it to the output string.
        if (data.Temp != null) {
            DecimalFormatSymbols tempDecimalFormatSymbols = new DecimalFormatSymbols();
            tempDecimalFormatSymbols.setDecimalSeparator('.');
            DecimalFormat tempdecimalFormat = new DecimalFormat("+#,000.00000;-#", tempDecimalFormatSymbols);
            printString += tempdecimalFormat.format(Double.longBitsToDouble(data.Temp.longValue()));
        }
        printString += "\t\t\t\t";

        // If the value of the altitude (in meters) isn't empty then,
        // formats the altitude and add it to the output string.
        if (data.Altitude != null) {
            DecimalFormatSymbols attitudeDecimalFormatSymbols = new DecimalFormatSymbols();
            attitudeDecimalFormatSymbols.setDecimalSeparator('.');
            DecimalFormat attitudeDecimalFormat = new DecimalFormat("000000.00000", attitudeDecimalFormatSymbols);
            printString += attitudeDecimalFormat.format(Double.longBitsToDouble(data.Altitude.longValue()));
        }
        printString  += "\t\t\t";

        // If the value of the pressure isn't empty then,
        // formats the pressure and add it to the output string.
        if (data.Pressure != null) {
            DecimalFormatSymbols pressureDecimalFormatSymbols = new DecimalFormatSymbols();
            pressureDecimalFormatSymbols.setDecimalSeparator(':');
            DecimalFormat pressureDecimalFormat = new DecimalFormat("00.00000", pressureDecimalFormatSymbols);
            printString += pressureDecimalFormat.format(Double.longBitsToDouble(data.Pressure.longValue()));
            if (data.WildPressure != null) {
                printString += "*";
            }
        }
        // Write data in outputBFileName
        writeData (outputBFileName, printString);


        // If a wildpoint detected check if the file "WildPoints.dat" exists or not.
        // If the file exists it will be deleted and create new.
        if (data.WildPressure != null) {
            if (firstWriteWildPoints) {
                File f = new File(wildPointoutputBFileName);
                try {
                    if (f.exists())f.delete();
                    f.createNewFile();
                    firstWriteWildPoints = false;
                    String header = "Time: \t\t\t\t\t\t" + "Pressure (psi)\n" +
                                    "___________________________________________";
                    writeData(wildPointoutputBFileName, header);
                } catch (IOException e) {
                    System.out.print("Error while create output file.");
                }
            }

            String wildString = "";

            // Get the timestamp in the format yyyy:dd:hh:mm:ss (e.g. 2014:14::07:04:11).
            // The timestamp is written in the first column and add it to the output string.
            Calendar WildtimeStamp = Calendar.getInstance();
            SimpleDateFormat WildtimeStampFormat = new SimpleDateFormat("yyyy:dd:hh:mm:ss");
            WildtimeStamp.setTimeInMillis(data.Time.longValue());
            wildString += WildtimeStampFormat.format(WildtimeStamp.getTime()) + "\t\t\t";


            // Formats the wildpoint pressure
            // and add it to the output string.
            DecimalFormatSymbols pressureDecimalFormatSymbols = new DecimalFormatSymbols();
            pressureDecimalFormatSymbols.setDecimalSeparator(':');
            DecimalFormat pressureDecimalFormat = new DecimalFormat("00.00000", pressureDecimalFormatSymbols);
            wildString += pressureDecimalFormat.format(Double.longBitsToDouble(data.WildPressure.longValue()));

            // Write data in wildPointoutputBFileName (the wildpoints)
            writeData(wildPointoutputBFileName, wildString);

        }
    }

    // Writes the whole string in the "OutputB.dat" or in "Wildpoints.dat" depending on file name.
    // Structure OUTPUTB.dat
    // Header
    // First column     --> Date and time information
    // Second column    --> The temperature in Celsius
    // Third column     --> The altitude in meters
    //Fourth column     --> The pressure in psi

    // Structure WILDPOINTS.dat
    // Header
    // First column     --> Date and time information
    // Second column    --> The pressure in psi
    public void writeData(String fileName, String dataToWrite) {
        try {
            PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(fileName, true)));
            writer.println(dataToWrite);
            writer.close();
        } catch (IOException e) {
            System.out.println("Error while writing in " + fileName + " --> " + e.getMessage());
        }
    }

}