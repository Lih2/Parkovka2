
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashSet;

public class Parkovka {

    public static void main(String[] args) {

        Parkovka parkovkatest = new Parkovka(5,2);

        ////// Test Variant 1
        for(int i=0;i<5;i++)
            parkovkatest.park(String.valueOf(i),1);

        for(int i=5;i<7;i++)
            parkovkatest.park(String.valueOf(i),2);

        for(int i=0; i<7; i++)
            parkovkatest.unpark(String.valueOf(i));

        ////// Test Variant 2
        for(int i=0;i<9;i++)
            parkovkatest.park(String.valueOf(i),1);

        for(int i=0; i<7; i++)
            parkovkatest.unpark(String.valueOf(i));


        ////// Test Variant 3
        for(int i=0;i<7;i++)
            parkovkatest.park(String.valueOf(i),2);

        for(int i=0; i<7; i++)
            parkovkatest.unpark(String.valueOf(i));



    }

    private int maxnumberCars;
    private int maxnumberTrucks;

    private HashSet<String> carPlaces = new HashSet<>(); // список легковых мест
    private HashSet<String> truckPlaces = new HashSet<>(); // список грузовых мест

    public Parkovka(int maxnumberCars, int maxnumberTrucks ) {
        this.maxnumberCars=maxnumberCars;
        this.maxnumberTrucks=maxnumberTrucks;
    }

    public synchronized boolean park(String carnumber, int type) {

        if( type == 2 ) { // грузовой автомобиль
            if( truckPlaces.size() ==  maxnumberTrucks ) return false; // мест нет
            truckPlaces.add(carnumber);
            return true;
        }
        else if( type == 1 ) { // легковой автомобиль

            if( carPlaces.size() <  maxnumberCars ) { // автомобильные места есть
                carPlaces.add(carnumber);
                return true;
            }
            if( truckPlaces.size() ==  maxnumberTrucks ) return false; // грузовых мест нет

            // проверяем на всякий случай не парковали ли машину раннее
            if( carPlaces.contains(carnumber) || truckPlaces.contains(carnumber) ) return false;
            truckPlaces.add(carnumber);
            return false;

        }

        return false;

    }
    public synchronized void unpark(String carnumber) {
        carPlaces.remove(carnumber);
        truckPlaces.remove(carnumber);
    }
}

