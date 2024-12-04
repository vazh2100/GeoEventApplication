#!/bin/bash

PRE_COMMIT="config/pre-commit"
PRE_PUSH="config/pre-push"

TARGET_PRE_COMMIT=".git/hooks/pre-commit"
TARGET_PRE_PUSH=".git/hooks/pre-push"

rm "$TARGET_PRE_COMMIT"
rm "$TARGET_PRE_PUSH"
echo "Старые ссылки удалены."

# Создаём символическую ссылку
ln -s "../../${PRE_COMMIT}" "$TARGET_PRE_COMMIT"
ln -s "../../${PRE_PUSH}" "$TARGET_PRE_PUSH"
echo "Символическая ссылка создана: ${TARGET_PRE_COMMIT} -> ${PRE_COMMIT}"
echo "Символическая ссылка создана: ${TARGET_PRE_PUSH} -> ${PRE_PUSH}"
