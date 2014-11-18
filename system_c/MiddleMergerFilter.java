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

public class MiddleMergerFilter extends FilterFramework2Sources
{
		static int MeasurementLength = 8;		// This is the length of all measurements (including time) in bytes
		static int IdLength = 4;				// This is the length of IDs in the byte stream
		private int bytesread;				// This is the number of bytes read from the stream
		private int byteswritten;				// Number of bytes written to the stream.
		private int id1;						// This is the measurement id
		private int id2;						// This is the measurement id
                private HashMap<Integer, Long> myCurrentFrame_stream1 = new HashMap<Integer, Long>();
                private HashMap<Integer, Long> myCurrentFrame_stream2 = new HashMap<Integer, Long>();

	public void run()
        {
                id1 = -1;
                id2 = -1;
                bytesread = 0;
                byteswritten = 0;
		byte databyte = 0;				// This is the data byte read from the stream

		long measurement;				// This is the word used to store all measurements - conversions are illustrated.
		int i;						// This is a loop counter
                double last_valid_point = 0.0;


		// Next we write a message to the terminal to let the world know we are alive...

		System.out.print( "\n" + this.getName() + "::MiddleMergerFilter Reading ");

		while (true)
                {
                    /*************************************************************
                     *	Here we read a byte and write a byte
                     *************************************************************/
                    boolean s1 = ReadFrameFrom1();
                    boolean s2 = ReadFrameFrom2();
                    if(s1 && s2)
                    {
                        // we have read an complete frame from input 1 and a complete frame from input2
                        // now check which one we sould output
                        if(myCurrentFrame_stream1.get(0) <= myCurrentFrame_stream2.get(0))
                        {
                            WriteMapToOutputPort(myCurrentFrame_stream1);
                            myCurrentFrame_stream1.clear();
                        }
                        else
                        {
                            WriteMapToOutputPort(myCurrentFrame_stream2);
                            myCurrentFrame_stream2.clear();
                        }
                    }
                    else if(s1)
                    {
                        WriteMapToOutputPort(myCurrentFrame_stream1);
                        myCurrentFrame_stream1.clear();
                    }
                    else if(s2)
                    {
                        WriteMapToOutputPort(myCurrentFrame_stream2);
                        myCurrentFrame_stream2.clear();
                    }
                    if(!s1 && !s2)
                    {
                        ClosePorts();
                        System.out.print( "\n" + this.getName() + "::Middle Exiting; bytes read: " + bytesread + " bytes written: " + byteswritten );
                        break;
                    }

                } // while
   } // run

    boolean ReadFrameFrom1()
    {
        long measurement;
        while(true)
        {
            try
            {
                if(id1 == -1)
                {
                    id1 = ReadIdFormInput1();
                }
                if(id1 == 0 && myCurrentFrame_stream1.size() > 0)
                {
                    return true;
                }
                measurement = ReadDataFromInput1();
                myCurrentFrame_stream1.put(id1, measurement);
                id1 = -1;
            }
            catch (EndOfStreamException e)
            {
                System.out.print( "\n" + this.getName() + "::Middle Exiting; End of stream1 bytes read: " + bytesread + " bytes written: " + byteswritten );
                break;
            } // catch
        }
        return false;
    }

    boolean ReadFrameFrom2()
    {
        long measurement;
        while(true)
        {
            try
            {
                if(id2 == -1)
                {
                    id2 = ReadIdFormInput2();
                }
                if(id2 == 0 && myCurrentFrame_stream2.size() > 0)
                {
                    return true;
                }
                measurement = ReadDataFromInput2();
                myCurrentFrame_stream2.put(id2, measurement);
                id2 = -1;
            }
            catch (EndOfStreamException e)
            {
                System.out.print( "\n" + this.getName() + "::Middle Exiting; End of stream2 bytes read: " + bytesread + " bytes written: " + byteswritten );
                break;
            } // catch
        }
        return false;
    }

    int WriteMapToOutputPort(HashMap<Integer,Long> hm)
    {
        byte databyte = 0;
        int i;
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
        return byteswritten;
    }

    int ReadIdFormInput1() throws FilterFramework.EndOfStreamException
    {
        /***************************************************************************
        // We know that the first data coming to this filter is going to be an ID and
        // that it is IdLength long. So we first decommutate the ID bytes.
         ****************************************************************************/
        int id = 0;
        byte databyte;

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

    int ReadIdFormInput2() throws FilterFramework.EndOfStreamException
    {
        /***************************************************************************
        // We know that the first data coming to this filter is going to be an ID and
        // that it is IdLength long. So we first decommutate the ID bytes.
         ****************************************************************************/
        int id = 0;
        byte databyte;

        for(int i=0; i<IdLength; i++ )
        {
            databyte = ReadFilterInput2Port();	// This is where we read the byte from the stream...

            id = id | (databyte & 0xFF);		// We append the byte on to ID...

            if (i != IdLength-1)				// If this is not the last byte, then slide the
            {									// previously appended byte to the left by one byte
                id = id << 8;					// to make room for the next byte we append to the ID

            } // if

            bytesread++;						// Increment the byte count

        } // for
        return id;
    }


    long ReadDataFromInput1() throws FilterFramework.EndOfStreamException
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
        byte databyte;

        for (int i=0; i<MeasurementLength; i++ )
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


    long ReadDataFromInput2() throws FilterFramework.EndOfStreamException
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
        byte databyte;

        for (int i=0; i<MeasurementLength; i++ )
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
