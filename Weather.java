package org.example;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class Main
{
    public static final String BASE_LOCATION_URL = "https://geocoding-api.open-meteo.com/v1/search?name=";
    public static final String BASE_WEATHER_URL = "https://api.open-meteo.com/v1/forecast?latitude=";
 
    private static JSONObject getLocationData(String city)
    {
        //city = city.replaceAll(" ", "+");

        String URL = BASE_LOCATION_URL + city + "&count=1&language=en&format=json";

        try
        {
           //Creating a HttpURLConnection Object to establish connection for Https requests
            HttpURLConnection apiConnection = fetchResponse(URL);

           //HTTP Code 200 suggests successful Connection
            if (apiConnection.getResponseCode() != 200)
            {
                System.out.println("Error: Could not connect to API");
                return null;
            }

            //Reading response and convert to String
            String jsonResponse = readApiResponse(apiConnection);

            //Parsing the data into a JSON Object
            JSONParser parser = new JSONParser();
            JSONObject resultsJsonObj = (JSONObject) parser.parse(jsonResponse);

            //Retrieving the data for location
            JSONArray locationData = (JSONArray) resultsJsonObj.get("results");
            if (locationData != null)
            {
                return (JSONObject) locationData.get(0);
            }

            else
            {
                System.out.println("No data found");
            }

        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }

    private static void displayWeatherData(double latitude, double longitude){
        try{
            // 1. Fetch the API response based on API Link
            String url = BASE_WEATHER_URL + latitude + "&longitude=" + longitude + "&current=temperature_2m,relative_humidity_2m,wind_speed_10m";
            HttpURLConnection apiConnection = fetchResponse(url);

            // check for response status
            // 200 - means that the connection was a success
            if(apiConnection.getResponseCode() != 200){
                System.out.println("Error: Could not connect to API");
                return;
            }

            // 2. Read the response and convert store String type
            String jsonResponse = readApiResponse(apiConnection);

            // 3. Parse the string into a JSON Object
            JSONParser parser = new JSONParser();
            JSONObject jsonObject = (JSONObject) parser.parse(jsonResponse);
            JSONObject currentWeatherJson = (JSONObject) jsonObject.get("current");
//            System.out.println(currentWeatherJson.toJSONString());

            // 4. Store the data into their corresponding data type
            String time = (String) currentWeatherJson.get("time");
            System.out.println("Current Time: " + time);
            double temperature = (double) currentWeatherJson.get("temperature_2m");
            System.out.println("Current Temperature (Celsius): " + temperature);

            long relativeHumidity = (long) currentWeatherJson.get("relative_humidity_2m");
            System.out.println("Relative Humidity: " + relativeHumidity);

            double windSpeed = (double) currentWeatherJson.get("wind_speed_10m");
            System.out.println("Weather Description: " + windSpeed);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    private static String readApiResponse(HttpURLConnection apiConnection) {
        try
        {
            // Create a StringBuilder to store the resulting JSON data
            StringBuilder resultJson = new StringBuilder();

            // Create a Scanner to read from the InputStream of the HttpURLConnection
            Scanner scanner = new Scanner(apiConnection.getInputStream());

            // Loop through each line in the response and append it to the StringBuilder
            while (scanner.hasNext()) {
                // Read and append the current line to the StringBuilder
                resultJson.append(scanner.nextLine());
            }

            // Close the Scanner to release resources associated with it
            scanner.close();

            // Return the JSON data as a String
            return resultJson.toString();

        }
        catch (IOException e)
        {
            // Print the exception details in case of an IOException
            e.printStackTrace();
        }

        // Return null if there was an issue reading the response
        return null;
    }

    private static double convertToFarenheit(double temperature)
    {
        temperature = temperature * 1.8 + 32;
        return temperature;
    }

    private static HttpURLConnection fetchResponse(String urlString)
    {
        try
        {
            // attempt to create connection
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            // set request method to get
            conn.setRequestMethod("GET");

            return conn;
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }

        // could not make connection
        return null;
    }
}
