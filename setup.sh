#!/bin/bash

rm /etc/apt/sources.list.d/zen.list
echo 'deb https://zencashofficial.github.io/repo/ '$(lsb_release -cs)' main' | tee --append /etc/apt/sources.list.d/zen.list
gpg --keyserver ha.pool.sks-keyservers.net --recv 219F55740BBF7A1CE368BA45FB7053CE4991B669
gpg --export 219F55740BBF7A1CE368BA45FB7053CE4991B669 | apt-key add -

sudo apt-get update -y
apt-get install -y zen

cat > /home/vagrant/.zen/zen.conf <<EOF
rpcallowip=0.0.0.0/0
rpcuser=zenuser
rpcpassword=change_this_to_a_long_random_password
rpcport=18231
rpcallowip=0.0.0.0/0
server=1
daemon=1
listen=1
txindex=1
logtimestamps=1
testnet=1
EOF

zen-fetch-params

zend

// execute zen commands:
zen-cli getblockcount
zen-cli listtransactions
