
public class MiddleTemperatureFilter extends ConvertMiddleFilter
{
    long ConvertData(int id, long measurement)
    {
        if(id == 4)
        {
            double d = Double.longBitsToDouble(measurement);
            measurement = Double.doubleToLongBits( (d - 32)/1.8 );
        }
        return measurement;
    }
}

