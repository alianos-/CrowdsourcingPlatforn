/*
 * Copyright (c) 2014-2016 Andreas Lianos
 * Licensed under the MIT Liecense (LICENSE.txt).
 * Author Andreas Lianos
 */
package domain.questions;

import java.util.Collection;

/**
 * Possible answers are registered by each implementation of "question"
 */
public class PossibleAnswer {
    //possible answer types
    /**
     * Implies that the answer has been defined before hand so that the user does not need an input box.@@
     */
    public static final int TYPE_PREDEFINED = 1;
    public static final int TYPE_INPUT = 2;

    /**
     * The type of this possible answer. Takes values from the public type list <code>PossibleAnswer.TYPE_XXXXXXX</code>
     */
    private int type;
    /**
     * The string that is returned to the interface, describing this answer
     */
    private String answerString;

    private String answerCode;
    
    /**
     * if set to true, it will not change line before this answer is presented
     * (so answers are presented horizontally rather than vertically.
     */
    private boolean noSpace = false;

    public String getAnswerCode() {
        return answerCode;
    }
     /**
     * The answer code is used as an ID to identify this answer, if the type is <code>TYPE_PREDEFINED</code>.
     * @param answerCode Any integer
     */
    public void setAnswerCode(String answerCode) {
        this.answerCode = answerCode;
    }
    
    public void setNoSpace(boolean noSpace){
        this.noSpace = noSpace;
    }

    public String getAnswerString() {
        return answerString;
    }

    public void setAnswerString(String answerString) {
        this.answerString = answerString;
    }

    public int getType() {
        return type;
    }

    /**
     * @param type should be an integer of the Static variables <code>PossibleAnswer.TYPE_XXXXXXX</code>
     */
    public void setType(int type) {
        this.type = type;
    }
    
    public static boolean contains(Collection<PossibleAnswer> list, String answerCode){
        for( PossibleAnswer possibleAnswer : list ) {
            if(possibleAnswer.getAnswerCode().equals( answerCode) ){
                return true;
            }
        }
        
        return false;
    }

    @Override
    public String toString(){
        return "Answer \""+answerString+"\" ("+answerCode+") type:"+type;
    }
}
