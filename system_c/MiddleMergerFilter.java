/******************************************************************************************************************
* File:MiddleFilter.java
* Course: 17655
* Project: Assignment 1
* Copyright: Copyright (c) 2003 Carnegie Mellon University
* Versions:
*	1.0 November 2008 - Sample Pipe and Filter code (ajl).
*
* Description:
*
* This class serves as an example for how to use the FilterRemplate to create a standard filter. This particular
* example is a simple "pass-through" filter that reads data from the filter's input port and writes data out the
* filter's output port.
*
* Parameters: 		None
*
* Internal Methods: None
*
******************************************************************************************************************/

import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Iterator;

public class MiddlePressureWildPoints extends FilterFramework
{
		static int MeasurementLength = 8;		// This is the length of all measurements (including time) in bytes
		static int IdLength = 4;				// This is the length of IDs in the byte stream
		int bytesread;				// This is the number of bytes read from the stream
		int byteswritten;				// Number of bytes written to the stream.

	public void run()
        {
                bytesread = 0;
                byteswritten = 0;
		byte databyte = 0;				// This is the data byte read from the stream

		long measurement;				// This is the word used to store all measurements - conversions are illustrated.
		int id1;						// This is the measurement id
		int id2;						// This is the measurement id
		int i;						// This is a loop counter
                double last_valid_point = 0.0;

                HashMap<Integer, Long> myCurrentFrame_stream1 = new HashMap<Integer, Long>;
                HashMap<Integer, Long> myCurrentFrame_stream2 = new HashMap<Integer, Long>;

		// Next we write a message to the terminal to let the world know we are alive...

		System.out.print( "\n" + this.getName() + "::Middle Reading ");

		while (true)
		{
			/*************************************************************
			*	Here we read a byte and write a byte
			*************************************************************/
			try
			{
                            while(!EndOfInputStream())
                            {
                                id1 = ReadIdFormInput1();
                                if(id1 == 0 && myCurrentFrame_stream1.size() > 0)
                                {
                                    // we reached end of stream2 so just write the data from stream1
                                    if(EndOfInput2Stream())
                                    {
                                        WriteMapToOutputPort(myCurrentFrame_stream1);
                                        myCurrentFrame_stream1.clear();
                                    }

                                    while(!EndOfInput2Stream())
                                    {
                                        id2 = ReadIdFormInput2();
                                        if(id2 == 0 && myCurrentFrame_stream2.size() > 0)
                                        {
                                            // we have read an complete frame from input 1 and a complete frame from input2
                                            // now check which one we sould output
                                            if(myCurrentFrame_stream1.get(0) <= myCurrentFrame_stream2.get(0))
                                            {
                                                WriteMapToOutputPort(myCurrentFrame_stream1);
                                                myCurrentFrame_stream1.clear();
                                                break;
                                            }
                                            else
                                            {
                                                WriteMapToOutputPort(myCurrentFrame_stream2);
                                                myCurrentFrame_stream2.clear();
                                            }
                                        }
                                        measurement = ReadDataFromInput2();
                                        myCurrentFrame_stream2.put(id2, measurement);
                                    }

                                }
                                measurement = ReadDataFromInput1();
                                myCurrentFrame_stream1.put(id1, measurement);
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

    int WriteMapToOutputPort(HashMap<Integer,Long> hm)
    {
        byte databyte = 0;
        int i;
        int byteswritten = 0;
        for(Map.Entry<Integer,Long> entry : hm.entrySet())
        {
            for (i=IdLength-1; i>=0; i-- )
            {
                databyte = (byte)(entry.getKey() >> 8*i );
                WriteFilterOutputPort(databyte);
                byteswritten++;
            }
            for (i=MeasurementLength-1; i>=0 ; i-- )
            {
                databyte = (byte)(entry.getValue() >> 8*i & 0xFF);
                WriteFilterOutputPort(databyte);
                byteswritten++;
            }
        }
        System.out.println("Written: "+hm);
        return byteswritten;
    }

    int ReadIdFormInput1()
    {
        /***************************************************************************
        // We know that the first data coming to this filter is going to be an ID and
        // that it is IdLength long. So we first decommutate the ID bytes.
         ****************************************************************************/
        int id = 0;

        for(int i=0; i<IdLength; i++ )
        {
            databyte = ReadFilterInputPort();	// This is where we read the byte from the stream...

            id = id | (databyte & 0xFF);		// We append the byte on to ID...

            if (i != IdLength-1)				// If this is not the last byte, then slide the
            {									// previously appended byte to the left by one byte
                id = id << 8;					// to make room for the next byte we append to the ID

            } // if

            bytesread++;						// Increment the byte count

        } // for
        return id;
    }

    int ReadIdFormInput2()
    {
        /***************************************************************************
        // We know that the first data coming to this filter is going to be an ID and
        // that it is IdLength long. So we first decommutate the ID bytes.
         ****************************************************************************/
        int id = 0;

        for(int i=0; i<IdLength; i++ )
        {
            databyte = ReadFilter2InputPort();	// This is where we read the byte from the stream...

            id = id | (databyte & 0xFF);		// We append the byte on to ID...

            if (i != IdLength-1)				// If this is not the last byte, then slide the
            {									// previously appended byte to the left by one byte
                id = id << 8;					// to make room for the next byte we append to the ID

            } // if

            bytesread++;						// Increment the byte count

        } // for
        return id;
    }


    long ReadDataFromInput1()
    {
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

        long measurement = 0;

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

        } // if
        return measurement;
    }


    long ReadDataFromInput2()
    {
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

        long measurement = 0;

        for (i=0; i<MeasurementLength; i++ )
        {
            databyte = ReadFilterInput2Port();
            measurement = measurement | (databyte & 0xFF);	// We append the byte on to measurement...

            if (i != MeasurementLength-1)					// If this is not the last byte, then slide the
            {												// previously appended byte to the left by one byte
                measurement = measurement << 8;				// to make room for the next byte we append to the
                // measurement
            } // if

            bytesread++;									// Increment the byte count

        } // if
        return measurement;
    }

    long ConvertData(int id, long measurement)
    {
        return measurement;
    }
    boolean IgnoreData(int id)
    {
        return false;
    }

} // MiddleFilter
