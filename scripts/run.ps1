$ErrorActionPreference = "Stop"
$projectRoot = Split-Path -Parent $PSScriptRoot
& "$projectRoot\mvnw.cmd" -q exec:java
