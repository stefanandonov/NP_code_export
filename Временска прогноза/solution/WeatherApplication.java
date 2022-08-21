import java.util.*;

public class WeatherApplication {

    public static void main(String[] args) {
        WeatherDispatcher weatherDispatcher = new WeatherDispatcher();

        CurrentConditionsDisplay currentConditions = new CurrentConditionsDisplay(weatherDispatcher);
        ForecastDisplay forecastDisplay = new ForecastDisplay(weatherDispatcher);

        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            System.out.println();
            String line = scanner.nextLine();
            String[] parts = line.split("\\s+");
            weatherDispatcher.setMeasurements(Float.parseFloat(parts[0]), Float.parseFloat(parts[1]), Float.parseFloat(parts[2]));
            if(parts.length > 3) {
                int operation = Integer.parseInt(parts[3]);
                if(operation==1) {
                    weatherDispatcher.remove(forecastDisplay);
                }
                if(operation==2) {
                    weatherDispatcher.remove(currentConditions);
                }
                if(operation==3) {
                    weatherDispatcher.register(forecastDisplay);
                }
                if(operation==4) {
                    weatherDispatcher.register(currentConditions);
                }

            }
        }
    }
}

interface Updatable {
    public void update(float temp, float humidity, float pressure);
}

interface Subject {
    void register(Updatable o);
    void remove(Updatable o);
    void notifyUpdatable();
}

interface Displayable {
    void display();
}

class CurrentConditionsDisplay implements Updatable, Displayable {
    private float temperature;
    private float humidity;
    private Subject weatherStation;

    public CurrentConditionsDisplay(Subject weatherStation) {
        this.weatherStation = weatherStation;
        weatherStation.register(this);
    }

    public void update(float temperature, float humidity, float pressure) {
        this.temperature = temperature;
        this.humidity = humidity;
        display();
    }

    public void display() {
        System.out.println("Temperature: " + temperature + "F");
        System.out.println("Humidity: " + humidity + "%");
    }
}

class ForecastDisplay implements Updatable, Displayable {
    private float currentPressure = 0.0f;
    private float lastPressure;
    private WeatherDispatcher weatherDispatcher;

    public ForecastDisplay(WeatherDispatcher weatherDispatcher) {
        this.weatherDispatcher = weatherDispatcher;
        weatherDispatcher.register(this);
    }

    public void update(float temp, float humidity, float pressure) {
        lastPressure = currentPressure;
        currentPressure = pressure;
        display();
    }

    public void display() {
        System.out.print("Forecast: ");
        if (currentPressure > lastPressure) {
            System.out.println("Improving");
        } else if (currentPressure == lastPressure) {
            System.out.println("Same");
        } else if (currentPressure < lastPressure) {
            System.out.println("Cooler");
        }
    }
}


class WeatherDispatcher implements Subject {
    private Set<Updatable> updatables;
    private float temperature;
    private float humidity;
    private float pressure;

    public WeatherDispatcher() {
        updatables = new HashSet<>();
    }

    public void register(Updatable o) {
        updatables.add(o);
    }

    public void remove(Updatable o) {
        updatables.remove(o);
    }

    public void notifyUpdatable() {
        for (Updatable updatable : updatables) {
            updatable.update(temperature, humidity, pressure);
        }
    }

    public void measurementsChanged() {
        notifyUpdatable();
    }

    public void setMeasurements(float temperature, float humidity, float pressure) {
        this.temperature = temperature;
        this.humidity = humidity;
        this.pressure = pressure;
        measurementsChanged();
    }

    public float getTemperature() {
        return temperature;
    }

    public float getHumidity() {
        return humidity;
    }

    public float getPressure() {
        return pressure;
    }

}