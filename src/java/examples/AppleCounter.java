/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package examples;

import domain.questions.Detective;
import domain.questions.Q_Boolean;
import domain.questions.Q_GenericOptionsList;
import domain.questions.Question;
import domain.questions.QuestionObserver;

/**
 *
 * @author alianos
 */
public class AppleCounter implements QuestionObserver {

    //fill in the QuestionObserver behaviour
    @Override
    public void answerReceived(Question question) {
        //Track the type of question for validation, or to separate actions based
        //on different types
        if (question instanceof Q_GenericOptionsList) {
            //You will need to know what kind of questions you expect to be answered
            //and cast them in the appropriate type to get answers
            Q_GenericOptionsList q = (Q_GenericOptionsList) question;
            //You can also use the question identifier to separate different questions you need to monitor
            //The question identifier can be set when creating the question
            if ("numOfApples1".equals(q.getIdentifier())) {
                //This will give you the most predominant answer
                String answer = q.getTopResult().getValueA();

                //upon which you can base further logic
                //in this case if the answer is "one", we will validate
                if (answer.equals("one")) {
                    Q_Boolean followupQ = new Q_Boolean();
                    followupQ.setQuestion("Really only one apple?");
                    followupQ.setAnswersNeeded(1);
                    followupQ.setNextButtonType(Question.NEXT_BUTTON_TYPE_NOBUTTON);
                    followupQ.setIdentifier("validationQuestion");
                    followupQ.addArgument("argument1", "can be anything");
                    followupQ.registerObserver(this);
                    Detective.SHERLOCK.addQuestion(followupQ);
                    System.out.println("They are almost on to us, I will try to trick them.");
                } else {
                    //else we just accept it
                    System.out.println("We tricked them, they think there are more than one apples.");
                }

                //we can use any of the custom arguments passed to us by whoever created the question
                System.out.println("Whoever asked, wanted to us to know that "+q.getArgument("ArgumentA"));
            }
        } 
        // The only boolean question we are expecting is the validation question above
        else if (question instanceof Q_Boolean) {
           
            Q_Boolean q = (Q_Boolean) question;
            String answer = q.getTopResult().getValueA();
            //do something based on the outcome
            if (answer.equals("yes")) {
                System.out.println("The users figured it out, they ensist its only one apple.");
            } else {
                System.out.println("They changed their mind, they think there are more than one apples.");
            }
        }
    }

}
