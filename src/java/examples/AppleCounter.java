/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package examples;

import domain.questions.Q_Boolean;
import domain.questions.Q_GenericOptionsList;
import domain.questions.Question;
import domain.questions.QuestionObserver;
import tools.Globals;

/**
 *
 * @author alianos
 */
public class AppleCounter implements QuestionObserver {

    @Override
    public void answerReceived( Question question ) {
        if( question instanceof Q_GenericOptionsList ) {
            Q_GenericOptionsList q = (Q_GenericOptionsList) question;
            if( "numOfApples1".equals( q.getIdentifier() ) ) {
                String answer = q.getTopResult().getValueA();
                
                if(answer.equals( "one") ){
                    Q_Boolean followupQ = new Q_Boolean();
                    followupQ.setQuestion( "Really only one apple?" );
                    followupQ.setAnswersNeeded( 1 );
                    followupQ.setNextButtonType( Question.NEXT_BUTTON_TYPE_NOBUTTON );
                    followupQ.setIdentifier( "numOfApples1" );
                    followupQ.addArgument( "username", "a" );
                    followupQ.registerObserver( this );
                    Globals.sherlock.addQuestion( followupQ );
                }

            }
        }
    }
    
}
