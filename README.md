# AA-NavUI
Minimum sample to reproduce bug

![image](https://raw.githubusercontent.com/frankkienl/AA-NavUI/master/Schermafbeelding%202021-11-26%20om%2015.58.59.png)

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
