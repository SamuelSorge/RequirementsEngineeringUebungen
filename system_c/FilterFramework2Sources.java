/******************************************************************************************************************
* File:FilterFramework.java
* Course: 17655
* Project: Assignment 1
* Copyright: Copyright (c) 2003 Carnegie Mellon University
* Versions:
*	1.0 November 2008 - Initial rewrite of original assignment 1 (ajl).
*
* Description:
*
* This superclass defines a skeletal filter framework that defines a filter in terms of the input and output
* ports. All filters must be defined in terms of this framework - that is, filters must extend this class
* in order to be considered valid system filters. Filters as standalone threads until the inputport no longer
* has any data - at which point the filter finishes up any work it has to do and then terminates.
*
* Parameters:
*
* Input[1,2]ReadPort:	This is the filter's input port. Essentially this port is connected to another filter's piped
*					output steam. All filters connect to other filters by connecting their input ports to other
*					filter's output ports. This is handled by the Connect() method.
*
* OutputWritePort:	This the filter's output port. Essentially the filter's job is to read data from the input port,
*					perform some operation on the data, then write the transformed data on the output port.
*
* FilterFramework:  This is a reference to the filter that is connected to the instance filter's input port. This
*					reference is to determine when the upstream filter has stopped sending data along the pipe.
*
* Internal Methods:
*
*	public void Connect( FilterFramework Filter )
*	public byte ReadFilterInputPort()
*	public void WriteFilterOutputPort(byte datum)
*	public boolean EndOfInputStream()
*
******************************************************************************************************************/

import java.io.*;

public class FilterFramework2Sources extends FilterFramework
{

        // Define filter input and output ports
        FilterFramework filter2 = new FilterFramework();

        /***************************************************************************
	* CONCRETE METHOD:: Connect
	* Purpose: This method connects filters to each other. All connections are
	* through the inputport of each filter. That is each filter's inputport is
	* connected to another filter's output port through this method.
	*
	* Arguments:
	* 	FilterFramework - this is the filter that this filter will connect to.
	*
	* Returns: void
	*
	* Exceptions: IOException
	*
	****************************************************************************/

	void Connect2( FilterFramework Filter )
        {
            // Connect this filter's input to the upstream pipe's output stream
            filter2.Connect(Filter);
        } // Connect2

	/***************************************************************************
	* CONCRETE METHOD:: ReadFilterInputPort
	* Purpose: This method reads data from the input port one byte at a time.
	*
	* Arguments: void
	*
	* Returns: byte of data read from the input port of the filter.
	*
	* Exceptions: IOExecption, EndOfStreamException (rethrown)
	*
	****************************************************************************/

	byte ReadFilterInput2Port() throws EndOfStreamException
	{
            return filter2.ReadFilterInputPort();
	} // ReadFilter2Port

	/***************************************************************************
	* CONCRETE METHOD:: ClosePorts
	* Purpose: This method is used to close the input and output ports of the
	* filter. It is important that filters close their ports before the filter
	* thread exits.
	*
	* Arguments: void
	*
	* Returns: void
	*
	* Exceptions: IOExecption
	*
	****************************************************************************/

	void ClosePorts()
	{
            filter2.ClosePorts();
            super.ClosePorts();
	} // ClosePorts

	/***************************************************************************
	* CONCRETE METHOD:: run
	* Purpose: This is actually an abstract method defined by Thread. It is called
	* when the thread is started by calling the Thread.start() method. In this
	* case, the run() method should be overridden by the filter programmer using
	* this framework superclass
	*
	* Arguments: void
	*
	* Returns: void
	*
	* Exceptions: IOExecption
	*
	****************************************************************************/

	public void run()
        {
		// The run method should be overridden by the subordinate class. Please
		// see the example applications provided for more details.

	} // run

} // FilterFramework class
