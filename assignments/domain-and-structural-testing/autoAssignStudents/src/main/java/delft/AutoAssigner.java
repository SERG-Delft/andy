package delft;

import java.util.*;
import java.time.*;
import java.time.format.*;
import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
@interface GeneratedExcludeFromJacoco {
}

class Student {
    private final int id;
    private final String name;
    private final String email;

    public Student(int id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }

    public String getName() {
        return this.name;
    }

    public String getEmail() {
        return this.email;
    }

    @GeneratedExcludeFromJacoco
    public int getId() {
        return this.id;
    }

    @Override
    @GeneratedExcludeFromJacoco
    public int hashCode() {
        return Objects.hash(this.id, this.name, this.email);
    }

    @Override
    @GeneratedExcludeFromJacoco
    public boolean equals(Object o) {
        // self check
        if (this == o)
            return true;
        // null check
        if (o == null)
            return false;
        // type check and cast
        if (getClass() != o.getClass())
            return false;
        Student other = (Student) o;
        // field comparison
        return Objects.equals(id, other.id)
                && Objects.equals(name, other.name)
                && Objects.equals(email, other.email);
    }
}

class Workshop {
    private final int id;
    private final String name;
    private final Map<ZonedDateTime, Integer> spotsPerDate;

    public Workshop(int id, String name, Map<ZonedDateTime, Integer> spotsPerDate) {
        this.id = id;
        this.name = name;
        this.spotsPerDate = new HashMap<ZonedDateTime, Integer>(spotsPerDate);
    }

    @GeneratedExcludeFromJacoco
    public Map<ZonedDateTime, Integer> getSpotsPerDate() {
        return new HashMap<ZonedDateTime, Integer>(this.spotsPerDate);
    }

    public String getName() {
        return this.name;
    }

    @GeneratedExcludeFromJacoco
    public int getId() {
        return this.id;
    }

    public boolean hasAvailableDate() {
        // If any date has more than zero spots, this means there's an available date
        return spotsPerDate.values().stream().anyMatch(v -> v > 0);
    }

    // If there's an available date, it returns this date.
    // This method should not be called if there's no spot available.
    public ZonedDateTime getNextAvailableDate() {
        // picks the first one that has available spots
        ZonedDateTime firstAvailableDate = spotsPerDate
            .entrySet()
            .stream()
            .filter(e -> e.getValue() > 0) // get all entries in the map that have a spot left
            .map(Map.Entry::getKey) // get the dates (which are the keys in the map)
            .sorted() // order the keys
            .findFirst() // get the first one
            .get();

        return firstAvailableDate;
    }

    // This method takes up a spot on the given date.
    // You should not call this method with an invalid date.
    // (In real code, you would want to write some defensive code here,
    // we just don't do it for the sake of simplicity in the exam.)
    public void takeASpot(ZonedDateTime date) {
        spotsPerDate.put(date, spotsPerDate.get(date) - 1);
    }

    @Override
    @GeneratedExcludeFromJacoco
    public int hashCode() {
        return Objects.hash(this.id, this.name, this.spotsPerDate);
    }

    @Override
    @GeneratedExcludeFromJacoco
    public boolean equals(Object o) {
        // self check
        if (this == o)
            return true;
        // null check
        if (o == null)
            return false;
        // type check and cast
        if (getClass() != o.getClass())
            return false;
        Workshop other = (Workshop) o;
        // field comparison
        return Objects.equals(id, other.id)
                && Objects.equals(name, other.name)
                && Objects.equals(spotsPerDate, other.spotsPerDate);
    }
}

// In real life, this class would assign students to workshops
// by means of, e.g., persisting new information in a database.
// For the sake of simplicity in this exam, we just store this information as a log.
class AssignmentsLogger {
    private final List<String> errors = new ArrayList<String>();
    private final List<String> assignments = new ArrayList<String>();
    // The formatter generates strings like "13/07/2022 14:00"
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    public void noAvailableSpots(Workshop workshop, Student student) {
        // Example of the added string: "Python,Frank"
        errors.add(String.format("%s,%s", workshop.getName(), student.getName()));
    }

    public void assign(Workshop workshop, Student student, ZonedDateTime date) {
        // Example of the added string: "Java,Mauricio,13/07/2022 14:00"
        assignments.add(String.format("%s,%s,%s", workshop.getName(), student.getName(), format(date)));
    }

    public List<String> getErrors() {
        return Collections.unmodifiableList(errors);
    }

    public List<String> getAssignments() {
        return Collections.unmodifiableList(assignments);
    }

    private String format(ZonedDateTime date) {
        return date.format(formatter);
    }
}


class AutoAssigner {
    public AssignmentsLogger assign(List<Student> students, 
      List<Workshop> workshops) {

          AssignmentsLogger assignments = new AssignmentsLogger();

          for(Workshop workshop : workshops) {
              for(Student student : students) {
                // Get the next available date for that workshop
                // If there's no way, nothing we can do, just log it!
                if(!workshop.hasAvailableDate()) {
                    assignments.noAvailableSpots(workshop, student);
                } else {
                    // Great, that workshop has an available date!
                    // Let's assign the student to that workshop on that specific date
                    ZonedDateTime nextDate = workshop.getNextAvailableDate();
                    assignments.assign(workshop, student, nextDate);
                    workshop.takeASpot(nextDate);
                }
              }
          }

          return assignments;

    }

}
