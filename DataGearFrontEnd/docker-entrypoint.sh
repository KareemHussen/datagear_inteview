#!/bin/sh

# Replace API_URL_PLACEHOLDER with actual API URL from environment variable
API_URL="${API_URL:-http://localhost:8080/api/v1}"

# Find and replace the placeholder in index.html
sed -i "s|API_URL_PLACEHOLDER|${API_URL}|g" /usr/share/nginx/html/index.html

# Start nginx
exec nginx -g 'daemon off;'