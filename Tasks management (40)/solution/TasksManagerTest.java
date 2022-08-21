//package mk.ukim.finki.primeri;

import java.io.*;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

interface ITask {
    LocalDateTime getDeadline();
    int getPriority();
    String getCategory();
}

class DeadlineNotValidException extends Exception {
    public DeadlineNotValidException(LocalDateTime deadline) {
        super(String.format("The deadline %s has already passed", deadline));
    }
}


class SimpleTask implements ITask {

    String category;
    String name;
    String description;

    public SimpleTask(String category, String name, String description) {
        this.category = category;
        this.name = name;
        this.description = description;
    }

    @Override
    public LocalDateTime getDeadline() {
        return LocalDateTime.MAX;
    }

    @Override
    public int getPriority() {
        return Integer.MAX_VALUE;
    }

    @Override
    public String getCategory() {
        return category;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Task{");
        sb.append("name='").append(name).append('\'');
        sb.append(", description='").append(description).append('\'');
        sb.append('}');
        return sb.toString();
    }
}

abstract class TaskDecorator implements ITask{
    ITask iTask;

    public TaskDecorator(ITask iTask) {
        this.iTask = iTask;
    }
}

class PriorityTaskDecorator extends TaskDecorator {

    int priority;

    public PriorityTaskDecorator(ITask iTask, int priority) {
        super(iTask);
        this.priority = priority;
    }

    @Override
    public LocalDateTime getDeadline() {
        return iTask.getDeadline();
    }

    @Override
    public int getPriority() {
        return priority;
    }

    @Override
    public String getCategory() {
        return iTask.getCategory();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(iTask.toString(), 0, iTask.toString().length()-1);
        sb.append(", priority=").append(priority);
        sb.append('}');
        return sb.toString();
    }
}

class TimeTaskDecorator extends TaskDecorator {

    LocalDateTime deadline;

    public TimeTaskDecorator(ITask iTask, LocalDateTime deadline) {
        super(iTask);
        this.deadline = deadline;
    }

    @Override
    public LocalDateTime getDeadline() {
        return deadline;
    }

    @Override
    public int getPriority() {
        return iTask.getPriority();
    }

    @Override
    public String getCategory() {
        return iTask.getCategory();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(iTask.toString(), 0, iTask.toString().length()-1);
        sb.append(", deadline=").append(deadline);
        sb.append('}');
        return sb.toString();
    }
}

class TaskFactory {
    public static ITask createTask (String line) throws DeadlineNotValidException {
        String [] parts = line.split(",");
        String category = parts[0];
        String name = parts[1];
        String description = parts[2];
        SimpleTask base = new SimpleTask(category, name, description);
        if (parts.length==3) {
            return base;
        } else if (parts.length==4) {
            try {
                int priority = Integer.parseInt(parts[3]);
                return new PriorityTaskDecorator(base, priority);
            }
            catch (Exception e) { //parsing failed, it's a date
                LocalDateTime deadline = LocalDateTime.parse(parts[3]);
                checkDeadline(deadline);
                return new TimeTaskDecorator(base, deadline);
            }
        } else {
            LocalDateTime deadline = LocalDateTime.parse(parts[3]);
            checkDeadline(deadline);
            int priority = Integer.parseInt(parts[4]);
            return new PriorityTaskDecorator(new TimeTaskDecorator(base,deadline), priority);
        }
    }

    private static void checkDeadline (LocalDateTime deadline) throws DeadlineNotValidException {
        if (deadline.isBefore(LocalDateTime.now()))
            throw new DeadlineNotValidException(deadline);
    }
}

class TaskManager {
    Map<String,List<ITask>> tasks;

    public TaskManager() {
        tasks = new TreeMap<>();
    }

    public void readTasks (InputStream inputStream) {
        tasks = new BufferedReader(new InputStreamReader(inputStream))
                .lines()
                .map(line -> {
                    try {
                        return TaskFactory.createTask(line);
                    } catch (DeadlineNotValidException e) {
                        System.out.println(e.getMessage());
                    }
                    return null;
                })
                .filter(Objects::nonNull)
                .collect(Collectors.groupingBy(
                        ITask::getCategory,
                        TreeMap::new,
                        Collectors.toList())
                );
    }

    public void addTask (ITask iTask) {
        tasks.computeIfAbsent(iTask.getCategory(), k -> new ArrayList<>());
        tasks.computeIfPresent(iTask.getCategory(), (k,v) -> {
            v.add(iTask);
            return v;
        });
    }

    public void printTasks(OutputStream os, boolean includePriority, boolean byCategory) {
        PrintWriter pw = new PrintWriter(os);

        Comparator<ITask> priorityComparator = Comparator.comparing(ITask::getPriority).thenComparing(task -> Duration.between(LocalDateTime.now(), task.getDeadline()));
        Comparator<ITask> simpleComparator = Comparator.comparing(task -> Duration.between(LocalDateTime.now(), task.getDeadline()));

        if (byCategory) {
            tasks.forEach((category, t) -> {
                pw.println(category.toUpperCase());
                t.stream().sorted(includePriority ? priorityComparator : simpleComparator).forEach(pw::println);
            });
        }
        else {
            tasks.values().stream()
                    .flatMap(Collection::stream)
                    .sorted(includePriority ? priorityComparator : simpleComparator)
                    .forEach(pw::println);
        }

        pw.flush();
    }
}

public class TasksManagerTest {

    public static void main(String[] args) {
//        ITask timedTask = new TimeTaskDecorator(new SimpleTask("SCHOOL","SP", "lab po SP"), LocalDateTime.now().plusHours(240));
//        ITask priorityTask = new PriorityTaskDecorator(new SimpleTask("SCHOOL","NP", "lab po NP"),2);
//        ITask priorityTask2 = new PriorityTaskDecorator(new SimpleTask("SCHOOL","NP", "lab2  po NP"),1);
//        ITask priorityAndTimedTask = new PriorityTaskDecorator(new TimeTaskDecorator(new SimpleTask("SCHOOL","VP", "lab po VP"), LocalDateTime.now().plusHours(1000)), 3);
//
        TaskManager manager = new TaskManager();
//
//        manager.addTask(timedTask);
//        manager.addTask(priorityTask);
//        manager.addTask(priorityTask2);
//        manager.addTask(priorityAndTimedTask);

        System.out.println("Tasks reading");
        manager.readTasks(System.in);
        System.out.println("By categories with priority");
        manager.printTasks(System.out, true, true);
        System.out.println("-------------------------");
        System.out.println("By categories without priority");
        manager.printTasks(System.out, false, true);
        System.out.println("-------------------------");
        System.out.println("All tasks without priority");
        manager.printTasks(System.out, false, false);
        System.out.println("-------------------------");
        System.out.println("All tasks with priority");
        manager.printTasks(System.out, true, false);
        System.out.println("-------------------------");

    }
}
