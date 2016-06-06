/*
 * Copyright (c) 2014-2016 Andreas Lianos
 * Licensed under the MIT Liecense (LICENSE.txt).
 * Author Andreas Lianos
 */
package domain.questions;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import tools.Pair;

/**
 * Defines the abstract class upon which questions of various forms are based. We create the Question and feed
 * it to Detective to find the answers.
 */
public abstract class Question implements Serializable {

    public static final String TYPE_MULTIPLE_OPTIONS = "multiple_options";
    public static final String TYPE_UNIQUE_OPTION = "unique_options";
    private int answersNeeded = 1;
    private int answersGiven = 0;
    private List<QuestionObserver> observers = new ArrayList<QuestionObserver>();
    private final UUID ID = UUID.randomUUID();
    private String nextButtonType = NEXT_BUTTON_TYPE_IDONTKNOW; //default
    public static final String NEXT_BUTTON_TYPE_IDONTKNOW = "premade_idontknow";
    public static final String NEXT_BUTTON_TYPE_NEXT = "premade_next";
    public static final String NEXT_BUTTON_TYPE_NOBUTTON = "premade_nobutton";
    private Map<String, Object> customArguments;

    /**
     * Debug method, returns a string of answers given / answers needed
     *
     * @return
     */
    public String getAnsweredByNeeded() {
        return answersGiven + "/" + answersNeeded;
    }

    /**
     * Represents the question not as an object, but as a logical question. i.e. different itterations that
     * produce the same question, will have the same signature.
     *
     * @return a String with the MD5 hash
     */
    public String getSignature() {
        String digest = null;
        try {
            StringBuilder message = new StringBuilder();
            message.append( getQuestion() );
            for( PossibleAnswer answer : getPossibleAnswers() ) {
                message.append( answer.getType() ).append( answer.getAnswerCode() ).append( answer.getAnswerString() );
            }

            MessageDigest md = MessageDigest.getInstance( "MD5" );
            byte[] hash = md.digest( message.toString().getBytes( "UTF-8" ) );
            //converting byte array to Hexadecimal String
            StringBuilder sb = new StringBuilder( 2 * hash.length );
            for( byte b : hash ) {
                sb.append( String.format( "%02x", b & 0xff ) );
            }
            digest = sb.toString();

        } catch( UnsupportedEncodingException ex ) {
            Logger.getLogger( Question.class.getName() ).log( Level.SEVERE, null, ex );
        } catch( NoSuchAlgorithmException ex ) {
            Logger.getLogger( Question.class.getName() ).log( Level.SEVERE, null, ex );
        }
        return digest;
    }

        public void addArgument( String key, Object arg ) {
        if( customArguments == null ) {//lazy initialize
            customArguments = new HashMap<String, Object>();
        }

        customArguments.put( key, arg );
    }

    public Object getArgument( String key ) {
        return customArguments.get( key );
    }
    
    @Override
    public boolean equals( Object q ) {
        if( q instanceof Question ) {
            Question question = (Question) q;
            if( question.getID().equals( this.getID() ) ) {
                return true;
            }
        }

        return false;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + (this.ID != null ? this.ID.hashCode() : 0);
        return hash;
    }
    /**
     * Give a custom identifier to the question so you can later retrieve it right away
     */
    private String identifier = "";
    /**
     * A default amount to increment the answers needed. It set by default to 1.
     */
    private int increment = answersNeeded;

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier( String identifier ) {
        this.identifier = identifier;
    }

    public void registerObserver( QuestionObserver obs ) {
        observers.add( obs );
    }

    protected void notifyObservers() {
        for( QuestionObserver obs : observers ) {
            obs.answerReceived( this );
        }
    }

    /**
     * Returns true if this is the answer that was missing, to mark the question answered.
     *
     * @param data
     * @return True if this answer completed the question.
     */
    public boolean submitSingleAnswer( Map<String, String[]> data, String userID ) {
        boolean finalAnswer = false;

        String[] isClueless = data.get( "isClueless" );
        String[] skip = data.get( "skip" );


        if( skip != null && skip[0].equals( "true" ) ) {
            //@@remedy to ensure the user is not spamed with the same question when hitting skip.
            this.setAnswersNeeded( this.getAnswersNeeded() - 1 );
        }
        //Normal answer
        else if( isClueless == null ) {

            if( !this.isAnswered() ) {
                this.incrementAnswersGiven();
                this.singleAnswerReceived( data );

                //if that answer, was the answer that completed the question
                if( this.answersNeeded == this.answersGiven ) {
                    finalAnswer = true;
                    this.notifyObservers();
                }
            }
        }

        return finalAnswer;
    }

  
    public String getNextButtonType() {
        return nextButtonType;
    }

    /**
     * 
     * @param nextButtonType Choose from NEXT_BUTTON_TYPE_ or add your own text. If you
     * add your own text, the button acts as an "I don't know" button with different text. If you choose
     * from the premade ones, look at each type for details.
     */
    public void setNextButtonType( String nextButtonType ) {
        this.nextButtonType = nextButtonType;
    }   
    

    /**
     * Informs the Question that an answer came in. Each implementation is responsible to act accordingly
     * based on the answer
     *
     * @param data Contains the data in a form that can be handled by each Question type
     */
    protected abstract void singleAnswerReceived( Map<String, String[]> data );

    public abstract List<PossibleAnswer> getPossibleAnswers();

    public abstract String getQuestion();

    public int getAnswersNeeded() {
        return answersNeeded;
    }

    public void setAnswersNeeded( int answersNeeded ) {
        this.answersNeeded = answersNeeded;
    }

    public boolean isAnswered() {
        if( this.getAnswersNeeded() > this.getAnswersGiven() ) {
            return false;
        }
        else {
            return true;
        }
    }

    public int getAnswersGiven() {
        return answersGiven;
    }

    protected void incrementAnswersGiven() {
        this.answersGiven++;
    }

    public UUID getID() {
        return ID;
    }

    //Types are predefined
    public abstract String getType();

    public abstract PossibleAnswer getAnswer( String answerCode ); //@@apati answer, to thema me tis apantiseis thelei lisimo

    public abstract Map<String, Integer> getResults();

    public abstract Pair<String, Integer> getTopResult();

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append( getQuestion() ).append( "\n" );
        for( PossibleAnswer ans : getPossibleAnswers() ) {
            sb.append( ans.getAnswerCode() ).append( " - " ).append( ans.getAnswerString() ).append( "\n" );
        }

        return sb.toString();
    }

    public String toHtml() {
        StringBuilder sb = new StringBuilder();
        sb.append( getQuestion() ).append( "<br>" );
        for( PossibleAnswer ans : getPossibleAnswers() ) {
            sb.append( ans.getAnswerCode() ).append( ":::" ).append( ans.getAnswerString() ).append( "<br>" );
        }

        return sb.toString();
    }

    public List<QuestionObserver> getObservers() {
        return Collections.unmodifiableList( observers );
    }

    /**
     * Increments the amount of answers needed, by the initial amount of answers needed. You can change this
     * number by using setIncrement. If answersNeeded is 0 it will increment by one.
     *
     */
    public void incrementAnswersNeeded() {
        //dont use setAnswersNeeded, because that will reset the "increment"
        if( this.answersNeeded == 0 ) {
            this.answersNeeded = 1;
        }
        else {
            this.answersNeeded += increment;
        }
        System.out.println( "incremented to " + answersNeeded );
    }

    /**
     * Increments the amount of answers needed by the given amount.
     *
     * @param increment
     */
    public void incrementAnswersNeeded( int increment ) {
        this.answersNeeded = answersNeeded + increment;
    }

    public void setIncrement( int increment ) {
        this.increment = increment;
    }

    /**
     * Returns the first occurrence in the list, of a question with the given identifier.
     *
     * @param list
     * @param identifier
     * @return The first question if found, null if not.
     */
    public static Question getByIdentifier( List<Question> list, String identifier ) {
        for( Question question : list ) {
            if( question.getIdentifier().equals( identifier ) ) {
                return question;
            }
        }
        return null;
    }
}
