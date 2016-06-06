package domain.questions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import tools.MapServices;

/**
 * Creates a detective than can transfer questions made by the system to the
 * users and handle the answers. The detective holds a list of the questions
 * that have been asked and monitors responses through a unique code. When an
 * answer comes back in it requires that code, along with a data string that
 * contains the answer.
 *
 * The Detective is set up as a singleton for simplicity.
 *
 * @author alianos
 */
public class Detective {

    /**
     * Prevents more questions from being asked.
     */
    public static boolean paused = false;

    /**
     * Easy access to the singleton
     */
    public static final Detective SHERLOCK = Detective.getInstance();

    public static boolean isPaused() {
        return Detective.paused;
    }

    public static void setPaused(boolean paused) {
        Detective.paused = paused;
    }

    /**
     * Map with all the pending questions [User, [Q_ID, Question]]
     */
    private Map<String, Map<UUID, Question>> questions = new HashMap<String, Map<UUID, Question>>();
    /**
     * Map with all the questions that have been asked and are awaiting an
     * answer.
     */
    private Map<UUID, ExpectedAnswer> expectedAnswers = new HashMap<UUID, ExpectedAnswer>();
    /**
     * [QuestionID,[UserIDs that answered it]]
     */
    private HashMap<UUID, HashSet<String>> usersByQuestions = new HashMap<UUID, HashSet<String>>();
    /**
     * [UserID, [Similar,Similar]]
     */

    private static final String ANY = "any_HDSJ$&1@JD*";//@@random code to not mix with usernames
    /**
     * [Question ID, [UserIDs that should be handed this question]]
     */
    private HashMap<UUID, HashSet<String>> excludedUsers = new HashMap<UUID, HashSet<String>>();

    private Detective() {
    }

    public static Detective getInstance() {
        return DetectiveHolder.INSTANCE;
    }

    /**
     * Returns the sum of the answers that have been given, and the answers that
     * will be needed for all the questions to be answered.
     *
     * @return
     */
    public int getNumOfTotalAnswersRequired() {
        int count = 0;

        for (Map<UUID, Question> qByUser : questions.values()) {
            for (Question q : qByUser.values()) {
                //if we changed the answers needed to a question, then use the highest of the 2 to maintain
                //concistency.
                if (q.getAnswersNeeded() < q.getAnswersGiven()) {
                    count += q.getAnswersGiven();
                } else {
                    count += q.getAnswersNeeded();
                }
            }
        }

        return count;
    }

    /**
     * How many answers have been given so far
     *
     * @return
     */
    public int getTotalAnswered() {

        int count = 0;
        for (HashSet<String> userIDs : usersByQuestions.values()) {
            count += userIDs.size();
        }
        return count;

    }

    /**
     * How many answers has this user given
     *
     * @param userID
     * @return
     */
    public int getNumOfAnsweredQuestions(String userID) {
        int count = 0;
        for (HashSet<String> userIDs : usersByQuestions.values()) {
            if (userIDs.contains(userID)) {
                count++;
            }
        }
        return count;
    }

    public int getNumOfPendingQuestions(String userID) {
        int count = 0;

        if (userID == null) {
            return count;
        }

        Map<UUID, Question> totalQuestions = new LinkedHashMap<UUID, Question>();
        Map<UUID, Question> userQuestions = questions.get(userID);
        Map<UUID, Question> genericQuestions = questions.get(ANY);

        if (userQuestions != null) {
            totalQuestions.putAll(userQuestions);
        }
        if (genericQuestions != null) {
            totalQuestions.putAll(genericQuestions);
        }

        for (Question q : totalQuestions.values()) {
            boolean userExcluded = isUserExcluded(q.getID(), userID);
            //if there are questions that still need answering and the user has not answered it before
            if (!q.isAnswered()
                    && (!isQuestionAnsweredByUser(userID, q.getID()))
                    && !userExcluded) {
                count++;
            }
        }

        return count;
    }

    /**
     * Checks if there are more pending questions of any type, for this
     * observer.
     *
     * @param qo
     * @return
     */
    public boolean arePendingQuestions(QuestionObserver qo) {
        if (numOfPendingQuestions(qo, null) > 0) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Returns the number of pending questions of any type, for this observer.
     *
     * @param qo
     * @return The number of pending questions
     */
    public int numOfPendingQuestions(QuestionObserver qo) {
        return numOfPendingQuestions(qo, null);
    }

    /**
     * Returns the number of pending questions of the given type, for this
     * observer.
     *
     * @param qo
     * @param cls The type of question, must be a subtype of Question. Give null
     * to get results for any type
     * @return The number of pending questions.
     */
    public int numOfPendingQuestions(QuestionObserver qo, Class<? extends Question> cls) {
        int count = 0;
        for (Map<UUID, Question> userQuestions : getQuestions().values()) {
            for (Question question : userQuestions.values()) {
                if (question.getObservers().contains(qo) && !question.isAnswered()) {
                    //if we have pending questions, see if we are looking for a spesific type
                    //or for any type of question
                    if (cls == null) {
                        count++;
                    } else if (question.getClass() == cls) {
                        count++;
                    }
                }
            }
        }
        return count;
    }

    /**
     * Returns the pending questions of the given type, for this observer.
     *
     * @@This leaks the question object outside
     *
     * @param qo
     * @param cls The type of question, must be a subtype of Question. Give null
     * to get results for any type
     * @return The number of pending questions.
     */
    public List<Question> getPendingQuestions(QuestionObserver qo, Class<? extends Question> cls) {
        List<Question> result = new ArrayList<Question>();
        List<Question> pendingQuestions = getPendingQuestions(cls);
        for (Question question : pendingQuestions) {
            if (question.getObservers().contains(qo)) {
                result.add(question);
            }
        }
        return result;
    }

    /**
     * Returns the pending questions of the given type
     *
     * @@This leaks the question object outside
     *
     * @param qo
     * @param cls The type of question, must be a subtype of Question. Give null
     * to get results for any type
     * @return The number of pending questions.
     */
    public List<Question> getPendingQuestions(Class<? extends Question> cls) {
        List<Question> result = new ArrayList<Question>();
        for (Map<UUID, Question> userQuestions : getQuestions().values()) {
            for (Question question : userQuestions.values()) {
                if (!question.isAnswered()) {
                    //if we have pending questions, see if we are looking for a spesific type
                    //or for any type of question
                    if (cls == null) {
                        result.add(question);
                    } else if (question.getClass() == cls) {
                        result.add(question);
                    }
                }
            }
        }
        return result;
    }

    public List<Question> getPendingQuestions() {
        return getPendingQuestions(null);
    }

    /**
     * Check if there are more pending questions of a specific type for this
     * observer
     *
     * @param qo
     * @param cls The type of question, must be a subtype of Question. Give null
     * to get results for any type
     * @return
     */
    public boolean arePendingQuestions(QuestionObserver qo, Class<? extends Question> cls) {
        if (numOfPendingQuestions(qo, cls) > 0) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * @return the questions
     */
    public Map<String, Map<UUID, Question>> getQuestions() {
        return Collections.unmodifiableMap(questions);
    }

    public Map<UUID, Question> getQuestions(String user) {
        return questions.get(user);
    }

    public List<Question> getByIdentifier(String identifier) {
        List<Question> result = new ArrayList<Question>();
        for (Map<UUID, Question> qmap : questions.values()) {
            for (Question q : qmap.values()) {
                if (q.getIdentifier().equals(identifier)) {
                    result.add(q);
                }
            }
        }
        return result;
    }

    /**
     * Returns a list of all the questions, answered or pending.
     *
     * @return
     */
    private synchronized List<Question> getAListOfAllQuestions() {
        List<Question> allQuestions = new ArrayList<Question>();
        for (Map<UUID, Question> map : questions.values()) {
            for (Question question : map.values()) {
                allQuestions.add(question);
            }
        }

        return allQuestions;

    }

    private static class DetectiveHolder {

        private static final Detective INSTANCE = new Detective();
    }

    /**
     * Registers a Question of any type with the detective. If the question is
     * registered already, it automatically increments the answersNeeded.
     *
     * @param q
     */
    public void addQuestion(Question q) {
        addQuestion(q, ANY);
    }

    public void addQuestion(Question q, String user) {
        if (q == null) {
            throw new IllegalArgumentException("Received empty question");
        }
        //see if the user exists
        Map<UUID, Question> userQuestions = this.getQuestions(user);
        if (userQuestions == null) {
            userQuestions = new LinkedHashMap<UUID, Question>();
        }
        //see if the question exists
        if (userQuestions.containsKey(q.getID())) {
            //if it exists, increment the answers needed
            q.incrementAnswersNeeded();
        } else //If we are adding a personal question, go for LIFO
        {
            if (user.equals(ANY)) {
                userQuestions.put(q.getID(), q);
            } else {
                userQuestions = MapServices.addToHead(userQuestions, q.getID(), q); //LIFO                
            }
        }

        this.questions.put(user, userQuestions);

    }

    public void exludeUser(UUID qID, String userID) {
        HashSet<String> excluded = excludedUsers.get(qID);
        if (excluded == null) {
            excluded = new HashSet<String>();
        }
        excluded.add(userID);
        excludedUsers.put(qID, excluded);
    }

    public boolean isUserExcluded(UUID qID, String userID) {
        HashSet<String> excluded = excludedUsers.get(qID);
        if (excluded != null && excluded.contains(userID)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * asks the detective if there is anything else that needs to be asked
     *
     * @return a Question in a light format, that is safe to be returned. Null
     * if no more questions are available for this user.
     */
    public DetectiveQuestion getNextQuestion() {
        return getNextQuestion(ANY);
    }

    public DetectiveQuestion getNextQuestion(String userID) {

        if (isPaused()) {
            return null;
        }
        if (userID.isEmpty()) {
            return null;
        }

        Map<UUID, Question> totalQuestions = new LinkedHashMap<UUID, Question>();
        Map<UUID, Question> userQuestions = questions.get(userID);
        Map<UUID, Question> genericQuestions = questions.get(ANY);

        if (userQuestions != null) {
            totalQuestions.putAll(userQuestions);
        }
        if (genericQuestions != null) {
            totalQuestions.putAll(genericQuestions);
        }

        for (Question q : totalQuestions.values()) {
            boolean userExcluded = isUserExcluded(q.getID(), userID);
            //if there are questions that still need answering and the user has not answered it before
            if (!q.isAnswered()
                    && (!isQuestionAnsweredByUser(userID, q.getID()))
                    && !userExcluded) {
                //add them to the list of asked questions (so we can trace it if an answer comes in)
                ExpectedAnswer ea = new ExpectedAnswer(q.getID(), userID);
                this.expectedAnswers.put(ea.getID(), ea);

                //create a friendly form of the Question to send back to the Client
                DetectiveQuestion dq = new DetectiveQuestion(q);
                dq.setExpectedAnswerUUID(ea.getID());
                return dq;
            }
        }
        return null;
    }

    /**
     * debugging function
     *
     * @param userID
     */
    public void printQuestions(String userID) {
        System.out.println("questions for user:" + userID);

        Map<UUID, Question> totalQuestions = new LinkedHashMap<UUID, Question>();
        Map<UUID, Question> userQuestions = questions.get(userID);
        Map<UUID, Question> genericQuestions = questions.get(ANY);

        if (userQuestions != null) {
            totalQuestions.putAll(userQuestions);
        }
        if (genericQuestions != null) {
            totalQuestions.putAll(genericQuestions);
        }
        for (Question q : totalQuestions.values()) {
            System.out.println(q.isAnswered() + " " + q.getAnswersNeeded() + " " + q.getID() + " " + q.getIdentifier());
        }
    }

    /**
     * Run through all users to find the requested question
     *
     * @param qUUID
     * @return
     */
    public Question getQuestion(UUID qUUID) {
        for (Map<UUID, Question> userQuestions : questions.values()) {
            if (userQuestions.containsKey(qUUID)) {
                return userQuestions.get(qUUID);
            }
        }
        return null;
    }

    /**
     * Retrieves the Question from the Detective and then looks if it is
     * answered or not.
     *
     * @param qUUID
     * @return
     */
    public Boolean isQuestionAnswered(UUID qUUID) {
        Question q = this.getQuestion(qUUID);
        if (q != null) {
            return q.isAnswered();
        } else {
            throw new IllegalArgumentException("No such Question ID\n" + q);
        }
    }

    /**
     * gives a bit of information to the detective, aka, someone answered
     * something.
     *
     * @param answerID = the UUID assigned when asked
     * @param data = the actual answer given
     */
    public void answerQuestion(UUID answerID, String userID, Map<String, String[]> data) {
        ExpectedAnswer ea = this.expectedAnswers.get(answerID);
        if (ea != null) {
            Question q = this.getQuestion(ea.getqID());

            //see if this user is allowed to answer this question
            if (ea.getUserID().equals(userID)) {
                if (q != null) {
                    q.submitSingleAnswer(data, userID);
                    userAnsweredQuestion(userID, q.getID());

                    String[] answerCodeA = data.get("answerCode");
                    String answerCode = "";
                    if (answerCodeA != null) {
                        answerCode = answerCodeA[0];
                    }
                    String[] payloadA = data.get("payload");
                    if (payloadA != null) {
                        answerCode = payloadA[0] + " (NEW INPUT)";//@@potential system exploit
                    }
                }
            } else {
                System.out.println("Hacking attempt. Incoming answer came from an unexpected user.");
            }
        }

        this.expectedAnswers.remove(answerID);
    }

    /**
     * make a note that the user answered this question
     *
     * @param userID
     * @param qID
     */
    private void userAnsweredQuestion(String userID, UUID qID) {
        HashSet<String> answerers = usersByQuestions.get(qID);
        if (answerers == null) {
            answerers = new HashSet<String>();
        }
        answerers.add(userID);
        usersByQuestions.put(qID, answerers);
    }

    /**
     * find out if the user has already answered this question
     *
     * @param qID
     * @return
     */
    private boolean isQuestionAnsweredByUser(String userID, UUID qID) {
        HashSet<String> answerers = usersByQuestions.get(qID);
        if (answerers != null) {
            return answerers.contains(userID);
        } else {
            return false;
        }
    }

    public UUID getQuestionIDforAnswer(UUID expectedAnswerID) {
        ExpectedAnswer ea = this.getExpectedAnswers().get(expectedAnswerID);
        if (ea != null) {
            return ea.getqID();
        }
        return null;
    }

    private Map<UUID, ExpectedAnswer> getExpectedAnswers() {
        return this.expectedAnswers;

    }

    /**
     * Internal object to hold the questions already asked (because each
     * Question, may be asked multiple times)
     */
    private class ExpectedAnswer {

        private UUID qID;
        /**
         * The user we expect to answer this question.
         */
        private String userID;
        /**
         * Unique ID generated automatically for this possible answer
         */
        private UUID ID = UUID.randomUUID();

        private ExpectedAnswer(UUID qID, String userID) {
            this.qID = qID;
            this.userID = userID;
        }

        public String getUserID() {
            return userID;
        }

        public UUID getqID() {
            return qID;
        }

        public UUID getID() {
            return ID;
        }
    }
}
