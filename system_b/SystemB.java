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

		// midIgnFilter is a middle filter which filters out unwanted data
		MiddleIgnoreFilter midIgnFilter = new MiddleIgnoreFilter();

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

		// Connect midPressureWildPointsFilter output port to sinkFilter input port
		sinkFilter.Connect(midPressureWildPointsFilter);

		// Connect midAltitudeFilter output port to midPressureWildPointsFilter input port
		midPressureWildPointsFilter.Connect(midAltitudeFilter);

		// Connect midTempFilter output port to midAltitudeFilter input port
		midAltitudeFilter.Connect(midTempFilter);

		// Connect midIgnFilter intput port to midTempFilter input port
		midTempFilter.Connect(midIgnFilter);

		// Connect srcFilter output port to midIgnFilter input port
		midIgnFilter.Connect(srcFilter);

		/****************************************************************************
		* Here we start the filters up.
		****************************************************************************/

		srcFilter.start();
		midIgnFilter.start();
		midTempFilter.start();
		midAltitudeFilter.start();
		midPressureWildPointsFilter.start();
		sinkFilter.start();

   } // main

}
