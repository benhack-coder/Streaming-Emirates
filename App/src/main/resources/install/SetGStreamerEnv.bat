cd %SystemRoot%\System32
for /F "tokens=2* delims= " %%f IN ('reg query HKCU\Environment /v PATH ^| findstr /i path') do set OLD_SYSTEM_PATH=%%g
setx PATH "%APPDATA%\streaming-emirates\gstreamer\gstreamer\1.0\mingw_x86_64\bin;%OLD_SYSTEM_PATH%"
shutdown /r
pause