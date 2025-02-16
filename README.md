# Утилита фильтрации содержимого файлов
## Инструкция по запуску утилиты
Скачайте файл FileFilterUtility.jar в любое удобное место из репозитория по ссылке 
```bash 
https://github.com/Evgeniy959/FileFilterUtility/blob/main/FileFilterUtility/out/artifacts/Utility_jar/FileFilterUtility.jar
``` 
Откройте командную строку в этой директории и напишите данную команду с необходимыми вам параметрами:
```bash
java -jar FileFilterUtility.jar -s -a -p sample- in1.txt in2.txt
```
### Особенности реализации
Чтобы перейти к списку всех команд нужно ввести опцию **-h** в начале: 

java -jar FileFilterUtility.jar -h

options:

**-o** <путь> - путь до файлов с результатом.

**-p** <префикс> - префикс имён файлов с результатом.

**-a** - режим добавления в существующие файлы.

**-s** - краткая статистика.

**-f** - полная статистика.

В программе используются входные файлы с именами in1.txt, in2.txt, так же дополнительно можно использовать файлы in3.txt, in4.txt.

Если не указать или не верно указать входные файлы программа не выполнится и сообщит об 
этом пользователю с указанием причины неудачи. Обработка других возможных видов ошибок c соответствующим сообщением пользователю.

При успешном выполнении чтении и записи файлов, в консоль будет выведено сообщение об этом. 

Если директория для записи результатов не существует, то она будет создана.

Для поочередного считывания файлов используется многопоточность.
#### Версия java - 23.
#### Система сборки - Maven 3.9.9. 
