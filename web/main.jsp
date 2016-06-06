<%--
    Document   : AnswerQuestions
    Created on : Aug 15, 2012, 3:01:26 PM
    Author     : alianos
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <script type="text/javascript" src="${pageContext.request.servletContext.contextPath}/js/lib/jquery-1.8.0.min.js?v=1"></script>
        <script type="text/javascript" src="${pageContext.request.servletContext.contextPath}/js/questionHandling.js?v=1"></script>
        <script type="text/javascript" src="${pageContext.request.servletContext.contextPath}/js/cp.js?v=1"></script>
        <script type="text/javascript" src="${pageContext.request.servletContext.contextPath}/js/UI.js?v=1"></script>
        <script>var cpath = "${pageContext.request.servletContext.contextPath}"</script>
        <link rel="stylesheet" type="text/css" href="${pageContext.request.servletContext.contextPath}/css/client.css?v=1
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Crowdsourcing Platform Demo Interface</title>
        <script>


            $(document).ready(function () {
                window.askNextQuestionTimeout;
                setCookie("lastAsked", new Date().getTime());
                $("#loginBlock").html(getLoginPanel());

                updateInterface();

                getCounters();
                setInterval(function () {
                    getCounters();
                }, 30000);
            });


        </script>
    </head>
    <body>
        <div id="mainContainer">
            <div id="topPanel">
                <div id="topLeftPanel">Crowdsourcing Platform Demo Interface</div>
                <div id="topRightPanel">
                    <div id="logoutBlock">
                        <div id="username" class="topUsername"></div>
                        <div id="subToolbarRight">
                            <span id="signOutButton" class="a" onclick="signOut();">Sign Out</span>
                        </div>
                    </div>
                    <div id="loginBlock"></div>
                </div>
            </div>
            <div id ="mainWrapper">
                <div id="sidePanel">
                    <button id="startAskingButton" onclick="getQuestions();" class="standarButton greenButton sidePanelButton">Start Asking</button>
                    <div id="countdown" class="timer">&nbsp;</div>
                    <br><br>
                    <div id="extraButtons">
                        Questions answered by all users
                        <div class="counters" id="totalCounter">0</div>
                        <br>Questions answered by you
                        <div class="counters" id="personalCounter">0</div>
                    </div>
                    <br>

                </div>
                <div id="infoWrapper"></div><div id="mainContent">Hello, and welcome.<br><br>You can login here with any username and password.</div>
                <div style="clear:both">&nbsp;</div>
            </div>
            <div id="footer">
                This research was sponsored by the University of Portsmouth. You can contact the author at 
                <span style="color:black;">andreas.lianos@port.ac.uk</span>
            </div>
        </div>
    </body>
</html>
