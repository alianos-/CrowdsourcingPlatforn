<%--
    Document   : AnswerQuestions
    Created on : Aug 15, 2012, 3:01:26 PM
    Author     : alianos
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <script type="text/javascript" src="${pageContext.request.servletContext.contextPath}/js/lib/jquery-1.8.0.min.js"></script>
        <script type="text/javascript" src="${pageContext.request.servletContext.contextPath}/js/formatQuestions.js"></script>
        <link rel="stylesheet" type="text/css" href="${pageContext.request.servletContext.contextPath}/css/admin.css">
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Admin Console</title>
    </head>
    <body>
        <script>
            function showPendingQuestions() {
                jQuery.ajax({
                    url: "${pageContext.request.servletContext.contextPath}/Admin_API",
                    data: {
                        "controller": "questions",
                        "action": "showPending"}
                }).done(function(data) {
                    jQuery("#mainWrapper").html(data);
                });
            }
            function togglePause() {
                jQuery.ajax({
                    url: "${pageContext.request.servletContext.contextPath}/Admin_API",
                    data: {
                        "controller": "system",
                        "action": "togglePause"}
                }).done(function(data) {
                    jQuery("#systemStatus").html(data);
                    jQuery("#mainWrapper").html(response);
                });
            }
           
            function test1() {
                jQuery.ajax({
                    url: "${pageContext.request.servletContext.contextPath}/Admin_API",
                    data: {
                        "controller": "testing",
                        "action": "test1"}
                }).done(function(response) {
                    jQuery("#mainWrapper").html(response);
                });
            }
           
        </script>
        <div><a href="main.jsp">User Page</a></div>
        <div>Questions 
            <button onclick="showPendingQuestions();">Show Pending</button></div>
        <div>System: <span id="systemStatus">Running</span>
            <button onclick="togglePause();">Toggle Pause</button></div>
        <div>Testing<button onclick="test1();">Test1</button></div>
        <hr><div><div id="sidePanel">
            </div>
            <div id="mainWrapper"></div></div>
    </body>
</html>
