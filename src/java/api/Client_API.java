/*
 * Copyright (c) 2014-2016 Andreas Lianos
 * Licensed under the MIT Liecense (LICENSE.txt).
 * Author Andreas Lianos
 */
package api;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import domain.questions.Detective;
import domain.questions.DetectiveQuestion;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.UUID;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import tools.GenericTools;

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

            //normally you would get the userID from the sessionID if the user was logged in.
            //If the user was not logged in, you might want to give him a temperary username.
            //Our demo interface sends the userID right away in the sessionID field, 
            //because no real sessions are going on.
            String userID = sessionID;

            JsonObject responseJson = new JsonObject();

            if (controller.equals("questions")) {
                //returns the next available question to whoever asked
                if (action.equals("getAvailable")) {
                    DetectiveQuestion dq = Detective.SHERLOCK.getNextQuestion(userID);
                    if (dq != null) {
                        String dqJsonString = new Gson().toJson(dq);
                        JsonParser parser = new JsonParser();
                        JsonObject dqJson = (JsonObject) parser.parse(dqJsonString);
                        responseJson = GenericTools.merge(responseJson, dqJson);
                    }

                }

            } else if (controller.equals("answer")) {
                //Lets the detective know someone gave an answer
                if (action.equals("submit")) {
                    Detective.SHERLOCK.answerQuestion(UUID.fromString(request.getParameter("answerID")), userID, request.getParameterMap());
                }
            }else if (controller.equals("counters")) {
                //Requests how many answers this particular user has given.
                if (action.equals("getUserSpecific")) {
                    int userAnswered = Detective.SHERLOCK.getNumOfAnsweredQuestions(userID);
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
