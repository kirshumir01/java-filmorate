# Filmorate

*Read this on [English](README.en.md).*

## Описание
___
Представьте, что вы решили отдохнуть и провести вечер за просмотром фильма. Вкусная еда уже готовится, ваш любимый плед уютно свернулся в кресле — а вы все еще не выбрали, что посмотреть!

Фильмов много и с каждым годом их становится все больше. Чем их больше, тем больше разных оценок. Чем больше оценок, тем сложнее сделать выбор. Однако сейчас не время сдаваться!

Filmorate — сервис, который работает с фильмами и оценками пользователей, предлагает список наиболее популярных фильмов, рекомендованных к просмотру. Теперь ни вам, ни вашим друзьям не придется долго думать, что посмотреть вечером.

![](https://github.com/kirshumir01/java-filmorate/blob/main/filmorate-picture.png)

## Структура проекта и функциональность
___

- **фильмы:**
  - **_модель данных_**: название, описание, дата релиза, продолжительность, рейтинг MPA, жанры, количество оценок пользователей;
  - **_валидация данных_**: название не может быть пустым, максимальная длина описания — 200 символов, дата релиза — не раньше 28 декабря 1895 года, продолжительность фильма должна быть положительным числом;
  - **_действия_**: добавление, обновление, хранение, получение и удаление;
  - **_функционал_**: назначение и удаление "лайков" пользователями, получение пользователем списка наиболее популярных фильмов.

- **пользователи:**
  - **_модель данных_**: имя, логин, email, дата рождения;
  - **_валидация данных_**: электронная почта не может быть пустой и должна содержать символ @, логин не может быть пустым и содержать пробелы, имя для отображения может быть пустым — в таком случае будет использован логин, дата рождения не может быть в будущем;
  - **_действия_**: добавление, обновление, хранение, получение и удаление;
  - **_функционал_**: добавление в друзья, удаление из друзей, получение полного списка друзей, получение списка общих друзей пользователей.

- **жанры**:
  - **_модель данных_**: наименование;
  - **_валидация данных_**: название не может быть пустым;
  - **_действия_**: получение информации о всех жанрах фильма, получение жанра по id.

- **рейтинг MPA**:
  - **_модель данных_**: наименование;
  - **_валидация данных_**: название не может быть пустым;
  - **_действия_**: получение информации о рейтинге фильма, получение рейтинга по id.

- **жанры фильмов**:
  - комедия;
  - драма;
  - мультфильм;
  - триллер;
  - документальный;
  - боевик.

- **рейтинги фильмов**:
  - ```G``` — у фильма нет возрастных ограничений;
  - ```PG``` — детям рекомендуется смотреть фильм с родителями;
  - ```PG-13``` — детям до 13 лет просмотр не желателен;
  - ```R``` — лицам до 17 лет просматривать фильм можно только в присутствии взрослого;
  - ```NC-17``` — лицам до 18 лет просмотр запрещён.

### Обработка исключений
- ошибки валидации данных - код ответа ```400 Bad Request```;
- ошибки валидации аргументов метода - код ответа ```400 Bad Request```;
- ошибки поиска объекта - код ответа ```404 Not Found```;
- внутренние ошибки сервера - код ответа ```500 Internal Server Error```.

## Инструменты и технологии
___

- фреймворк: **Spring Boot 3.2.4**:
  - spring-boot-starter-web;
  - spring-boot-starter-validation;
  - spring-boot-starter-jdbc;
  - spring-boot-starter-test.
- аннотирование классов приложения: **Lombok**
- логирование HTTP-запросов и ответов: **Logbook 3.7.2**
- хранение данных (рабочий режим): **H2 Database**
- хранение данных (режим тестирования): **память приложения**
- сборка и управление зависимостями: **Apache Maven 3.9.9**
- версия Java: **21**

## Схема базы данных приложения (ER-диаграмма)
___

![ER-diagram](https://github.com/kirshumir01/java-filmorate/blob/main/java-filmorate-diagram.png)

## Примеры запросов в базу данных
___

<br>

<details>

<summary> Получение списка всех фильмов </summary>

```sql
SELECT *
FROM films;
```

| film\_id | name          | description                                                                                                                                                                                              | release\_date | duration | mpa\_rating\_id |
|:---------|:--------------|:---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|:--------------|:---------|:----------------|
| 1        | Гладиатор     | Римская империя. Бесстрашного и благородного генерала Максимуса боготворят солдаты, а старый император Марк Аврелий безгранично доверяет ему и относится как к сыну.                                     | 2000-05-01    | 155      | 4               |
| 2        | Титаник <br/> | В первом и последнем плавании шикарного «Титаника» встречаются двое. Пассажир нижней палубы Джек выиграл билет в карты, а богатая наследница Роза отправляется в Америку, чтобы выйти замуж по расчёту.  | 1997-11-01    | 194      | 3               |

</details>

<br>

<details>

<summary> Получение списка всех пользователей </summary>

```sql
SELECT *
FROM users;
```

| user\_id | email       | login | name    | birthday   |
|:---------|:------------|:------|:--------|:-----------|
| 1        | user1@ya.ru | user1 | Николай | 1990-02-12 |
| 2        | user2@ya.ru | user2 | Кирилл  | 1993-11-01 |
| 3        | user3@ya.ru | user3 | Петр    | 1997-03-08 |

</details>

<br>

<details>

<summary> Получение списка MPA рейтинга </summary>

```sql
SELECT *
FROM mpa_ratings;
```

| mpa\_rating\_id | name  |
|:----------------|:------|
| 1               | G     |
| 2               | PG    |
| 3               | PG-13 |
| 4               | R     |
| 5               | NC-17 |

</details>

<br>

<details>

<summary> Получение списка жанров </summary>

```sql
SELECT *
FROM genres;
```

| name           | genre\_id |
|:---------------|:----------|
| Комедия        | 1         |
| Драма          | 2         |
| Мультфильм     | 3         |
| Триллер        | 4         |
| Документальный | 5         |
| Боевик         | 6         |

</details>

<br>

<details>

<summary> Получение списка фильмов и их жанров </summary>

```sql
SELECT f.name,
       g.name
FROM films f
         LEFT JOIN film_genres fg ON f.film_id = fg.film_id
         LEFT JOIN genres g ON fg.genre_id = g.genre_id;
```

| name          | name   |
|:--------------|:-------|
| Гладиатор     | Драма  |
| Гладиатор     | Боевик |
| Титаник <br/> | Драма  |

</details>

<br>

<details>

<summary> Получение списка популярных фильмов по лайкам </summary>

```sql
SELECT f.name,
       count(f.name) likes
FROM films f
         LEFT JOIN likes l ON f.film_id = l.film_id
GROUP BY f.name
ORDER BY count(f.name) DESC;
```

| name          | likes |
|:--------------|:------|
| Гладиатор     | 4     |
| Титаник <br/> | 3     |

</details>

<br>

<details>

<summary> Получение всего списка друзей </summary>

```sql
SELECT *
FROM users
         LEFT JOIN friendship f ON users.user_id = f.friend_id;
```

| user\_id | email       | login | name    | birthday   | user\_id | friend\_id | friendship\_status |
|:---------|:------------|:------|:--------|:-----------|:---------|:-----------|:-------------------|
| 1        | user1@ya.ru | user1 | Николай | 1990-02-12 | 1        | 2          | true               |
| 1        | user2@ya.ru | user2 | Кирилл  | 1993-11-01 | 2        | 1          | true               |
| 2        | user3@ya.ru | user3 | Петр    | 1997-03-08 | 3        | 1          | true               |

</details>

<br>

<details>

<summary> Получение списка друзей Николая </summary>

```sql
SELECT *
FROM users
WHERE user_id IN (SELECT f.friend_id
                  FROM users u
                           JOIN friendship f ON u.user_id = f.user_id
                  WHERE u.user_id = 1);
```

| user\_id | email         | login  | name   | birthday   |
|:---------|:--------------|:-------|:-------|:-----------|
| 2        | user2@ya.ru   | user2  | Кирилл | 1993-11-01 |
| 3        | user3@ya.ru   | user3  | Петр   | 1997-03-08 |

</details>

<br>

<details>

<summary> Получение списка общих друзей Кирилла и Петра </summary>

```sql
SELECT *
FROM users
WHERE user_id IN
      (SELECT friend_id
       FROM users u
                JOIN friendship f ON u.user_id = f.user_id
       WHERE u.user_id = 2
         AND f.is_confirmed = 'true')
  AND user_id IN
      (SELECT friend_id
       FROM users u
                JOIN friendship f ON u.user_id = f.user_id
       WHERE u.user_id = 3
         AND f.is_confirmed = 'true');
```

| user\_id | email             | login | name    | birthday   |
|:---------|:------------------|:------|:--------|:-----------|
| 1        | user1@ya.ru       | user1 | Николай | 1990-02-12 |

</details>

## Инструменты для запуска приложения
___

- Java Development Kit (JDK) - версия 21 или более поздняя;
- Apache Maven - версия 3.9.9 или более поздняя.

### Инструкции по установке JDK

Ниже приведены инструкции по установке JDK 21 на различных операционных системах.

<br>

<details>

<summary> Установка на macOS </summary>

Установите Homebrew запуском следующей команды в терминале (командной строке) операционной системы:

```shell
/bin/bash -c "$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/HEAD/install.sh)"
```

Установите JDK 21 с помощью Homebrew:

```shell
brew install openjdk@21
```

Создайте символическую ссылку, чтобы система могла найти JDK:

```shell
sudo ln -sfn /opt/homebrew/opt/openjdk@21/libexec/openjdk.jdk /Library/Java/JavaVirtualMachines/openjdk-21.jdk
```

Добавьте JDK 21 в PATH. Откройте файл .zshrc (или .bash_profile, в зависимости от используемой оболочки) и добавьте следующую строку:

```
echo 'export PATH="/opt/homebrew/opt/openjdk@21/bin:$PATH"' >> ~/.zshrc
```

Перезагрузите терминал или примените изменения с помощью команды:

```shell
source ~/.zshrc
```

Проверьте установленную версию Java:

```shell
java -version
```
</details>

<br>

<details>

<summary> Установка на Linux </summary>

Откройте терминал и выполните команду для обновления списка пакетов:

```shell
sudo apt update
```

Установите JDK 21:

```shell
sudo apt install openjdk-21-jdk
```

Убедитесь, что JDK установлен и настроен корректно:

```shell
java -version
```

</details>

<br>

<details>

<summary> Установка на Windows </summary>

1. Скачайте установочный файл JDK 21 с официального сайта [Oracle](https://www.oracle.com/java/technologies/javase/jdk21-archive-downloads.html) или OpenJDK.

2. Запустите установочный файл и следуйте инструкциям установщика.

3. После установки настройте переменную среды JAVA_HOME:
  - откройте ```Системные настройки``` > ```Переменные среды```;
  - в разделе ```Системные переменные``` нажмите ```Создать``` и введите:
    - имя переменной: ```JAVA_HOME```
    - значение переменной: путь к установленной JDK (например, ```C:\Program Files\Java\jdk-21```).
  - добавьте ```JAVA_HOME\bin``` в переменную Path.

4. Проверьте версию Java в командной строке:

```shell
java -version
```

</details>

### Инструкции по установке Maven

Ниже приведены инструкции по установке Maven на различных операционных системах.

<br>

<details>

<summary> Установка на macOS </summary>

Установите Homebrew, если он еще не установлен:

```shell
/bin/bash -c "$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/HEAD/install.sh)"
```

Установите Maven с помощью Homebrew:

```shell
brew install maven
```

Проверьте версию Maven, чтобы убедиться в правильной установке:

```shell
mvn -version
```

</details>

<br>

<details>

<summary> Установка на Linux </summary>

Откройте терминал и загрузите Apache Maven 3.9.9:

```shell
wget https://downloads.apache.org/maven/maven-3/3.9.9/binaries/apache-maven-3.9.9-bin.tar.gz
```

Распакуйте архив:

```shell
tar xzvf apache-maven-3.9.9-bin.tar.gz
```

Переместите распакованную папку в директорию /opt/:

```shell
sudo mv apache-maven-3.9.9 /opt/maven
```

Добавьте Maven в PATH. Откройте файл .bashrc или .zshrc и добавьте следующую строку:

```
export PATH=/opt/maven/bin:$PATH
```
Примените изменения:

```shell
source ~/.bashrc
```

Проверьте версию Maven:

```shell
mvn -version
```

</details>

<br>

<details>

<summary> Установка на Windows </summary>

1. Скачайте установочный архив Maven 3.9.9 с [официального сайта](https://maven.apache.org/download.cgi).

2. Распакуйте архив в удобное место, например ```C:\Program Files\Maven```.

3. Настройте переменные среды:
  - откройте ```Системные настройки``` > ```Переменные среды```.
  - в разделе ```Системные переменные``` нажмите ```Создать``` и добавьте:
    - имя переменной: ```MAVEN_HOME```
    - значение переменной: путь к папке Maven, например ```C:\Program Files\Maven```.
  - найдите переменную Path, выберите ее и нажмите ```Изменить```. Добавьте новый путь: ```%MAVEN_HOME%\bin```.

4. Перезапустите командную строку и проверьте версию Maven:

```shell
mvn -version
```

</details>

## Подготовка к запуску приложения
___

**ВАЖНО!** Запуск приложения осуществляется через системный порт ```8080```. Перед запуском убедитесь, что порт ```8080``` свободен!

### Как проверить, что системный порт свободен?

Ниже приведены инструкции по проверке порта на различных операционных системах.

<br>

<details>

<summary> Проверка порта на macOS </summary>

Откройте терминал.

Выполните следующую команду, заменив ```PORT``` на номер порта, который нужно проверить:

```
lsof -i :PORT
```

Если порт занят, команда выведет список процессов, использующих порт. Для завершения процесса используйте команду:

```
kill -9 PID
```
где ```PID``` — идентификатор процесса из вывода предыдущей команды.

Если порт свободен, команда не вернет никаких данных.

</details>

<br>

<details>

<summary> Проверка порта на Linux </summary>

Откройте терминал.

Выполните следующую команду, заменив ```PORT``` на номер порта:

```
sudo lsof -i :PORT
```

Если порт занят, команда выведет список процессов, использующих порт. Для завершения процесса используйте команду:

```
sudo kill -9 PID
```
где ```PID``` — идентификатор процесса из вывода предыдущей команды.

Если порт свободен, команда не вернет никаких данных.

</details>

<br>

<details>

<summary> Проверка порта на Windows </summary>

Откройте командную строку (cmd) или PowerShell с правами администратора.

Выполните следующую команду, заменив ```PORT``` на номер порта:

```
netstat -aon | findstr :PORT
```

Если порт занят, команда выведет информацию о процессе, использующем порт. Обратите внимание на PID (идентификатор процесса).

Чтобы завершить процесс, откройте ```Диспетчер задач```, перейдите на вкладку ```Подробности```, найдите процесс с соответствующим PID и завершите его.

Если порт свободен, команда не вернет никаких данных.

</details>

### Где следует выполнять команды для запуска приложения?

- в терминале (командной строке) операционной системы;
- в программной среде разработки [IntelliJ IDEA](https://www.jetbrains.com/ru-ru/idea/download/other.html) непосредственно в данном файле нажатием на символ тройной стрелки ```>>>```, расположенной слева от строки описания соответствующей команды.

## Запуск приложения
___

1. Склонируйте репозиторий с использованием следующей команды:

```shell
git clone https://github.com/kirshumir01/java-filmorate.git
```

2. Перейдите в корневую директорию склонированного проекта и выполните команду:

```shell
cd java-filmorate
```

3. Запустите сборку проекта командой:

```shell
./mvnw clean install
```

4. Запустите приложение, выполнив следующую команду:

```shell
./mvnw spring-boot:run
```

Доступ к публичному API будет открыт по ссылке через адресную строку интернет-браузера: `http://localhost:8080`.

*Read this on [English](README.en.md).*