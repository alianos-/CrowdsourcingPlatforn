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
public class Q_SendCommand extends Question {

    private List<PossibleAnswer> possibleAnswers = new ArrayList<PossibleAnswer>();
    private Map<String, Integer> answers = new HashMap<String, Integer>();
    private Map<String, String> answerCodeMap = new HashMap<String, String>();    
    private String question;
    private Map<String, Object> customArguments;

    public void addArgument( String key, Object arg ) {
        if( customArguments == null ) {//lazy initialize
            customArguments = new HashMap<String, Object>();
        }

        customArguments.put( key, arg );
    }

    public Object getArgument( String key ) {
        if( customArguments.containsKey( key ) ) {
            return customArguments.get( key );
        }
        return null;
    }

    @Override
    public String getQuestion() {
        return question;
    }

    public void setQuestion( String question ) {
        this.question = question;
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
        return Question.TYPE_COMMAND;
    }
}
