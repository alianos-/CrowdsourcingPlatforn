/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package domain.questions;

/**
 *
 * @author alianos
 */
public interface QuestionObserver {
    public void answerReceived(Question question);
}
