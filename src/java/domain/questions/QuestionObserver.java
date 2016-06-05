package domain.questions;

/**
 *
 * @author alianos
 */
public interface QuestionObserver {
    public void answerReceived(Question question);
}
