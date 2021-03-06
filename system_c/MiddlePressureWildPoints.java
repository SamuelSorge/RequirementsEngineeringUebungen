/******************************************************************************************************************
* File:MiddleFilter.java
* Course: 17655
* Project: Assignment 1
* Copyright: Copyright (c) 2003 Carnegie Mellon University
* Versions:
*   1.0 November 2008 - Sample Pipe and Filter code (ajl).
*
* Description:
*
* This class serves as an example for how to use the FilterRemplate to create a standard filter. This particular
* example is a simple "pass-through" filter that reads data from the filter's input port and writes data out the
* filter's output port.
*
* Parameters:       None
*
* Internal Methods: None
*
******************************************************************************************************************/

import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Iterator;

public class MiddlePressureWildPoints extends FilterFramework {

    // This is the length of all measurements (including time) in bytes
    static int MeasurementLength = 8;

    // This is the length of IDs in the byte stream
    static int IdLength = 4;

    public void run() {
		byte databyte = 0;      // This is the data byte read from the stream
		int bytesread = 0;      // This is the number of bytes read from the stream
		int byteswritten = 0;   // Number of bytes written to the stream.

		long measurement;		   // This is the word used to store all measurements - conversions are illustrated.
		int id;				     // This is the measurement id
		int i,j;				      // This is a loop counter
		double last_valid_point = 0.0;

		//
		List<HashMap<Integer, Long>> myBuffer = new ArrayList<HashMap<Integer, Long>>();
		HashMap<Integer, Long> myCurrentFrame = null;


		// Next we write a message to the terminal to let the world know we are alive...

		System.out.print( "\n" + this.getName() + "::Middle Reading ");

		while (true) {
		    /*************************************************************
		    *   Here we read a byte and write a byte
		    *************************************************************/

		    try {
				/***************************************************************************
				// We know that the first data coming to this filter is going to be an ID and
				// that it is IdLength long. So we first decommutate the ID bytes.
				****************************************************************************/

				id = 0;

				for (i = 0; i < IdLength; i++ ) {
				    databyte = ReadFilterInputPort();   // This is where we read the byte from the stream...

				    id = id | (databyte & 0xFF);		// We append the byte on to ID...

				    if (i != IdLength - 1) {		    // If this is not the last byte, then slide the
						// previously appended byte to the left by one byte
						id = id << 8;                   // to make room for the next byte we append to the ID

				    } // if

				    bytesread++;		                // Increment the byte count

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

				for (i = 0; i < MeasurementLength; i++ ) {
				    databyte = ReadFilterInputPort();
				    measurement = measurement | (databyte & 0xFF);  // We append the byte on to measurement...

				    if (i != MeasurementLength - 1) {		       // If this is not the last byte, then slide the
						// previously appended byte to the left by one byte
						measurement = measurement << 8;             // to make room for the next byte we append to the
						// measurement
				    } // if

				    bytesread++;		                            // Increment the byte count

				} // if

				// time so a new frame starts
				if (id == 0) {
				    if (myCurrentFrame != null) {
						myBuffer.add(myCurrentFrame);
				    }
				    //     System.out.println(myBuffer);
				    myCurrentFrame = new HashMap<Integer, Long>();
				}
				myCurrentFrame.put(id, measurement);

                // check if we find a matching value pair if we did not find any valid point yet
                for(i = 0 ; i < myBuffer.size() && last_valid_point == 0 ; i++)
                {
                    for(j = i + 1 ; j < myBuffer.size() ; j++)
                    {
                        double l0 = Double.longBitsToDouble((Long)myBuffer.get(i).get(3));
                        double l1 = Double.longBitsToDouble((Long)myBuffer.get(j).get(3));
                        // check if point is valid point (not wild)
                        if (Math.abs(l0 - l1) <= 10 && l0 > 0 && l1 > 0) {
                            // found valid pair
                            // first point in buffer is valid, so we send it out and proceed normaly
                            if(i == 0)
                            {
                                byteswritten += WriteMapToOutputPort(myBuffer.remove(0));
                                last_valid_point = l0;
                            }
                            else
                            {
                                // the first point is not a valid point. so set all points bevor the first valid one to the value of the first valid one.
                                HashMap<Integer, Long> hm_valid = myBuffer.remove(i);
                                // remove and send all invalid points
                                for(; i > 0 ; i--)
                                {
                                    HashMap<Integer, Long> hm = myBuffer.remove(0);
                                    Long lt = (Long)hm.get(3);
                                    hm.put(new Integer(3), hm_valid.get(3));
                                    hm.put(new Integer(6), lt);
                                    byteswritten += WriteMapToOutputPort(hm);
                                }
                                // send out the valid point
                                byteswritten += WriteMapToOutputPort(hm_valid);
                                last_valid_point = Double.longBitsToDouble(hm_valid.get(3));
                            }
                            // break out of both loops
                            i = myBuffer.size();
                            break;
                        }
                    }
                }

				while (myBuffer.size() > 1) {
				    double l0 = Double.longBitsToDouble((Long)myBuffer.get(0).get(3));
				    double l1 = Double.longBitsToDouble((Long)myBuffer.get(1).get(3));
				    // check if point is valid point (not wild)
				    if (Math.abs(l0 - l1) <= 10 && l0 > 0 && l1 > 0) {
						byteswritten += WriteMapToOutputPort(myBuffer.remove(0));
						last_valid_point = l0;
				    }else{
						break;
				    }
				}
				while(myBuffer.size() > 1 && last_valid_point > 0)
				{
				    double li = Double.longBitsToDouble((Long)myBuffer.get(0).get(3));
				    if (Math.abs(last_valid_point - li) <= 10 && li > 0) {
						// valid point
						byteswritten += WriteMapToOutputPort(myBuffer.remove(0));
						last_valid_point = li;
				    }
				    else
				    {
						break;
				    }
				}

				if(myBuffer.size() > 2 && last_valid_point > 0)
				{
				    //if first is not valid search vor next valid point
				    boolean found_valid = false;
				    HashMap<Integer, Long> hm_valid = new HashMap<Integer, Long>();// need init, just because java sucks
				    for(j = 0 ; j < myBuffer.size() && !found_valid ; j++)
				    {
						double lj = Double.longBitsToDouble((Long)myBuffer.get(j).get(3));
						if (Math.abs(last_valid_point - lj) <= 10 && lj > 0) {
						    found_valid = true;
						    hm_valid = myBuffer.remove(j);
						}
				    }
				    // there is an valid point in the queue, so all points inbetween are invalid
				    if(found_valid)
				    {
						double lv = Double.longBitsToDouble((Long)hm_valid.get(3));
						// calculate the avg for each invalid point
						for(i = 0 ; i < j-1 ; i++)
						{
						    // wild point
						    HashMap<Integer, Long> hm = myBuffer.remove(0);
						    Long lt = (Long)hm.get(3);
						    hm.put(new Integer(3), (Long)Double.doubleToLongBits((last_valid_point + lv) / 2));
						    hm.put(new Integer(6), lt);
						    byteswritten += WriteMapToOutputPort(hm);
						}
						// send out the valid point
						byteswritten += WriteMapToOutputPort(hm_valid);
						last_valid_point = lv;
				    }
				}
		    } // try

		    catch (EndOfStreamException e) {
				// check all remaining items
				for (i = 0 ; i < myBuffer.size() ; i++) {
				    double l0 = Double.longBitsToDouble((Long)myBuffer.get(0).get(3));
				    // valid point
				    if (Math.abs(l0 - last_valid_point) <= 10 && l0 > 0) {
						WriteMapToOutputPort(myBuffer.remove(0));
                        last_valid_point = l0;
				    } else {
						// wild point, set it to the value of the last valid point
						HashMap<Integer, Long> hm = myBuffer.remove(0);
						Long lt = (Long)hm.get(3);
						hm.put(new Integer(3), (Long)Double.doubleToLongBits(last_valid_point));
						hm.put(new Integer(6), lt);
						WriteMapToOutputPort(hm);
				    }
				    //System.out.println(myBuffer);
				}
				System.out.print( "\n" + this.getName() + "::Middle Exiting; bytes read: " + bytesread + " bytes written: " + byteswritten );
				ClosePorts();
				break;
		    } // catch

		} // while

    } // run

    int WriteMapToOutputPort(HashMap<Integer, Long> hm) {
		byte databyte = 0;
		int i;
		int byteswritten = 0;
		for (Map.Entry<Integer, Long> entry : hm.entrySet()) {
		    for (i = IdLength - 1; i >= 0; i-- ) {
				databyte = (byte)(entry.getKey() >> 8 * i );
				WriteFilterOutputPort(databyte);
				byteswritten++;
		    }
		    for (i = MeasurementLength - 1; i >= 0 ; i-- ) {
				databyte = (byte)(entry.getValue() >> 8 * i & 0xFF);
				WriteFilterOutputPort(databyte);
				byteswritten++;
		    }
		}
		return byteswritten;
    }

    long ConvertData(int id, long measurement) {
		return measurement;
    }
    boolean IgnoreData(int id) {
		return false;
    }

} // MiddleFilter
