import java.util.*; 
 
// ---------------------- Plan Class ---------------------- 
class Plan { 
    private int planId; 
    private String name; 
    private double monthlyPrice; 
    private int screens; 
    private String quality; 
 
    public Plan(int planId, String name, double monthlyPrice, int screens, String quality) { 
        this.planId = planId; 
        this.name = name; 
        this.monthlyPrice = monthlyPrice; 
        this.screens = screens; 
        this.quality = quality; 
    } 
 
    // Getters 
    public int getPlanId() { return planId; } 
    public String getName() { return name; } 
    public double getMonthlyPrice() { return monthlyPrice; } 
    public int getScreens() { return screens; } 
    public String getQuality() { return quality; } 
 
    // Setters (encapsulation - limited updates allowed) 
    public void setMonthlyPrice(double monthlyPrice) { 
        if(monthlyPrice > 0) this.monthlyPrice = monthlyPrice; 
    } 
 
    @Override 
    public String toString() { 
        return name + " (" + quality + ", " + screens + " screens, $" + monthlyPrice + ")"; 
    } 
} 
 
// ---------------------- Content (Parent) ---------------------- 
abstract class Content { 
    protected int contentId; 
    protected String title; 
    protected String type; // Movie / Series 
    protected int rating; // 1-5 
 
    public Content(int contentId, String title, String type, int rating) { 
        this.contentId = contentId; 
        this.title = title; 
        this.type = type; 
        this.rating = rating; 
    } 
 
    public String getTitle() { return title; } 
    public int getRating() { return rating; } 
    public String getType() { return type; } 
 
    public void setRating(int rating) { 
        if (rating >= 1 && rating <= 5) { 
            this.rating = rating; 
        } 
    } 
 
 
    // Abstract play (polymorphic) 
    public abstract void play(); 
} 
 
// ---------------------- Movie (Child of Content) ---------------------- 
class Movie extends Content { 
    private int duration; // in minutes 
 
    public Movie(int contentId, String title, int duration, int rating) { 
        super(contentId, title, "Movie", rating); 
        this.duration = duration; 
    } 
 
    @Override 
    public void play() { 
        System.out.println("▶ Playing Movie: " + title + " [" + duration + " mins]"); 
    } 
} 
 
// ---------------------- Series (Child of Content) ---------------------- 
class Series extends Content { 
    private int episodes; 
 
    public Series(int contentId, String title, int episodes, int rating) { 
        super(contentId, title, "Series", rating); 
        this.episodes = episodes; 
    } 
 
    @Override 
    public void play() { 
        System.out.println("▶ Playing Series: " + title + " [Episodes: " + episodes + "]"); 
    } 
} 
 
// ---------------------- User ---------------------- 
class User { 
    private int userId; 
    private String name; 
    private String email; 
    private Plan activePlan; 
    private List<Content> watchlist; 
    private List<Content> history; 
 
    public User(int userId, String name, String email) { 
        this.userId = userId; 
        this.name = name; 
        this.email = email; 
        this.watchlist = new ArrayList<>(); 
        this.history = new ArrayList<>(); 
    } 
 
    public void subscribe(Plan plan) { 
        this.activePlan = plan; 
        System.out.println(name + " subscribed to " + plan.getName()); 
    } 
 
    public void addToWatchlist(Content content) { 
        watchlist.add(content); 
        System.out.println(content.getTitle() + " added to " + name + "'s watchlist."); 
    } 
 
    public void play(Content content) {  
 
        content.play(); 
        history.add(content); 
    } 
 
    public List<Content> getHistory() { return history; } 
    public Plan getActivePlan() { return activePlan; } 
    public String getName() { return name; } 
} 
 
// ---------------------- StreamingService ---------------------- 
class StreamingService { 
    private List<User> users; 
    private List<Plan> plans; 
    private List<Content> catalog; 
    private Map<Content, Integer> watchCount; 
 
    public StreamingService() { 
        users = new ArrayList<>(); 
        plans = new ArrayList<>(); 
        catalog = new ArrayList<>(); 
        watchCount = new HashMap<>(); 
    } 
 
    // Manage entities 
    public void addUser(User user) { users.add(user); } 
    public void addPlan(Plan plan) { plans.add(plan); } 
    public void addContent(Content content) { catalog.add(content); watchCount.put(content, 0); } 
 
    // Update watch count 
    public void recordWatch(Content content) { 
        watchCount.put(content, watchCount.get(content) + 1); 
    } 
 
    // Recommendation - Overloading 
    public List<Content> recommend() { 
        return catalog.subList(0, Math.min(3, catalog.size())); 
    } 
 
    public List<Content> recommend(String type) { 
        List<Content> result = new ArrayList<>(); 
        for(Content c : catalog) { 
            if(c.getType().equalsIgnoreCase(type)) result.add(c); 
        } 
        return result; 
    } 
 
    public List<Content> recommend(int minRating) { 
        List<Content> result = new ArrayList<>(); 
        for(Content c : catalog) { 
            if(c.getRating() >= minRating) result.add(c); 
        } 
        return result; 
    } 
 
    // Reports 
    public void printTopWatched() { 
        System.out.println("\n      Top Watched Content:"); 
        watchCount.entrySet().stream() 
                .sorted((a, b) -> b.getValue() - a.getValue()) 
                .forEach(e -> System.out.println(e.getKey().getTitle() + " → " + e.getValue() + " views")); 
    } 

 
 
    public void printRevenue() { 
        double revenue = 0; 
        for(User u : users) { 
            if(u.getActivePlan() != null) revenue += u.getActivePlan().getMonthlyPrice(); 
        } 
        System.out.println("\n   Total Monthly Revenue: $" + revenue); 
    } 
} 
 
// ---------------------- Main App ---------------------- 
public class StreamingAppMain { 
    public static void main(String[] args) { 
        StreamingService service = new StreamingService(); 
 
        // Plans 
        Plan basic = new Plan(1, "Basic", 8.99, 1, "SD"); 
        Plan premium = new Plan(2, "Premium", 15.99, 4, "4K"); 
        service.addPlan(basic); 
        service.addPlan(premium); 
 
        // Content 
        Movie m1 = new Movie(101, "Inception", 148, 5); 
        Series s1 = new Series(201, "Stranger Things", 25, 4); 
        Movie m2 = new Movie(102, "Interstellar", 169, 5); 
 
        service.addContent(m1); 
        service.addContent(s1); 
        service.addContent(m2); 
 
        // Users 
        User u1 = new User(1, "Alice", "alice@mail.com"); 
        User u2 = new User(2, "Bob", "bob@mail.com"); 
 
        service.addUser(u1); 
        service.addUser(u2); 
 
        // Subscriptions 
        u1.subscribe(premium); 
        u2.subscribe(basic); 
 
        // Watchlist & Play 
        u1.addToWatchlist(m1); 
        u1.play(m1); service.recordWatch(m1); 
 
        u2.addToWatchlist(s1); 
        u2.play(s1); service.recordWatch(s1); 
        u2.play(m2); service.recordWatch(m2); 
 
        // Reports 
        service.printTopWatched(); 
        service.printRevenue(); 
 
        // Recommendations 
        System.out.println("\n       Recommended Movies:"); 
        for(Content c : service.recommend("Movie")) { 
            System.out.println(" - " + c.getTitle()); 
        } 
    } 
}
