package mike706574;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.UncheckedIOException;
import java.io.Writer;

import java.net.MalformedURLException;
import java.net.URL;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import java.util.Arrays;
import java.util.List;

import java.util.stream.Collectors;
import java.util.stream.Stream;

public class IO {
    public static String slurp( String path ) {
        try( InputStream is = new URL( path ).openConnection().getInputStream() ) {
            return slurp( is );
        }
        catch( MalformedURLException mue ) {
            try {
                return new String( Files.readAllBytes( Paths.get( path ) ), "UTF-8" );
            }
            catch( IOException ex ) {
                throw new UncheckedIOException( ex );
            }
        }
        catch( IOException ex ) {
            throw new UncheckedIOException( ex );
        }
    }

    public static String slurp( InputStream is ) {
        try( Reader isReader = new InputStreamReader( is, "UTF-8" );
             Reader reader = new BufferedReader( isReader ) ) {
            StringBuilder stringBuilder = new StringBuilder();
            int c = 0;
            while( ( c = reader.read() ) != -1 ) {
                stringBuilder.append( (char)c );
            }

            return stringBuilder.toString();
        }
        catch( IOException ex ) {
            throw new UncheckedIOException( ex );
        }
    }

    public static void nukeDirectory( String path ) {
        File dir = new File( path );
        for( String filePath : dir.list() ) {
            new File( dir.getPath(), filePath ).delete();
        }
        dir.delete();
    }

    public static Stream<String> streamLines( String path ) {
        try {
            return Files.lines( Paths.get( path ) );
        }
        catch( IOException ex ) {
            throw new UncheckedIOException( ex );
        }
    }

    // Everything in memory
    public static List<List<String>> slurpHeadlessDelimited( String path,
                                                       String delimiter ) {

        try( Stream<String> lines = Files.lines( Paths.get( path ) ) ) {
            return lines
                .map( line -> Arrays.asList( line.split( delimiter ) ) )
                .collect( Collectors.toList() );
        }
        catch( IOException ex ) {
            throw new UncheckedIOException( ex );
        }
    }

    public static void spitHeadlessDelimited( String path,
                                              String delimiter,
                                              List<List<String>> rows ) {
        try( Writer out = new PrintWriter( path ) ) {
            String content = rows.stream()
                .map( row -> String.join( "|", row ) )
                .collect( Collectors.joining( "\n" ) );
            out.write( content );
        }
        catch( IOException ex ) {
            throw new UncheckedIOException( ex );
        }
    }
}
