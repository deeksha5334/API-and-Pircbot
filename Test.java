import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;



public class Stock
{
    public static final String ALPHA_VANTAGE_API_KEY = "W0JP1VNQYSBPZFX4";
    public static final String ALPHA_VANTAGE_BASE_URL = "https://www.alphavantage.co/query";
    private static final String EXCHANGE_RATE_API_KEY = "66bbe3895a8ece0cb430aca9";

    private static HttpURLConnection fetchAPIResponse(String url_string)
    {
        try
        {
            URL url = new URL(url_string);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            return conn;
        }
        catch (IOException e)
        {
            //Prints out the error to the console
            e.printStackTrace();
        }

        System.out.println("Could not connect");
        return null; //Could not connect
    }

    private static String readAPIresponse(HttpURLConnection conn)
    {
        try
        {
            StringBuilder resultJson = new StringBuilder();
            Scanner scn = new Scanner(conn.getInputStream());

            while (scn.hasNext())
            {
                resultJson.append(scn.nextLine());
            }

            scn.close();
            return resultJson.toString();
        }

        catch (IOException e)
        {
            e.printStackTrace();
        }

        return null;
    }

    private static JSONArray searchStock(String symbol)
    {
        //Defining the function parameter for the API request
        String function = "SYMBOL_SEARCH";

        //Construct the URL with necessary parameters
        //Alpha Vantage offers a base URL and
        String params = String.format("function=%s&keywords=%s&apikey=%s", function, symbol, ALPHA_VANTAGE_API_KEY);
        String URL = ALPHA_VANTAGE_BASE_URL + "?" + params;
        try
        {
            //Create a URL Object from the URL String
            HttpURLConnection conn = (HttpURLConnection) fetchAPIResponse(URL);
            //Reference - HTTP URL Codes
            if (conn.getResponseCode() == 200)
            {
                InputStream in = conn.getInputStream();
                Scanner scn = new Scanner(in);
                StringBuilder jsonResult = new StringBuilder();

                while (scn.hasNext())
                {
                    jsonResult.append(scn.nextLine());
                }

                scn.close();

                JSONParser parser = new JSONParser();
                JSONObject responseObject = (JSONObject) parser.parse(jsonResult.toString());

                //Returning the JSON Object
                return (JSONArray) responseObject.get("bestMatches");
            }

            else
            {
                System.out.println("Could not connect");
            }
        }
        catch (IOException | ParseException e)
        {
            e.printStackTrace();
        }

        return null;
    }

    private static void displayStock(String symbol) throws ParseException {
        JSONArray stocks = searchStock(symbol);

        //Printing error message if data not found
        if (stocks == null || stocks.isEmpty())
        {
            System.out.println("No stocks found");
            return;
        }

        for (int i = 0; i < stocks.size(); i++)
        {
            JSONObject stock = (JSONObject) stocks.get(i);
            System.out.println("-".repeat(80));
            System.out.println("STOCK INFORMATION");
            System.out.println("-".repeat(80));
            System.out.println("Symbol: " + stock.get("1. symbol"));
            System.out.println("Name: " + stock.get("2. name"));
            System.out.println("Type: " + stock.get("3. type"));
            System.out.println("Region: " + stock.get("4. region"));
            System.out.println("Currency: " + stock.get("8. currency"));
            System.out.println("-".repeat(80));
        }
    }

    private static JSONArray getStockQuote(String symbol)
    {
        String function = "GLOBAL_QUOTE";
        String params = String.format("function=%s&symbol=%s&apikey=%s", function, symbol, ALPHA_VANTAGE_API_KEY);
        String URL = ALPHA_VANTAGE_BASE_URL + "?" + params;
        try
        {
            HttpURLConnection conn = (HttpURLConnection) fetchAPIResponse(URL);
            if (conn.getResponseCode() == 200)
            {
                InputStream in = conn.getInputStream();
                Scanner scn = new Scanner(in);
                StringBuilder jsonResult = new StringBuilder();

                while (scn.hasNext())
                {
                    jsonResult.append(scn.nextLine());
                }

                scn.close();
                JSONParser parser = new JSONParser();
                JSONObject responseObject = (JSONObject) parser.parse(jsonResult.toString());

                return (JSONArray) responseObject.get("Global Quote");
            }
            else
            {
                System.out.println("Could not connect");
            }
        }
        catch (IOException | ParseException e)
        {
            e.printStackTrace();
        }

        return null;
    }

    private static void displayQuote(String symbol) throws ParseException
    {
        JSONArray quote = getStockQuote(symbol);

        System.out.println(quote);
        //Printing error message if data not found
        if (quote == null || quote.isEmpty())
        {
            System.out.println("No stocks found");
            return;
        }

        for (int i = 0; i < quote.size(); i++)
        {
            JSONObject q = (JSONObject) quote.get(i);
            System.out.println("-".repeat(80));
            System.out.println("STOCK QUOTE");
            System.out.println("-".repeat(80));
            System.out.println("Stock Name: " + q.get("01. symbol"));
            System.out.println("Open: " + q.get("02. open"));
            System.out.println("High: " + q.get("03. high"));
            System.out.println("Low: " + q.get("04. low"));
            System.out.println("Price: " + q.get("05. price"));
            System.out.println("Volume: " + q.get("06. volume"));
            System.out.println("Latest: " + q.get("07. latest trading day"));
            System.out.println("Previous Close: " + q.get("08. previous close"));
            System.out.println("Change: " + q.get("09. change"));
            System.out.println("Change Percent: " + q.get("10. change percent"));
            System.out.println("-".repeat(80));

//            'Symbol': data.get('01. symbol'),
//                'Open': data.get('02. open'),
//                'High': data.get('03. high'),
//                'Low': data.get('04. low'),
//                'Price': data.get('05. price'),
//                'Volume': data.get('06. volume'),
//                'Latest Trading Day': data.get('07. latest trading day'),
//                'Previous Close': data.get('08. previous close'),
//                'Change': data.get('09. change'),
//                'Change Percent': data.get('10. change percent'),
        }


    }

    private static JSONArray getStockHistory(String symbol)
    {
        String function = "TIME_SERIES_DAILY";
        String params = String.format("function=%s&symbol=%s&apikey=%s", function, symbol, ALPHA_VANTAGE_API_KEY);
        String url = ALPHA_VANTAGE_BASE_URL + "?" + params;
        try
        {
            //Create a URL Object from the URL String
            HttpURLConnection conn = (HttpURLConnection) fetchAPIResponse(url);
            //Reference - HTTP URL Codes
            if (conn.getResponseCode() == 200)
            {
                InputStream in = conn.getInputStream();
                Scanner scn = new Scanner(in);
                StringBuilder jsonResult = new StringBuilder();

                while (scn.hasNext())
                {
                    jsonResult.append(scn.nextLine());
                }

                scn.close();

                JSONParser parser = new JSONParser();
                JSONObject responseObject = (JSONObject) parser.parse(jsonResult.toString());

                return (JSONArray) responseObject.get("Time Series (Daily)");
            }

            else
            {
                System.out.println("Could not connect");
            }
        }
        catch (IOException | ParseException e)
        {
            e.printStackTrace();
        }

        return null;
    }

    private static void displayHistory(String symbol)
    {
        JSONArray history = getStockHistory(symbol);
        System.out.println(history);

        //Printing error message if data not found
        if (history == null || history.isEmpty())
        {
            System.out.println("No stocks found");
            return;
        }

        for (int i = 0; i < history.size(); i++)
        {
            JSONObject q = (JSONObject) history.get(i);
            System.out.println("-".repeat(80));
            System.out.println("STOCK QUOTE");
            System.out.println("-".repeat(80));
            System.out.println("Stock Name: " + q.get("01. symbol"));
            System.out.println("Open: " + q.get("02. open"));
            System.out.println("High: " + q.get("03. high"));
            System.out.println("Low: " + q.get("04. low"));
            System.out.println("Price: " + q.get("05. price"));
            System.out.println("Volume: " + q.get("06. volume"));
            System.out.println("Latest: " + q.get("07. latest trading day"));
            System.out.println("Previous Close: " + q.get("08. previous close"));
            System.out.println("Change: " + q.get("09. change"));
            System.out.println("Change Percent: " + q.get("10. change percent"));
            System.out.println("-".repeat(80));
        }

    }

    private static double currencyConversion(double amount, String fromCurrency, String toCurrency)
    {
        String URL =  String.format("https://v6.exchangerate-api.com/v6/%s/pair/%s/%s", EXCHANGE_RATE_API_KEY, fromCurrency, toCurrency);
        try
        {
            //Create a URL Object from the URL String
            HttpURLConnection conn = (HttpURLConnection) fetchAPIResponse(URL);
            //Reference - HTTP URL Codes
            if (conn.getResponseCode() == 200)
            {
                InputStream in = conn.getInputStream();
                Scanner scn = new Scanner(in);
                StringBuilder jsonResult = new StringBuilder();

                while (scn.hasNext())
                {
                    jsonResult.append(scn.nextLine());
                }

                scn.close();

                JSONParser parser = new JSONParser();
                JSONObject responseObject = (JSONObject) parser.parse(jsonResult.toString());

                double rate = (double) responseObject.get("conversion_rate");
                System.out.println("Current Conversion rate: " + rate);
                return amount * rate;
            }

            else
            {
                System.out.println("Could not connect");
            }
        }
        catch (IOException | ParseException e)
        {
            e.printStackTrace();
        }

        return 0;

    }

    public static void main(String[] args) throws ParseException {
        String symbol = "AAPL";
        Scanner scn = new Scanner(System.in);
        JSONArray stockData = searchStock(symbol);
        // If bestMatches is not null, iterate through each match and print it
        if (stockData != null) {
            for (Object match : stockData) {
                JSONObject stock = (JSONObject) match;
                //System.out.println(stock);
            }
        }

        String to = "USD";
        String from = "INR";


        System.out.println("-".repeat(80));
        System.out.println("Enter the amount to convert: ");
        double amount = scn.nextDouble();

        System.out.println("Enter the From currency (e.g., USD): ");
        String fromCurrency = scn.next();
        fromCurrency = fromCurrency.toUpperCase();
        System.out.println("Enter the To currency (e.g., INR): ");
        String toCurrency = scn.next();
        toCurrency = toCurrency.toUpperCase();


        displayStock(symbol);
        displayQuote(symbol);
        double convertedAmount = currencyConversion(amount, fromCurrency, toCurrency);
        System.out.println("Converted Amount: " + convertedAmount);
    }

}