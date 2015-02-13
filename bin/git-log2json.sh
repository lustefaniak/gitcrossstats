#!/usr/bin/env bash

# Use this one-liner to produce a JSON literal from the Git log:

git log \
    --pretty=format:'{ "sha": "%H",%n "author": { "name": "%an", "email": "%ae", "date": "%ad" },%n "committer": { "name": "%cn", "email": "%ce", "date": "%cd" },%n "message": "%f",%n "url": "http://some/%H",%n "parents":[],%n "tree":{"url":"","sha":""}%n},' \
    $@ | \
    perl -pe 'BEGIN{print "["}; END{print "]\n"}' | \
    perl -pe 's/},]/}]/'
