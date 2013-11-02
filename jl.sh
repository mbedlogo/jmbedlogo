#!/bin/bash
set -o history
(while true; do read -e lastcmd; history -s $lastcmd; echo $lastcmd; done) | java -jar jl.jar

