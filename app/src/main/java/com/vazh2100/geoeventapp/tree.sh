#!/bin/bash

# Функция для печати дерева папок
print_tree() {
    local dir="$1"
    local prefix="$2"

    # Сначала обрабатываем каталоги
    local dirs=()
    local files=()

    # Проходим по всем элементам в каталоге
    for item in "$dir"/*; do
        if [ -d "$item" ]; then
            dirs+=("$item")
        elif [ -f "$item" ]; then
            files+=("$item")
        fi
    done

    # Печатаем каталоги
    for dir_item in "${dirs[@]}"; do
        echo "${prefix}├── $(basename "$dir_item")"
        print_tree "$dir_item" "${prefix}│   "
    done

    # Печатаем файлы
    for file_item in "${files[@]}"; do
        echo "${prefix}└── $(basename "$file_item")"
    done
}

# Основная программа
root_dir="${1:-.}"  # Если директория не указана, используем текущую
echo "$root_dir/"
print_tree "$root_dir" "   "
