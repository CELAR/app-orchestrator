#!/bin/bash

git add . && git commit -m 'debugging' && git push && curl  -ik -X POST http://83.212.116.20/job/build_app-orchestrator/build --user ggian:giannaros
