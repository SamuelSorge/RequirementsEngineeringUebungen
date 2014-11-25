/******************************************************************************************************************
* File: SystemA.java
* Course: 17655
* Project: Assignment 1
* Copyright: Copyright (c) 2003 Carnegie Mellon University
* Versions:
*	1.0 November 2014 - SystemA.
*
* Description:
*
* This class serves as main thread that* instantiates and connects a set of filters. 
* This example consists of four filters: a source, two middle filters
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
		* Here we instantiate three filters:
		****************************************************************************/

		SourceFilter srcFilter = new SourceFilter();// srcFilter is a source filter which reads some input and writes it to its outport
		MiddleTemperatureFilter midTempFilter = new MiddleTemperatureFilter();// midTempFilter is a middle filter which converts the altitude from feet to meters
		MiddleAltitudeFilter midAltFilter = new MiddleAltitudeFilter();// midAltFilter is a middle filter which converts the temperature from Fahrenheit to Celsius
		SinkE1 sinkFilter = new SinkE1();// sinkFilter is a sink filter which generates an output file "OutputA.dat" that contains the converted data in a given format

		/****************************************************************************
		* Here we connect the filters starting with the sink filter (sinkFilter) which
		* we connect to midAltFilter the middle filter, which is used to convert feet in meters.
		* Then we connect midAltFilter to midTempFilter which is used to convert the temperature from Fahrenheit to Celsius. .
		* At last we connect midTempFilter to the source filter (srcFilter).
		****************************************************************************/

		sinkFilter.Connect(midAltFilter);// Connect midAltFilter input port to sinkdFilter output port
		midAltFilter.Connect(midTempFilter);// Connect midTempFilter input port to midAltFilter output port
		midTempFilter.Connect(srcFilter);// Connect srcFilter intput port to midTempFilter output port

		/****************************************************************************
		* Here we start the filters up. Great work!
		****************************************************************************/

		srcFilter.start();
		midTempFilter.start();
		midAltFilter.start();
		sinkFilter.start();

   } // main

}
