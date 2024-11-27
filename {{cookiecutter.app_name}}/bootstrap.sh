#!/bin/bash

SCRIPT_NAME=$(basename "$0")

prompt_input() {
    local prompt="$1"
    local variable="$2"
    local default="$3"

    if [ -n "$default" ]; then
        read -p "$prompt [$default]: " input
        eval $variable="${input:-$default}"
    else
        read -p "$prompt: " $variable
    fi
}

prompt_input "Enter the app name to replace 'APPNAME'" APP_NAME
echo ""
echo "****** DEV ENVIRONMENT ******"
prompt_input "Enter the project ID for dev to replace 'PROJECT_ID_DEV'" PROJECT_ID_DEV
prompt_input "Enter the IAM owners for dev to replace 'IAM_OWNERS_DEV'. Press enter to accept the default value: " IAM_OWNERS_DEV "group:ki-web-nonprod@ki-insurance.com"

echo "****** Live ENVIRONMENT ******"
prompt_input "Enter the project ID for dev to replace 'PROJECT_ID_LIVE'" PROJECT_ID_LIVE
prompt_input "Enter the IAM owners for dev to replace 'IAM_OWNERS_LIVE'. Press enter to accept the default value: " IAM_OWNERS_LIVE "group:ki-web-prod@ki-insurance.com"

perform_replacements() {
    local file="$1"
    sed -i '' "s/APPNAME/$APP_NAME/g" "$file"
    sed -i '' "s/STATE_BUCKET_DEV/$PROJECT_ID_DEV-tfstate/g" "$file"
    sed -i '' "s/STATE_BUCKET_LIVE/$PROJECT_ID_LIVE-tfstate/g" "$file"
    sed -i '' "s/PROJECT_ID_DEV/$PROJECT_ID_DEV/g" "$file"
    sed -i '' "s/PROJECT_ID_LIVE/$PROJECT_ID_LIVE/g" "$file"
    sed -i '' "s/IAM_OWNERS_DEV/$IAM_OWNERS_DEV/g" "$file"
    sed -i '' "s/IAM_OWNERS_LIVE/$IAM_OWNERS_LIVE/g" "$file"
}

echo "The following replacements will be made:"
echo "APPNAME -> $APP_NAME"
echo "PROJECT_ID_DEV -> $PROJECT_ID_DEV"
echo "PROJECT_ID_LIVE -> $PROJECT_ID_LIVE"
echo "IAM_OWNERS_DEV -> $IAM_OWNERS_DEV"
echo "IAM_OWNERS_LIVE -> $IAM_OWNERS_LIVE"
read -p "Do you want to proceed? (y/n): " confirm

if [[ $confirm != [yY] ]]; then
    echo "Operation cancelled."
    exit 1
fi

# Find all files in the current directory and subdirectories, excluding specified directories, .jar files, and this script
find . -type d \( -name .git -o -name .idea -o -name .vs -o -name .vscode -o -name bin -o -name obj -o -name .gradle -o -path "./app/build" \) -prune -o -type f ! -name "*.jar" ! -name "$SCRIPT_NAME" -print | while read -r file; do
    # Check if the file is a regular file and not a binary file
    if [[ -f "$file" && ! $(file "$file" | grep -q "binary") ]]; then
        perform_replacements "$file"
        echo "Processed: $file"
    fi
done

echo "Bootstrap process completed."
