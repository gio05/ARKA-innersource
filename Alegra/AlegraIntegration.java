import java.net.*;
import java.util.*;
import java.lang.*;
import java.io.*;
import java.time.*;
import java.time.format.*;

public class AlegraIntegration {

    private int firstProductId;
    private int lastProductId;
    private String ALEGRA_TOKEN;

    public AlegraIntegration( HashMap<String, String> alegraParams ){
        this.firstProductId = alegraParams.get( "firstProductId" ) != null && Integer.parseInt( alegraParams.get( "firstProductId" ) ) > 0 ? Integer.parseInt( alegraParams.get( "firstProductId" ) ) : 1;
        this.lastProductId = alegraParams.get( "lastProductId" ) != null && Integer.parseInt( alegraParams.get( "lastProductId" ) ) > 0 ? Integer.parseInt( alegraParams.get( "lastProductId" ) ) : 1;
        this.lastProductId = Math.max( this.firstProductId, this.lastProductId );
        this.ALEGRA_TOKEN = alegraParams.get( "token" );
    }

    /**
        Retrieves the information of a product present in Alegra,
        makes a HTTP GET call to Alegra API.
        @param productId the id of the product to be consulted
        @return A String with the information of the product
    */
    private String getProduct( int productId ) throws Exception {

        /**
            Create request object
        */
        URL url = new URL("https://api.alegra.com/api/v1/items/" + productId );
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");

        /**
            Setting Request Headers
         */
        con.setRequestProperty("Accept", "application/json");
        con.setRequestProperty("Content-Type", "application/json");
        con.setRequestProperty("Authorization", "Basic " + this.ALEGRA_TOKEN );

        /**
            Reading the Response
        */
        int status = con.getResponseCode();
        BufferedReader in = new BufferedReader( new InputStreamReader( con.getInputStream() ) );
        String inputLine;
        StringBuffer content = new StringBuffer();
        while ( ( inputLine = in.readLine() ) != null ) {
            content.append( inputLine );
        }

        /**
            Close connection
        */
        in.close();

        return new String( content );
    }

    /**
        Retrieves the information of a list of product present in Alegra.
        @return An array with the information of the products
    */
    private ArrayList<String> getAllProducts() throws Exception {
         if( this.ALEGRA_TOKEN == null ) {
            System.out.println( "Token is required" );
            return null;
        }

        ArrayList<String> products = new ArrayList<>();
        products.add( "[" );

        boolean first = true;
        for( int i = this.firstProductId; i <= this.lastProductId; i++ )
        {
            if( first )
                first = false;
            else
                products.add( "," );

            products.add( getProduct( i ) );
            
            System.out.println( "Completed: " + ( i - this.firstProductId ) + " of " + ( this.lastProductId - this.firstProductId ) );
        }

        products.add( "]" );

        return products;

    }

    /**
        Stores the product information into a JSON file
    */
    public void saveProductToFile( String product ) throws IOException {
        LocalDateTime localDateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        BufferedWriter writer = new BufferedWriter( new FileWriter( localDateTime.format( formatter ) + ".json" ) );
        writer.write( product );
        writer.close();
    }

    /**
        Stores the products information into a JSON file
    */
    public void saveProductsToFile( ArrayList<String> products ) throws IOException {
        LocalDateTime localDateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        BufferedWriter writer = new BufferedWriter( new FileWriter( localDateTime.format( formatter ) + ".json" ) );
        for( String product: products )
            writer.write( product );

        writer.close();
    }

    public static void main( String... args ) throws Exception {

        AlegraIntegration alegraIntegration = new AlegraIntegration( ParametersBuilder.getParametersMap( args ) );
        alegraIntegration.saveProductsToFile( alegraIntegration.getAllProducts() );
    }
}

class ParametersBuilder {
    public static HashMap<String, String> getParametersMap( String... args ){
        HashMap<String, String> params = new HashMap<>();
        for( int i = 0; i < args.length - 1 ; i = i + 2 ) {
            params.put( args[ i ].substring( 2 ), args[ i + 1 ] );
        }

        return params;
    }
}

