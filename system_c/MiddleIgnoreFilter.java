
public class MiddleIgnoreFilter extends ConvertMiddleFilter
{
    boolean IgnoreData(int id)
    {
        if(id == 0 || id == 2 || id == 4 || id == 3)
        {
            return false;
        }
        return true;
    }
}

