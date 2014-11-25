/******************************************************************************************************************
* File: MiddleAltitudeFilter.java
* @author Team Bud Spencer
* Course: Requirements Engineering und Software-Architektur
* Project: Assignment 1
* Copyright: Copyright (c) 2003 Carnegie Mellon University
* Versions:
*	1.0 November 2014 - MiddleAltitudeFilter.
*
* Description:
*
* This class serves as middle filter to convert the altitude from "feet" to "meters".
*
* Parameters: none
*
* Internal Methods:	None
*
******************************************************************************************************************/

public class MiddleAltitudeFilter extends ConvertMiddleFilter
{
	/****************************************************************************
	* This function converts the altitude from feet to meters if there
	* is a matching ID.
	*
	* @param id used for comparison with the altitude id (=2)
	* @param measurement used to store the converted data
	*
	* @return the converted altitude data
	****************************************************************************/
    long ConvertData(int id, long measurement)
    {
        if(id == 2)
        {
            double temp = Double.longBitsToDouble(measurement);
            measurement = Double.doubleToLongBits( temp / 3.2808);
        }
        return measurement;
    }
}

