/******************************************************************************************************************
*
* This class is responsible for convert the temperature from
* Fahrenheit to Celsius.
* The ConvertMiddleFilter serves as base class.
*
******************************************************************************************************************/
public class MiddleTemperatureFilter extends ConvertMiddleFilter {

    // The method needs a id and a measurement
    // The return-value is the measurement with the
    // converted temperature data in Celsius.
    long ConvertData(int id, long measurement) {
        if (id == 4) {
            double d = Double.longBitsToDouble(measurement);
            measurement = Double.doubleToLongBits( (d - 32) / 1.8 );
        }
        return measurement;
    }
}

