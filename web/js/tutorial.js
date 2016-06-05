/*
 
 "Hi! Welcome to Market Troll. Market Troll is a platform where we test if it is feasible for a machine to make\n\
 decisions and assumptions, based on multiple user opinions aggregated together.\n\
 \n\
 It looks like a questionaire but it is not one. You do not have to finish every possible question. You can stop whenever\n\
 you feel like it. Every little helps.\n\
 \n\
 When and if you come back you might either find more questions, or there is a chance the system is reset and \n\
 you get the same questions. Whichever the case it is always helpfull that you answer.\n\
 \n\
 If you wish to come back I need a way to know it is you. You can either tell me your name, or I can just pick one\n\
 for you.\n\
 \n\
 Good. Next time you are here just tell me you are ... and I'll remember you. If you want I can email you that so\n\
 you won't forget it. I am a researcher and I treat your emails with respect.\n\
 \n\
 Great, lets get started!";
 
 */

function create() {
    var $nextButton = $('<button>', {
        "onclick": "progressPanels();",
        "class": "standarButton greyButton inGuideButton"
    });
    $nextButton.html("Next");

    $clearDiv = $('<div>', {
        "class": "clear"
    });
    $clearDiv.html(" ");

    $slidingPanels = $("<div>", {
        "id": "tutorialPanels",
        "class": "slidingPanels"
    });
    //PANELS
    //Panel 0 - Login Or Register
    var $panel0 = $("<div>", {
        "class": "panel active",
        "panelNumber": "0",
        "id": "panel0"
    });
    $panel0.html("<div class='slidingPanelContents'>Hi!<br>Welcome to Market Troll. Have you been here before?</div>");
    var $yesButton = $('<button>', {
        "onclick": "swapTo(getLoginPanel());",
        "class": "standarButton greenButton inGuideButton"
    });
    $yesButton.html("Yes, let me Log In");
    $panel0.append($yesButton);
    $panel0.append($clearDiv);

    

    $slidingPanels.append($panel0);

    return $slidingPanels;
}


function getResults(resultsNumber){
    var $panel = $("<div>",{          
        "id":"resultsPanel"
    }); 
    $.ajax({
        async: false,  
        type:    "GET",
        url:     "results_"+resultsNumber+".jsp",
        success: function(content) {
            $panel.append("<div class='slidingPanelContents'>"+content+"</div>");
        },
        error:   function() {
        // An error occurred
        }
    });    
    return $panel;
}


function progressPanels() {
    var $active = $(".panel.active");
    var nextPanelNumber = parseInt($active.attr("panelNumber")) + 1;
    var $next = $("[panelNumber=" + nextPanelNumber + "]");

    swap($active, $next);
}

function goBack() {
    var $active = $(".panel.active");
    var nextPanelNumber = parseInt($active.attr("panelNumber")) - 1;
    var $next = $("[panelNumber=" + nextPanelNumber + "]");

    swap($active, $next);
}

function swapTo($newPanel) {
    //reload the sliding panels framework if needed
    if($("#tutorialPanels").length == 0){
        loadTutorial();
    }
    
    var $active = $(".panel.active");
    //see if the panel exists
    var $oldPanel = $("#" + $newPanel.attr("id"));
    if ($oldPanel.length > 0) {
        $oldPanel.remove();
    }
    
    $slidingPanels.append($newPanel);
    swap($active, $newPanel);
}

function swap($thisOne, $thatOne) {
    $thisOne.css("left", "0").animate({
        left: "-670"
    }, 500, function() {
        $(this).removeClass("active").addClass("inactive");
    });
    $thatOne.css("left", "670px").animate({
        left: "0"
    }, 500, function() {
        $(this).removeClass("inactive").addClass("active");
    });

}

function swapBackwards($thisOne, $thatOne) {
    $thisOne.css("left", "0").animate({
        left: "670"
    }, 500, function() {
        $(this).removeClass("active").addClass("inactive");
    });
    $thatOne.css("left", "-670px").animate({
        left: "0"
    }, 500, function() {
        $(this).removeClass("inactive").addClass("active");
    });

}

function getTutorial() {
    return create();
}

function loadTutorial() {
    
    setMainFrame(getTutorial());
}



function getJson(data) {
    if (data == null) {
        data = "";
    }
    try {
        var json = jQuery.parseJSON(data);
    }
    catch (err) {
        json = null;
    }

    return json;
}