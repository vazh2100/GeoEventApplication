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

# Функция для обработки каталога
process_directory() {
    local dir="$1"
    local label="$2"

    if [ -d "$dir" ]; then
        echo "$label/"
        print_tree "$dir" "   "
    else
        echo "Каталог $dir не существует или недоступен."
    fi
}

# Основная программа
declare -a directories=(
    "0_app:0_app/src/main/java/vazh2100/geoeventapp"
    "1_core:1_core/src/main/java/core"
    "1_core_a:1_core_a/src/main/java/core"
    "1_geolocation:1_geolocation/src/main/java/geolocation"
    "1_network:1_network/src/main/java/network"
    "1_theme:1_theme/src/main/java/theme"
    "2_events:2_events/src/main/java/events"
    "2_events_test:2_events/src/test/java/events"
)

# Итерируемся по массиву directories
for item in "${directories[@]}"; do
    IFS=':' read -r label dir <<< "$item"
    process_directory "$dir" "$label"
done
