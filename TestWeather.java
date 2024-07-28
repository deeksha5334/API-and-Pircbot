
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

import static org.example.Main.displayWeatherData;
import static org.example.Main.getLocationData;

public class TestWeather
{

    public static void main(String[] args)
    {
        try
        {
            Scanner scanner = new Scanner(System.in);
            String city;
            do
            {
                // Retrieve user input
                System.out.println("===================================================");
                System.out.print("Enter City (Space to exit): ");
                city = scanner.nextLine();

                //Adding error handling
                if (city.equals(" "))
                {
                    break;
                }

                // Get location data
                JSONObject cityLocationData = (JSONObject) getLocationData(city);
                if (cityLocationData != null)
                {
                    double latitude = (double) cityLocationData.get("latitude");
                    double longitude = (double) cityLocationData.get("longitude");
                    displayWeatherData(latitude, longitude);
                }
                else
                {
                    System.out.println("City not found");
                }



            } while(!city.equalsIgnoreCase("No"));
        }
          
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
}
