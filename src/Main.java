import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Main {
    public Main(){
    }

    public static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
        Map<Object,Boolean> seen = new ConcurrentHashMap<>();
        return t -> seen.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
    }

    public List<MonitoredData> readData(String file){
        List<MonitoredData> date = new ArrayList<MonitoredData>();
        List<String> buffer = null;

        try (Stream<String> stream = Files.lines(Paths.get(file))){
            buffer = stream.collect(Collectors.toList());
        }catch (IOException e){
            e.printStackTrace();
        }

        for (String i : buffer){
            String[] parts = i.split(" ");
            String[] parts1 = parts[1].split("		");
            String[] parts2 = parts[2].split("		");
            date.add(new MonitoredData(parts[0]+" " + parts1[0],parts1[1]+ " " + parts2[0],parts2[1]));
        }

        return date;
    }

    public long distinctDays(List<MonitoredData> d){
        long dist = d.stream()
                .filter(distinctByKey(p->p.getStart().substring(0,10)))
                .count();

        System.out.print("TASK_2->Numarul de zile diferite:");
        return dist;
    }

    public Map<String,Long> diffOcc(List<MonitoredData> d){
        final Map<String,Long> map = d.stream().collect(Collectors.groupingBy(MonitoredData::getLabel,Collectors.counting()));
        try{
            Files.write(Paths.get("TASK_3.txt"),()->map
                    .entrySet()
                    .stream().<CharSequence>map(rez->rez.getValue() + " " + rez.getKey()).iterator());
        }catch(IOException e){
            e.printStackTrace();
        }
        return map;

    }

    public Map<Integer, Map<String, Long>> diffDay(List<MonitoredData> d){
        Map<Integer, Map<String,Long>> rez = d.stream().collect(Collectors.groupingBy(MonitoredData::getStartDay,Collectors.groupingBy(MonitoredData::getLabel,Collectors.counting())));
        try{
            Files.write(Paths.get("TASK_4.txt"),()->rez.entrySet()
                    .stream().<CharSequence>map(g->g.getValue() + " "  +  "\n\n\n\n\n").iterator());
        }catch(IOException e){
            e.printStackTrace();
        }


        return rez;

    }

    public Map<String,Long> totalDuration(List<MonitoredData> d){
        final Map<String, Long> rez = d.stream()
                .collect(Collectors.groupingBy(MonitoredData::getLabel,Collectors.summingLong(MonitoredData::getDuration)));


        try{
            Files.write(Paths.get("TASK_5.txt"),()->rez.entrySet()
                    .stream()
                    .filter(x -> x.getValue() > 600)
                    .<CharSequence>map(g->g.getValue() + " "  +  "\n\n\n\n\n" + g.getKey()).iterator());
        }catch(IOException e){
            e.printStackTrace();
        }


        return rez;
    }

    public List<String> filtruActiv(List<MonitoredData> d){
        List<String> fin = new ArrayList<String>();
        final Map<String,Long> rez1 = d.stream()
                .collect(Collectors.groupingBy(MonitoredData::getLabel,Collectors.counting()));

        final Map<String,Long> rez = d.stream()
                .filter(x->x.getDuration() < 5)
                .collect(Collectors.groupingBy(MonitoredData::getLabel,Collectors.counting()));


        for (Map.Entry<String, Long> x : rez.entrySet()){
            double tmp = (double)((double)rez1.get(x.getKey())/(double)rez.get(x.getKey()));

            if(tmp == 1){
                fin.add(x.getKey());
            }

        }

        try{
            Files.write(Paths.get("TASK_6.txt"),()->fin
                    .stream().<CharSequence>map(g->g.toString()).iterator());
        }catch(IOException e){
            e.printStackTrace();

        }
        return fin;


    }


    public static void main(String args[]){
        String fileName = "Activities.txt";
        Main m = new Main();
        List<MonitoredData> data = m.readData(fileName);
        m.diffOcc(data);
        long rez = m.distinctDays(data);
        System.out.println(rez);
        m.diffDay(data);
        m.totalDuration(data);
        m.filtruActiv(data);
    }
}