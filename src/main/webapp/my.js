
$(function () {
    console.log("ready");
    var auth2;
    var $signInButton = $('#googleSignIn');
    var $signOutButton = $('#googleSignOut');
    var $authenticatedControls = $('div.authenticated');
    var $userEmailSpan = $authenticatedControls.find(".userEmail");
    var clientId = $('input#clientId').val();
    $signInButton.click(onClickSignIn);
    $signOutButton.click(onClickSignOut);

    function showSignedInUserControls(email) {
        console.log('>showSignedInUserControls: email=' + email);
        $signInButton.hide();
        $userEmailSpan.text(email);
        $authenticatedControls.show();
    }
    function showSignedOutUserControls() {
        console.log('>showSignedOutUserControls');
        $signInButton.show();
        $authenticatedControls.hide();
    }

    function onClickSignOut() {
        console.log('>onClickSignOut');
        var auth2 = gapi.auth2.getAuthInstance(); //  You must initialize the GoogleAuth object with gapi.auth2.init() before calling this method.
        auth2.signOut().then(onSignOut); // A Promise that is fulfilled when the user has been signed out.
    }

    function onSignOut() {
        console.log('>onSignOut');
        showSignedOutUserControls();
    }
    function onSignIn(user) {
        console.log('>onSignIn');
        printUser(user);
        signIntoBackEnd(user);
    }

    function onSignIntoBackEnd(email) {
        console.log('>onSignIntoBackEnd: email=' + email);
        showSignedInUserControls(email);
    }
    function printUser(googleUser) {
        console.log('>printUser: ' + googleUser.isSignedIn());
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

    function checkIfUserSignedIn() {
        console.log(">checkIfUserSignedIn");
        var user = auth2.currentUser.get();// Returns a GoogleUser object that represents the current user.
        if (user && user.isSignedIn()) { // true if the user is signed in 
            onSignIn(user);
        } else {
            onSignOut();
        }
    }

    function signinChanged(val) { //  true when the user signs in, and false when the user signs out.
        console.log('>signinChanged: ', val);
    }


    function userChanged(user) {
        console.log('>userChanged: ', user);
    }
    function initAuth() {
        console.log(">initAuth with client id: " + clientId);
        // Retrieve the singleton for the GoogleAuth library and setup the client.
        gapi.auth2.init({
            client_id: clientId,
            fetch_basic_profile: false, // By default, the fetch_basic_profile parameter of gapi.auth2.init() is set to true, which will automatically add 'email profile openid' as scope.
            scope: 'email',
            ux_mode: 'redirect', // The UX mode to use for the sign-in flow. By default, it will open the consent flow in a popup. Valid values are popup and redirect.
            redirect_uri: 'http://localhost:8080/test/' //If using ux_mode='redirect', this parameter allows you to override the default redirect_uri that will be used at the end of the consent flow. 
                    //// The default redirect_uri is the current URL stripped of query parameters and hash fragment.
        }).then(function (googleAuth) {
            console.log('>initAuth Loading');
            auth2 = googleAuth;
            auth2.isSignedIn.listen(signinChanged);// Listen for sign-in state changes.
            auth2.currentUser.listen(userChanged);// Listen for changes to current user.
            // Use the then() method to get a Promise that is resolved when the gapi.auth2.GoogleAuth object finishes initializing.
            auth2.then(onLoadedAuth, function (error) {
                console.log(error); // if GoogleAuth failed to initialize.
            });
        });
    }

    function onLoadedAuth(auth2) {
        console.log(">onLoadedAuth");
        checkIfUserSignedIn(); // this happens after redirect
    }

    function onClickSignIn(e) {
        console.log(">onClickSignIn");
        auth2.signIn().then(// Signs in the user with the options specified to gapi.auth2.init(). returns A Promise   with the GoogleUser
                onSignIn, function (error) {
                    console.log("failed to sign in: " + error);
                });
    }

    function signIntoBackEnd(googleUser) {
        console.log(">signIntoBackEnd");
        $.ajax({
            method: "POST",
            url: "api/test",
            data: googleUser.getAuthResponse().id_token,
            dataType: "json"
        })
                .done(function (data) {
                    console.log(">received data from server: " + JSON.stringify(data));
                    onSignIntoBackEnd(data.email);
                }).fail(function (jqXHR, textStatus, errorThrown) {
            console.log("error: " + textStatus);
        });
    }

    function startApp() {
        console.log(">startApp");
        gapi.load('auth2', initAuth); //load the auth2 library
    }

    startApp();
});



