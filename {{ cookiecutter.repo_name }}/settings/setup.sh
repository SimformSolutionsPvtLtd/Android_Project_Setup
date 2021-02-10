#!/bin/bash

RED='\033[0;1;31m'
NC='\033[0m' # No Color

GIT_DIR=$(git rev-parse --git-dir 2> /dev/null)
GIT_ROOT=$(git rev-parse --show-toplevel 2> /dev/null)

# Check if we are in git repo
if [ ! "$(git rev-parse --is-inside-work-tree 2>/dev/null)" == "true" ]; then
    echo -e "${RED}ERROR:${NC} Git is not initialized yet."
    exit 1
fi

echo "Installing git commit message hook"
cp hooks/commit-msg "${GIT_DIR}/hooks/"
chmod +x "${GIT_DIR}/hooks/commit-msg"

echo "Installing git pre commit hook"
cp hooks/pre-commit "${GIT_DIR}/hooks/"
chmod +x "${GIT_DIR}/hooks/pre-commit"