/******************************************************************************************************************
*
* Data which are not required will be ignored.
* The ConvertMiddleFilter serves as base class.
*
******************************************************************************************************************/
public class MiddleIgnoreFilter extends ConvertMiddleFilter {

	// If the id 0, 2, 3, 4 the data will be ignored
	// else it will be used
    boolean IgnoreData(int id) {
        if (id == 0 || id == 2 || id == 3 || id == 4) {
            return false;
        }
        return true;
    }
}

