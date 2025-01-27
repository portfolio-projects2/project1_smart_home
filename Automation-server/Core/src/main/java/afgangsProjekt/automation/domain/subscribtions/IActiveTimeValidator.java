package afgangsProjekt.automation.domain.subscribtions;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;

public interface IActiveTimeValidator {
    Boolean checkActiveTimeConditionIsSatisfied(ArrayList<String[]> occupancyData, double occupancyLevel) throws Exception;

    Boolean isActiveHourSatisfied(String activeHour)throws Exception;
}