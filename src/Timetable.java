package ru.yandex.practicum.gym;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.TreeMap;
import java.util.Comparator;
import java.util.Collections;

public class Timetable {
    private Map<DayOfWeek, TreeMap<TimeOfDay, List<TrainingSession>>> timetable = new HashMap<>();

    public void addNewTrainingSession(TrainingSession trainingSession) {
        DayOfWeek day = trainingSession.getDayOfWeek();
        TimeOfDay time = trainingSession.getTimeOfDay();

        if (!timetable.containsKey(day)) {
            timetable.put(day, new TreeMap<>(new Comparator<TimeOfDay>() {
                @Override
                public int compare(TimeOfDay t1, TimeOfDay t2) {
                    if (t1.getHours() != t2.getHours()) {
                        return Integer.compare(t1.getHours(), t2.getHours());
                    }
                    return Integer.compare(t1.getMinutes(), t2.getMinutes());
                }
            }));
        }
        TreeMap<TimeOfDay, List<TrainingSession>> daySchedule = timetable.get(day);
        if (!daySchedule.containsKey(time)) {
            daySchedule.put(time, new ArrayList<>());
        }
        daySchedule.get(time).add(trainingSession);
    }


    public List<TrainingSession> getTrainingSessionsForDay(DayOfWeek dayOfWeek) {
        if (!timetable.containsKey(dayOfWeek)) {
            return Collections.emptyList();
        }
        List<TrainingSession> result = new ArrayList<>();
        TreeMap<TimeOfDay, List<TrainingSession>> daySchedule = timetable.get(dayOfWeek);
        for (TimeOfDay time : daySchedule.navigableKeySet()) {
            result.addAll(daySchedule.get(time));
        }

        return result;
    }


    public List<TrainingSession> getTrainingSessionsForDayAndTime(DayOfWeek dayOfWeek, TimeOfDay timeOfDay) {
        if (!timetable.containsKey(dayOfWeek)) {
            return Collections.emptyList();
        }
        TreeMap<TimeOfDay, List<TrainingSession>> daySchedule = timetable.get(dayOfWeek);
        return daySchedule.getOrDefault(timeOfDay, Collections.emptyList());
    }

    public List<CounterOfTrainings> getCountByCoaches() {
        Map<Coach, Integer> countsMap = new HashMap<>();
        for (TreeMap<TimeOfDay, List<TrainingSession>> daySchedule : timetable.values()) {
            for (List<TrainingSession> sessions : daySchedule.values()) {
                for (TrainingSession session : sessions) {
                    Coach coach = session.getCoach();
                    countsMap.put(coach, countsMap.getOrDefault(coach, 0) + 1);
                }
            }
        }
        List<CounterOfTrainings> resultList = new ArrayList<>();
        for (Map.Entry<Coach, Integer> entry : countsMap.entrySet()) {
            resultList.add(new CounterOfTrainings(entry.getKey(), entry.getValue()));
        }
        resultList.sort(new Comparator<CounterOfTrainings>() {
            @Override
            public int compare(CounterOfTrainings c1, CounterOfTrainings c2) {
                return Integer.compare(c2.getCount(), c1.getCount());
            }
        });
        return resultList;
    }
}
