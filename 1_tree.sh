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
cd 0_app/src/main/java/vazh2100/geoeventapp
echo "0_app/"
print_tree "." "   "
cd ../../../../../..
cd 1_core/src/main/java/core
echo "1_core/"
print_tree "." "   "
cd ../../../../..
cd 1_core_a/src/main/java/core
echo "1_core_a/"
print_tree "." "   "
cd ../../../../..
cd 1_geolocation/src/main/java/geolocation
echo "1_geolocation/"
print_tree "." "   "
cd ../../../../..
cd 1_network/src/main/java/network
echo "1_network/"
print_tree "." "   "
cd ../../../../..
cd 1_theme/src/main/java/theme
echo "1_theme/"
print_tree "." "   "
cd ../../../../..
cd 2_events/src/main/java/events
echo "2_events/"
print_tree "." "   "
cd ../../../../..
cd 2_events/src/test/java/events
echo "2_events/"
print_tree "." "   "
cd ../../../../..
