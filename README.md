**Продовження ознайомлення з методами CompletableFuture.**
У цьому проєкті продемонстровано дві практичні реалізації асинхронних операцій за допомогою CompletableFuture. Перша реалізація SlaverySimulator ілюструє паралельний запуск декількох задач та виведення результату тієї з них, що завершується першою. Друга реалізація з іменем TicketBooking моделює процес бронювання авіаквитків із випадковим формуванням місць, перевіркою доступності та пошуком найкращої ціни.

**1. Паралельне виконання асинхронних завдань**

У першій програмі за допомогою supplyAsync створюються три асинхронні задачі, кожна з яких виконується зі своєю випадковою затримкою. Після старту задач використано anyOf, так фіксується завершення найшвидшої з них і одразу виводить її результат. Решта завдань може тривати довше, але для логіки проєкту важливе лише завершення першої.
**2. Бронювання авіаквитків**

Друга частина коду містить демонстрацію бронювання квитка. Програма генерує кілька місць, кожне з назвою, доступністю та випадковою ціною. На етапі checkSeatAvailability визначається, чи взагалі існує місце, придатне для бронювання. Якщо воно є, thenCompose переходить до findBestPrice, де з’являється найменша вартість серед доступних місць. При одночасному врахуванні результатів доступності й ціни thenCombine здійснює логіку завершення бронювання та викликає payForTicket. На фінальному етапі відбувається імітація оплати, і виводиться повідомлення.


Для запуску першого прикладу треба скомпілювати SlaverySimulator і виконати його, після чого на екран виведуться повідомлення про старт кожної задачі, їх завершення і результат найшвидшої з них. 

![Screenshot_1382](https://github.com/user-attachments/assets/f89b93f8-e8f4-4b77-adef-5cebd3d6c8e9)


Для перевірки другого прикладу компілюємо TicketBooking і запускаємо. Програма генерує кілька місць, перевіряє їх доступність, з’ясовує найкращу ціну та проводить віртуальну оплату квитка. У консолі будуть видні всі проміжні етапи й результат бронювання.
![Screenshot_1383](https://github.com/user-attachments/assets/1d00f344-f778-483d-8697-7ccf3cac1334)