#!/bin/bash

# Путь к файлу pre-commit в папке проекта
SOURCE_FILE="config/pre-commit"

# Путь к папке хуков Git
TARGET_DIR=".git/hooks"

# Полный путь к целевой символической ссылке
TARGET_LINK="${TARGET_DIR}/pre-commit"

# Проверяем, существует ли файл SOURCE_FILE
if [ ! -f "$SOURCE_FILE" ]; then
    echo "Ошибка: Файл ${SOURCE_FILE} не найден!"
    exit 1
fi

# Удаляем старую символическую ссылку или файл, если они существуют
if [ -e "$TARGET_LINK" ] || [ -L "$TARGET_LINK" ]; then
    rm "$TARGET_LINK"
    echo "Старая ссылка или файл pre-commit удалены."
fi

# Создаём символическую ссылку
ln -s "../../${SOURCE_FILE}" "$TARGET_LINK"
echo "Символическая ссылка создана: ${TARGET_LINK} -> ${SOURCE_FILE}"
