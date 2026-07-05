@echo off
chcp 65001 >nul
cd /d "%~dp0"
if exist "wine.db" (
    del /f /q "wine.db"
    echo Local database wine.db has been deleted.
) else (
    echo Local database wine.db does not exist.
)
echo It will be recreated with seed data next time you run the system.
pause
