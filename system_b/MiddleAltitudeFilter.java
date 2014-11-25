/******************************************************************************************************************
*
* This class is responsible for convert the altitude from
* feet to meters.
* The ConvertMiddleFilter serves as base class.
*
******************************************************************************************************************/
public class MiddleAltitudeFilter extends ConvertMiddleFilter {

	// The method needs a id and a measurement
	// The return-value is the measurement with the
	// converted altitude data in meters.
    long ConvertData(int id, long measurement) {
        if (id == 2) {
            double d = Double.longBitsToDouble(measurement);
            measurement = Double.doubleToLongBits( d / 3.2808);
        }
        return measurement;
    }
}

