//  TODO:convert to JSP


var myClientId = '517342657945-qv1ltq618ijj9edusgnnbpmbghkatc2q.apps.googleusercontent.com';
var auth2;

function startApp() {
    console.log(">startApp");
    gapi.load('auth2', function () {

        // Retrieve the singleton for the GoogleAuth library and setup the client.
        gapi.auth2.init({
            client_id: myClientId,
//            cookiepolicy: 'single_host_origin',
            fetch_basic_profile: false, // By default, the fetch_basic_profile parameter of gapi.auth2.init() is set to true, which will automatically add 'email profile openid' as scope.
            scope: 'email',
            ux_mode: 'redirect', // The UX mode to use for the sign-in flow. By default, it will open the consent flow in a popup. Valid values are popup and redirect.
            redirect_uri: 'http://localhost:8080/test/' //If using ux_mode='redirect', this parameter allows you to override the default redirect_uri that will be used at the end of the consent flow. 
                    //// The default redirect_uri is the current URL stripped of query parameters and hash fragment.
        }).then(function (googleAuth) {
            console.log('init');
            auth2 = googleAuth;
            // Use the then() method to get a Promise that is resolved when the gapi.auth2.GoogleAuth object finishes initializing.
            auth2.then(function (auth2) {
                console.log(">loaded googleAuth: "+auth2);
                

                checkIfUserLoggedIn(); // this happens after redirect
            }, function (error) {
                console.log(error); // if GoogleAuth failed to initialize.
            });
        });
    });
}
$(function () {
    console.log("ready");
    var $signInButton = $('#googleSignIn');
    $signInButton.click(onClickSignIn);

    function hideSignInButton() {
        $signInButton.hide();
    }

    startApp();
});


function checkIfUserLoggedIn() {
    console.log(">checkIfUserLoggedIn");
    // auth2 is initialized with gapi.auth2.init() and a user is signed in.
    if (auth2.isSignedIn.get()) { // true if the user is signed in, or false if the user is signed out or the GoogleAuth object isn't initialized.
        var user = auth2.currentUser.get();
        printUser(user);
        sendOut(user);
    }
}

function onClickSignIn(e) {
    console.log("onClickSignIn");

    auth2.signIn().then(
            function (googleUser) {
                printUser(googleUser);
                printUser2();
                var id_token = googleUser.getAuthResponse().id_token;
                console.log(id_token);
                sendOut(googleUser);
            }, function (error) {
        alert(JSON.stringify(error, undefined, 2));
    });
}

function sendOut(googleUser) {
    console.log(">sendOut");
    $.ajax({
        method: "POST",
        url: "api/test",
        data: googleUser.getAuthResponse().id_token
    })
            .done(function (msg) {
                console.log("msg: " + msg);
            }).fail(function (jqXHR, textStatus, errorThrown) {
        console.log("error: " + textStatus);
    });
}

function printUser2() {
    console.log("printUser2");
    // auth2 is initialized with gapi.auth2.init() and a user is signed in.
    if (auth2.isSignedIn.get()) { // true if the user is signed in, or false if the user is signed out or the GoogleAuth object isn't initialized.
        printUser(auth2.currentUser.get());

    }
}
function printUser(googleUser) {

    // Useful data for your client-side scripts:
    var profile = googleUser.getBasicProfile();
    console.log("ID: " + profile.getId()); // Don't send this directly to your server!
    console.log('Full Name: ' + profile.getName());
    console.log('Given Name: ' + profile.getGivenName());
    console.log('Family Name: ' + profile.getFamilyName());
    console.log("Image URL: " + profile.getImageUrl());
    console.log("Email: " + profile.getEmail());

    // The ID token you need to pass to your backend:
    var id_token = googleUser.getAuthResponse().id_token;
    console.log("ID Token: " + id_token);
}

function signOut() {
    var auth2 = gapi.auth2.getAuthInstance(); //  You must initialize the GoogleAuth object with gapi.auth2.init() before calling this method.
    auth2.signOut().then(function () {
        console.log('User signed out.');
    });
}