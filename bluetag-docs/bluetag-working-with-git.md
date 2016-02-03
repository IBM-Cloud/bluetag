#Working with Git

We want the Bluetag project to follow micro-services principles wherever possible.  True micro-services might be organized into completely separate git projects.  However we didn’t want to have the code scattered about so we compromised by making each micro-service  a git  submodules.Git submodules have some quarks that you need to be aware of.   Specifically each submodule doesn’t necearrily follow the branch set by the umbrella Bluetag project.  So as people update various submodules,  your  local git repository submodules  will get in a state where the HEAD is detatched state.  You can see that by running this command at the Bluetag repo level:
```
git submodule foreach 'git status’
```
When you have detatched HEAD pointers, a git pull at the Bluetag level will NOT get you the latest changes.   Once you reattach the HEAD to a branch, the pull at the bluetag level will work.  You can reattach this way:
```
cd submodule
git checkout master
```
Now if you have changes commited in the submodule, use this sequence to get them merged:
```
git branch temp-branch
git checkout master
git merge temp-branch
```
Finally, this article may help:  
http://stackoverflow.com/questions/3965676/why-did-my-git-repo-enter-a-detached-head-state/3965714#3965714