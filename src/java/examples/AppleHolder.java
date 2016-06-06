/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package examples;

import domain.questions.Q_GenericMultipleAnswer;
import domain.questions.Question;
import domain.questions.QuestionObserver;
import tools.MapServices;

/**
 *
 * @author Andreas
 */
public class AppleHolder implements QuestionObserver {

    @Override
    public void answerReceived(Question question) {
        if (question instanceof Q_GenericMultipleAnswer) {
            Q_GenericMultipleAnswer q = (Q_GenericMultipleAnswer) question;

            MapServices.friendlyPrint(q.getResults());

        }
    }

}
