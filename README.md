
<p align="center">
  <img src="https://github.com/MartinTiangco/VARPedia/blob/master/src/Application/assets/logo.png" alt="VARpedia by Martin and Will" />
</p>

VARpedia is designed to aid English learning to English to Speakers of Other Languages (ESOL) students aged between 18-25 years. The application caters to those who learn best visually, aurally or by reading/writing by presenting information in different formats. This information is referred to as ‘Creations’ of a chosen word the user wishes to learn about. The Creation offers information in formats such as a voiceover of results from Wikipedia with optional background music, a variety of images and a subtitle of the chosen word.


## Motivation
The motivation behind VARpedia is to give guidance to ESOL students about the English language. Due to us, not native English speakers, we had difficulty adjusting to the language in our first years in New Zealand. We believe that catering to different styles of learning will help the majority of English learners to be more acclimated to a new language.


## Screenshots
![](https://i.ibb.co/KGgYVLT/all3.png "Bootstrap, Red Devil and Sky themes")

## Technologies used
Built with
* Java
* JavaFX
* Bash
* CSS
* FXML/SceneBuilder


## Features
Basic features in VARpedia include creating a ‘Creation’ that consists of both visual and aural components to help the user learn about a term of his/her wish, playing/deleting a saved ‘Creation’, a quiz feature to give the user feedback on his/her learning progress, and additional individualised choices for better user experience.

Special features in VARpedia include:
* difficulty selection for the quiz 
* background music/Image selection and preview for the ‘Creation’
* theme selection in Settings Screen
* help buttons on individual screens for better user experience


## Installation
Before launching VARpedia, please make sure you are using Linux operating systems. VARpedia needs plugins such as “espeak”, “ffmpeg” and “wikit”. 


Type in these commands in the Linux terminal to install espeak. `apt-get install espeak 5` 


To install ffmpeg.
```
sudo add-apt-repository ppa:mc3man/trusty-media
sudo apt-get update
sudo apt-get install ffmpeg
sudo apt-get install frei0r-plugins
```

“wikit” requires “npm” and “nodejs” for installation. You can type this into the terminal to install these. 
```
sudo apt install nodejs 
sudo npm install wikit -g
```

Upon first launch, VARpedia will create a folder “Creation_Directory” in your current directory. This is where the ‘Creations’ will be stored. Click **‘Get Started!’** to begin.


**Important!**
To successfully run the application, an API key from Flickr is needed. To obtain one, visit https://www.flickr.com/services/api/ and create an app. Upon installation of the project, you will have a file called `flickr-api-keys.txt`. Replace the comment next to the API key with your own key.


Read the user manual for more information.


## API Reference
Wikit: A command line program for getting Wikipedia summaries easily.

https://www.npmjs.com/package/wikit


FFmpeg: A cross-platform solution to record, convert and stream audio and video.

https://www.ffmpeg.org/ 


Espeak: A compact open-source software speech synthesizer for English and other languages.

http://espeak.sourceforge.net/ 


Flickr4Java: API for Image gathering from Flickr.

https://github.com/boncey/Flickr4Java


## How to use?

To run this application, you need to run the latest version of Linux.


**Ensure the following files are in the current directory:**
```
flickr-api-keys.txt	
README.md
src	
VARpedia.sh
VARpedia_group25.jar
```
	
	
**Launching:**
Open a bash terminal in the same directory as the script file named "VARpedia.sh", and type in the following commands:

```
chmod 777 ./VARpedia.sh
./VARpedia.sh
```


## Credits
**Source Code Acknowledgement**

Our project uses adaptations from users' code. We thank their authors for their work.

Flickr API retrieval:
- Nasser Giacaman

Flickr API library:
- SmugMug, Inc.

CSS Themes:
- carldea - https://github.com/carldea/jfx9be/tree/master/chap15/LookNFeelChooser/src/jfx9be
- dicolar - https://github.com/dicolar/jbootx

**Music Track Acknowledgement**
The following tracks have been used in this application. They have been edited such that they are background music to Text-to-speech voices. 

We thank their authors for their work. 

yellow by cyba (c) copyright 2019 
Licensed under a Creative Commons Attribution Noncommercial(3.0) license. 
http://dig.ccmixter.org/files/cyba/60166 
http://ccmixter.org/content/cyba/cyba_-_yellow.mp3

Triptych of Snippets by septahelix (c) copyright 2019 
Licensed under a Creative Commons Attribution (3.0) license. 
http://dig.ccmixter.org/files/septahelix/60171 
http://ccmixter.org/content/septahelix/septahelix_-_Triptych_of_Snippets.mp3

commonGround by airtone (c) copyright 2018 
Licensed under a Creative Commons Attribution Noncommercial (3.0) license. 
http://dig.ccmixter.org/files/airtone/58703 
http://ccmixter.org/content/airtone/airtone_-_commonGround.mp3

Little reindeer by Stefan Kartenberg (c) copyright 2018 
Licensed under a Creative Commons Attribution Noncommercial (3.0) license. 
http://dig.ccmixter.org/files/JeffSpeed68/58885 Ft: Martin Cee (softmartin)
http://ccmixter.org/content/JeffSpeed68/JeffSpeed68_-_Little_reindeer.mp3
