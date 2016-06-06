/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package domain.questions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import tools.Pair;

/**
 *
 * @author alianos
 */
public class Q_GenericMultipleAnswer extends Question {

    private List<PossibleAnswer> possibleAnswers = new ArrayList<PossibleAnswer>();
    private Map<String, Integer> answers = new HashMap<String, Integer>();
    private Map<String, String> answerCodeMap = new HashMap<String, String>();
    private List<String> options;
    private boolean setup = false;
    private String question;

    public void reset() {
        setup = false;
    }

    private void initializeIfNeeded() {
        if( !setup ) {
//            addDefaultAnswers();
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

    public void setOptions( List<String> options ) {
        this.options = options;
    }

    public void addOption( String option ) {
        if( this.options == null )//lazy initialize
        {
            this.options = new ArrayList<String>();
        }
        this.options.add( option );
    }

    public Map<String, String> getAnswerCodeMap() {
        return Collections.unmodifiableMap( answerCodeMap );
    }

    public void createAnswersByOptionsList() {
        if( this.options != null ) {
            for( int i = 0; i < options.size(); i++ ) {
                addPossibleAnswer( i + "", options.get( i ) );
            }
        }
//        &#13;
        else {
            System.out.println( "cannot form generic options questions, because options are not set" );
        }
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
            System.out.println( "GENERIC OPTIONS LIST - ANSWER ERROR" );
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
        Map<String, Integer> copy = new HashMap<String, Integer>();
        for( Map.Entry<String, Integer> entry : answers.entrySet() ) {
            copy.put( entry.getKey(), entry.getValue() );
        }


        return copy;
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

    @Override
    public String getType() {
        return Question.TYPE_MULTIPLE_OPTIONS;
    }
}
