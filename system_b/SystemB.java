/******************************************************************************************************************
* File:SystemAr.java
* Course: 17655
* Project: Assignment 1
* Copyright: Copyright (c) 2003 Carnegie Mellon University
* Versions:
*	1.0 November 2008 - Sample Pipe and Filter code (ajl).
*
* Description:
*
* This class serves as main thread that* instantiates and connects a set of filters. 
* This system consists of five filters: a source which reads the source file, three middle filters
* that convert data (Fahrenheit to Celsius and feet to meters) and sort wildpoints,
* and a sink filter which generates an output file.

* Parameters: 		None
*
* Internal Methods:	None
*
******************************************************************************************************************/
public class SystemB
{
   public static void main( String argv[])
   {
		/****************************************************************************
		* Here we instantiate five filters.
		****************************************************************************/

		// srcFilter is a source filter which reads some input and writes it to its outport
		SourceFilter srcFilter = new SourceFilter();

		// midTempFilter is a middle filter which converts the altitude from feet to meters
		MiddleTemperatureFilter midTempFilter = new MiddleTemperatureFilter();

		// midAltitudeFilter is a middle filter which converts the temperature from Fahrenheit to Celsius
		MiddleAltitudeFilter midAltitudeFilter = new MiddleAltitudeFilter();

		// midPressureWildPointsFilter is a middle filter which checks if the value is correct or a wildpoint
        MiddlePressureWildPoints midPressureWildPointsFilter = new MiddlePressureWildPoints();

        // sinkFilter is a sink filter which generates an output file "OutputB.dat" that contains the converted data in a given format
		SinkFilter sinkFilter = new SinkE2();

		/****************************************************************************
		* Here we connect the filters starting with the sink filter (sinkFilter), which
		* we connect to the midPressureWildPointsFilter.
		* The MiddlePressureWildPoints (midPressureWildPointsFilter) is responsible for detecting wildpoints.
		* In the next step we connect the middle filter (midAltFilter), which is used to convert feet in meters.
		* Then we connect middle filter (midAltitudeFilter) to midTempFilter which is 
		* used to convert the temperature from Fahrenheit to Celsius.
		* At last we connect midTempFilter to the source filter (srcFilter).
		****************************************************************************/

		// Connect midPressureWildPointsFilter input port to sinkFilter output port
		sinkFilter.Connect(midPressureWildPointsFilter);

		// Connect midAltitudeFilter input port to midPressureWildPointsFilter output port
		midPressureWildPointsFilter.Connect(midAltitudeFilter);

		// Connect midTempFilter input port to midAltitudeFilter output port
		midAltitudeFilter.Connect(midTempFilter);

		// Connect srcFilter intput port to midTempFilter output port
		midTempFilter.Connect(srcFilter); 

		/****************************************************************************
		* Here we start the filters up.
		****************************************************************************/

		srcFilter.start();
		midTempFilter.start();
		midAltitudeFilter.start();
		midPressureWildPointsFilter.start();
		sinkFilter.start();

   } // main

}
