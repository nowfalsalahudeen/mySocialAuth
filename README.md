# mySocialAuth

add defaultconfig (android section) in app build.gradle
manifestPlaceholders = [
                facebook_api_key   : 'my valid facebook app key',
                twitter_api_key    : 'my valid twitter app key',
                twitter_api_secret : 'my valid twitter app secret'
        ]

add 
<meta-data
            android:name="google_server_client_id"
            android:value="10294852845-url76h4plhri6ppjabcnj7tqskil22jc.apps.googleusercontent.com" />
in manifest
