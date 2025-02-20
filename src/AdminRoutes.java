public class AdminRoutes {
    private String routeID;
    private String username;
    private String route_startpoint;
    private String route_endpoint;
  

    public void AdminUser(String routeID, String username, String route_startpoint, String route_endpoint) {
        this.routeID = routeID;
        this.username = username;
        this.route_startpoint = route_startpoint;
        this.route_endpoint = route_endpoint;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }

    public String getrouteID() {
        return routeID;
    }
    public void setrouteID(String routeID) {
        this.routeID = routeID;
    }
    public String getroute_startpoint() {
        return route_startpoint;
    }
    public void setroute_startpoint(String route_startpoint) {
        this.route_startpoint = route_startpoint;
    }
    public String getroute_endpoint() {
        return route_endpoint;
    }
    public void setroute_endpoint(String route_endpoint) {
        this.route_endpoint = route_endpoint;
    }
}

