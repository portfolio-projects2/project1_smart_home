package afgangsProjekt.automation.domain.subscribtions;

import aQute.bnd.annotation.component.Component;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class ActiveTimeLogic {

    //get mostRepeated->getactivehours->sortByDay


//this class find the active hours in work days and weekends for a specific room
//the logic start with getting all data via persistance class as list of arrays
//then sort data by putting echa day data in map index
//then get the active hours of each day and find out how many times each active hour was repeated through all time



    public static HashMap<String, ArrayList<String[]>> sortDataByDay(ArrayList<String[]> dataList) throws SQLException {
        // getting all data form database sorted as list of arrays
        HashMap<String, ArrayList<String[]>> sameDayMap = new HashMap<>();

        ArrayList<String[]> fill = new ArrayList<>();

        // iterate each data row and put data from the same day into a map (day,list of all day's data each list index is array)
        for (int i = 0; i < dataList.size() - 1; i++) {
            //
            if (dataList.get(i)[3].equals(dataList.get(i + 1)[3]) ||
                    (!dataList.get(i)[3].equals(dataList.get(i + 1)[3])
                            & dataList.get(i)[3].equals(dataList.get(i - 1)[3]))) {
                fill.add(dataList.get(i));
                sameDayMap.put(dataList.get(i)[3], fill);
            }

            if (!dataList.get(i)[3].equals(dataList.get(i + 1)[3])) {
                fill = new ArrayList<>();

            }

        }

        if (dataList.get(dataList.size() - 1)[3].equals(dataList.get(dataList.size() - 2)[3])) {
            ArrayList<String[]> lastArray = (ArrayList<String[]>) sameDayMap.get(dataList.get(dataList.size() - 1)[3]);
            lastArray.add(dataList.get(dataList.size() - 1));
            sameDayMap.put(dataList.get(dataList.size() - 1)[3], lastArray);
        }

         printData(sameDayMap);

        return sameDayMap;


    }

    static void printData(HashMap<String, ArrayList<String[]>> sameDayMap){
        sameDayMap.forEach((k,v)->{
            System.out.println(k);
            for (String[] s:v ) {
                System.out.println(Arrays.toString(s));
            }

        });
    }



    // the system will cheek the motion sensor data each 30 sec that means there will be 120 rows each hour
    // the system will cheek every hour's data and if there were people inside the room for more than 50% of time so it considers as active hour
    // this method take (day,dataof the day)map and return (day,activeHours list at this day)map
    // this method return how many times each da
    public static HashMap<String, ArrayList<String>> getActiveHours( ArrayList<String[]> occupancyDataFromDB) throws SQLException {
        System.out.println(1111111111);
        HashMap<String, ArrayList<String[]>> allDaysDataMap=sortDataByDay(occupancyDataFromDB);
        HashMap<String, ArrayList<String>> daysActiveHours = new HashMap<>();

        allDaysDataMap.forEach((dayDate, dataList) -> {
            HashMap<String, Integer> timesActiveEachHour = new HashMap<>();
            ArrayList<String> acceptedActiveHours = new ArrayList<>();
            int activeTimes = 0;
            for (String[] dataArray : dataList) {


                String hour = Character.toString(dataArray[4].charAt(0)) + Character.toString(dataArray[4].charAt(1));
                  System.out.println(hour);
                if (!timesActiveEachHour.containsKey(hour)) {
                    activeTimes = 0;
                }
                if (dataArray[5].equals("1")) {
                    ++activeTimes;
                    timesActiveEachHour.put(hour, activeTimes);


                }

            }
            System.out.println(timesActiveEachHour);
            timesActiveEachHour.forEach((hour, times) -> {
                if (times >= 2) {
                    acceptedActiveHours.add(hour);

                }
            });

           daysActiveHours.put(dayDate, acceptedActiveHours);
         //   System.out.println(timesActiveEachHour);
           // System.out.println(activeTimes);
        });
//

        System.out.println(daysActiveHours);
        return daysActiveHours;

    }


    public static HashMap<AtomicInteger, HashMap<String, Integer>> getActiveHoursWeekend(HashMap<String, ArrayList<String>> roomAllActiveHours) throws SQLException {

        HashMap<String, ArrayList<String>> weekendActiveHours = new HashMap<>();
        AtomicInteger daysCounter= new AtomicInteger();
        roomAllActiveHours.forEach((day, activeHours) -> {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy", Locale.ENGLISH);
            LocalDate date = LocalDate.parse(day, formatter);
            if (date.getDayOfWeek().toString().equals("SATURDAY") || date.getDayOfWeek().toString().equals("SUNDAY")) {
                weekendActiveHours.put(day, activeHours);
                daysCounter.incrementAndGet();
            }

        });
        HashMap<String, ArrayList<String>> comulutiveRepeatWeekend = new HashMap<>();
        weekendActiveHours.forEach((day, activeHours) -> {
            for (int i = 0; i < 25; i++) {
                // the date format includes hours of format HH thats why the loop must matches hours by adding 0 to the left of each hour under 10
                if (i < 10) {
                    if (activeHours.contains(String.valueOf(i))) {
                        //  System.out.println("contain");
                        int finalI = i;
                        comulutiveRepeatWeekend.computeIfPresent(String.valueOf(i), (k, v) -> {
                            v.add(String.valueOf(finalI));
                            return v;
                        });
                        //  System.out.println("afterIfPresent"+comulutiveRepeat);
                        if (comulutiveRepeatWeekend.containsKey(String.valueOf(i)) == false) {
                            comulutiveRepeatWeekend.computeIfAbsent(String.valueOf(i), k -> new ArrayList<>()).add(String.valueOf(i));

                        }
                    }
                } else if (i >= 10) {

                    if (activeHours.contains(String.valueOf(i))) {
                        //  System.out.println("contain");
                        int finalI = i;
                        comulutiveRepeatWeekend.computeIfPresent(String.valueOf(i), (k, v) -> {
                            v.add(String.valueOf(finalI));
                            return v;
                        });
                        //System.out.println("afterIfPresent" + comulutiveRepeatWeekend);
                        if (comulutiveRepeatWeekend.containsKey(String.valueOf(i)) == false) {
                            comulutiveRepeatWeekend.computeIfAbsent(String.valueOf(i), k -> new ArrayList<>()).add(String.valueOf(i));

                        }
                        // System.out.println("afterIsAbsent" + comulutiveRepeatWeekend);
                    }
                }


            }
        });
        HashMap<AtomicInteger,HashMap<String,Integer>> timesHourRepeatedThroghAllWorkDaysCounter = new HashMap<>();
        HashMap<String,Integer> timesHourRepeatedThroghAllWorkDays = new HashMap<>();

        comulutiveRepeatWeekend.forEach((k, v) -> {
            timesHourRepeatedThroghAllWorkDays.put(k,v.size());
            timesHourRepeatedThroghAllWorkDaysCounter.put(daysCounter,timesHourRepeatedThroghAllWorkDays);
        });

        System.out.println("weekendActiveHours"+comulutiveRepeatWeekend);
        System.out.println("timesrepeatedThroughDaysCountWeekend"+timesHourRepeatedThroghAllWorkDaysCounter);

        return timesHourRepeatedThroghAllWorkDaysCounter;
    }

    public static HashMap<AtomicInteger, HashMap<String, Integer>> getActiveHoursWorkDays(HashMap<String, ArrayList<String>> roomAllActiveHours) throws SQLException {
        // first  got all active hours(day, List of active hours) map from roomAllActiveHours
        //then  check the week day for each day and if its not saturday or sunday add to (day,list of active hours) map only for workdays
        HashMap<String, ArrayList<String>> workdaysActiveHours = new HashMap<>();
        AtomicInteger daysCounter= new AtomicInteger();
        roomAllActiveHours.forEach((day, activeHours) -> {

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy", Locale.ENGLISH);
            LocalDate date = LocalDate.parse(day, formatter);
            // System.out.println(date.getDayOfWeek().toString());
            if (!date.getDayOfWeek().toString().equals("SATURDAY") & !date.getDayOfWeek().toString().equals("SUNDAY")) {
                workdaysActiveHours.put(day, activeHours);
                daysCounter.incrementAndGet();
            }

        });
        // System.out.println("workdaysActive "+workdaysActiveHours);
        // here  iterate all worksday active hours map and for each day  iterate all hours through a dy and if the active hours list contans
        //--> this specific hour  add the hour as key of a new map whith the hour added as value of list
        // so for each day for each time the hours matches the array list will have a new element thats men the size of this list after iterating all days
        //--> shows how many times the active hour has been repeated through all time
        HashMap<String, ArrayList<String>> comulutiveRepeat = new HashMap<>();

        workdaysActiveHours.forEach((day, activeHours) -> {


            for (int i = 0; i < 25; i++) {
                // the date format includes hours of format HH thats why the loop must matches hours by adding 0 to the left of each hour under 10
                if (i < 10) {
                    if (activeHours.contains(String.valueOf(i))) {
                        //  System.out.println("contain");
                        int finalI = i;
                        comulutiveRepeat.computeIfPresent(String.valueOf(i), (k, v) -> {
                            v.add(String.valueOf(finalI));
                            return v;
                        });
                        //  System.out.println("afterIfPresent"+comulutiveRepeat);
                        if (comulutiveRepeat.containsKey(String.valueOf(i)) == false) {
                            comulutiveRepeat.computeIfAbsent(String.valueOf(i), k -> new ArrayList<>()).add(String.valueOf(i));

                        }
                    }
                } else if (i >= 10) {

                    if (activeHours.contains(String.valueOf(i))) {
                        //  System.out.println("contain");
                        int finalI = i;
                        comulutiveRepeat.computeIfPresent(String.valueOf(i), (k, v) -> {
                            v.add(String.valueOf(finalI));
                            return v;
                        });
                        // System.out.println("afterIfPresent" + comulutiveRepeat);
                        if (comulutiveRepeat.containsKey(String.valueOf(i)) == false) {
                            comulutiveRepeat.computeIfAbsent(String.valueOf(i), k -> new ArrayList<>()).add(String.valueOf(i));

                        }
                        // System.out.println("afterIsAbsent" + comulutiveRepeat);
                    }
                }


            }
        });
        HashMap<AtomicInteger,HashMap<String,Integer>> timesHourRepeatedThroghAllWorkDaysCounter = new HashMap<>();
        HashMap<String,Integer> timesHourRepeatedThroghAllWorkDays = new HashMap<>();

        comulutiveRepeat.forEach((k, v) -> {
            timesHourRepeatedThroghAllWorkDays.put(k,v.size());
            timesHourRepeatedThroghAllWorkDaysCounter.put(daysCounter,timesHourRepeatedThroghAllWorkDays);
        });

        System.out.println("worksdayActiveHours"+comulutiveRepeat);
        System.out.println("timesrepeatedThroughDaysCount"+timesHourRepeatedThroghAllWorkDaysCounter);

        return timesHourRepeatedThroghAllWorkDaysCounter;
    }



    public static ArrayList<String> getMostRepeatedActiveHoursWorkdays( HashMap<AtomicInteger, HashMap<String, Integer>> workDaysActiveHours) throws SQLException {
        ArrayList<String> activeHoursList = new ArrayList<>();
        workDaysActiveHours.forEach((k,v)->{

            v.forEach((k2,v2)->{

                        if( (double)v2/k.doubleValue()>0.75){
                            activeHoursList.add(k2);
                };
            });
        });
    return activeHoursList;
    }

    public static ArrayList<String> getMostRepeatedActiveHoursWeekend( HashMap<AtomicInteger, HashMap<String, Integer>> weekendActiveHours) throws SQLException {

        ArrayList<String> activeHoursList = new ArrayList<>();
        weekendActiveHours.forEach((k,v)->{

            v.forEach((k2,v2)->{

                if( (double)v2/k.doubleValue()>0.75){
                    activeHoursList.add(k2.toString());
                };
            });
        });
        return activeHoursList;
    }


}
