
public class MiddleAltitudeFilter extends ConvertMiddleFilter
{
    long ConvertData(int id, long measurement)
    {
        if(id == 2)
        {
            double d = Double.longBitsToDouble(measurement);
            measurement = Double.doubleToLongBits( d / 3.2808);
        }
        return measurement;
    }
}

