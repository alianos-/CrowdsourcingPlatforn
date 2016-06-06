/*
 * Copyright (c) 2014-2016 Andreas Lianos
 * Licensed under the MIT Liecense (LICENSE.txt).
 * Author Andreas Lianos
 */
package tools;

import domain.questions.Question;
import java.util.List;

public class printTools {

    /**
     * To String
     *
     * @param expandedCC
     * @return
     */
    public static String ts( List<String> expandedCC ) {
        StringBuilder sb = new StringBuilder();
        int i = 0;
        for( String string : expandedCC ) {
            sb.append( i ).append( "->" ).append( string ).append( ":::" );
        }
        return sb.toString();
    }

    public static String toHtml( List<Question> questions ) {
        StringBuilder sb = new StringBuilder();
        int i = 0;
        for( Question q : questions ) {
            sb.append( "<strong>").append( i++ ).append( " - </strong>" )
                    .append( q.toHtml() ).append( "<br><br>" );
        }

        return sb.toString();
    }
}
