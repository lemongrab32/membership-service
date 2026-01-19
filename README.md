# Membership Service

Сервис для управления абонементами спортзала.

Функциональные возможности:

- API для управления тарифами
- Оформление абонемента
- Расчёт стоимости абонемента на основании типа клиента, категории клиента и т. п.
- Управление параметрами для расчёта

## Требования для запуска

- Git (установить можно здесь https://git-scm.com/downloads)
- Docker Desktop (установить можно здесь https://www.docker.com/products/docker-desktop)
- Make (установить можно здесь https://gnuwin32.sourceforge.net/packages/make.htm)

## Запуск и визуализация

 - Клонируем репозиторий
```bash
git clone https://github/lemongrab32/membership-service/
```
 - Переходим в директорию сервиса
```bash
cd membership-service
```
 - Запускаем сборку и развёртывание сервиса с инфраструктурой
```bash
make up
```
