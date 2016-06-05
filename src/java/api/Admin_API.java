/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
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
import tools.Globals;

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

        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        try {
            StringBuilder result = new StringBuilder();

            String controller = request.getParameter("controller");
            String action = request.getParameter("action");

            //QUESTIONS
            if (controller.equals("questions")) {
                if (action.equals("showPending")) {
                    String html = tools.printTools.toHtml(Globals.sherlock.getPendingQuestions());
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
       
        AppleCounter counter = new AppleCounter();

        Q_GenericOptionsList q = new Q_GenericOptionsList();
        q.setQuestion("How many apples?");
        q.addPossibleAnswer("one", "one");
        q.addPossibleAnswer("two", "two");
        q.addPossibleAnswer("three", "three");
        q.addPossibleAnswer("four", "four");
        q.setAnswersNeeded(1);
        q.setNextButtonType(Question.NEXT_BUTTON_TYPE_NOBUTTON);
        q.setIdentifier("numOfApples1");
        q.addArgument("username", "a");
        q.registerObserver(counter);
        Globals.sherlock.addQuestion(q);
        
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
