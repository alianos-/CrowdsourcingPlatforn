/*Author: Andreas Lianos, 2013 */

//### EXAMPLE API FUNCTIONS

//### YOU NEED TO UPDATE THOSE FUNCTIONS, IN ORDER TO FIT THEM TO YOUR UI.
//### This is a demo UI only. You should not attempt to extend, please pick
//### functions that you need and add them to your UI, and customize them to 
//### your code.


function getCounters() {
    jQuery.ajax({
        url: "${pageContext.request.servletContext.contextPath}/../Client_API",
        data: {
            "controller": "counters",
            "action": "getUserSpecific",
            "sessionID": getCookie("sessionID")}
    }).done(function (data) {
        data = getJson(data);
        if (data.error == "notLoggedIn") {
            signOut("timeout");
            updateCounters({userAnswered: 0});
        } else {
            updateCounters(data);
        }
    });
    jQuery.ajax({
        url: "${pageContext.request.servletContext.contextPath}/../PublicClient_API",
        data: {
            "controller": "counters",
            "action": "getGeneric"}
    }).done(function (data) {
        data = getJson(data);
        updateCounters(data);
    });
}

function getQuestions() {
    //introduces a delay between requests for new questions.
    var remaining = 0 - (new Date().getTime() - getCookie("lastAsked"));
    if (remaining < 0) {
        remaining = 0;
    }
    if (window.askNextQuestionTimeout !== undefined) {
        clearTimeout(window.askNextQuestionTimeout);
    }
    window.askNextQuestionTimeout = window.setTimeout("getQuestionsRequest()", remaining);
    return remaining;
}

function getQuestionsRequest() {
    startAutoUpdate();
    getCounters();
    //note when we last asked, so we dont spam answer buttons.
    setCookie("lastAsked", new Date().getTime());
    jQuery.ajax({
        url: "${pageContext.request.servletContext.contextPath}/../Client_API",
        data: {
            "controller": "questions",
            "action": "getAvailable",
            "sessionID": getCookie("sessionID")}
    }).done(function (data) {
        data = getJson(data);
        if (data.error == "notLoggedIn") {
            signOut("timeout");
        } else {
            setMainFrame(formatedQuestion(data));
            disableAnswerButtons();
            window.setTimeout("enableAnswerButtons()", 0);
        }
    });
}

function getJson(data) {
    if (data == null) {
        data = "";
    }
    try {
        var json = jQuery.parseJSON(data);
    } catch (err) {
        json = null;
    }

    return json;
}



