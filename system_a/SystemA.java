/******************************************************************************************************************
* File: SystemA.java
* @author Team Bud Spencer
* Course: Requirements Engineering und Software-Architektur
* Project: Assignment 1
* Copyright: Copyright (c) 2003 Carnegie Mellon University
* Versions:
*	1.0 November 2014 - SystemA.
*
* Description:
*
* This class serves as main thread that instantiates and connects a set of filters.
* This system consists of four filters: a source, two middle filters
* that convert data, and a sink filter which generates an output file.
*
* Parameters: 		None
*
* Internal Methods:	None
*
******************************************************************************************************************/
public class SystemA
{
   public static void main( String argv[])
   {
		/****************************************************************************
		* Here we instantiate four filters:
		****************************************************************************/

		// srcFilter is a source filter which reads some input and writes it to its outport
		SourceFilter srcFilter = new SourceFilter();

		// midIgnFilter is a middle filter which filters out unwanted data
		MiddleIgnoreFilter midIgnFilter = new MiddleIgnoreFilter();

		// midTempFilter is a middle filter which converts the altitude from feet to meters
		MiddleTemperatureFilter midTempFilter = new MiddleTemperatureFilter();

		// midAltFilter is a middle filter which converts the temperature from Fahrenheit to Celsius
		MiddleAltitudeFilter midAltFilter = new MiddleAltitudeFilter();

		// sinkFilter is a sink filter which generates an output file "OutputA.dat" that contains the converted data in a given format
		SinkFilter sinkFilter = new SinkE1();

		/****************************************************************************
		* Here we connect the filters starting with the sink filter (sinkFilter) which
		* we connect to midAltFilter the middle filter, which is used to convert feet in meters.
		* Then we connect midAltFilter to midTempFilter which is used to convert the temperature from Fahrenheit to Celsius.
		* At last we connect midTempFilter to the source filter (srcFilter).
		****************************************************************************/

		// Connect sinkFilter output port to midAltFilter input port
		sinkFilter.Connect(midAltFilter);

		// Connect midTempFilter output port to midAltFilter input port
		midAltFilter.Connect(midTempFilter);

		// Connect midIgnFilter intput port to midTempFilter input port
		midTempFilter.Connect(midIgnFilter);

		// Connect srcFilter output port to midIgnFilter input port
		midIgnFilter.Connect(srcFilter);

		/****************************************************************************
		* Here we start the filters up. Great work!
		****************************************************************************/

		srcFilter.start();
		midIgnFilter.start();
		midTempFilter.start();
		midAltFilter.start();
		sinkFilter.start();

   } // main

}
