Ваше приложение для шеринга вещей почти готово! В нём уже реализована
вся нужная функциональность - осталось добавить несколько технических
усовершенствований.

### Ставим проблему

Пользователей приложения `ShareIt` становится больше. Вы рады этому, но
замечаете, что не всё идёт гладко: приложение работает медленнее,
пользователи чаще жалуются, что их запросы подолгу остаются без ответа.  
После небольшого самостоятельного исследования вы начинаете понимать, в
чём дело. Пользователи учатся программировать - совсем так же, как и вы!
Некоторые из них теперь используют ваше приложение через другие
программы: собственноручно написанные интерфейсы, боты... Чего они
только не придумали!  
Не все эти программы работают правильно. В `ShareIt` поступает много
некорректных запросов - например, с невалидными входными данными, в
неверном формате или просто дублей. Ваше приложение тратит ресурсы на
обработку каждого из запросов, и в результате его работа замедляется.
Пришло время разобраться с этим!

### Ищем решение

В реальной разработке для решения подобных проблем часто применяется
микросервисная архитектура - с ней вы познакомились в этом модуле. Можно
вынести часть приложения, с которой непосредственно работают
пользователи, в отдельное небольшое приложение и назвать его, допустим,
`**gateway**` (англ. \<\<шлюз\>\>). В нём будет выполняться вся
валидация запросов - некорректные будут исключаться.  
Поскольку для этой части работы не требуется базы данных и каких-то
особых ресурсов, приложение `gateway` будет легковесным. При
необходимости его получится легко масштабировать. Например, вместо
одного экземпляра `gateway` можно запустить целых три - чтобы справиться
с потоком запросов от пользователей.  
После валидации в `gateway` запрос будет отправлен основному приложению,
которое делает всю реальную работу - в том числе обращается к базе
данных. Также на стороне `gateway` может быть реализовано кэширование:
например, если один и тот же запрос придёт два раза подряд, `gateway`
будет самостоятельно возвращать предыдущий ответ без обращения к
основному приложению.

### Формулируем задачу

Вся работа в этом спринте будет вестись в ветке `add-docker`. Вот ваши
задачи:

* Разбить приложение `ShareIt` на два - `shareIt-server` и
  `shareIt-gateway`. Они будут общаться друг с другом через REST.
  Вынести в `shareIt-gateway` всю логику валидации входных данных -
  кроме той, которая требует работы с БД.
* Настроить запуск `ShareIt` через Docker. Приложения `shareIt-server`,
  `shareIt-gateway` и база данных PostgreSQL должны запускаться в
  отдельном Docker-контейнере каждый. Их взаимодействие должно быть
  настроено через Docker Compose.

Приложение `shareIt-server` будет содержать всю основную логику и почти
полностью повторять приложение, с которым вы работали ранее, - за
исключением того, что можно будет убрать валидацию данных в
контроллерах.  
Во второе приложение `shareIt-gateway` нужно вынести контроллеры, с
которыми непосредственно работают пользователи, - вместе с валидацией
входных данных.  
Каждое из приложений будет запускаться как самостоятельное
Java-приложение, а их общение будет происходить через REST. Чтобы
сделать запуск и взаимодействие приложений более предсказуемым и
удобным, разместите каждое из них в своём Docker-контейнере. Также не
забудьте вынести в Docker-контейнер базу данных.

### Ещё несколько технических моментов

Вам нужно разбить одно приложение `ShareIt` на два так, чтобы оба
остались в том же репозитории и собирались одной Maven-командой.
Реализовать подобный механизм в Maven помогают **многомодульные
проекты** (англ. *multi-module project*). Такие проекты содержат в себе
несколько более мелких подпроектов.  
В нашем случае каждый из подпроектов будет представлять собой
самостоятельное Java-приложение. Вообще же подпроект может содержать
любой набор кода или других сущностей, которые собираются с помощью
Maven. Это может быть, например, набор статических ресурсов -
HTML-файлы, изображения и так далее.  
Многомодульный проект содержит один родительский `pom`-файл для всего
проекта, в котором перечисляются все модули или подпроекты. Также для
каждого из модулей создается собственный `pom`-файл со всей информацией
о сборке отдельного модуля. Когда в корневой директории проекта
запустится команда сборки (например, `mvn clean install`), Maven соберёт
каждый из модулей и положит результирующий `jar`-файл в директорию
`target` соответствующего модуля.

💡 Подробнее о том, как работать с многомодульными проектами, вы можете
узнать [из этого ресурса](https://spring.io/guides/gs/multi-module/).

Мы уже подготовили для вас шаблон многомодульного проекта - ищите его в
ветке
[add-docker](https://github.com/yandex-praktikum/java-shareit/tree/add-docker).
Всё, что остаётся сделать, - распределить код вашего приложения между
модулями, а также добавить в `shareIt-gateway` код для обращения к
`shareIt-server` через REST. Чтобы вам было проще работать с REST, мы
создали в `shareIt-gateway` класс `BaseClient`, который реализует
базовый механизм взаимодействия через REST. Вы можете использовать и
дорабатывать этот класс по своему усмотрению. Ещё больше деталей для
работы с REST-вызовами вы найдёте
[по ссылке](https://www.baeldung.com/rest-template), а также в
\<\<Дополнительных советах ментора\>\>.  
Подготовьте Dockerfile для каждого из сервисов - `shareIt-server` и
`shareIt-gateway.` Шаблон для этих файлов расположен в корневой папке
каждого модуля, его содержимое будет таким же, как и в теме про Docker.
Затем опишите настройки развёртывания контейнеров в файле
`docker-compose.yaml` в корне проекта. Конфигурация развёртывания должна
включать три контейнера для следующих сервисов: `shareIt-server`,
`shareIt-gateway` и `postgresql`.  

💡 Для целей разработки вы по-прежнему можете запускать каждый из
сервисов локально через IDE, а работу через Docker проверять после
завершения очередного этапа разработки. Перед тем как тестировать новую
версию в Docker, обязательно пересоберите код проекта и удалите старый
Docker-образ!

Убедитесь, что ваше приложение успешно запускается командой
`docker-compose up` и пользователи, как и прежде, могут создавать и
бронировать вещи.

### Тестирование

Как и всегда, воспользуйтесь нашей
[Postman-коллекцией](../postman/specification_4.json), чтобы
протестировать работу приложения.  
Ура! Теперь в проекте `ShareIt` реализована микросервисная архитектура,
а значит, его легче поддерживать и масштабировать. Пользователи больше
не будут страдать из-за проблем с производительностью и смогут
продолжить делиться вещами с удовольствием. Поздравляем!  
[Дополнительные советы ментора](../advice/mentors_advice_4.pdf)
