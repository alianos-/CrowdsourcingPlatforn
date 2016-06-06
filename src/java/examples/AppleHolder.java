/*
 * Copyright (c) 2014-2016 Andreas Lianos
 * Licensed under the MIT Liecense (LICENSE.txt).
 * Author Andreas Lianos
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
