
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.HashSet;

public class Parkovka {

    public static void main(String[] args) {

        Parkovka parkovkatest = new Parkovka(5,2);

        ////// Test Variant 1
        for(int i=0;i<5;i++)
            parkovkatest.park(new Car(String.valueOf(i)),i*2);

        for(int i=5;i<7;i++)
            parkovkatest.park(new Truck(String.valueOf(i)),i*2);

        for(int i=1000; i<1007; i++)
            parkovkatest.unpark(String.valueOf(i) , i);

    }

    private int maxnumberCars;
    private int maxnumberTrucks;

    private HashMap<String, Integer> carPlaces = new HashMap<>();
    private HashMap<String, Integer> truckPlaces = new HashMap<>();

    public Parkovka(int maxnumberCars, int maxnumberTrucks ) {
        this.maxnumberCars=maxnumberCars;
        this.maxnumberTrucks=maxnumberTrucks;
    }

    public synchronized boolean park(Transport transport, int startHours) {

        String carnumber = transport.getNumber();

        if( transport instanceof Truck ) { // грузовой автомобиль
            if( truckPlaces.size() ==  maxnumberTrucks ) return false; // мест нет
            truckPlaces.put(carnumber,startHours);
            return true;
        }
        else if( transport instanceof Car  ) { // легковой автомобиль

            if( carPlaces.size() <  maxnumberCars ) { // автомобильные места есть
                carPlaces.put(carnumber,startHours);
                return true;
            }
            if( truckPlaces.size() ==  maxnumberTrucks ) return false; // грузовых мест нет

            // проверяем на всякий случай не парковали ли машину раннее
            if( carPlaces.keySet().contains(carnumber) || truckPlaces.keySet().contains(carnumber) ) return false;
            truckPlaces.put(carnumber,startHours);
            return false;
        }

        return false;

    }
    public synchronized int unpark(String carnumber, int hoursFinish ) {

        Integer hoursBegin = carPlaces.get(carnumber);
        if( hoursBegin == null  )
            hoursBegin = truckPlaces.get(carnumber);

        if( hoursBegin == null ) return -100;

        carPlaces.remove(carnumber);
        truckPlaces.remove(carnumber);

        return hoursFinish-hoursBegin;
    }


    public static class Transport {
        private String number;
        public Transport() {
            this.number=null;
        }
        public Transport(String number) {
            this.number=number;
        }
        public String getNumber() {
            return number;
        }
    }
    public static class Car extends Transport {
        public Car(String number) { super(number); }
    }
    public static class Truck extends Transport {
        public Truck(String number) { super(number);}
    }




}

