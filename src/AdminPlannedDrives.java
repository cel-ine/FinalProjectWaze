import java.time.LocalDate;
import java.time.LocalTime;

public class AdminPlannedDrives {
    private int plannedDriveId;
    private String username;
    private String routeId;
    private LocalDate calendar;
    private LocalTime plannedTime;
    private String pinnedLoc;

    // Constructor
    public AdminPlannedDrives(int plannedDriveId, String username, String routeId, LocalDate calendar, LocalTime plannedTime, String pinnedLoc) {
        this.plannedDriveId = plannedDriveId;
        this.username = username;
        this.routeId = routeId;
        this.calendar = calendar;
        this.plannedTime = plannedTime;
        this.pinnedLoc = pinnedLoc;
    }

    // Getters and Setters
    public int getPlannedDriveId() {
        return plannedDriveId;
    }

    public void setPlannedDriveId(int plannedDriveId) {
        this.plannedDriveId = plannedDriveId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRouteId() {
        return routeId;
    }

    public void setRouteId(String routeId) {
        this.routeId = routeId;
    }

    public LocalDate getCalendar() {
        return calendar;
    }

    public void setCalendar(LocalDate calendar) {
        this.calendar = calendar;
    }

    public LocalTime getPlannedTime() {
        return plannedTime;
    }

    public void setPlannedTime(LocalTime plannedTime) {
        this.plannedTime = plannedTime;
    }

    public String getPinnedLoc() {
        return pinnedLoc;
    }

    public void setPinnedLoc(String pinnedLoc) {
        this.pinnedLoc = pinnedLoc;
    }
}