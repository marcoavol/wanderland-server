#!/usr/bin/env bash
# use certbot to renew the certificate
docker compose -f docker-compose-deploy.yml run --rm certbot renew
# convert the certificate to PKCS12 format to use in backend
cd /mnt/docker_images/certbot/conf/live/www.wanderland.dev
openssl pkcs12 -export -in fullchain.pem -inkey privkey.pem -out keystore.p12 -name tomcat -CAfile chain.pem -caname root
