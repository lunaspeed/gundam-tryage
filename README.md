### ガンダムトライエイジ Card List Parser

Parses data from [Official website](http://www.gundam-try.com/cardlist/index.php) and write to Excel.

Data grouped by set group:

1. Promos
1. Original
1. ジオンの興亡
1. BUILD MS
1. BUILD G
1. 鉄血
1. 鉄華繚乱
1. VS IGNITION
1. OPERATION ACE
1. DELTA WARS
1. EVOL BOOST!!

See [application.conf](src/main/resources/application.conf.bak) for settings.

Run 
(rename application.conf.bak to application.conf and modify accordingly)
```bash
sbt run
```


### TODO
1. code cleanup
2. download images
3. attach image as comment (currently not possible)

[Generated Files](https://1drv.ms/f/s!AqUT26kFh1b_hG8K4G7Eu8xuyOkA)