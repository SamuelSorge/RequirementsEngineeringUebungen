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
* This class serves as an example to illstrate how to use the PlumberTemplate to create a main thread that
* instantiates and connects a set of filters. This example consists of three filters: a source, a middle filter
* that acts as a pass-through filter (it does nothing to the data), and a sink filter which illustrates all kinds
* of useful things that you can do with the input stream of data.
*
* Parameters: 		None
*
* Internal Methods:	None
*
******************************************************************************************************************/
public class SystemC
{
   public static void main( String argv[])
   {
		/****************************************************************************
		* Here we instantiate three filters.
		****************************************************************************/

		SourceFilter FilterS1 = new SourceFilter("SubSetA.dat");
		SourceFilter FilterS2 = new SourceFilter("SubSetB.dat");
                MiddleMergerFilter FilterM = new MiddleMergerFilter();
                MiddlePressureWildPoints FilterW = new MiddlePressureWildPoints();
		SinkFilter FilterSink = new SinkE3();

		/****************************************************************************
		* Here we connect the filters starting with the sink filter (Filter 1) which
		* we connect to Filter2 the middle filter. Then we connect Filter2 to the
		* source filter (Filter3).
		****************************************************************************/

		FilterSink.Connect(FilterW);
		FilterW.Connect(FilterM);
		FilterM.Connect(FilterS1);
		FilterM.Connect2(FilterS2);

		/****************************************************************************
		* Here we start the filters up. All-in-all,... its really kind of boring.
		****************************************************************************/

		FilterS1.start();
		FilterS2.start();
                FilterM.start();
		FilterW.start();
		FilterSink.start();

   } // main

}
