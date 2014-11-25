/******************************************************************************************************************
* File: ConvertMiddleFilter.java
* @author Team Bud Spencer
* Course: Requirements Engineering und Software-Architektur
* Project: Assignment 1
* Copyright: Copyright (c) 2003 Carnegie Mellon University
* Versions:
*	1.0 November 2008 - ConvertMiddleFilter.
*
* Description:
*
* This class serves as middle filter to convert data, reads data from the filter's input port and writes data out the
* filter's output port.
*
* Parameters: 		None
*
* Internal Methods: None
*
******************************************************************************************************************/

abstract public class ConvertMiddleFilter extends FilterFramework
{
	public void run()
    {
		int MeasurementLength = 8;		// This is the length of all measurements (including time) in bytes
		int IdLength = 4;				// This is the length of IDs in the byte stream

		byte databyte = 0;				// This is the data byte read from the stream
		int bytesread = 0;				// This is the number of bytes read from the stream
		int byteswritten = 0;			// Number of bytes written to the stream.

		long measurement;				// This is the word used to store all measurements - conversions are illustrated.
		int id;						// This is the measurement id
		int i;						// This is a loop counter



		// Next we write a message to the terminal to let the world know we are alive...

		System.out.print( "\n" + this.getName() + "::Middle Reading ");

		while (true)
		{
			/*************************************************************
			*	Here we read a byte and write a byte
			*************************************************************/

			try
			{
				/***************************************************************************
				// We know that the first data coming to this filter is going to be an ID and
				// that it is IdLength long. So we first decommutate the ID bytes.
				****************************************************************************/

				id = 0;

				for (i=0; i<IdLength; i++ )
				{
					databyte = ReadFilterInputPort();	// This is where we read the byte from the stream...

					id = id | (databyte & 0xFF);		// We append the byte on to ID...

					if (i != IdLength-1)				// If this is not the last byte, then slide the
					{									// previously appended byte to the left by one byte
						id = id << 8;					// to make room for the next byte we append to the ID

					} // if

					bytesread++;						// Increment the byte count

				} // for

				/****************************************************************************
				// Here we read measurements. All measurement data is read as a stream of bytes
				// and stored as a long value. This permits us to do bitwise manipulation that
				// is neccesary to convert the byte stream into data words. Note that bitwise
				// manipulation is not permitted on any kind of floating point types in Java.
				// If the id = 0 then this is a time value and is therefore a long value - no
				// problem. However, if the id is something other than 0, then the bits in the
				// long value is really of type double and we need to convert the value using
				// Double.longBitsToDouble(long val) to do the conversion which is illustrated.
				// below.
				*****************************************************************************/

				measurement = 0;

				for (i=0; i<MeasurementLength; i++ )
				{
					databyte = ReadFilterInputPort();
					measurement = measurement | (databyte & 0xFF);	// We append the byte on to measurement...

					if (i != MeasurementLength-1)					// If this is not the last byte, then slide the
					{												// previously appended byte to the left by one byte
						measurement = measurement << 8;				// to make room for the next byte we append to the
																	// measurement
					} // if

					bytesread++;									// Increment the byte count

				} // for
				// convert data regarding its id
                 measurement = ConvertData(id,measurement); 
				
				// only write data if id matches temperature or altitude
                if(!IgnoreData(id)) 
                {
                    for (i=IdLength-1; i>=0; i-- )
                    {
                        databyte = (byte)(id >> 8*i );
                        WriteFilterOutputPort(databyte);
                        byteswritten++;
                    }
                    for (i=MeasurementLength-1; i>=0 ; i-- )
                    {
                        databyte = (byte)( measurement >> 8*i & 0xFF);
                        WriteFilterOutputPort(databyte);
                        byteswritten++;
                    }
                }

			} // try

			catch (EndOfStreamException e)
			{
				ClosePorts();
				System.out.print( "\n" + this.getName() + "::Middle Exiting; bytes read: " + bytesread + " bytes written: " + byteswritten );
				break;

			} // catch

		} // while

   } // run

	
	/****************************************************************************
	// The following functions should be overridden
	// @see MiddleAltitudeFilter#ConvertData(int id, long measurement)
	// @see MiddleTemperatureFilter#ConvertData(int id, long measurement)
	*****************************************************************************/
    long ConvertData(int id, long measurement)
    {
        return measurement;
    }
    boolean IgnoreData(int id)
    {
        return false;
    }

} // MiddleFilter
