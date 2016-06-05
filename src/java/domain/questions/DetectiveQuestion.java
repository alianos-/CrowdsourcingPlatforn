/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package domain.questions;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import tools.Globals;
import tools.Pair;

/**
 * A simplified question suitable to be returned for handling by the interface. It only contains the question
 * string, the answer options and a unique ID assigned by the Detective when asked.
 *
 * @author alianos
 */
public final class DetectiveQuestion {

    private String question;
    private UUID ID;
    private List<PossibleAnswer> possibleAnswers = new ArrayList<PossibleAnswer>();
    private String type;
    private List<String> derivedQuestions = new ArrayList<String>();
    private Boolean allowMultipleAnswers = false;//from the same user
    private String nextButtonType;
    private List<String> excludeAnswers = new ArrayList<String>();
    private String signature;

    /**
     * Automatically sets the following<br>
     * * question<br>
     * * type<br>
     * * possibleAnswers<br>
     * * allowMultipleAnswers<br>
     * * derivedQuestions<br>
     *
     * @param q
     */
    public DetectiveQuestion( Question q ) {
        this.signature = q.getSignature();
        for( PossibleAnswer a : q.getPossibleAnswers() ) {
            this.addPossibleAnswer( a );
        }
        this.addQuestion( q.getQuestion() );
        this.setType( q.getType() );

        Question derived = q.getDerived( Question.TYPE_DERIVED_SIMILAR_OPTIONS );
        if( derived != null ) {
            this.addDerived( "similarOptions" );
        }

        this.setAllowMultipleAnswers( q.isMultipleAnswersFromSameUserAllowed() );
        this.setNextButtonType( q.getNextButtonType() );
    }

    public String getSignature() {
        return signature;
    }

    public void addQuestion( String question ) {
        this.question = question;
    }

    void setAllowMultipleAnswers( boolean flag ) {
        allowMultipleAnswers = flag;
    }

    void setNextButtonType( String type ) {
        nextButtonType = type;
    }

    void addPossibleAnswer( PossibleAnswer a ) {
        this.possibleAnswers.add( a );
    }

    public void setExpectedAnswerUUID( UUID ID ) {
        this.ID = ID;
    }

    void setType( String type ) {
        this.type = type;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();

        result.append( ID ).append( "\n" );
        result.append( question ).append( "\n" );

        return result.toString();
    }

    public String getQuestion() {
        return question;
    }

    public UUID getID() {
        return ID;
    }

    public List<PossibleAnswer> getPossibleAnswers() {
        return possibleAnswers;
    }

    public String getType() {
        return type;
    }

    public List<String> getDerivedQuestions() {
        return derivedQuestions;
    }

    public Boolean isAllowMultipleAnswers() {
        return allowMultipleAnswers;
    }

    public String getNextButtonType() {
        return nextButtonType;
    }

    public List<String> getExcludeAnswers() {
        return excludeAnswers;
    }

    void addDerived( String derivedType ) {
        derivedQuestions.add( derivedType );
    }
}
