/*Author: Andreas Lianos, 2013 */

//### UI functions

function startAutoUpdate() {
    stopAutoUpdate();
    window.autoUpdate = setInterval(function () {
        getQuestionIfNeeded();
    }, 10000);
}
function stopAutoUpdate() {
    clearInterval(window.autoUpdate);
}

/*
 * You can hide a div with the updateme class to tell the UI to start getting
 * Questions. Bad practice, but a common one.
 */
function getQuestionIfNeeded() {
    //frames that require an update later on, sneak in a hidden element
    var $updateme = $(".updateme");
    if ($updateme.length) {
        getQuestions();
    }
}

function updateCounters(data) {
    if (data === null || data === "") {
        return "incorect json";
    }
    var totalQuestions = data["totalQuestions"];
    var totalAnswered = data["totalAnswered"];
    var userAnswered = data["userAnswered"];

    $totalDiv = $("#totalCounter");
    $personalDiv = $("#personalCounter");

    if (totalQuestions !== null) {
        $totalDiv.html(totalAnswered);
    }
    if (userAnswered !== null) {
        $personalDiv.html(userAnswered);
    }

}

function toggleStartAskingButton(state) {
    toggleButton($("#startAskingButton"), state);
    toggleButton($("#showResultsButton"), state);
}

function canAsk() {
    if (getCookie("username") != "") {
        toggleStartAskingButton("on");
        return true;
    } else {
        toggleStartAskingButton("off");
        return false;
    }
}

function toggleButton($button, state) {
    if (state == 'off') {
        $button.attr('disabled', 'disabled');
    } else if (state == 'on') {
        $button.removeAttr('disabled');
    } else if (state == 'toggle') {
        alert('toggling button not supported');
    }
}

function setMainFrame(html) {
    jQuery("#mainContent").html(html);
}

// ##### Login and cookie control
function setCookie(cname, cvalue) {
    //                var d = new Date();
    //                d.setTime(d.getTime() + (exdays*24*60*60*1000));
    //                var expires = "expires="+d.toGMTString();
    document.cookie = cname + "=" + cvalue;
}

function getCookie(cname) {
    var name = cname + "=";
    var ca = document.cookie.split(';');
    for (var i = 0; i < ca.length; i++) {
        var c = ca[i];
        while (c.charAt(0) == ' ')
            c = c.substring(1);
        if (c.indexOf(name) != -1) {
            var stringValue = c.substring(name.length, c.length);

            if (stringValue.toLowerCase() == "true") {
                return true;
            } else if (stringValue.toLowerCase() == "false") {
                return false;
            } else
                return stringValue;
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

function login(username, password, showPanel) {

    setCookie("username", username);
    setCookie("sessionID", username);

    getCounters();
    startAutoUpdate();

    updateInterface();

    return true;
}


/**
 * Updates the interface with the elements needed, if a clickworker just logged in
 */
function updateInterface()
{

    //do things that need to be done, when logged in or out
    if (isLoggedIn()) {
        //show the signout block
        $("#loginBlock").hide();
        $("#logoutBlock").show();
        toggleStartAskingButton("on");
    } else {
        //hide the sign out button
        $("#loginBlock").show();
        $("#logoutBlock").hide();

        toggleStartAskingButton("off");
    }

    //make sure the visual username, matches the cookie
    $("#username").html(getCookie("username"));
}

function isLoggedIn() {
    return (getCookie("sessionID") != "");
}


function signOut(reason) {
    //if there is a visual perception that someone is logged in, 
    //then log out and move back to the starting page.   
    if ($("#username").text() != "") {
        if (reason == "timeout") {
            //
        }
    }
    if (isLoggedIn()) {
        deleteAllCookies();
        getCounters();
    }
    updateInterface();
}

function getLoginPanel() {
    var $div = $("<div>");

    var $panel = $div.clone();
    $panel.attr("id", "loginPanel");
    var $inputPanel = $("<div>", {
        "style": "text-align:right;"
    });
    var $usernameInput = $("<input/>", {
        "id": "usernameInput",
        "class": "panelInput",
        "maxlength": "50",
        "style": "width:300px; margin:0 92px 0 0;",
        "placeholder": "Username"
    });
    var $passwordInput = $("<input/>", {
        "id": "passwordInput",
        "class": "panelInput",
        "maxlength": "50",
        "style": "width:300px;",
        "placeholder": "Password",
        "type": "password"
    });
    var $loginButton = $("<button>", {
        "class": "standarButton greenButton right",
        "onclick": "login($('#usernameInput').val(),$('#passwordInput').val(), true);"
    });
    $loginButton.html("Login");
    $usernameInput.keypress(function (e) {//default action when enter is hit
        if (e.which == 13) {
            e.preventDefault();
            $loginButton.click();
        }
    });
    $passwordInput.keypress(function (e) {//default action when enter is hit
        if (e.which == 13) {
            e.preventDefault();
            $loginButton.click();
        }
    });
    $inputPanel.append($usernameInput).append("<br>").
            append($passwordInput).append("").
            append($loginButton);

    $panel.append($inputPanel);
    return $panel;
}