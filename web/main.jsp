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
        <script type="text/javascript" src="${pageContext.request.servletContext.contextPath}/js/formatQuestions.js?v=1"></script>
        <script type="text/javascript" src="${pageContext.request.servletContext.contextPath}/js/tutorial.js?v=1"></script>
        <script type="text/javascript" src="${pageContext.request.servletContext.contextPath}/js/login.js?v=1"></script>
        <script>var cpath = "${pageContext.request.servletContext.contextPath}"</script>
        <link rel="stylesheet" type="text/css" href="${pageContext.request.servletContext.contextPath}/css/client.css?v=1">
        <link rel="stylesheet" type="text/css" href="${pageContext.request.servletContext.contextPath}/css/slidingPanels.css?v=1">
        <link rel="stylesheet" type="text/css" href="${pageContext.request.servletContext.contextPath}/css/results.css?v=1">
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Market Troll</title>
        <script>            
            function getCounters(){
                jQuery.ajax({
                    url:"${pageContext.request.servletContext.contextPath}/Client_API",
                    data:{
                        "controller":"counters",
                        "action":"getUserSpecific",
                        "sessionID":getCookie("sessionID")}
                }).done(function(data) {
                    data = getJson(data);
                    if(data.error == "notLoggedIn"){
                        signOut("timeout");
                        updateCounters( {userAnswered:0} );
                    }
                    else{
                        updateCounters( data );
                    }                    
                });
                jQuery.ajax({
                    url:"${pageContext.request.servletContext.contextPath}/PublicClient_API",
                    data:{
                        "controller":"counters",
                        "action":"getGeneric"}
                }).done(function(data) {
                    data = getJson(data);
                    updateCounters( data );
                });
            }            
            function updateCounters(data){
                if( data === null || data === ""){        
                    return "incorect json";
                }
                var totalQuestions = data["totalQuestions"];
                var totalAnswered = data["totalAnswered"];
                var userAnswered = data["userAnswered"];
                
                $totalDiv = $("#totalCounter");
                $personalDiv = $("#personalCounter");
                
                if(totalQuestions!==null){
                    $totalDiv.html(totalAnswered);
                }
                if(userAnswered!==null){
                    $personalDiv.html(userAnswered);
                }
                
            }
            
            function backToStart(){
                loadTutorial();
                updateInterface();
            }
            
            /*
             * Gets a new question, if nothing is loaded in the main frame.             
             */
            function getQuestionIfNeeded(){   
                //frames that require an update later on, sneak in a hidden element
                var $updateme = $(".updateme");
                if($updateme.length){                    
                    getQuestions();
                }                
            }
            
            function getQuestions(){
                //introduces a delay between requests for new questions.
                var remaining = 0-(new Date().getTime() - getCookie("lastAsked"));
                if(remaining<0){remaining=0;}                
                if(window.askNextQuestionTimeout !== undefined){
                    clearTimeout(window.askNextQuestionTimeout);
                }
                window.askNextQuestionTimeout = window.setTimeout("getQuestionsRequest()", remaining);  
                return remaining;
            }
            
            function getQuestionsRequest(){
                startAutoUpdate(); 
                getCounters();
                //note when we last asked, so we dont spam answer buttons.
                setCookie("lastAsked",new Date().getTime()); 
                jQuery.ajax({
                    url:"${pageContext.request.servletContext.contextPath}/Client_API",
                    data:{
                        "controller":"questions",
                        "action":"getAvailable",
                        "sessionID":getCookie("sessionID")}
                }).done(function(data) {
                    data = getJson(data);
                    if(data.error == "notLoggedIn"){
                        signOut("timeout");
                    }
                    else{
                        setMainFrame( formatedQuestion(data) );
                        disableAnswerButtons();
                        window.setTimeout("enableAnswerButtons()", 0);  
                    }
                });
            }            

            function getSimilariryQuestion(){
                //find the displayed question, if any
                var $active = jQuery("#activeQuestion");
                var id = $active.attr("questionId");
                if(id!=null && id!=""){
                    jQuery.ajax({
                        url:"${pageContext.request.servletContext.contextPath}/Client_API",
                        data:{
                            "controller":"questions",
                            "action":"getSimilarityQuestion",
                            "originalQuestion":id,
                            "sessionID":getCookie("sessionID")}
                    }).done(function(data) {
                        data = getJson(data);
                        if(data.error == "notLoggedIn"){
                            signOut("timeout");
                        }
                        else{
                            setMainFrame( formatedQuestion(data) );
                            disableAnswerButtons();
                            window.setTimeout("enableAnswerButtons()", 5500);  
                        }
                    });
                }
                else{
                    setMainFrame( "There was no active question." );
                }
            }
            
            function showResults(){
                stopAutoUpdate(); 
                jQuery.ajax({
                    url:"${pageContext.request.servletContext.contextPath}/Client_API",
                    data:{
                        "controller":"results",
                        "action":"showResults",
                        "sessionID":getCookie("sessionID")}
                }).done(function(data) {
                    data = getJson(data);
                    if(data.error == "notLoggedIn"){
                        signOut("timeout");
                    }
                    else{
                        getQuestions();
                    }
                });
                
                
                toggleButton($("#showResultsButton"), "off");
                window.setTimeout('toggleButton($("#showResultsButton"), "on");', 5000); 
            }
            
            function giveResultsFeedback(usage){
                stopAutoUpdate(); 
                jQuery.ajax({
                    url:"${pageContext.request.servletContext.contextPath}/Client_API",
                    data:{
                        "controller":"results",
                        "action":"giveFeedback",
                        "sessionID":getCookie("sessionID")}
                }).done(function(data) {
                    data = getJson(data);
                    if(data.error == "notLoggedIn"){
                        signOut("timeout");
                    }
                    else{
                        getQuestions();
                    }
                });
            }
            
            function toggleStartAskingButton(state){
                toggleButton($("#startAskingButton"), state);
                toggleButton($("#showResultsButton"), state);
            }

            function canAsk(){
                if(getCookie("username") != ""){
                    toggleStartAskingButton("on");
                    return true;
                }
                else{
                    toggleStartAskingButton("off");
                    return false;
                }
            }

            function toggleButton($button, state){
                if(state == 'off'){
                    $button.attr('disabled','disabled');
                }
                else if(state == 'on'){
                    $button.removeAttr('disabled');
                }
                else if(state == 'toggle'){
                    alert('toggling button not supported');
                }
            }

            function setMainFrame(html){
                jQuery("#mainContent").html(html);                
            }
            
            function setCookie(cname, cvalue) {
                //                var d = new Date();
                //                d.setTime(d.getTime() + (exdays*24*60*60*1000));
                //                var expires = "expires="+d.toGMTString();
                document.cookie = cname + "=" + cvalue ;
            }
            
            function getCookie(cname) {
                var name = cname + "=";
                var ca = document.cookie.split(';');
                for(var i=0; i<ca.length; i++) {
                    var c = ca[i];
                    while (c.charAt(0)==' ') c = c.substring(1);
                    if (c.indexOf(name) != -1) {
                        var stringValue = c.substring(name.length,c.length);
                    
                        if(stringValue.toLowerCase() == "true"){
                            return true;
                        }
                        else if(stringValue.toLowerCase() == "false"){
                            return false;
                        }
                        else return stringValue;
                    }
                }
                return "";
            }

            function deleteAllCookies() {
                var cookies = document.cookie.split(";");

                for (var i = 0; i < cookies.length; i++) {
                    var cookie = cookies[i];
                    var eqPos = cookie.indexOf("=");
                    var name = eqPos > -1 ? cookie.substr(0, eqPos) : cookie;
                    document.cookie = name + "=;expires=Thu, 01 Jan 1970 00:00:00 GMT";
                }
            }
            
            $(document).ready(function() {
                $("#signOutButton").hide();       
                window.askNextQuestionTimeout;
                setCookie("lastAsked", new Date().getTime()); 
                if(getCookie("sessionID")!=""){
                    updateInterface();           
                }
                else{ 
                    setMainFrame(loadTutorial());                
                }    
                
                updateInterface();
                
                getCounters();
                setInterval(function(){getCounters();},30000);
            });


        </script>
    </head>
    <body>
        <div id="mainContainer">
            <div id="topPanel">
                <div id="topLeftPanel">MarketTroll</div>
                <div id="topRightPanel">
                    <div id="username" class="topUsername"></div>
                    <div id="subToolbarRight">
                        <span id="backToStartButton" class="a" onclick="backToStart();">Back to start</span>
                        <span id="signOutButton" class="a" onclick="signOut();">Sign Out</span>
                    </div>

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
                    <button id="showResultsButton" onclick="showResults();" class="standarButton greenButton sidePanelButton">Show Results</button>
                </div>
                <div id="infoWrapper"></div><div id="mainContent"></div>
                <div style="clear:both">&nbsp;</div>
            </div>
            <div id="footer">
                This research was sponsored by the University of Portsmouth. You can contact the author at 
                <span style="color:black;">andreas.lianos@port.ac.uk</span>
            </div>
        </div>
    </body>
</html>
