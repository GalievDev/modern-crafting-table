# Недо ТЗ
>Описание: Мод должен иметь 4 тира верстаков. Модельку брать с того мода (для всех 4 тиров). Для каждого тира можно добавлять свои предметы, которые не будут доступны для тира ниже, но предметы текущего тира будут доступны тиру выше. Скорость будет увеличиваться от конфиговой в 2 раза на каждом тире (Например: 1 тир - 20 мин, 2 тир - 10 мин, 3 тир - 5 мин, 4 тир - 3(округляем до целого выше))
>Пример конфига:
```json
{
    "tier1": {
        "itemID": "minecraft:bone",
        "time": 40000 (in unix)
        "items": [
            {
                "itemID": "minecraft:redstone"
                "count": 100
            }
        ]
    }
}
```
>Худ: большая полоска итогового прогресса, под ней оставшееся время, под временем поле (со скроллом) с отображением "предмет": текущее количество/итоговое количество. Ну и кнопка для старта крафта, соответственно. Также возможность забрать предметы из него (все, до того как будет забит инвентарь, дальше не давать, пока не будет места)
