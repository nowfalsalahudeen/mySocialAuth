# mySocialAuth

add defaultconfig (android section) in app build.gradle
manifestPlaceholders = [
                facebook_api_key   : 'my valid facebook app key',
                twitter_api_key    : 'my valid twitter app key',
                twitter_api_secret : 'my valid twitter app secret'
        ]




1. Dependency
implementation 'com.github.nowfalsalahudeen:mySocialAuth:1.4'
2. Social Network Application keys
You must add your valid App key and secrets in Manifest placeholders, for both Facebook and Twitter. Google does not require this. So in your app's build.gradle file, add the following:

android {
    defaultConfig {
        manifestPlaceholders = [
                facebook_api_key   : 'my valid facebook app key',
                twitter_api_key    : 'my valid twitter app key',
                twitter_api_secret : 'my valid twitter app secret'
        ]
    }
}

add 
<meta-data
            android:name="google_server_client_id"
            android:value="10294852845-url76h4plhri6ppjabcnj7tqskil22jc.apps.googleusercontent.com" />

in your project manifest for get idToken for  backend authentication

3. Extend NsSocialActivity
Every social network SDK uses the startActivityForResult in one way or another. In the spirit of making stuff as easy as possible, this lib provides a Base Activity class, NsSocialActivity which handles registering, unregistering, request codes and the lot. Just extend in your Activities and you are done.

4. Endpoint initialization
Prior to using, the Endpoints you need must be initialized. A good place to do this is in the onCreate of your app's Application class. And you can do so like this:

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        GoogleEndpoint.initialize(this)
        FacebookEndpoint.initialize(this)
    }
}
Usage
All three wrappers, GoogleEndpoint, FacebookEndpoint and TwitterEndpoint, implement the RoguinEndpoint interface. So they all expose the following:

val isSignedIn for checking the Sign In status for your app.
fun requestSignIn for starting a new Sing In flow in your app.
fun requestSignOut for starting a new Sign Out flow in your app.
fun requestProfile for getting Basic Profile information of the logged in user.
The following code is an outtake from the demo project in this repository and shows how simple signing in and out of all three networks is with Roguin.

googleButton.setOnClickListener {
    if (googleEndpoint.isSignedIn) {
        googleEndpoint.requestSignOut { success ->
            if (success) {
                googleStatus.text = "Google is DISCONNECTED"
            }
        }
    } else {
        googleEndpoint.requestSignIn { success, token, error ->
            if (success) {
                googleStatus.text = "Google is CONNECTED"
            }
        }
    }
}

facebookButton.setOnClickListener {
    if (facebookEndpoint.isSignedIn) {
        facebookEndpoint.requestSignOut { success ->
            if (success) {
                facebookStatus.text = "Facebook is DISCONNECTED"
            }
        }
    } else {
        facebookEndpoint.requestSignIn { success, token, error ->
              if (success) {
                facebookStatus.text = "Facebook is CONNECTED"
            }
        }
    }
}



