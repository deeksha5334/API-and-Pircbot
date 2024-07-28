public static void main(String[] args) throws ParseException {
    Scanner scn = new Scanner(System.in);

    while (true) {
        System.out.println("=".repeat(80));
        System.out.println("STOCK MARKET ANALYSER");
        System.out.println("=".repeat(80));
        System.out.println("Services: ");
        System.out.println("1. Search Stocks");
        System.out.println("2. Get Stock Quote");
        System.out.println("3. Get Stock History (Last 100 Days)");
        System.out.println("4. Evaluate a stock (display recent stock data and also evaluate)");
        System.out.println("5. Convert currency");
        System.out.println("6. Exit");

        System.out.print("Choose an option: ");
        String userChoice = scn.nextLine();

        switch (userChoice) {
            case "1":
                System.out.print("Enter a symbol or keywords to search for stocks: ");
                String symbol = scn.nextLine();
                displayStock(symbol);
                break;
            case "2":
                System.out.print("Enter a stock symbol to get quote: ");
                symbol = scn.nextLine();
                displayQuote(symbol);
                break;
            case "3":
                System.out.print("Enter a stock symbol to get history: ");
                symbol = scn.nextLine();
                displayHistory(symbol);
                break;
//                case "4":
//                    System.out.print("Enter stock symbol: ");
//                    symbol = scn.nextLine();
//                    System.out.println(evaluateStock(symbol));
//                    break;
            case "5":
                System.out.print("Enter amount: ");
                double amount = Double.parseDouble(scn.nextLine());
                System.out.print("From currency (e.g., USD): ");
                String fromCurrency = scn.nextLine().toUpperCase();
                System.out.print("To currency (e.g., EUR): ");
                String toCurrency = scn.nextLine().toUpperCase();
                System.out.println(amount + " " + fromCurrency + " = " + currencyConversion(amount, fromCurrency, toCurrency) + " " + toCurrency);
                break;
            case "6":
                System.out.println("Thank you for using the Stock Market Analyser.");
                scn.close();
                return;
            default:
                System.out.println("This choice is invalid. Please try again.");
        }
    }
}
}
