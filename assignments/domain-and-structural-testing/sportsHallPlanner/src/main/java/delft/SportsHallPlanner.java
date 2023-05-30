package delft;

import java.util.*;

enum Property {
    NEAR_CITY_CENTRE,
    HAS_RESTAURANT,
    CLOSE_PUBLIC_TRANSPORT
}

enum Field {
    BADMINTON,
    TENNIS,
    VOLLEYBALL,
    BASKETBALL
}

class SportsHallPlanner {

    private SportsHallPlanner() {
        // Empty constructor
    }

    /**
     * This method assigns the given requests to the available sports halls in the list.
     * A request can only be assigned to a sports hall that has
     * enough sports fields of the given type and all the required properties.
     * Each sports hall can only be used to fulfill (at most) one request.
     * Not all sports halls need to be used, but all requests have to be assigned to a sports hall.
     * Whenever a request can be fulfilled by multiple halls, the first hall in the list should be chosen.
     * If there are multiple requests that can be assigned to multiple halls, 
     * the first request will be assigned to the first hall, the second to the second and so on. 
     * Finally, there should not be any duplicate halls in the list.
     * @param requests the requests to fulfill
     * @param halls the available sports halls
     * @return a map of sports halls to planned requests, or null if no suitable planning exists
     * @throws IllegalArgumentException if there are duplicate halls in the list
     */
    public static Map<SportsHall, Request> planHalls(List<Request> requests, List<SportsHall> halls) {
        if (new HashSet<>(halls).size() != halls.size()) {
            throw new IllegalArgumentException("There should be no duplicate elements in the halls list.");
        }
        if (requests.isEmpty()) {
            return new HashMap<>();
        }
        List<Request> nextRequests = new ArrayList<>(requests);
        Request current = nextRequests.remove(0);
        for (SportsHall hall : halls) {
            // If hall is a suitable hall
            if (hall.canFulfillRequest(current)) {
                List<SportsHall> nextHalls = new ArrayList<>(halls);
                nextHalls.remove(hall);
                // Check if we can fulfill other requests
                Map<SportsHall, Request> solution = planHalls(nextRequests, nextHalls);
                if (solution != null) {
                    solution.put(hall, current);
                    return solution;
                }
            }
        }

        return null;
    }
}

class Request {
    private final Set<Property> properties;
    private final Field requiredFieldType;
    private final int minNumberOfFields;

    public Request(Set<Property> properties, Field requiredFieldType, int minNumberOfFields) {
        this.properties = properties;
        this.requiredFieldType = requiredFieldType;
        this.minNumberOfFields = minNumberOfFields;
    }

    /**
     * Gives the set of required properties.
     * @return the required properties
     */
    public Set<Property> getProperties() {
        return properties;
    }

    /**
     * Gives the required field type.
     * @return the field type
     */
    public Field getRequiredFieldType() {
        return requiredFieldType;
    }

    /**
     * Gives the minimum number of fields that the sports hall should have.
     * @return the minimum number of fields
     */
    public int getMinNumberOfFields() {
        return minNumberOfFields;
    }

    @Override
    public String toString() {
        return "Request{" +
                "properties=" + properties +
                ", requiredFieldType=" + requiredFieldType +
                ", minNumberOfFields=" + minNumberOfFields +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Request request = (Request) o;

        if (minNumberOfFields != request.minNumberOfFields) return false;
        if (!Objects.equals(properties, request.properties)) return false;
        return requiredFieldType == request.requiredFieldType;
    }

    @Override
    public int hashCode() {
        int result = properties != null ? properties.hashCode() : 0;
        result = 31 * result + (requiredFieldType != null ? requiredFieldType.hashCode() : 0);
        result = 31 * result + minNumberOfFields;
        return result;
    }
}

class SportsHall {
    protected final Set<Property> properties;
    protected final Map<Field, Integer> fields;

    public SportsHall(Set<Property> properties, Map<Field, Integer> fields) {
        this.properties = properties;
        this.fields = fields;
    }

    /**
     * Checks whether this sports hall is suitable for the given requests.
     * It checks whether the required field is available, if there are enough fields,
     * and whether it has the required properties.
     * @param request the request to fulfill
     * @return true iff this sports hall can fulfill the request
     */
    public boolean canFulfillRequest(Request request) {
        return fields.containsKey(request.getRequiredFieldType())
                && fields.get(request.getRequiredFieldType()) >= request.getMinNumberOfFields()
                && properties.containsAll(request.getProperties());
    }

    @Override
    public String toString() {
        return "SportsHall{" +
                "properties=" + properties +
                ", fields=" + fields +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SportsHall that = (SportsHall) o;

        if (!Objects.equals(properties, that.properties)) return false;
        return Objects.equals(fields, that.fields);
    }

    @Override
    public int hashCode() {
        int result = properties != null ? properties.hashCode() : 0;
        result = 31 * result + (fields != null ? fields.hashCode() : 0);
        return result;
    }
}
