package api;

import com.google.gson.JsonObject;
import domain.questions.Detective;
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
public class PublicClient_API extends HttpServlet {

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
            JsonObject responseJson = new JsonObject();

            String controller = request.getParameter("controller");
            String action = request.getParameter("action");

            //API for access without username and password, for actions that anyone can do
            //(e.g. login, register)
            
            if (controller == null || action == null) {
                System.out.println(getClass().getName() + " The incoming request was missing important data\n" + request);
                return;
            }
            if (controller.equals("counters")) {
                if (action.equals("getGeneric")) {
                    //Returns a couple of counters anyone can know
                    int totalQuestions = Detective.SHERLOCK.getNumOfTotalAnswersRequired();
                    int totalAnswered = Detective.SHERLOCK.getTotalAnswered();
                    responseJson.addProperty("totalQuestions", totalQuestions);
                    responseJson.addProperty("totalAnswered", totalAnswered);
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
