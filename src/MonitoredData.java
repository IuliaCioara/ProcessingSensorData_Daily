import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class MonitoredData {
    String startTime;
    String endTime;
    String activityLabel;

    public MonitoredData(String s, String e, String a){
        this.startTime = s;
        this.endTime = e;
        this.activityLabel = a.replaceAll("\\p{Z}", "");
    }

    public String getStart(){
        return startTime;
    }

    public String getEnd(){
        return endTime;
    }

    public String getLabel(){

        return activityLabel;
    }

    public int getStartDay(){
        return Integer.parseInt(startTime.substring(8, 10));
    }

    public Long getDuration(){
        DateTimeFormatter f = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime date1 = LocalDateTime.from(f.parse(startTime));
        LocalDateTime date2 = LocalDateTime.from(f.parse(endTime));
        Duration d = Duration.between(date1, date2);

        return d.getSeconds()/60;


    }

    public Long getTimp(){
        return getDuration();
    }
}
