/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tools;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author alianos
 */
public class Log {
    private static final String PATH = "../";

    public static final String INFO = "info.csv";
    public static final String ANSWERS = "answers.csv";
    public static final String USERS = "userDetails.csv";
    public static final String SCORING = "scoring.csv";
    public static final String RAW_ATTR = "rawAttr.csv";
    public static final String ATTR_LOG = "attributeCleaning.csv";

    public static synchronized void log( String log, Object... info ) {
        BufferedWriter out = null;
        try {
            StringBuilder outString = new StringBuilder();
            out = getLog( log );
            start( out );

            int i = 0;
            for( Object infoPiece : info ) {
                if( infoPiece == null ) {
                    infoPiece = "null";
                }
                String infoString = safeString( infoPiece.toString() );
                i++;
                //if it is the last argument, close the line
                outString.append( q( infoString, i == info.length ) );
            }
            out.write( outString.toString() );

          
        } catch( Exception ex ) {
            Logger.getLogger( Log.class.getName() ).log( Level.SEVERE, null, ex );
        } finally {
            if( out != null ) {
                try {
                    out.close();
                } catch( IOException ex ) {
                    Logger.getLogger( Log.class.getName() ).log( Level.SEVERE, null, ex );
                }
            }
        }

    }

    public static String safeString( String s ) {
        String safe = s;
        safe = safe.replaceAll( "\"", "_INCH_" );
        safe = safe.replaceAll( "\n", "_NL_" );

        return safe;
    }

    /**
     * The buffer must be closed to release the file.
     *
     * @param log
     * @return
     * @throws IOException
     */
    private static BufferedWriter getLog( String log ) throws IOException {
        FileWriter fstream = new FileWriter( PATH + log, true );
        BufferedWriter out = new BufferedWriter( fstream );

        return out;
    }

    private static void start( BufferedWriter out ) throws IOException {
        DateFormat dateFormat = new SimpleDateFormat( "yyyy/MM/dd HH:mm:ss:SS" );
        Date date = new Date();
        out.write( q( dateFormat.format( date ) ) );
        out.write( q( safeString( Thread.currentThread().getName() ) ) );
    }

    /**
     * surround the value with quotes and add a comma at the end
     *
     * @param value
     * @return
     */
    public static String q( String value ) {
        return q( value, false );
    }

    /**
     * surround the value with quotes and add a comma or a new line
     *
     * @param value
     * @param finalValue adds a new line instead of a coma
     * @return
     */
    public static String q( String value, boolean finalValue ) {

        if( finalValue ) {
            return "\"" + value + "\"\n";
        }
        else {
            return "\"" + value + "\",";
        }
    }

    public static boolean clear( String log ) {
        log = PATH + log;
        File logfile = new File( log );
        return logfile.delete();
    }
}
