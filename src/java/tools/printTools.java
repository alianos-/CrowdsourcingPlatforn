/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tools;

import domain.questions.Question;
import java.util.List;

/**
 *
 * @author alianos
 */
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
