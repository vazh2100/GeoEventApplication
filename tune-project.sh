#!/bin/bash

PRE_PUSH="config/pre-push"

TARGET_PRE_PUSH=".git/hooks/pre-push"

rm "$TARGET_PRE_PUSH"
echo "Старые ссылки удалены."

# Создаём символическую ссылку
ln -s "../../${PRE_PUSH}" "$TARGET_PRE_PUSH"
echo "Символическая ссылка создана: ${TARGET_PRE_PUSH} -> ${PRE_PUSH}"
