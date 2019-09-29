#!/bin/sh

./localizable -c $1 -i /tmp/Localizable

for f in /tmp/*.Localizable; do
  result=$(echo $f | sed 's/.tmp.//' | sed 's/.Localizable$//')
  destination=$2/$result.lproj/Localizable.strings

  if [ -f $destination ]
  then
    echo "Copying $f to $destination."
    cp $f $destination
  else
    echo "Could not find dir and file to put $destination in."
  fi
done
