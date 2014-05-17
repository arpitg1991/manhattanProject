manhattanProject
================

Top secret cloud project

Team members-
----------------

Arpit Gupta - <b>arpitg1991</b>
<br>
Anjishnu Kumar - <b>anjishnu</b>
<br>
Vipul Raheja - <b>vipulraheja</b>


<b>APIs</b>
------------------------
Post Event : /createPost
<br>
Get Event : /getPost
<br>
Post Comment : /createComment
<br>
Expected JSONS : Post Event : {"post":{"exp":"900","lon":"-73.960543","text":"VVAan","userId":"123","catId":"E","lat":"40.808142"}}
<br>
Get Event : /getPost?lat=-42&lon=70 received JSON 
<br>

<b>TODOs</b>
------------------------
- Vipul : Get UserID/UserName/Thumbnail from FB
- Vipul : Integrate Twitter Datamining code into Seekr
- Vipul : Convert video to GIF to make it mobile friendly
- Arpit : Modify DB to incorporate user names and thumbnails
- Arpit : Remove Posts if X no. of ppl commented
- Anjishnu: - Integrate comments/chat with API
- Anjishnu: Overlay Panoramio useless map with cool WebView

DONE -
--------------
- Anjishnu: Integrate makeNewPost with API
- Add makePost activity (Anjishnu) 
- Add API for getComments?postId=abcXYZ (Arpit)
- Added chat section.
- Get the Map to start working (Anjishnu)
- Code to populate ListView and CommentViews (Anjishnu)

Tech Discussion-
-----------------------
Software Architecture:
MVC?
Anjishnu - Ham ko naa pata

Controller Stack:
Ruby on Rails?
Scala?

<br>
Anjishnu - I'm leaning towards Scala for a bunch of reasons-
<br>1) The companies I want to work for (Coursera/LinkedIn/Twitter) are all migrating to Scala
<br>2) I already know Java - so Scala is like a JVM Power up
<br>3) Ruby and Python as languages are very close to each other in terms of syntax and capabilities - Scala is very different. 
<br>4) RoR devs are a dime-a-dozen, there's no shortage of job opportunities, but the average quality of those jobs isn't very high. Scala has much fever developers and the average job quality is pretty high.


DB:
Mongo?
HBase?
Cassandra?

<br>Anj - No experience with any of them. Would appreciate input from someone who has played with each. Leaning towards Mongo/Cassandra simply because of the companies associated with them.

View:
Bootstrap?
Reuse some appealing templates?

<br>Anj - We were leaning towards making an Android app rather than a WebApp. I am a fan of bootstrap in case we go the web dev route though.

---------------------
Please build something that we can reuse later for the project
-----------------------------------------------------------------

User Post ->
<br>1) Free Text (Character limit ?)
<br>2) Valid Till ( defaults to 1 hour) 
<br>3) Event Location (Defaults to User Location)
<br><br>
Feed -> 
<br>1) Sorted by (Distance, Popularity, Expiration)
<br>2) (Followers?)
<br>3) Post as anonymous 

<br>
<b>UPDATES/INDIVIDUAL</b>:

<b>31st March - Anj:</b> I've spent the whole night tweaking and playing with GUI elements and breaking the code over and over understanding how to use DataObservers. I think I have understood most of it now and can tweak views with relative ease. I've also downloaded some Custom UI libraries for use in the final project. I even implemented a relatively simple piece of code that samples the image dataset, finds a good candidate, and then blurs it to make it the background image. This is gonna be (or at least Look) awesome.
<br>


