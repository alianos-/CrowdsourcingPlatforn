package api;

import domain.questions.Detective;
import examples.AppleCounter;
import domain.questions.Q_GenericOptionsList;
import domain.questions.Question;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author alianos
 */
public class Admin_API extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        //This administration API is just an example. If you really need an admin API
        //you should hide it behind administration login, and make sure to include 
        //keeping logs of who is doing what
        //This basic admin API can be accesed through "app/admin536GHSND238B.jsp" webpage
        //It can only do 3 things. 
        //- Show pending questions
        //- Pause and Unpause the system (that means stop/start serving questions)
        //- Do a random action as an example. In this case we add a question.
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        try {
            StringBuilder result = new StringBuilder();

            String controller = request.getParameter("controller");
            String action = request.getParameter("action");

            //QUESTIONS
            if (controller.equals("questions")) {
                if (action.equals("showPending")) {
                    String html = tools.printTools.toHtml(Detective.SHERLOCK.getPendingQuestions());
                    result.append(html);
                }
            } //SYSTEM
            else if (controller.equals("system")) {
                if (action.equals("togglePause")) {
                    result.append(togglePaused());
                }
            } //TESTING
            else if (controller.equals("testing")) {
                if (action.equals("test1")) {
                    result.append(test1());
                }
            }

            out.print(result);
        } finally {
            out.close();
        }
    }

    public static String togglePaused() {
        Detective.setPaused(!Detective.isPaused());
        return (Detective.isPaused() ? "Paused" : "Running");
    }

    public static String test1() {
        //we need the apple counter to monitor the response
        AppleCounter counter = new AppleCounter();

        //Create a new question
        Q_GenericOptionsList q = new Q_GenericOptionsList();
        //register who should be notified when this question completes
        q.registerObserver(counter);
        //set the question text and the possible options
        q.setQuestion("How many apples?");
        q.addPossibleAnswer("one", "one");
        q.addPossibleAnswer("two", "two");
        q.addPossibleAnswer("three", "three");
        q.addPossibleAnswer("four", "four");
        //how many people should answer it
        q.setAnswersNeeded(3);
        //if they can skip it
        q.setNextButtonType(Question.NEXT_BUTTON_TYPE_NEXT);
        //a unique identifier, so the observer can tell that it is THIS question that finished
        q.setIdentifier("numOfApples1");
        //vehicle to pass any information that the observer 
        q.addArgument("ArgumentA", "That observer is horrible");
        //Register the question with the Detective (Singleton)
        Detective.SHERLOCK.addQuestion(q);

        return "Started counting apples";
    }

// <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
}
