
public class MiddleIgnoreFilter extends ConvertMiddleFilter
{
    boolean IgnoreData(int id)
    {
        if(id == 4 || id == 2)
        {
            return false;
        }
        return true;
    }
}

