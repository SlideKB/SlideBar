# SliderBar
The official Slide Bar Repo

lol this needs work

send me a pm with your email and I can add you to the slack channel

# setup projects in eclipse

0. recommend using eclipse (instructions are for eclipse)
1. Clone or download SlideBar repo
2. Open new project and select the repo as a directory (File -> Open Projects from File System)
3. browse to the repo by clicking **Directory...**
4. select to project to Import as a maven project
5. repeat steps 1-4 with the [SlideBar-API](https://github.com/SlideKB/slidebar-api) repo

# Maven

0. navigate to run configurations (Run -> Run configurations)
1. Create a new Maven build by right clicking on **maven build** and selecting new
2. 
  - give it a unique name. example "mavenSlideBar".
  - base directory -> "${workspace_loc:/slidebar}".
  - goals -> "clean package -DskipTests"
3. Apply
4. setup the slidebar-API project
5. Create a new Maven build by right clicking on **maven build** and selecting new
6. 
  - give it a unique name. example "mavenSlideBarAPI".
  - base directory -> "${workspace_loc:/slidebar-api}".
  - goals -> "clean compile install"
7. apply and run
8. after completetion, run the first maven build (example mavenSlideBar) created in step 1

# Running

run "MainFront.java" as a java application after building the slidebar-api and the slidebar repo

or 


run "UI.java" as a java application after building the slidebar-api and the slidebar repo
