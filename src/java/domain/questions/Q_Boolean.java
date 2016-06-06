/*
 * Copyright (c) 2014-2016 Andreas Lianos
 * Licensed under the MIT Liecense (LICENSE.txt).
 * Author Andreas Lianos
 */
package domain.questions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import tools.Pair;

public class Q_Boolean extends Question {

    private List<PossibleAnswer> possibleAnswers = new ArrayList<PossibleAnswer>();
    private Map<String, Integer> answers = new HashMap<String, Integer>();
    private Map<String, String> answerCodeMap = new HashMap<String, String>();
    public static final String YES = "yes";
    public static final String NO = "no";
    private boolean setup = false;
    private String question;

    private void addDefaultAnswers() {
        addPossibleAnswer( Q_Boolean.YES, "Yes" );
        addPossibleAnswer( Q_Boolean.NO, "No" );
    }


    private void initializeIfNeeded() {
        if( !setup ) {
            addDefaultAnswers();
            for( PossibleAnswer possibleAnswer : possibleAnswers ) {
                answers.put( possibleAnswer.getAnswerCode(), 0 );
            }
            setup = true;
        }
    }

    @Override
    public String getQuestion() {
        return question;
    }

    public void setQuestion( String question ) {
        this.question = question;
    }

    public void addPossibleAnswer( String answerCode, String answerString ) {

        if( answerCode.contains( "\"" ) ) {
            throw new IllegalArgumentException( "AnswerCodes cannot contain double quotes." );
        }
        if( !setup ) {
            PossibleAnswer answer = new PossibleAnswer();
            answer.setType( PossibleAnswer.TYPE_PREDEFINED );
            answer.setAnswerCode( answerCode );
            answer.setAnswerString( answerString );
            this.possibleAnswers.add( answer );
            answerCodeMap.put( answerCode, answerString );
        }
        else {
            throw new IllegalStateException( "Possible answers have already been setup. No more answers can be added." );
        }
    }

    @Override
    public void singleAnswerReceived( Map<String, String[]> data ) {
        try {
            String[] answerCodes = data.get( "answerCode" );
            for( String answerCode : answerCodes ) {
                answerCode = answerCode.replaceAll( "\"", "&quot;" ); //@@ there should not be any, catch it if there are
                Integer count = answers.get( answerCode );
                answers.put( answerCode, ++count );
            }
        } catch( Exception e ) {
            System.out.println( "BOOLEAN - ANSWER ERROR" );
        }
    }

    @Override
    public PossibleAnswer getAnswer( String answerCode ) {
        for( PossibleAnswer possibleAnswer : possibleAnswers ) {
            if( possibleAnswer.getAnswerCode().equals( answerCode ) ) {
                return possibleAnswer;
            }
        }

        throw new IllegalArgumentException( "Invalid AnswerCode (" + answerCode + ") for question " + getQuestion() );
    }

    @Override
    public Map<String, Integer> getResults() {
        return Collections.unmodifiableMap( answers );
    }

    @Override
    public List<PossibleAnswer> getPossibleAnswers() {
        initializeIfNeeded();
        return possibleAnswers;
    }

    @Override
    public Pair<String, Integer> getTopResult() {
        Integer max = Integer.MIN_VALUE;
        Pair<String, Integer> result = null;
        for( Map.Entry<String, Integer> entry : answers.entrySet() ) {
            if( entry.getValue() > max ) {
                max = entry.getValue();
                result = new Pair( entry.getKey(), entry.getValue() );
            }
        }
        return result;
    }

    /**
     * Returns true for yes, false for no.
     * @return 
     */
    public boolean getBooleanResult() {
        Pair<String, Integer> topResult = this.getTopResult();
        if( topResult.getValueA().equals( Q_Boolean.YES ) ) {
            return true;
        }
        else {
            return false;
        }
    }

    @Override
    public String getType() {
        return Question.TYPE_UNIQUE_OPTION;
    }
}
