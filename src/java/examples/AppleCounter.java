/*
 * Copyright (c) 2014-2016 Andreas Lianos
 * Licensed under the MIT Liecense (LICENSE.txt).
 * Author Andreas Lianos
 */
package examples;

import domain.questions.Detective;
import domain.questions.Q_Boolean;
import domain.questions.Q_GenericMultipleAnswer;
import domain.questions.Q_GenericOptionsList;
import domain.questions.Question;
import domain.questions.QuestionObserver;
import tools.MapServices;

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
                    //no skip or I dont know buttons.
                    followupQ.setNextButtonType(Question.NEXT_BUTTON_TYPE_NOBUTTON);
                    followupQ.setIdentifier("validationQuestion");
                    followupQ.addArgument("fooArgumentName", "fooArgumentValue");
                    followupQ.registerObserver(this);
                    Detective.SHERLOCK.addQuestion(followupQ);
                    System.out.println("They are almost on to us, I will try to trick them.");
                } else {
                    //else we just accept it
                    System.out.println("The fools think there are more than one apples.");
                    decideWhereToPutTheApples();
                }

                //we can use any of the custom arguments passed to us by whoever created the question
                System.out.println("Whoever asked, wanted to us to know that " + q.getArgument("ArgumentA"));
            }
        } // The only boolean question we are expecting is the validation question above
        else if (question instanceof Q_Boolean) {

            Q_Boolean q = (Q_Boolean) question;
            String answer = q.getTopResult().getValueA();
            //do something based on the outcome
            if (answer.equals("yes")) {
                System.out.println("They ensisted.");
            } else {
                System.out.println("They fell for it.");
                decideWhereToPutTheApples();
            }
        }
    }

    private void decideWhereToPutTheApples() {
        //create the object that should be notified once we know the response
        AppleHolder ah = new AppleHolder();
        
        //regardless, ask another question
        Q_GenericMultipleAnswer followupQ = new Q_GenericMultipleAnswer();
        followupQ.setQuestion("Where should I put them?");
        //We could use  q.addPossibleAnswer("answerIdentifier", "Literal Answer String");
        //But just for demonstration, we will create the answers automatically.
        //we will add the options, and then call createAnswersByOptionsList() to create the answer map.
        //Answers will get identifiers incrementally from 0. This is usefull if the answers are created
        //dynamically, e.g. in a loop.
        followupQ.addOption("In a basket");
        followupQ.addOption("In the fridge");
        followupQ.addOption("Eat them all");
        followupQ.createAnswersByOptionsList();
        
        followupQ.setAnswersNeeded(3);
        //Allow the user to answer the question, indicating that he does not know.
        followupQ.setNextButtonType(Question.NEXT_BUTTON_TYPE_IDONTKNOW);
        followupQ.registerObserver(ah);
        Detective.SHERLOCK.addQuestion(followupQ);        
    }

}
