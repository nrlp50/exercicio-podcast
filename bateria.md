**Bateria**
==========

Avaliamos a bateria utilizando um dispositivo próprio (SAMSUNG GALAXY J5).

Foi desativado o modo de carregamento do aparelho com o intuito de conseguir resultados mais precisos. 

Foi utilizado o Adb (Android Debug Bridge):
```sh
adb shell dumpsys batterystats --reset
```

O uso do aplicativo foi de aproximadamente 20 minutos, sendo feito o download de 2 episódios. Durante o download dos episódios ocorreu a navegação pelo aplicativo. Os podcasts foram colocados para tocar e esperou-se cerca de 8 minutos ouvindo. 

Após essa navegação pelo aplicativo foi gerado o txt atravéz , fizemos uso da ferramente Battery Historian para analisar os dados extraídos pelo adb
```sh
adb shell dumpsys batterystats > batteryteste.txt
```

O arquivo txt se encontra no atual link abaixo:

https://github.com/nrlp50/exercicio-podcast/blob/master/Podcast/batterytest.txt

