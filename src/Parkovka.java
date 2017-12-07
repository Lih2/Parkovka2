
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.HashSet;

public class Parkovka {

    public static void main(String[] args) {


        Parkovka parkovkatest = new Parkovka(5,2, new Tarif(7,22,4,2));

        ////// Test Variant 1
        Car car1=new Car("AAA");
        Car car2=new Car("BBB");
        Car car3=new Car("CCC");
        Car car4=new Car("DDD");

        Truck truck1=new Truck("EEE");
        Truck truck2=new Truck("GGG");

        parkovkatest.park(car1,5);
        parkovkatest.park(car2,6);
        parkovkatest.park(car3,12);
        parkovkatest.park(car4,22);

        parkovkatest.park(truck1,6);
        parkovkatest.park(truck2,10);


        try {
            float price = parkovkatest.unpark("AAA", 55);
            System.out.println("AAA " + "Цена " + price);
            price = parkovkatest.unpark("BBB", 8);
            System.out.println("BBB " + "Цена " + price);
            price = parkovkatest.unpark("CCC", 22);
            System.out.println("CCC " + "Цена " + price);
            price = parkovkatest.unpark("DDD", 52);
            System.out.println("DDD " + "Цена " + price);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private int maxnumberCars;
    private int maxnumberTrucks;

    private HashMap<String, Integer> carPlaces = new HashMap<>();
    private HashMap<String, Integer> truckPlaces = new HashMap<>();

    private Tarif tarif;

    public Parkovka(int maxnumberCars, int maxnumberTrucks, Tarif tarif ) {
        this.maxnumberCars=maxnumberCars;
        this.maxnumberTrucks=maxnumberTrucks;
        this.tarif=tarif;
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
    public synchronized int unpark(String carnumber, int hoursFinish ) throws Exception {

        Integer hoursBegin = carPlaces.get(carnumber);
        if( hoursBegin == null  )
            hoursBegin = truckPlaces.get(carnumber);
        if( hoursBegin == null ) throw new Exception();

        carPlaces.remove(carnumber);
        truckPlaces.remove(carnumber);

        return tarif.getTotalCost(hoursBegin, hoursFinish);
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

    public static class Tarif {

        private int costDay,costNight;  // стоимость дневного и ночного тарифов
        private int timeNight1, timeNight2; // время перехода тарифов , допустим 7 и 22

        public Tarif(int timeNight1, int timeNight2, int costDay, int costNight) {
            this.costDay=costDay;
            this.costNight=costNight;
            this.timeNight1=timeNight1;
            this.timeNight2=timeNight2;
        }

        public int getTotalCost(int hoursBegin, int hoursFinish) throws Exception { // Расширенный метод под несколько суток

            if(hoursFinish < hoursBegin) throw new Exception();
            if( hoursBegin == hoursFinish) return 0;

            int daysBegin=(int)(hoursBegin/24);
            int daysFinish=(int)(hoursFinish/24);
            int daysDiff = daysFinish - daysBegin; // сколько неполных суток

            int sum=0;

            if( daysDiff == 0 ) { // одни сутки
                sum=getCostBetween(hoursBegin,hoursFinish );
            }
            else  if( daysDiff > 0 ) { // несколько суток
                int restBegin=hoursBegin%24;
                sum=sum+getCostBetween(restBegin,24 );
                int restFinish=hoursFinish%24;
                sum=sum+getCostBetween(0,restFinish );
                sum=sum+getCostBetween(0,24 )*(daysDiff-1);
            }

            return sum;
        }

        public int getCostBetween(int hoursBegin, int hoursFinish ) throws Exception { // Метод для одних суток
            if(hoursFinish < hoursBegin) throw new Exception();
            return getCostForHour(hoursFinish) - getCostForHour(hoursBegin);
        }
        private int getCostForHour(int hour) { // Стоимость выезда с начала суток
            if( hour <= timeNight1 )
               return hour*costNight;
            else if( hour > timeNight1 && hour <= timeNight2 )
                return timeNight1*costNight + (hour-timeNight1)*costDay;
            else
                return timeNight1*costNight + (timeNight2-timeNight1)*costDay + (hour-timeNight2)*costNight;
        }

    }



}

