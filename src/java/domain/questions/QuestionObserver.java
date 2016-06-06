/*
 * Copyright (c) 2014-2016 Andreas Lianos
 * Licensed under the MIT Liecense (LICENSE.txt).
 * Author Andreas Lianos
 */
package domain.questions;

public interface QuestionObserver {
    public void answerReceived(Question question);
}
