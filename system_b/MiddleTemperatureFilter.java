
public class MiddleTemperatureFilter extends ConvertMiddleFilter
{
    long ConvertData(int id, long measurement)
    {
        if(id == 4)
        {
            double d = Double.longBitsToDouble(measurement);
            measurement = Double.doubleToLongBits( d * 1.8 + 32);
        }
        return measurement;
    }
}

