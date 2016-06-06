/*Author: Andreas Lianos, 2013 */

//### EXAMPLE RENDERING OF THE QUESTIONS

//### This functions can be used as is. You extend the library by adding options
//### to the DetectiveQuestion java class, and then picking up those options here
//### and doing whatever it it needs to be done with those options.
//### The last function in this file contains the upload ajax call, which 
//### you will have to update to match your url.


//intercept and handle form submissions
$(document).on('submit', "#multiple_options_form", function (e) {
    e.preventDefault();
    var $form = $(this);
    var data = {
        "answerID": $form.attr("answerID")
    }
    //add all the checked checkboxes to the answer
    var answers = new Array();
    var $checkboxes = $form.find('input:checkbox:checked');
    $checkboxes.each(function () {
        answers.push($(this).attr("answerCode"));
    });

    data['answerCode'] = answers;

    submitGenericAnswer(data);
});

/**
 * Takes as input the json formatted question as sent by the server,
 * and renders it to the browser using javascript. This is how the server and
 * the client exchange information.
 * 
 * @param {json} detectiveQuestion
 * @returns {String|formatedQuestion.$html|jQuery}
 */
function formatedQuestion(data) {
    if (data == null || data == "" || jQuery.isEmptyObject(data)) {
        return "It appears there are no more questions you can answer at the moment! Please check back later.<br>" +
                "(This page will auto-update when more questions become available)<div class='updateme'>&nbsp<div>";
    }
    
    var type = data["type"];
    var answers = data["possibleAnswers"];
    var derived = data["derivedQuestions"];   
    var allowMultipleAnswers = data["allowMultipleAnswers"];
    var nextButtonType = data["nextButtonType"];

    var $html = $('<div>').attr("id", "activeQuestion").attr("questionId", data.ID);
    var key;


    var $BlankIDontKnowButton = $('<button>', {
        "answerID": data.ID,
        "onclick": "submitNoAnswer(this);",
        "class": "standarButton greyButton"
    });

    var $skip = $('<button>', {
        "answerID": data.ID,
        "skip": "true",
        "onclick": "submitNoAnswer(this);",
        "class": "standarButton greyButton"
    });
    $skip.html("Skip");

    if (type === "unique_options") {
        if (answers !== null) {
            $html.html(data.question + "<br><br>");
            for (key in answers) {
                if (answers.hasOwnProperty(key) /*&& $.inArray(answers[key].answerString, exclude)==-1 */) {
                    var $button = $('<button>', {
                        "answerCode": answers[key].answerCode,
                        "answerID": data.ID,
                        "onclick": "submitButtonAnswer(this);",
                        "class": "standarButton greyButton"
                    });
                    $button.html(answers[key].answerString);
                }
                $html.append($button);
                if (answers[key].noSpace === false)
                {
                    $html.append($("<br>"));
                }
            }
        } else {
            $html.append($("The question has no answers! Bummer!"));
        }
    } else if (type === "multiple_options") {

        if (answers !== null) {
            var $form = $("<form>", {
                id: "multiple_options_form",
                answerID: data.ID
            });
            $form.html(data.question + "<br><br>");
            for (key in answers) {
                if (answers.hasOwnProperty(key)/* &&
                 $.inArray(answers[key].answerString, exclude)==-1*/) {
                    var $input = $('<input />', {
                        type: 'checkbox',
                        answerCode: answers[key].answerCode,
                        id: answers[key].answerCode
                    });
                    $input.after($("<span>" + answers[key].answerString + "</span>"));

                    $form.append($input).append("<br>");
                }
            }
            $form.append($("<br>"));
            var $submitButton = $('<button>', {
                "type": "submit",
                "class": "standarButton"
            });
            $submitButton.html("Submit");
            //the submission is intercepted by jQuery "on" event on the form ID

            $form.append($submitButton);

            $html.append($form);
        } else {
            $html.append($("The question has no answers! Bummer!"));
        }
    } else if (type === "optional_input_text") {
        if (answers !== null) {
            $html.html(data.question + "<br><br>");
            for (key in answers) {
                if (answers.hasOwnProperty(key) /*&& $.inArray(answers[key].answerString, exclude)==-1 */) {
                    if (answers[key].type === 1) {
                        var $button = $('<button>', {
                            "answerCode": answers[key].answerCode,
                            "answerID": data.ID,
                            "onclick": "submitButtonAnswer(this);",
                            "class": "standarButton"
                        });
                        $button.html(answers[key].answerString);
                    } else if (answers[key].type === 2) {
                        var $button = $('<button>', {
                            "answerCode": answers[key].answerCode,
                            "answerID": data.ID,
                            "onclick": "swapForInputField(this);",
                            "class": "standarButton"
                        });
                        $button.html("Let me add My own");
                    }
                }
                $html.append($button);
                if (answers[key].noSpace === false)
                {
                    $html.append($("<br>"));
                }
            }
        } else {
            $html.append($("The question has no answers! Bummer!"));
        }
    }

    if (type === "optional_input_text") {
        //dont add anything
    }
    if (nextButtonType === "premade_idontknow") {
        $BlankIDontKnowButton.html("I don't know");
        $html.append($BlankIDontKnowButton).append($("<br>"));
    }
    else if (nextButtonType === "premade_next") {
        $BlankIDontKnowButton.html("Next");
        $html.append($BlankIDontKnowButton).append($("<br>"));
    } else if (nextButtonType === "premade_nobutton") {
        //add bo button! Surpriiiiise!
    } else {
        $BlankIDontKnowButton.html(nextButtonType);
        $html.append($BlankIDontKnowButton).append($("<br>"));
    }

     //add an extra button for similarity question, if needed
    if ($.inArray("similarOptions", derived) > -1) {
        var $similarOptionsButton = $('<button>', {
            "id": "similarAnswersButton",
            "answerID": data.ID,
            "onclick": "getSimilariryQuestion();",
            "class": "standarButton"
        });
        $similarOptionsButton.html("Some of the answers here are the same!");
        $html.append($similarOptionsButton).append($("<br>"));
    }

    return $html;
}

/**
 * Takes a javascript object, extracts the appropriate options and submits the answer
 */
function swapForInputField(obj) {
    var $button = $(obj);
    var $input = $("<input>", {
        "answerCode": $button.attr("answerCode"),
        "class": "panelInput",
        "placeholder": "Your input"
    });
    $button.replaceWith($input);
    $input.focus();

    var $submitButton = $("<button>", {
        "answerID": $button.attr("answerID"),
        "answerCode": $button.attr("answerCode"),
        "class": "standarButton greenButton",
        "onclick": "submitButtonAnswer(this);"
    });
    $submitButton.html("Submit");

    $input.keypress(function (e) {//default action when enter is hit
        if (e.which === 13) {
            e.preventDefault();
            $submitButton.click();
        }
    });


    $input.after($submitButton);
}

/**
 *Disabled all answer buttons
 */
function disableAnswerButtons() {
    $("#activeQuestion button").each(function () {
        $(this).prop('disabled', true);
    });
}
/**
 *Enables all answer buttons
 */
function enableAnswerButtons() {
    $("#activeQuestion button").each(function () {
        $(this).prop('disabled', false);
    });
}

/**
 * Swaps the given object, for an input field
 */
function submitButtonAnswer(obj) {
    var $button = $(obj);
    var data = {
        "answerID": $button.attr("answerID"),
        "answerCode": $button.attr("answerCode")
    };
    //if you can find an input bot with that answerCode, send that as well
    var $input = $("input[answerCode='" + $button.attr("answerCode") + "']");
    if ($input.length > 0) {
        data["payload"] = $input.val();
    }
    submitGenericAnswer(data);
}

/**
 * Takes a javascript object, extracts the appropriate options, and lets the server know
 * that the user does not know the answer.
 */
function submitNoAnswer(obj) {
    var $button = $(obj);
    var data = {
        "answerID": $button.attr("answerID"),
        "skip": $button.attr("skip"),
        "isClueless": "0"
    };
    submitGenericAnswer(data);
}


/**
 * Takes an object with options, appends some defaults, and sends them to server
 */
function submitGenericAnswer(obj) {

    var data = {
        "controller": "answer",
        "action": "submit",
        "sessionID": getCookie("sessionID")
    };
    for (var attrname in obj) {
        data[attrname] = obj[attrname];
    }

    //intercept the answer, if there is no asnwercode, submit no answer instead
    if ((!data.hasOwnProperty("answerCode") || data.answerCode == "") && !data.hasOwnProperty("isClueless")) {
        submitNoAnswer({
            "answerID": data.answerID
        });
        return;
    }

    submitAnswer(data);
}

/**
 * Takes an object with options, and sends them to the server.
 */
function submitAnswer(data) {
    setMainFrame("Fetching next question... ");/*(If you answer too fast, there is an extra delay added here!)*/
    jQuery.ajax({
        url: "/app/Client_API",
        data: $.param(data, true),
        success: function (data) {
            data = getJson(data);
            if (data.error == "notLoggedIn") {
                signOut("timeout");
            } else {
                var cooldown = getQuestions();
                //                setMainFrame( "Fetching next question... (+"+Math.floor((cooldown/1000))+"sec delay)" );
            }
        }
    });
}