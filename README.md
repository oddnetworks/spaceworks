# spaceworks

## Blast off!

You will need a server running with at least V2 of [Oddworks](https://github.com/oddnetworks/oddworks).

We suggest using this to get started: [example-single-process](https://github.com/oddnetworks/example-single-process)

_Note:_ if you use a different server, or modify the views, you'll need to update the relationship names in `MainActivity`.

Once you have your server running, you'll need to update the `accessToken` which is located in `app/src/main/AndroidManifest.xml'

         <application
            ... />
            
            ...
            
                <meta-data
                    android:name="io.oddworks.accessToken"
                    android:value="your access token here"/>
         </application>
         
Then you'll want to update the `baseUrl` so that the SDK can talk to your server. Check out `app/src/main/java/io.oddworks.space/Space.java`

        // change
        apiCaller.setBaseUrl("http://192.168.12.111:3000/");
        
        // to
        apiCaller.setBaseUrl("http://your-oddworks-server/");
        