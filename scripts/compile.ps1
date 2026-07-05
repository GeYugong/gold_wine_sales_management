$ErrorActionPreference = "Stop"
$projectRoot = Split-Path -Parent $PSScriptRoot
& "$projectRoot\mvnw.cmd" compile
