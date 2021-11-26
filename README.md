# AA-NavUI
Minimum sample to reproduce bug


create a config with the following content:  
```
[general]
touch = true
touchpad = false
controller = false
instrumentcluster = false
resolution = 800x600
marginHeight= 40
dpi = 160
framerate = 30
```
save it as default_wide5.ini  

run the DHU with the following command:
```
./desktop-head-unit -c config/default_wide5.ini
```
