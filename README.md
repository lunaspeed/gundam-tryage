### ガンダムトライエイジ Card List Parser

Parses data from [Official website](http://www.gundam-try.com/cardlist/index.php) and write to Excel.

Data grouped by set group:
0. Promos
1. Original
2. ジオンの興亡
3. BUILD MS
4. BUILD G
5. 鉄血
6. 鉄華繚乱
7. VS IGNITION
8. OPERATION ACE
9. DELTA WARS
10. EVOL BOOST!!

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