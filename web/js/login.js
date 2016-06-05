function login(username, password, showPanel) {

    setCookie("username", username);
    setCookie("sessionID", username);

    swapTo(getSuccessPanel());
    getCounters();
    startAutoUpdate();

    updateInterface();

    return true;
}

function startAutoUpdate() {
    stopAutoUpdate();
    window.autoUpdate = setInterval(function () {
        getQuestionIfNeeded();
    }, 10000);
}
function stopAutoUpdate() {
    clearInterval(window.autoUpdate);
}

/**
 * Updates the interface with the elements needed, if a clickworker just logged in
 */
function updateInterface()
{
    //add or remove the redeem button
    var $possibleRedeemButton = $("#redeemButton");
    
    //do things that need to be done, when logged in or out
    if (isLoggedIn()) {
        //show the signout button
        $("#signOutButton").show();
        $("#backToStartButton").hide();

        toggleStartAskingButton("on");
    } else {
        //hide the sign out button
        $("#signOutButton").hide();
        $("#backToStartButton").show();

        toggleStartAskingButton("off");
    }

    //make sure the visual username, matches the cookie
    $("#username").html(getCookie("username"));
}

function isLoggedIn() {
    return (getCookie("sessionID") != "");
}

/**
 * returns the json with the response
 */
function requestLogin(username, password) {
    var response;
    $.ajax({
        url: cpath + "/PublicClient_API",
        data: {
            "dataType": "json",
            "controller": "userManagement",
            "action": "login",
            "userID": username,
            "password": password
        },
        async: false
    }).done(function (data) {
        response = data
    });
    return getJson(response);
}

function validate(username) {
    var error = "";
    var legalCharacters = /^[a-z0-9_ ]*$/i; // allow letters, numbers, underscores and spaces

    if (username == "") {
        error = "You didn't enter a username.\n";
    } else if (username.length > 40) {
        error = "The username is cannot be longer than 40 characters.\n";
    } else if (!legalCharacters.test(username)) {
        error = "The username contains illegal characters. Only latin letters, numbers, underscore and spaces are allowd.\n";
    } else {

    }
    return error;
}

function getSuccessPanel() {
    var $successPanel = $("<div>", {
        "class": "inactive panel",
        "id": "successPanel"
    });
    var $intro = $("<div>");
    $intro.addClass("slidingPanelContents");
    $intro.html("Hey <span class='green highlight' >" + getCookie("username") + "</span>. Nice to see you! :)" +
            "<span class='updateme'>&nbsp;</span>");

    $successPanel.append($intro);

    return $successPanel;
}


function signOut(reason) {
    //if there is a visual perception that someone is logged in, 
    //then log out and move back to the starting page.   
    if ($("#username").text() != "") {
        if (reason == "timeout") {
            //
        }
        backToStart();
    }
    if (isLoggedIn()) {
        deleteAllCookies();
        getCounters();
    }
    updateInterface();
}

function requestSignOut() {
    $.ajax({
        url: cpath + "/Client_API",
        data: {
            "dataType": "json",
            "controller": "userManagement",
            "action": "logout",
            "sessionID": getCookie("sessionID")
        }
    });
}

function getLoginPanel() {
    var $div = $("<div>");

    var $panel = $div.clone();
    $panel.addClass("panel");
    $panel.addClass("inactive");
    $panel.attr("id", "loginPanel");
    var $intro = $div.clone();
    $intro.addClass("slidingPanelContents");
    $intro.html("Heeeeey! I know you from somewhere don't I?");

    var $inputPanel = $("<div>", {
        "class": "right",
        "style": "text-align:right;"
    });
    var $usernameInput = $("<input/>", {
        "id": "usernameInput",
        "class": "panelInput",
        "maxlength": "50",
        "style": "width:300px;",
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
        "class": "standarButton greenButton",
        "onclick": "login($('#usernameInput').val(),$('#passwordInput').val(), true);"
    });
    $loginButton.html("It's me!");
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
            append($passwordInput).append("<br>").
            append($loginButton);

    $panel.append($intro).append($inputPanel);
    return $panel;
}