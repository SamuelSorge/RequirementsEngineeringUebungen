/******************************************************************************************************************
* File: MiddleTemperatureFilter.java
* @author Team Bud Spencer 
* Course: Requirements Engineering und Software-Architektur
* Project: Assignment 1
* Copyright: Copyright (c) 2003 Carnegie Mellon University
* Versions:
<<<<<<< HEAD
*   1.0 November 2014 - SystemA.
=======
*	1.0 November 2014 - MiddleTemperatureFilter.
>>>>>>> ef54f360924fa22b5fa69c20015bd0ac24672608
*
* Description:
*
* This class serves as middle filter to convert the temperature from "Fahrenheit" to "Celsius".
*
* Parameters: none
*
* Internal Methods: None
*
******************************************************************************************************************/
public class MiddleTemperatureFilter extends ConvertMiddleFilter {
    /****************************************************************************
    * This function converts the temperature from Fahrenheit to Celsius if there
    * is a matching ID.
    *
    * @param id used for comparison with the temperature id (=4)
    * @param measurement used to store the converted data
    *
    * @return the converted temperature data
    ****************************************************************************/
    long ConvertConvertData(int id, long measurement) {
        if (id == 4) {
            double temp = Double.longBitsToDouble(measurement);
            measurement = Double.doubleToLongBits( (temp - 32) / 1.8);
        }
        return measurement;
    }
}

