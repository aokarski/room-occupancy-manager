#!/bin/bash

./gradlew clean build
docker build --tag=rom:latest .
docker run -d -p 8080:8080 rom:latest

printf "\nWaiting for the server to start..."

sleep 7

printf "\nTesting REST API requests:"

printf "\nFree premium: 3, free economy: 3: "
curl -XPOST http://localhost:8080/occupancy -d'{"freeEconomyRooms":3, "freePremiumRooms":3, "priceOffers":[23, 45, 155, 374, 22, 99.99, 100, 101, 115, 209]}' -H "Content-Type: application/json"
sleep 1

printf "\nFree premium: 7, free economy: 5: "
curl -XPOST http://localhost:8080/occupancy -d'{"freeEconomyRooms":5, "freePremiumRooms":7, "priceOffers":[23, 45, 155, 374, 22, 99.99, 100, 101, 115, 209]}' -H "Content-Type: application/json"
sleep 1

printf "\nFree premium: 2, free economy: 7: "
curl -XPOST http://localhost:8080/occupancy -d'{"freeEconomyRooms":7, "freePremiumRooms":2, "priceOffers":[23, 45, 155, 374, 22, 99.99, 100, 101, 115, 209]}' -H "Content-Type: application/json"
sleep 1

printf "\nFree premium: 7, free economy: 1: "
curl -XPOST http://localhost:8080/occupancy -d'{"freeEconomyRooms":1, "freePremiumRooms":7, "priceOffers":[23, 45, 155, 374, 22, 99.99, 100, 101, 115, 209]}' -H "Content-Type: application/json"
sleep 1

printf "\nTesting invalid requests:"
printf "\n"
curl -XPOST http://localhost:8080/occupancy -d'{"freeEconomyRooms":1, "freePremiumRooms":7, "priceOffers":[0]}' -H "Content-Type: application/json"
sleep 1

printf "\n"
curl -XPOST http://localhost:8080/occupancy -d'{"freeEconomyRooms":0, "freePremiumRooms":0, "priceOffers":[1, 2, 3, 4, 101, 102]}' -H "Content-Type: application/json"

printf "\nkilling docker containers...\n"
docker kill $(docker ps -q)

