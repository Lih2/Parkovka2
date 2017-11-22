
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashSet;

public class Parkovka {

    public static void main(String[] args) {

        Parkovka parkovkatest = new Parkovka(5);
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        try {

            while(true) {
                String line=reader.readLine();
                if( line.equals("exit")) break;

                String[] actions;
                actions=line.split(":");

                if( actions[0].equals("add") ) {
                    if (parkovkatest.park(actions[1])) {
                        System.out.println("Машина с номером " + actions[1] + " Припаркована");
                        System.out.println("Всего Машин на парковке " + parkovkatest.numcars());
                    } else {
                        System.out.println("Машину с номером " + actions[1] + " Припарковать не удалось - нет мест");
                        System.out.println("Всего Машин на парковке " + parkovkatest.numcars());
                    }
                }
                else if( actions[0].equals("del") ) {
                    parkovkatest.unpark(actions[1]);
                    System.out.println("Машина с номером " + actions[1] + " Выехала с парковки");
                    System.out.println("Всего Машин на парковке " + parkovkatest.numcars());
                }
            }
        }
        catch(IOException e) {

        }



    }

    private int maxnumber; // максимальное количество мест на парковке
    private HashSet<String> cars = new HashSet<>(); // список машин

    public Parkovka(int maxnumber) {
        this.maxnumber=maxnumber;
    }

    public synchronized int numcars() {
        return cars.size();
    }
    public synchronized boolean park(String carnumber) {

        if( cars.size() == maxnumber ) return false; // месm на парковке больше нет
        cars.add(carnumber);
        return true;
    }
    public synchronized void unpark(String carnumber) {
        cars.remove(carnumber);
    }
}
