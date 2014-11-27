/******************************************************************************************************************
* File: MiddleIgnoreFilter.java
* @author Team Bud Spencer
* Course: Requirements Engineering und Software-Architektur
* Project: Assignment 1
* Copyright: Copyright (c) 2003 Carnegie Mellon University
* Versions:
*	1.0 November 2014 - MiddleIgnoreFilter.
*
* Description:
*
* This class serves as middle filter to ignore other IDs than  temperature or altitude.
*
* Parameters: none
*
* Internal Methods:	None
*
******************************************************************************************************************/
public class MiddleIgnoreFilter extends ConvertMiddleFilter
{
	/****************************************************************************
	* This function checks the ID wether its temperature or altitude
	*
	* @param id used for comparison
	*
	* @return the true, if id == 4|2 else false
	****************************************************************************/
    boolean IgnoreData(int id)
    {
        if(id == 4 || id == 2 || id == 0)
        {
            return false;
        }
        return true;
    }
}

