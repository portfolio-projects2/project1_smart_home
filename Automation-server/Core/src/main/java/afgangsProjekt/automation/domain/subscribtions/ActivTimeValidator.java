package afgangsProjekt.automation.domain.subscribtions;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class ActivTimeValidator implements IActiveTimeValidator{

@Override
    public  Boolean checkActiveTimeConditionIsSatisfied(ArrayList<String[]> occupancyData, double occupancyLevel) throws SQLException, ParseException {

        Boolean activeHourSatisfied = false;
        // check the weekday for current day
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        String currentDate = LocalDateTime.now().format(formatter);
        LocalDate date = LocalDate.parse(currentDate, formatter);
        if (date.getDayOfWeek().toString().equals("SATURDAY") || date.getDayOfWeek().toString().equals("SUNDAY")) {
            ArrayList<String> activeHoursWeekend = ActiveTimeLogic.getMostRepeatedActiveHoursWeekend(ActiveTimeLogic.getActiveHoursWeekend(ActiveTimeLogic.getActiveHours(occupancyData)));
            for (String activeHourWeekend : activeHoursWeekend) {
                activeHourSatisfied = isActiveHourSatisfied(activeHourWeekend);
            }
        } else {
            ArrayList<String> activeHoursWorkDays = ActiveTimeLogic.getMostRepeatedActiveHoursWorkdays(ActiveTimeLogic.getActiveHoursWorkDays(ActiveTimeLogic.getActiveHours(occupancyData)));
            for (String activeHourWorkDay : activeHoursWorkDays) {
                activeHourSatisfied = isActiveHourSatisfied(activeHourWorkDay);
            }
        }


        return activeHourSatisfied;
    }

    @Override
    public Boolean isActiveHourSatisfied(String activeHour) throws ParseException {
        // get the current time and format it as the style used on the system database
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH-mm");
        String currentTime = LocalDateTime.now().format(formatter);
        System.out.println(currentTime);
        Boolean timeSatisfied = false;
        String startTime = activeHour + "-00";
        String offTime = activeHour + "-60";
        Date startTimeDate = new SimpleDateFormat("HH-mm").parse(startTime);
        System.out.println(startTimeDate);
        Calendar startTimecalendarInstance = Calendar.getInstance();
        startTimecalendarInstance.setTime(startTimeDate);
        startTimecalendarInstance.add(Calendar.DATE, 1);


        Date offTimeDate = new SimpleDateFormat("HH-mm").parse(offTime);
        Calendar offTimecalendarInstance = Calendar.getInstance();
        offTimecalendarInstance.setTime(offTimeDate);
        offTimecalendarInstance.add(Calendar.DATE, 1);


        Date nowTime = new SimpleDateFormat("HH-mm").parse(currentTime);
        Calendar nowCalendarInstance = Calendar.getInstance();
        nowCalendarInstance.setTime(nowTime);
        nowCalendarInstance.add(Calendar.DATE, 1);

        Date nowDate = nowCalendarInstance.getTime();

        System.out.println("now :" + nowDate + "  start :" + startTimeDate + "  off :" + offTimeDate);

        if (nowDate.after(startTimecalendarInstance.getTime()) && nowDate.before(offTimecalendarInstance.getTime())) {
            timeSatisfied = true;
            System.out.println(true);
        } else {
            timeSatisfied = false;
            System.out.println(false);
        }
        return timeSatisfied;
    }


}
