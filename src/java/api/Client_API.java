/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package api;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import domain.questions.DetectiveQuestion;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.UUID;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import tools.GenericTools;
import tools.Globals;

/**
 *
 * @author alianos
 */
public class Client_API extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected synchronized void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

        try {

            String controller = request.getParameter("controller");
            String action = request.getParameter("action");

            String sessionID = request.getParameter("sessionID");

            if (controller == null || action == null || sessionID == null) {
                System.out.println(getClass().getName() + " The incoming request was missing important data\n" + request);
                return;
            }

            //normally you would get the userID from the sessionID, if the user was logged in
            //if the user was not logged in, you'd give him a temperary username
            //our demo interface sends the userID right away in the sessionID field, 
            //because no real sessions are going on.
            String userID = sessionID;

            JsonObject responseJson = new JsonObject();

            if (controller.equals("questions")) {
                if (action.equals("getAvailable")) {
                    DetectiveQuestion dq = Globals.sherlock.getNextQuestion(userID);
                    if (dq != null) {
                        String dqJsonString = new Gson().toJson(dq);
                        JsonParser parser = new JsonParser();
                        JsonObject dqJson = (JsonObject) parser.parse(dqJsonString);
                        responseJson = GenericTools.merge(responseJson, dqJson);
                    }

                }

            } else if (controller.equals("answer")) {
                if (action.equals("submit")) {
                    Globals.sherlock.answerQuestion(UUID.fromString(request.getParameter("answerID")), userID, request.getParameterMap());
                }
            }else if (controller.equals("counters")) {
                if (action.equals("getUserSpecific")) {
                    int userAnswered = 5;
                    responseJson.addProperty("userAnswered", userAnswered);
                }
            }

            out.print(responseJson.toString());

        } finally {
            out.close();
        }
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
