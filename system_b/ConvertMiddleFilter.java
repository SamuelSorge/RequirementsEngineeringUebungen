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

abstract public class ConvertMiddleFilter extends FilterFramework
{
		static int MeasurementLength = 8;		// This is the length of all measurements (including time) in bytes
		static int IdLength = 4;				// This is the length of IDs in the byte stream

	public void run()
    {
		byte databyte = 0;				// This is the data byte read from the stream
		int bytesread = 0;				// This is the number of bytes read from the stream
		int byteswritten = 0;				// Number of bytes written to the stream.

		long measurement;				// This is the word used to store all measurements - conversions are illustrated.
		int id;						// This is the measurement id
		int i;						// This is a loop counter
                double last_valid_point = 0.0;

                List<HashMap<Integer, Long>> myBuffer = new ArrayList<HashMap<Integer, Long>>();
                HashMap<Integer, Long> myCurrentFrame = null;


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

				} // if

                                // time so a new frame starts
                                if(id == 0)
                                {
                                    if(myCurrentFrame != null)
                                    {
                                        myBuffer.add(myCurrentFrame);
                                    }
                               //     System.out.println(myBuffer);
                                    myCurrentFrame = new HashMap<Integer,Long>();
                                }
                                myCurrentFrame.put(id, measurement);


                                while(myBuffer.size() > 1)
                                {
                                    double l0 = Double.longBitsToDouble((Long)myBuffer.get(0).get(3));
                                    double l1 = Double.longBitsToDouble((Long)myBuffer.get(1).get(3));
                                    // whild point
                                    if(Math.abs(l0-l1) <= 10 && l0 > 0 && l1 > 0)
                                    {
                                        byteswritten += WriteMapToOutputPort(myBuffer.remove(0));
                                        last_valid_point = l0;
                                    }
                                    else
                                    {
                                        break;
                                    }
                                }
                                if(myBuffer.size() > 2)
                                {
                                    double l0 = Double.longBitsToDouble((Long)myBuffer.get(0).get(3));
                                    for(i=0; i < myBuffer.size() ; i++)
                                    {
                                        double li = Double.longBitsToDouble((Long)myBuffer.get(i).get(3));
                                        if(Math.abs(l0-li) <= 10 && l0 > 0 && li > 0)
                                        {
                                            // valid point found at 0 and i, between all points are 'wild' and will be set to avg(l0,li)
                                            byteswritten += WriteMapToOutputPort(myBuffer.remove(0));
                                            for(int j = 0 ; j < i ; j++)
                                            {
                                                HashMap<Integer,Long> hm = myBuffer.remove(0); // bevor it was at position 1
                                                Long lt = (Long)hm.get(3);
                                                hm.put(new Integer(3), (Long)Double.doubleToLongBits((l0+li)/2));
                                                hm.put(new Integer(6), lt);
                                                byteswritten += WriteMapToOutputPort(hm);
                                            }
                                            last_valid_point = l0;
                                        }
                                    }
                                }

			} // try

			catch (EndOfStreamException e)
			{
				System.out.print( "\n" + this.getName() + "::Middle Exiting; bytes read: " + bytesread + " bytes written: " + byteswritten );
                                for(i = 0 ; i < myBuffer.size() ; i++)
                                {
                                    double l0 = Double.longBitsToDouble((Long)myBuffer.get(0).get(3));
                                    // whild point
                                    if(Math.abs(l0-last_valid_point) <= 10 && l0 > 0)
                                    {
                                        WriteMapToOutputPort(myBuffer.remove(0));
                                    }
                                    else
                                    {
                                        HashMap<Integer,Long> hm = myBuffer.remove(0); // bevor it was at position 1
                                        Long lt = (Long)hm.get(3);
                                        hm.put(new Integer(3), (Long)Double.doubleToLongBits((l0+last_valid_point)/2));
                                        hm.put(new Integer(6), lt);
                                        WriteMapToOutputPort(hm);
                                    }
                                    System.out.println(myBuffer);
                                }
				ClosePorts();
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

    long ConvertData(int id, long measurement)
    {
        return measurement;
    }
    boolean IgnoreData(int id)
    {
        return false;
    }

} // MiddleFilter
